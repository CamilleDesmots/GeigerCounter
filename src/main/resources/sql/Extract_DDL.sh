#!/bin/sh
# Extract DDL of the Derby database

if [ -n $DERBY_HOME ]; then
    export DERBY_HOME="/Users/camilledesmots/GlassFish_Server/glassfish5/javadb/"
fi 

exec java -jar $DERBY_HOME//lib/derbyrun.jar dblook -d 'jdbc:derby://localhost:1527//Users/camilledesmots/.netbeans-derby/GeigerCounter;user=GeigerCounter;password=GeigerCounter' -o GeigerCounter_extracted_DDL.sql -verbose