/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geigercounter.hardware;

import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataEventListener;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author camilledesmots
 */
public class GQGMCSerialDataEventListener implements SerialDataEventListener {

    private String commandName;
    private String version;
    private String serialNumber;
    private int cpm;
    private int cps;
    private int tempInt;
    private float temp;
    private int sign;
    private String tempText;
    private float volt;
    private LocalDateTime dateTime;
    
    private static final Logger logger = Logger.getLogger(Object.class.getName());

    @Override
    public void dataReceived(SerialDataEvent sde) {
        logger.log(Level.INFO, "In the method dataReceived() : [{0}]", this.commandName);

        try {
            switch (this.commandName) {
                case "<GETVER>>":
                    this.version = sde.getAsciiString();
                    break;
                case "<GETSERIAL>>":
                    this.serialNumber = sde.getHexByteString();
                    break;
                case "<GETCPM>>":
                    this.cpm = Integer.parseInt(sde.getHexByteString("", "", ""), 16);
                    break;
                case "<GETCPS>>":
                    this.cps = Integer.parseInt(sde.getHexByteString("", "", "").substring(1), 16);
                    break;
                case "<GETTEMP>>":
                    this.tempText = sde.getHexByteString("", "", "");
                    this.tempInt = Integer.parseInt(this.tempText.substring(0, 3), 16);
                    this.sign = Integer.parseInt(this.tempText.substring(4, 5), 16);
                    this.temp = this.tempInt;
                    this.temp = (this.temp / 10);
                    if (this.sign > 0) {
                        this.temp = this.temp * -1;
                    }
                    break;
                case "<GETVOLT>>":
                    this.volt = Integer.parseInt(sde.getHexByteString("", "", "").substring(1), 16);
                    this.volt = this.volt / 10;
                    break;
                case "<GETDATETIME>>":
                    this.tempText = sde.getHexByteString("", "", "");
                    this.dateTime = LocalDateTime.of(
                            Integer.parseInt(this.tempText.substring(0, 2), 16) + 2000,
                            Integer.parseInt(this.tempText.substring(2, 4), 16),
                            Integer.parseInt(this.tempText.substring(4, 6), 16),
                            Integer.parseInt(this.tempText.substring(6, 8), 16),
                            Integer.parseInt(this.tempText.substring(8, 10), 16),
                            Integer.parseInt(this.tempText.substring(10, 12), 16)
                    );
                    break;
                default:
                    this.tempText = sde.getHexByteString("", "", "");
                    logger.log(Level.WARNING, "Don''t know how to handle command ''{0}'' The HEXA response is [{1}]", new Object[]{this.commandName, this.tempText});
                    break;
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, null, e);
        }
    }

    public void setCommanName(String newCommandName) {
        this.commandName = newCommandName;
    }

    public String getCommandName() {
        return this.commandName;
    }

    public String getVersion() {
        logger.log(Level.INFO, "In the method getVersion() version :{0}", this.version);

        return this.version;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public int getCPM() {
        logger.log(Level.INFO, "In the method getCPM() CPM : {0}", this.cpm);
        return this.cpm;
    }

    public int getCPS() {
        return this.cps;
    }

    public float getTemp() {
        return this.temp;
    }

    public float getVolt() {
        return this.volt;
    }

    public LocalDateTime getDateTime() {
        return this.dateTime;
    }
}