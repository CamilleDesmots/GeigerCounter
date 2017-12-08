-- ============================

-- Ce fichier a �t� cr�� � l'aide de l'utilitaire dblook de Derby.
-- Horodatage : 2017-12-08 16:05:21.861
-- La base de donn�es source est : C:\Users\cdesmots-ext\.netbeans-derby\GeigerCounter
-- L'URL de connexion est : jdbc:derby://localhost:1527/C:\Users\cdesmots-ext\.netbeans-derby\GeigerCounter;user=GeigerCounter;password=GeigerCounter
-- appendLogs: false

-- ----------------------------------------------
-- Instructions DDL pour les sch�mas
-- ----------------------------------------------

DROP SCHEMA  "DATA";
CREATE SCHEMA "DATA";

-- ----------------------------------------------
-- Instructions DDL pour les tables
-- ----------------------------------------------

CREATE TABLE "DATA"."CPM" ("HARWAREID" SMALLINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "TIMESTAMP" TIMESTAMP NOT NULL, "CPM" INTEGER);

CREATE TABLE "DATA"."HARDWARE" ("HARDWAREID" SMALLINT NOT NULL, "VERSION" VARCHAR(14), "SERIALNUMBER" VARCHAR(25));

-- ----------------------------------------------
-- Instructions DDL pour les cl�s
-- ----------------------------------------------

-- PRIMARY/UNIQUE
ALTER TABLE "DATA"."CPM" ADD CONSTRAINT "SQL171207143932130" PRIMARY KEY ("HARWAREID", "TIMESTAMP");

ALTER TABLE "DATA"."HARDWARE" ADD CONSTRAINT "SQL171207143314930" PRIMARY KEY ("HARDWAREID");

