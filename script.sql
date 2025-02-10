CREATE DATABASE crypto_data;

\connect crypto_data

CREATE TABLE utilisateur(
    id_utilisateur SERIAL PRIMARY KEY,
    f_id INT NOT NULL ,
    nom VARCHAR(250) not null ,
    email VARCHAR(100) unique not null ,
    monnaie NUMERIC DEFAULT 0,
    imageUrl TEXT
);

CREATE TABLE sessionUser(
    id_session SERIAL PRIMARY KEY,
    token TEXT not null ,
    f_token TEXT not null ,
    expiration TIMESTAMP not null ,
    id_utilisateur int REFERENCES utilisateur(id_utilisateur) not null
);

CREATE TABLE crypto(
    id_crypto SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    unit_nom VARCHAR(10) NOT NULL,
    url TEXT not null
);

INSERT INTO crypto (nom, unit_nom, url) VALUES ('Bitcoin', 'BTC', 'https://bin.bnbstatic.com/static/assets/logos/BTC.png');
INSERT INTO crypto (nom, unit_nom, url) VALUES ('Polkadot', 'DOT', 'https://bin.bnbstatic.com/image/admin_mgs_image_upload/20230404/8e0060bf-9aed-4003-aba3-3d2367c18215.png');
INSERT INTO crypto (nom, unit_nom, url) VALUES ('Sui', 'SUI', 'https://bin.bnbstatic.com/static/assets/logos/SUI.png');
INSERT INTO crypto (nom, unit_nom, url) VALUES ('Raydium', 'RAY', 'https://bin.bnbstatic.com/static/assets/logos/RAY.png');
INSERT INTO crypto (nom, unit_nom, url) VALUES ('Ethereum', 'ETH', 'https://bin.bnbstatic.com/static/assets/logos/ETH.png');
INSERT INTO crypto (nom, unit_nom, url) VALUES ('Litecoin', 'LTC', 'https://bin.bnbstatic.com/static/assets/logos/LTC.png');
INSERT INTO crypto (nom, unit_nom, url) VALUES ('THORChain', 'RUNE', 'https://bin.bnbstatic.com/static/assets/logos/RUNE.png');
INSERT INTO crypto (nom, unit_nom, url) VALUES ('SuperVerse', 'SUPER', 'https://bin.bnbstatic.com/static/assets/logos/SUPER.png');
INSERT INTO crypto (nom, unit_nom, url) VALUES ('Synapse', 'SYN', 'https://bin.bnbstatic.com/static/assets/logos/SYN.png');
INSERT INTO crypto (nom, unit_nom, url) VALUES ('PAX Gold', 'PAXG', 'https://bin.bnbstatic.com/static/assets/logos/PAXG.png');
INSERT INTO crypto (nom, unit_nom, url) VALUES ('IQ', 'IQ', 'https://bin.bnbstatic.com/static/assets/logos/IQ.png');


CREATE TABLE cour(
    id_cour SERIAL PRIMARY KEY,
    id_crypto INT REFERENCES crypto(id_crypto) not null ,
    valeur NUMERIC not null ,
    date_changement TIMESTAMP NOT NULL
);


CREATE TABLE portefeuille(
    id_portefeuille SERIAL PRIMARY KEY,
    id_crypto INT REFERENCES crypto(id_crypto) not null ,
    id_utilisateur INT REFERENCES utilisateur(id_utilisateur) not null ,
    quantite NUMERIC not null
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
    id_type INT REFERENCES type(id_type),
    commission NUMERIC NOT NULL ,
    total NUMERIC NOT NULL ,
    total_with_commission NUMERIC NOT NULL
);

CREATE TABLE etat (
  id_etat SERIAL PRIMARY KEY ,
  designation varchar(20) not null
);

INSERT INTO ETAT (designation) values ('attente');
INSERT INTO ETAT (designation) values ('valider');
INSERT INTO ETAT (designation) values ('anuller');
INSERT INTO ETAT (designation) values ('supprimer');


CREATE TABLE demande_transaction_fond (
      id_demande_transaction_fond SERIAL PRIMARY KEY ,
      key TEXT NOT NULL,
      id_utilisateur int references utilisateur(id_utilisateur),
      valeur NUMERIC not null,
      id_type INT REFERENCES type(id_type),
      date_demande TIMESTAMP NOT NULL,
      date_reponse timestamp,
      id_etat INT REFERENCES etat(id_etat) not null
);

CREATE TABlE transaction_fond(
    id_transaction_fond SERIAL PRIMARY KEY,
    id_type INT REFERENCES type(id_type),
    id_utilisateur INT REFERENCES utilisateur(id_utilisateur),
    valeur NUMERIC,
    date_action TIMESTAMP NOT NULL,
    id_demande INT REFERENCES demande_transaction_fond(id_demande_transaction_fond)
);

CREATE table key_validation_email (
    id_validation SERIAL PRIMARY KEY,
    email VARCHAR(100), 
    key TEXT NOT NULL
);

CREATE table feedback (
    id_feedback SERIAL PRIMARY KEY,
    subject VARCHAR(100) not null,
    content TEXT not null,
    id_sender int references utilisateur(id_utilisateur)
);

CREATE TABLE commission(
    id_commission SERIAL PRIMARY KEY,
    commission_achat NUMERIC,
    commission_vente NUMERIC
);

