-- ============================

-- Ce fichier a �t� cr�� � l'aide de l'utilitaire dblook de Derby.
-- Horodatage : 2017-12-12 10:58:42.282
-- La base de donn�es source est : C:\Users\cdesmots-ext\.netbeans-derby\GeigerCounter
-- L'URL de connexion est : jdbc:derby://localhost:1527/C:\Users\cdesmots-ext\.netbeans-derby\GeigerCounter;user=GeigerCounter;password=GeigerCounter
-- appendLogs: false

-- ----------------------------------------------
-- Instructions DDL pour les sch�mas
-- ----------------------------------------------

CREATE SCHEMA "DATA";

CREATE SCHEMA "USER";

-- ----------------------------------------------
-- Instructions DDL pour les tables
-- ----------------------------------------------

CREATE TABLE "DATA"."HARDWARE" ("ID" SMALLINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "VERSION" VARCHAR(14) NOT NULL, "SERIALNUMBER" VARCHAR(25) NOT NULL);

CREATE TABLE "DATA"."CPM_DAY" ("HARWARE_ID" SMALLINT NOT NULL, "DATE" DATE NOT NULL, "CPM_MIN" INTEGER, "CPM_MAX" INTEGER, "CPM_AVERAGE" DOUBLE);

CREATE TABLE "USER"."USER" ("ID" INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "NICKNAME" VARCHAR(256) NOT NULL, "FIRSTNAME" VARCHAR(256) NOT NULL, "LASTNAME" VARCHAR(256) NOT NULL, "MAIL" VARCHAR(320) NOT NULL, "PHONE" CHAR(20), "SALT" VARCHAR (256) FOR BIT DATA NOT NULL, "PASSWORD" VARCHAR (256) FOR BIT DATA NOT NULL, "ACTIVATION_KEY" VARCHAR(256) NOT NULL);

CREATE TABLE "DATA"."CPM" ("HARWARE_ID" SMALLINT NOT NULL, "TIMESTAMP" TIMESTAMP NOT NULL, "CPM" INTEGER);

CREATE TABLE "USER"."GROUP" ("ID" INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "NAME" VARCHAR(50) NOT NULL, "DESCRIPTION" VARCHAR(300));

CREATE TABLE "USER"."USER_GROUP" ("USER_ID" INTEGER NOT NULL, "GROUP_ID" INTEGER NOT NULL);

CREATE TABLE "DATA"."CPM_HOUR" ("HARWARE_ID" SMALLINT NOT NULL, "TIMESTAMP" TIMESTAMP NOT NULL, "CPM_MIN" INTEGER, "CPM_MAX" INTEGER, "CPM_AVERAGE" DOUBLE);

-- ----------------------------------------------
-- Instructions DDL pour les index
-- ----------------------------------------------

CREATE UNIQUE INDEX "USER"."USER_IDX_4" ON "USER"."USER" ("ACTIVATION_KEY");

CREATE UNIQUE INDEX "USER"."USER_IDX_3" ON "USER"."USER" ("MAIL");

CREATE UNIQUE INDEX "USER"."USER_IDX_2" ON "USER"."USER" ("NICKNAME");

-- ----------------------------------------------
-- Instructions DDL pour les cl�s
-- ----------------------------------------------

-- PRIMARY/UNIQUE
ALTER TABLE "DATA"."CPM_HOUR" ADD CONSTRAINT "SQL171212103215710" PRIMARY KEY ("HARWARE_ID", "TIMESTAMP");

ALTER TABLE "USER"."USER_GROUP" ADD CONSTRAINT "SQL171212105740530" PRIMARY KEY ("USER_ID", "GROUP_ID");

ALTER TABLE "USER"."USER" ADD CONSTRAINT "SQL171212105722120" PRIMARY KEY ("ID");

ALTER TABLE "DATA"."HARDWARE" ADD CONSTRAINT "SQL171212102839080" UNIQUE ("ID");

ALTER TABLE "DATA"."HARDWARE" ADD CONSTRAINT "SQL171212102839081" PRIMARY KEY ("VERSION", "SERIALNUMBER");

ALTER TABLE "DATA"."CPM_DAY" ADD CONSTRAINT "SQL171212103314280" PRIMARY KEY ("HARWARE_ID", "DATE");

ALTER TABLE "USER"."GROUP" ADD CONSTRAINT "SQL171212104639650" PRIMARY KEY ("ID");

ALTER TABLE "DATA"."CPM" ADD CONSTRAINT "SQL171212103154030" PRIMARY KEY ("HARWARE_ID", "TIMESTAMP");

-- FOREIGN
ALTER TABLE "DATA"."CPM_HOUR" ADD CONSTRAINT "SQL171212103215711" FOREIGN KEY ("HARWARE_ID") REFERENCES "DATA"."HARDWARE" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "USER"."USER_GROUP" ADD CONSTRAINT "SQL171212105740531" FOREIGN KEY ("USER_ID") REFERENCES "USER"."USER" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "USER"."USER_GROUP" ADD CONSTRAINT "SQL171212105740532" FOREIGN KEY ("GROUP_ID") REFERENCES "USER"."GROUP" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "DATA"."CPM_DAY" ADD CONSTRAINT "SQL171212103314281" FOREIGN KEY ("HARWARE_ID") REFERENCES "DATA"."HARDWARE" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "DATA"."CPM" ADD CONSTRAINT "SQL171212103154031" FOREIGN KEY ("HARWARE_ID") REFERENCES "DATA"."HARDWARE" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

