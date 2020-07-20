# GeigerCounter
Java EE 8 application for Raspberry Pi 3 with Pi4J and a GQ-GMC320 Geiger Counter
The purpose of this application is to have a server on Raspberry Pi that will communicate with a Geigeer counter GQ-GMC320 throw the serial to USB interface thanks to the Pi4j API.
The application will run on a Glassfish server on the Raspberry Pi.

TODO :
+ In emulation mode the hardware id should be 0 and above 0 for real hardware data.
+ Add a realm so that only an admin from localhost can modify hardware setting
+ Detect and list USB port where the GQ-GMC is connected
+ Add a registration process
+ Add an alert system so that registered user can receive an alert when the CPM have reach a certain level.
+ Enable the possibilitie to change the duration of each statistics.


At the end we will have those functions :
+ Users can register for having an alert when the CPM (count per minutes) have reach a certain level.
+ The registers could be notified by mail or SMS.
+ Sendiing data to GQ-Electronics World Map of radio-activity and others collaboratives websites of this kind.
+ Get all the date throw REST.
+ Displaying nice graph with statistics per hour, days, years : min, max, average CPM.
+ Throw the admin panel possibility to access most of the parameters of the GQ-GMC320 available throw serial to usb.
+ It should work for most of the terminals with a web browser.