INSERT INTO commission (commission_achat, commission_vente) VALUES (2 , 2);

CREATE TABLE crypto_favori (
    id_crypto_favori SERIAL PRIMARY KEY ,
    key TEXT NOT NULL,
    id_crypto INT REFERENCES crypto(id_crypto) NOT NULL ,
    id_utilisateur int references utilisateur(id_utilisateur) NOT NULL ,
    UNIQUE (id_crypto, id_utilisateur)
);


CREATE OR REPLACE FUNCTION update_fond()
RETURNS TRIGGER AS $$
BEGIN
    IF (NEW.id_type = 1) THEN 
        UPDATE utilisateur
        SET monnaie = monnaie + NEW.valeur
        WHERE id_utilisateur = NEW.id_utilisateur;
    ELSIF (NEW.id_type = 2) THEN 
        UPDATE utilisateur
        SET monnaie = monnaie - NEW.valeur
        WHERE id_utilisateur = NEW.id_utilisateur;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_fond
AFTER INSERT ON transaction_fond
FOR EACH ROW
EXECUTE PROCEDURE update_fond();

CREATE OR REPLACE FUNCTION update_portefeuille()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM portefeuille
        WHERE id_crypto = NEW.id_crypto AND id_utilisateur = NEW.id_utilisateur
    ) THEN
        IF (NEW.id_type = 1) THEN -- Achat de crypto
            UPDATE portefeuille
            SET quantite = quantite + NEW.qtty
            WHERE id_crypto = NEW.id_crypto AND id_utilisateur = NEW.id_utilisateur;
        ELSIF (NEW.id_type = 2) THEN -- Vente de crypto
            UPDATE portefeuille
            SET quantite = quantite - NEW.qtty
            WHERE id_crypto = NEW.id_crypto AND id_utilisateur = NEW.id_utilisateur;
        END IF;
    ELSE
        -- Si le portefeuille n'existe pas, insérer un nouvel enregistrement
        INSERT INTO portefeuille (id_crypto, id_utilisateur, quantite)
        VALUES (NEW.id_crypto, NEW.id_utilisateur, NEW.qtty);
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_portefeuille
AFTER INSERT ON transaction_crypto
FOR EACH ROW
EXECUTE PROCEDURE update_portefeuille();

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
            dernier_cours.valeur := 20000;
        END IF;

        variation := (random() * 0.02) - 0.01;
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

CREATE VIEW operations_utilisateurs AS
SELECT
    tc.id_utilisateur,
    'crypto' AS type_transaction_str,
    tc.id_type AS id_type_transaction,
    tc.cour,
    tc.id_crypto,
    NULL AS valeur,
    tc.date_action
FROM
    transaction_crypto tc
UNION ALL
SELECT
    tf.id_utilisateur,
    'fond' AS type_transaction_str,
    tf.id_type AS id_type_transaction,
    NULL AS cour,
    NULL AS id_crypto,
    tf.valeur,
    tf.date_action
FROM
    transaction_fond tf
ORDER BY
    date_action DESC;

CREATE VIEW v_user_analyse AS
SELECT
    u.id_utilisateur,
    COALESCE(SUM(CASE WHEN t.id_type = 1 THEN t.total_with_commission END), 0) AS total_achat,
    COALESCE(SUM(CASE WHEN t.id_type = 2 THEN t.total_with_commission END), 0) AS total_vente
FROM utilisateur u
         LEFT JOIN transaction_crypto t ON u.id_utilisateur = t.id_utilisateur
GROUP BY u.id_utilisateur;

CREATE OR REPLACE FUNCTION get_user_analyse(p_max_date TIMESTAMP)
    RETURNS TABLE (
                      id_utilisateur INT,
                      total_achat NUMERIC,
                      total_vente NUMERIC
                  ) AS $$
BEGIN
    RETURN QUERY
        SELECT
            u.id_utilisateur,
            COALESCE(SUM(CASE WHEN t.id_type = 1 THEN t.total_with_commission END), 0) AS total_achat,
            COALESCE(SUM(CASE WHEN t.id_type = 2 THEN t.total_with_commission END), 0) AS total_vente
        FROM utilisateur u
                 LEFT JOIN transaction_crypto t
                           ON u.id_utilisateur = t.id_utilisateur
                               AND t.date_action <= p_max_date
        GROUP BY u.id_utilisateur;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_favorite_emails(p_id_crypto INT)
    RETURNS TABLE(email TEXT) AS $$
BEGIN
    RETURN QUERY
        SELECT u.email::TEXT
        FROM crypto_favori cf
                 JOIN utilisateur u ON cf.id_utilisateur = u.id_utilisateur
        WHERE cf.id_crypto = p_id_crypto;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_favorite_emails_ex(p_idCrypto INT, p_idUserToExclude INT)
    RETURNS TABLE(email TEXT) AS $$
BEGIN
    RETURN QUERY
        SELECT u.email::TEXT
        FROM crypto_favori cf
                 JOIN utilisateur u ON cf.id_utilisateur = u.id_utilisateur
        WHERE cf.id_crypto = p_idCrypto
          AND cf.id_utilisateur != p_idUserToExclude;
END;
$$ LANGUAGE plpgsql;







