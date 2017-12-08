if "%DERBY_HOME%"=="" set DERBY_HOME=C:\Program Files\db-derby-10.14.1.0-bin

java -jar "%DERBY_HOME%\lib\derbyrun.jar" dblook -d 'jdbc:derby://localhost:1527/C:\Users\cdesmots-ext\.netbeans-derby\GeigerCounter;user=GeigerCounter;password=GeigerCounter' -o GeigerCounter_extracted_DDL.sql -verbose