/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geigercounter.hardware;

import java.time.LocalDateTime;
import com.pi4j.io.serial.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;

/**
 *
 * @author camilledesmots
 */
//@Stateless
@Named
public class GQGMC320 implements GQGMCInterface {

    private final boolean heartBeatStatus;
    private final boolean serialStatus;
    private SerialConfig config;
    private Serial serial;
    private int cpm;
    private int cps;
    private String version;
    private String revision;
    private boolean emulation;
    private String devicePath;
    private int idleTime;
    private String commandName;
    private String serialNumber;
    private float voltage;
    private float temp;
    
    private static final Logger LOGGER = Logger.getLogger(Object.class.getName());

    private GQGMCSerialDataEventListener sde;

    public GQGMC320() {
        LOGGER.log(Level.INFO, "In the method GQGMC320()");
        this.heartBeatStatus = Boolean.FALSE;
        this.serialStatus = Boolean.FALSE;
        
        config = new SerialConfig();
        
        this.cpm = 0;
        this.cps = 0;
        this.version = "";
        this.revision = "";
        this.emulation = Boolean.FALSE;
        this.devicePath = "";
        // 2018-07-22 idletime modified from 310 to 350 as I have issued no response after many GETCPM
        this.idleTime = 350;
        this.commandName = "";
        this.serialNumber = "";
        this.voltage = 0;
        this.temp = 0;
        
        sde = new GQGMCSerialDataEventListener();
    }

    public void setDevicePath(String newDevicePath){
        LOGGER.log(Level.INFO, "In the method setDevicePath()");
        this.devicePath = newDevicePath;
    }
    
    public void setConfig(){
        LOGGER.log(Level.INFO, "In the method setConfig()");
        config.device(devicePath)
                .baud(Baud._115200)
                .dataBits(DataBits._8)
                .parity(Parity.NONE)
                .stopBits(StopBits._1)
                .flowControl(FlowControl.NONE); 
    }
    
    /**
     *
     */
    @Override
    public void setSerialOpen() {
        LOGGER.log(Level.INFO, "In the method setSerialOpen()");
        serial = SerialFactory.createInstance();
        try {
            serial.open(config);
            serial.addListener(sde);

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        LOGGER.log(Level.INFO, "End of the method setSerialOpen()");

    }

    @Override
    public String getVersion() {
        LOGGER.log(Level.INFO, "In the method getVersion()");
        try {

            this.commandName = "<GETVER>>";
            sde.setCommanName(this.commandName);
            serial.writeln(this.commandName);
            Thread.sleep(idleTime);

        } catch (InterruptedException | IllegalStateException | IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        LOGGER.log(Level.INFO, "End of the method getVersion()");
        this.version = sde.getVersion();
        LOGGER.log(Level.INFO, "this.Version()[{0}]", this.version);

        return this.version;
    }

    @Override
    public String getSerialNumber() {
        LOGGER.log(Level.INFO, "In the method getSerialNumber()");
        try {

            this.commandName = "<GETSERIAL>>";
            sde.setCommanName(this.commandName);
            serial.writeln(this.commandName);
            Thread.sleep(idleTime);

        } catch (InterruptedException | IllegalStateException | IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        LOGGER.log(Level.INFO, "End of the method getSerialNumber()");
        this.serialNumber = sde.getSerialNumber();
        LOGGER.log(Level.INFO, "SerialNumber[{0}]", this.serialNumber);

        return this.serialNumber;
    }

    @Override
    public float getVoltage() {
        LOGGER.log(Level.INFO, "In the method getVoltage())");
        try {

            this.commandName = "<GETVOLT>>";
            sde.setCommanName(this.commandName);
            serial.writeln(this.commandName);
            Thread.sleep(idleTime);

        } catch (InterruptedException | IllegalStateException | IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        LOGGER.log(Level.INFO, "End of the method getVoltage()");
        this.serialNumber = sde.getSerialNumber();
        LOGGER.log(Level.INFO, "Voltage [{0}]", this.voltage);

        return this.voltage;
    }

    @Override
    public void setRealtime(LocalDateTime localDate
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean checkUSBId(short vendorId, short productId
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setHeartBeatOn() {
        if (heartBeatStatus == false) {
            // 
        }
    }

    @Override
    public void setHeartBeatOff() {
        if (heartBeatStatus == true) {
            // 
        }
    }

    @Override
    public boolean getHeartBeatStatus() {
        return heartBeatStatus;
    }

    @Override
    public String getModelNumber() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getRevision() {
        if (this.version.isEmpty()) {
            String bidon = this.getVersion();
        } else {
            // We got already the Version 
            if (this.version.length() >= 7) {
                this.revision = this.version.substring(7);
            } else {
                this.revision = "";
            }

        }
        return this.revision;
    }

    @Override
    public int getCPM() {
        try {
            this.commandName = "<GETCPM>>";
            sde.setCommanName(this.commandName);
            serial.writeln(this.commandName);
            Thread.sleep(idleTime);
        } catch (InterruptedException | IllegalStateException | IOException | NumberFormatException ex) {
            LOGGER.log(Level.SEVERE, "During process of the result of command \"{0}\" we get the exception : {1}", new Object[]{this.commandName,ex});
        }
        this.cpm = sde.getCPM();
        LOGGER.log(Level.INFO, "CPM [{0}]", this.cpm);
        return this.cpm;
    }

    @Override
    public int getCPS() {
        try {
            this.commandName = "<GETCPS>>";
            sde.setCommanName(this.commandName);
            serial.writeln(this.commandName);
            Thread.sleep(idleTime);
        } catch (InterruptedException | IllegalStateException | IOException | NumberFormatException ex) {
            LOGGER.log(Level.SEVERE, "During process of the result of command \"{0}\" we get the exception : {1}", new Object[]{this.commandName,ex});
        }
        this.cps = sde.getCPS();
        LOGGER.log(Level.INFO, "CPS [{0}]", this.cps);
        return this.cps;

    }

    @Override
    public boolean getSerialStatus() {
        return this.serialStatus;
    }

    @Override
    public boolean isEmulation() {
        return this.emulation;
    }

    @Override
    public int getIdleTime() {
        return this.idleTime;
    }

    @Override
    public void setIdleTime(int newIdleTime) {
        this.idleTime = newIdleTime;
    }

    @Override
    public float getTemp() {
        try {
            this.commandName = "<GETTEMP>>";
            sde.setCommanName(this.commandName);
            serial.writeln(this.commandName);
            Thread.sleep(idleTime);
        } catch (InterruptedException | IllegalStateException | IOException | NumberFormatException ex) {
            LOGGER.log(Level.SEVERE, "During process of the result of command \"{0}\" we get the exception : {1}", new Object[]{this.commandName,ex});
        }
        this.temp = sde.getTemp();
        LOGGER.log(Level.INFO, "temp [{0}]", this.temp);
        return this.temp;

    }

}
