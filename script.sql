CREATE TABLE utilisateur(
    id_utilisateur SERIAL PRIMARY KEY,
    nom VARCHAR(250),
    monnaie NUMERIC DEFAULT 0
);

CREATE TABLE crypto(
    id_crypto SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL
);


CREATE TABLE cour(
    id_cour SERIAL PRIMARY KEY,
    id_crypto INT REFERENCES crypto(id_crypto),
    valeur NUMERIC ,
    date_changement TIMESTAMP NOT NULL
);


CREATE TABLE portefeuille(
    id_portefeuille SERIAL PRIMARY KEY,
    id_crypto INT REFERENCES crypto(id_crypto),
    id_utilisateur INT REFERENCES utilisateur(id_utilisateur),
    quantite NUMERIC
);

CREATE TABLE type(
    id_type SERIAL PRIMARY KEY,
    etat VARCHAR(10) NOT NULL
);

CREATE TABLE transaction_crypto (
    id_transaction_crypto SERIAL PRIMARY KEY,
    id_crypto INT REFERENCES crypto(id_crypto),
    id_utilisateur INT REFERENCES utilisateur(id_utilisateur),
    cour NUMERIC,
    date_action TIMESTAMP NOT NULL,
    id_type INT REFERENCES type(id_type)
);


CREATE TABlE transaction_fond(
    id_transaction_fond SERIAL PRIMARY KEY,
    id_type INT REFERENCES type(id_type),
    id_utilisateur INT REFERENCES utilisateur(id_utilisateur),
    valeur NUMERIC,
    date_action TIMESTAMP NOT NULL
);

-- Fonction pour mettre à jour les fonds
CREATE OR REPLACE FUNCTION update_fond()
RETURNS TRIGGER AS $$
BEGIN
    IF (NEW.id_type = 1) THEN -- Supposons que id_type = 1 correspond à un ajout de fonds
        UPDATE utilisateur
        SET monnaie = monnaie + NEW.valeur
        WHERE id_utilisateur = NEW.id_utilisateur;
    ELSIF (NEW.id_type = 2) THEN -- Supposons que id_type = 2 correspond à un retrait de fonds
        UPDATE utilisateur
        SET monnaie = monnaie - NEW.valeur
        WHERE id_utilisateur = NEW.id_utilisateur;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger pour transaction_fond
CREATE TRIGGER trigger_update_fond
AFTER INSERT ON transaction_fond
FOR EACH ROW
EXECUTE FUNCTION update_fond();

CREATE OR REPLACE FUNCTION update_portefeuille()
RETURNS TRIGGER AS $$
BEGIN
    -- Vérifier si un portefeuille existe déjà pour l'utilisateur et la crypto
    IF EXISTS (
        SELECT 1 
        FROM protefeuille 
        WHERE id_crypto = NEW.id_crypto AND id_utilisateur = NEW.id_utilisateur
    ) THEN
        -- Si le portefeuille existe, mettre à jour la quantité
        IF (NEW.id_type = 1) THEN -- Achat de crypto
            UPDATE protefeuille
            SET quantite = quantite + NEW.cour
            WHERE id_crypto = NEW.id_crypto AND id_utilisateur = NEW.id_utilisateur;
        ELSIF (NEW.id_type = 2) THEN -- Vente de crypto
            UPDATE protefeuille
            SET quantite = quantite - NEW.cour
            WHERE id_crypto = NEW.id_crypto AND id_utilisateur = NEW.id_utilisateur;
        END IF;
    ELSE
        -- Si le portefeuille n'existe pas, insérer un nouvel enregistrement
        INSERT INTO protefeuille (id_crypto, id_utilisateur, quantite)
        VALUES (NEW.id_crypto, NEW.id_utilisateur, NEW.cour);
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger pour transaction_crypto
CREATE TRIGGER trigger_update_portefeuille
AFTER INSERT ON transaction_crypto
FOR EACH ROW
EXECUTE FUNCTION update_portefeuille();

