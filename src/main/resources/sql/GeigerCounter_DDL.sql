
-- ----------------------------------------------
-- Instructions DDL pour les schémas
-- ----------------------------------------------

CREATE SCHEMA "DATA";

-- ----------------------------------------------
-- Instructions DDL pour les tables
-- ----------------------------------------------

CREATE TABLE "DATA"."HARDWARE" (
    "ID" SMALLINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) UNIQUE, 
    "VERSION" VARCHAR(14) NOT NULL, 
    "SERIALNUMBER" VARCHAR(25) NOT NULL,
    PRIMARY KEY ("VERSION", "SERIALNUMBER")
);
CREATE UNIQUE INDEX "HARDWARE_IDX_1" ON "DATA"."HARDWARE" ("ID");
CREATE INDEX "HARDWARE_IDX_2" ON "DATA"."HARDWARE" ("VERSION","SERIALNUMBER");

CREATE TABLE "DATA"."CPM" (
    "hardware_ID" SMALLINT NOT NULL , 
    "TIMESTAMP" TIMESTAMP NOT NULL, 
    "CPM" INTEGER,
    PRIMARY KEY ("hardware_ID", "TIMESTAMP"),
    FOREIGN KEY( "hardware_ID") REFERENCES "DATA"."HARDWARE" ("ID")
 );

CREATE UNIQUE INDEX "CPM_IDX_1" ON "DATA"."CPM" ("hardware_ID", "TIMESTAMP");


CREATE TABLE "DATA"."CPM_HOUR" (
    "hardware_ID" SMALLINT NOT NULL , 
    "TIMESTAMP" TIMESTAMP NOT NULL, 
    "CPM_MIN" INTEGER,
    "CPM_MAX" INTEGER,
    "CPM_AVERAGE" FLOAT,
    PRIMARY KEY ("hardware_ID", "TIMESTAMP"),
    FOREIGN KEY( "hardware_ID") REFERENCES "DATA"."HARDWARE" ("ID")
 );

CREATE UNIQUE INDEX "CPM_HOUR_IDX_1" ON "DATA"."CPM_HOUR" ("hardware_ID", "TIMESTAMP");


CREATE TABLE "DATA"."CPM_DAY" (
    "hardware_ID" SMALLINT NOT NULL , 
    "DATE" DATE NOT NULL, 
    "CPM_MIN" INTEGER,
    "CPM_MAX" INTEGER,
    "CPM_AVERAGE" FLOAT,
    PRIMARY KEY ("hardware_ID", "DATE"),
    FOREIGN KEY( "hardware_ID") REFERENCES "DATA"."HARDWARE" ("ID")
 );

CREATE UNIQUE INDEX "CPM_DAY_IDX_1" ON "DATA"."CPM_DAY" ("hardware_ID", "DATE");

-- ----------------------------------------------
-- This schema is dedicated to the users data for login and alert sending 
CREATE SCHEMA "USER";

CREATE TABLE "USER"."USER" (
    "ID" INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
    "NICKNAME" VARCHAR(256) NOT NULL, 
    "FIRSTNAME" VARCHAR(256) NOT NULL,
    "LASTNAME" VARCHAR(256) NOT NULL,
    -- 64 car. max. + @ + 255 car. max.
    "MAIL" VARCHAR(320) NOT NULL,
    -- Phone number could be on 15 digits without the prefix
    "PHONE" CHAR(20),
    "SALT" VARCHAR(256) FOR BIT DATA NOT NULL, 
    "PASSWORD" VARCHAR(256) FOR BIT DATA NOT NULL,
    "ACTIVATION_KEY" VARCHAR(256) NOT NULL,
    PRIMARY KEY ("ID")
);

CREATE UNIQUE INDEX "USER_IDX_1" ON "USER"."USER" ("ID");
CREATE UNIQUE INDEX "USER_IDX_2" ON "USER"."USER" ("NICKNAME");
CREATE UNIQUE INDEX "USER_IDX_3" ON "USER"."USER" ("MAIL");
CREATE UNIQUE INDEX "USER_IDX_4" ON "USER"."USER" ("ACTIVATION_KEY");

CREATE TABLE "USER"."GROUP" (
   ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
   "NAME" VARCHAR(50) NOT NULL,
   "DESCRIPTION" varchar(300)
);

CREATE UNIQUE INDEX "USER_GROUP_1" ON "USER"."GROUP" ("ID");

CREATE TABLE "USER"."USER_GROUP" (
    USER_ID INT NOT NULL,
    GROUP_ID INT NOT NULL,
    PRIMARY KEY ("USER_ID", "GROUP_ID"),
    FOREIGN KEY( "USER_ID") REFERENCES "USER"."USER" ("ID"), 
    FOREIGN KEY( "GROUP_ID") REFERENCES "USER"."GROUP" ("ID")
    );

CREATE UNIQUE INDEX "USER_GROUP_IDX_1" ON "USER"."USER_GROUP" ("USER_ID","GROUP_ID");
CREATE INDEX "USER_GROUP_IDX_2" ON "USER"."USER_GROUP" ("USER_ID");
CREATE INDEX "USER_GROUP_IDX_3" ON "USER"."USER_GROUP" ("GROUP_ID");