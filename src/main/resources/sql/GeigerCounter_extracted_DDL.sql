-- ============================

-- Ce fichier a été créé à l'aide de l'utilitaire dblook de Derby.
-- Horodatage : 2017-12-11 15:45:57.849
-- La base de données source est : C:\Users\cdesmots-ext\.netbeans-derby\GeigerCounter
-- L'URL de connexion est : jdbc:derby://localhost:1527/C:\Users\cdesmots-ext\.netbeans-derby\GeigerCounter;user=GeigerCounter;password=GeigerCounter
-- appendLogs: false

-- ----------------------------------------------
-- Instructions DDL pour les schémas
-- ----------------------------------------------

CREATE SCHEMA "GEIGERCOUNTER";

CREATE SCHEMA "DATA";

-- ----------------------------------------------
-- Instructions DDL pour les tables
-- ----------------------------------------------

CREATE TABLE "GEIGERCOUNTER"."CPM" ("HARWAREID" SMALLINT NOT NULL, "TIMESTAMP" TIMESTAMP NOT NULL, "CPM" INTEGER);

CREATE TABLE "GEIGERCOUNTER"."HARDWARE" ("HARDWAREID" SMALLINT NOT NULL, "VERSION" VARCHAR(14), "SERIALNUMBER" VARCHAR(25));

CREATE TABLE "DATA"."CPM" ("HARWAREID" SMALLINT NOT NULL, "TIMESTAMP" TIMESTAMP NOT NULL, "CPM" INTEGER);

CREATE TABLE "DATA"."HARDWARE" ("HARDWAREID" SMALLINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "VERSION" VARCHAR(14) NOT NULL, "SERIALNUMBER" VARCHAR(25) NOT NULL);

-- ----------------------------------------------
-- Instructions DDL pour les clés
-- ----------------------------------------------

-- PRIMARY/UNIQUE
ALTER TABLE "DATA"."HARDWARE" ADD CONSTRAINT "SQL171211153835291" PRIMARY KEY ("VERSION", "SERIALNUMBER");

ALTER TABLE "DATA"."HARDWARE" ADD CONSTRAINT "SQL171211153835290" UNIQUE ("HARDWAREID");

ALTER TABLE "DATA"."CPM" ADD CONSTRAINT "SQL171211154314380" PRIMARY KEY ("HARWAREID", "TIMESTAMP");

ALTER TABLE "GEIGERCOUNTER"."CPM" ADD CONSTRAINT "SQL171207143932130" PRIMARY KEY ("HARWAREID", "TIMESTAMP");

ALTER TABLE "GEIGERCOUNTER"."HARDWARE" ADD CONSTRAINT "SQL171207143314930" PRIMARY KEY ("HARDWAREID");

-- FOREIGN
ALTER TABLE "DATA"."CPM" ADD CONSTRAINT "SQL171211154314381" FOREIGN KEY ("HARWAREID") REFERENCES "DATA"."HARDWARE" ("HARDWAREID") ON DELETE NO ACTION ON UPDATE NO ACTION;

