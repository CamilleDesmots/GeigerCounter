/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geigercounter.hardware;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author camilledesmots
 */
//@Stateless
public class GQGMCsim implements GQGMCInterface {

    private String version;
    private String model;
    private String revision;
    private String serialNumber;
    private Random rand;
    private boolean emulation;
    private String devicePath;
    private int idleTime;
    private float temp;
    
     private static final Logger LOGGER = Logger.getLogger("org.geigercounter.hardware.GQGMCsim");

    public GQGMCsim() {
        LOGGER.log(Level.INFO, "In the method GQGMCsim()");
        this.version = "GQsim Re 1.01";
        this.model = "GQSim";
        this.revision = "Re 1.01";
        this.serialNumber = "FF,AA,00,FF,AA,00,FF";
        this.rand = new Random();
        this.emulation = Boolean.TRUE;
        this.idleTime = 310;
    }
    
      public void setDevicePath(String newDevicePath){
        LOGGER.log(Level.INFO, "In the method setDevicePath()");
        this.devicePath = newDevicePath;
    }
      
        public void setConfig(){
        LOGGER.log(Level.INFO, "In the method setConfig()");   
    }

    @Override
    public void setSerialOpen() {
        Logger.getLogger(GQGMCsim.class.getName()).log(Level.INFO, "In the method setSerialOpen()");
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public String getSerialNumber() {
        return this.serialNumber;
    }

    @Override
    public float getVoltage() {
        // GQ-GMC320 return a single byte for voltage with one decimal
        this.temp = rand.nextInt(255) / 10;
        return this.temp;
    }

    @Override
    public void setRealtime(LocalDateTime localDate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean checkUSBId(short vendorId, short productId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setHeartBeatOn() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setHeartBeatOff() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean getHeartBeatStatus() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getModelNumber() {
        return this.model;
    }

    @Override
    public String getRevision() {
        return this.revision;
    }

    @Override
    public int getCPM() {
        // GQ-GMC320 return CPM on two bytes but only the lSB14 bites are used.   
        LOGGER.log(Level.INFO, "In the method getCPM()");
        // In theory we could have this :  
        // return rand.nextInt(16383);
        // In the reality we could have this :  
        return rand.nextInt(60);        
        
    }

    @Override
    public int getCPS() {
        // Same remarks as for getCPM
        LOGGER.log(Level.INFO, "In the method getCPS()");
        // In theory we could have this :  
        // return rand.nextInt(16383);
        // In the reality we could have this :  
        return rand.nextInt(6);        

    }

    @Override
    public boolean getSerialStatus() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isEmulation() {
        return this.emulation;

    }

    /**
     *
     * @return The idle time in milliseconds before retrieving the answer of a request to the serial port
     */
    @Override
    public int getIdleTime() {
        return this.idleTime;
    }

    @Override
    public void setIdleTime(int newIdleTime) {
        this.idleTime = newIdleTime;
    }

    /**
     *
     * @return The temperature in °c
     */
    @Override
    public float getTemp() {
        // In theory : 
        // this.temp = rand.nextInt(4095) / 10;
        // For realism
        this.temp = rand.nextInt(500) / 10;        
        return this.temp;
    }
}
