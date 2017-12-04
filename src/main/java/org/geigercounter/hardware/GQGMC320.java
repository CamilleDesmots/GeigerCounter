/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geigercounter.hardware;

import java.time.LocalDateTime;
import com.pi4j.io.serial.*;
import com.pi4j.util.Console;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author camilledesmots
 */
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

    private GQGMCSerialDataEventListener sde;

    public GQGMC320(String newDevicePath) {
        Logger.getLogger(GQGMC320.class.getName()).log(Level.INFO, "In the method GQGMC320(String newDevicePath)");
        this.heartBeatStatus = Boolean.FALSE;
        this.serialStatus = Boolean.FALSE;
        this.emulation = Boolean.FALSE;
        this.idleTime = 310;
this.devicePath = newDevicePath;
        config = new SerialConfig();
        config.device(devicePath)
                .baud(Baud._115200)
                .dataBits(DataBits._8)
                .parity(Parity.NONE)
                .stopBits(StopBits._1)
                .flowControl(FlowControl.NONE);
        sde = new GQGMCSerialDataEventListener();
    }

    @Override
    public void setSerialOpen() {
        Logger.getLogger(GQGMC320.class.getName()).log(Level.INFO, "In the method setSerialOpen()");
        serial = SerialFactory.createInstance();
        try {
            serial.open(config);
            serial.addListener(sde);

        } catch (IOException ex) {
            Logger.getLogger(GQGMC320.class.getName()).log(Level.SEVERE, null, ex);
        }
        Logger.getLogger(GQGMC320.class.getName()).log(Level.INFO, "End of the method setSerialOpen()");

    }

    @Override
    public String getVersion() {
        Logger.getLogger(GQGMC320.class.getName()).log(Level.INFO, "In the method getVersion()");
        try {

            this.commandName = "<GETVER>>";
            sde.setCommanName(this.commandName);
            serial.writeln(this.commandName);
            Thread.sleep(idleTime);

        } catch (InterruptedException | IllegalStateException | IOException ex) {
            Logger.getLogger(GQGMC320.class.getName()).log(Level.SEVERE, null, ex);
        }
        Logger.getLogger(GQGMC320.class.getName()).log(Level.INFO, "End of the method getVersion()");
        this.version = sde.getVersion();
         Logger.getLogger(GQGMC320.class.getName()).log(Level.INFO, "this.Version()[{0}]", this.version);
       
        return this.version;
    }

    @Override
    public String getSerialNumber() {
                Logger.getLogger(GQGMC320.class.getName()).log(Level.INFO, "In the method getSerialNumber()");
        try {

            this.commandName = "<GETSERIAL>>";
            sde.setCommanName(this.commandName);
            serial.writeln(this.commandName);
            Thread.sleep(idleTime);

        } catch (InterruptedException | IllegalStateException | IOException ex) {
            Logger.getLogger(GQGMC320.class.getName()).log(Level.SEVERE, null, ex);
        }
        Logger.getLogger(GQGMC320.class.getName()).log(Level.INFO, "End of the method getSerialNumber()");
        this.serialNumber = sde.getSerialNumber();
        Logger.getLogger(GQGMC320.class.getName()).log(Level.INFO, "SerialNumber[{0}]", this.serialNumber);
    
        return this.serialNumber;
}

    @Override
    public float getVoltage() {
             Logger.getLogger(GQGMC320.class.getName()).log(Level.INFO, "In the method getVoltage())");
        try {

            this.commandName = "<GETVOLT>>";
            sde.setCommanName(this.commandName);
            serial.writeln(this.commandName);
            Thread.sleep(idleTime);

        } catch (InterruptedException | IllegalStateException | IOException ex) {
            Logger.getLogger(GQGMC320.class.getName()).log(Level.SEVERE, null, ex);
        }
        Logger.getLogger(GQGMC320.class.getName()).log(Level.INFO, "End of the method getVoltage()");
        this.serialNumber = sde.getSerialNumber();
        Logger.getLogger(GQGMC320.class.getName()).log(Level.INFO, "Voltage [{0}]", this.voltage);
    
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
        } catch (InterruptedException | IllegalStateException | IOException ex) {
            Logger.getLogger(GQGMC320.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.cpm = sde.getCPM();
        Logger.getLogger(GQGMC320.class.getName()).log(Level.INFO, "CPM [{0}]", this.cpm);
        return this.cpm;
    }

    @Override
    public int getCPS() {
        try {
            this.commandName = "<GETCPS>>";
            sde.setCommanName(this.commandName);
            serial.writeln(this.commandName);
            Thread.sleep(idleTime);
        } catch (InterruptedException | IllegalStateException | IOException ex) {
            Logger.getLogger(GQGMC320.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.cps = sde.getCPS();
           Logger.getLogger(GQGMC320.class.getName()).log(Level.INFO, "CPS [{0}]", this.cps);
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
    public int getIdleTime(){
        return this.idleTime;
    }
    
    @Override 
    public void setIdleTime(int newIdleTime){
        this.idleTime = newIdleTime;
    }
            
    
     @Override
    public float getTemp() {
        try {
            this.commandName = "<GETTEMP>>";
            sde.setCommanName(this.commandName);
            serial.writeln(this.commandName);
            Thread.sleep(idleTime);
        } catch (InterruptedException | IllegalStateException | IOException ex) {
            Logger.getLogger(GQGMC320.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.temp = sde.getTemp();
           Logger.getLogger(GQGMC320.class.getName()).log(Level.INFO, "temp [{0}]", this.temp);
     return this.temp;
        
    }
    
}   
