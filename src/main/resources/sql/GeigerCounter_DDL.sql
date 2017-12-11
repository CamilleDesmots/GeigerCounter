
-- ----------------------------------------------
-- Instructions DDL pour les schémas
-- ----------------------------------------------

-- DROP SCHEMA  "DATA" RESTRICT;
CREATE SCHEMA "DATA";

-- ----------------------------------------------
-- Instructions DDL pour les tables
-- ----------------------------------------------

-- DROP TABLE "DATA"."HARDWARE";
CREATE TABLE "DATA"."HARDWARE" (
    "HARDWAREID" SMALLINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) UNIQUE, 
    "VERSION" VARCHAR(14) NOT NULL, 
    "SERIALNUMBER" VARCHAR(25) NOT NULL,
    PRIMARY KEY ("VERSION", "SERIALNUMBER")
);

-- DROP TABLE "DATA"."CPM";
CREATE TABLE "DATA"."CPM" (
    "HARWAREID" SMALLINT NOT NULL , 
    "TIMESTAMP" TIMESTAMP NOT NULL, 
    "CPM" INTEGER,
    PRIMARY KEY ("HARWAREID", "TIMESTAMP"),
    FOREIGN KEY( "HARWAREID") REFERENCES "DATA"."HARDWARE" ("HARDWAREID")
 );

-- DROP TABLE "DATA"."CPM_HOUR";
CREATE TABLE "DATA"."CPM_HOUR" (
    "HARWAREID" SMALLINT NOT NULL , 
    "TIMESTAMP" TIMESTAMP NOT NULL, 
    "CPM_MIN" INTEGER,
    "CPM_MAX" INTEGER,
    "CPM_AVERAGE" FLOAT,
    PRIMARY KEY ("HARWAREID", "TIMESTAMP"),
    FOREIGN KEY( "HARWAREID") REFERENCES "DATA"."HARDWARE" ("HARDWAREID")
 );

CREATE TABLE "DATA"."CPM_DAY" (
    "HARWAREID" SMALLINT NOT NULL , 
    "DATE" DATE NOT NULL, 
    "CPM_MIN" INTEGER,
    "CPM_MAX" INTEGER,
    "CPM_AVERAGE" FLOAT,
    PRIMARY KEY ("HARWAREID", "DATE"),
    FOREIGN KEY( "HARWAREID") REFERENCES "DATA"."HARDWARE" ("HARDWAREID")
 );

-- ----------------------------------------------
-- This schema is dedicated to the users data for login and alert sending 
CREATE SCHEMA "USER";

CREATE TABLE "USER"."USER"(
    // This USERID should be a random generated and unique strinG.
    "USERID" VARCHAR(256) NOT NULL UNIQUE,
    "NICKNAME" VARCHAR(256) NOT NULL, 
    "FIRSTNAME" VARCHAR(256) NOT NULL,
    "LASTTNAME" VARCHAR(256) NOT NULL,
    // 64 car. max. + @ + 255 car. max.
    "MAIL" VARCHAR(320) NOT NULL,
    // Phone number could be on 15 digits without the prefix
    "PHONE" CHAR(20),
    "SALT" VARCHAR(256) FOR BIG DATA NOT NULL, 
    "PASSWORD" VARCHAR(256) FOR BIG DATA NOT NULL,
    PRIMARY KEY ("USERID")
);
