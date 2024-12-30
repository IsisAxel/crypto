CREATE TABLE utilisateur(
    id_utilisateur SERIAL PRIMARY KEY,
    f_id INT, 
    nom VARCHAR(250),
    email VARCHAR(100),
    monnaie NUMERIC DEFAULT 0
);

CREATE TABLE sessionUser(
    id_session SERIAL PRIMARY KEY,
    token TEXT,
    f_token TEXT,
    expiration TIMESTAMP,
    id_utilisateur int REFERENCES utilisateur(id_utilisateur)
);

CREATE TABLE crypto(
    id_crypto SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    unit_nom VARCHAR(10) NOT NULL,
    logo VARCHAR(50)
);

INSERT INTO crypto (nom, unit_nom, logo) VALUES ('Bitcoin', 'BTC', 'BTC.png');
INSERT INTO crypto (nom, unit_nom, logo) VALUES ('Wrapped Bitcoin', 'WBTC', 'WBTC.png');
INSERT INTO crypto (nom, unit_nom, logo) VALUES ('yearn.finance', 'YFI', 'YFI.png');
INSERT INTO crypto (nom, unit_nom, logo) VALUES ('Wrapped Beacon ETH (WBETH)', 'WBETH', 'WBETH.png');
INSERT INTO crypto (nom, unit_nom, logo) VALUES ('Ethereum', 'ETH', 'ETH.png');
INSERT INTO crypto (nom, unit_nom, logo) VALUES ('PAX Gold', 'PAXG', 'PAXG.png');
INSERT INTO crypto (nom, unit_nom, logo) VALUES ('Litecoin', 'LTC', 'LTC.png');
INSERT INTO crypto (nom, unit_nom, logo) VALUES ('BNB', 'BNB', 'BNB.png');
INSERT INTO crypto (nom, unit_nom, logo) VALUES ('Bittensor', 'TAO', 'TAO.png');
INSERT INTO crypto (nom, unit_nom, logo) VALUES ('Bitcoin Cash', 'BCH', 'BCH.png');


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

insert into type (etat) values ('up');
insert into type (etat) values ('down');

CREATE TABLE transaction_crypto (
    id_transaction_crypto SERIAL PRIMARY KEY,
    id_crypto INT REFERENCES crypto(id_crypto),
    id_utilisateur INT REFERENCES utilisateur(id_utilisateur),
    cour NUMERIC,
    qtty NUMERIC NOT NULL,
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

CREATE table key_validation_email (
    id_validation SERIAL PRIMARY KEY,
    email VARCHAR(100) REFERENCES utilisateur(email), 
    key TEXT NOT NULL
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
EXECUTE PROCEDURE update_fond();

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



CREATE OR REPLACE FUNCTION generer_cours_crypto()
RETURNS VOID AS $$
DECLARE
    crypto RECORD;
    dernier_cours RECORD;
    variation DOUBLE PRECISION;
BEGIN
    FOR crypto IN
        SELECT id_crypto, nom FROM crypto
    LOOP
        SELECT valeur INTO dernier_cours FROM cour
        WHERE id_crypto = crypto.id_crypto
        ORDER BY date_changement DESC
        LIMIT 1;

        IF NOT FOUND THEN
            dernier_cours.valeur := 10000;
        END IF;

        variation := (random() * 0.04) - 0.02; 
        dernier_cours.valeur := dernier_cours.valeur * (1 + variation);

        INSERT INTO cour (id_crypto, valeur, date_changement)
        VALUES (crypto.id_crypto, dernier_cours.valeur, NOW());
    END LOOP;
END;
$$ LANGUAGE plpgsql;



CREATE OR REPLACE VIEW vue_dernier_cours AS
SELECT
    c.id_crypto,
    co.valeur,
    co.date_changement
FROM
    crypto c
        JOIN
    cour co
    ON
        c.id_crypto = co.id_crypto
WHERE
    co.date_changement = (
        SELECT MAX(cour.date_changement)
        FROM cour
        WHERE cour.id_crypto = c.id_crypto
    );



