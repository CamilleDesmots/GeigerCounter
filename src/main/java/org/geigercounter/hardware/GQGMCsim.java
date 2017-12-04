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
public class GQGMCsim implements GQGMCInterface {

    private String version ;
    private String model ;
    private String revision;
    private String serialNumber;
    private Random rand ;
    private boolean emulation ;
    private String devicePath;
    private int idleTime;
    private float temp;
    
    public GQGMCsim(String newDevicePath){
        Logger.getLogger(HardwareManager.class.getName()).log(Level.INFO, "In the method GQGMCsim()");
        this.devicePath = newDevicePath;
        this.version =  "GQsim Re 1.01";
        this.model = "GQSim";
        this.revision = "Re 1.01";
        this.serialNumber = "FF,AA,00,FF,AA,00,FF";
        this.rand = new Random();
        this.emulation = Boolean.TRUE;
        this.idleTime = 310;
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
    public String getVersion() {
        return this.version;
    }

    @Override
    public String getSerialNumber() {
        return this.serialNumber;
    }

    @Override
    public int getCPM() {
        // Retourne a value between [0 ... 65536[
        Logger.getLogger(HardwareManager.class.getName()).log(Level.INFO, "In the method getCPM()");
        return rand.nextInt(65536);

    }

    @Override
    public int getCPS() {
        // Return a value cbetween [0 ... 65536[
        Logger.getLogger(HardwareManager.class.getName()).log(Level.INFO, "In the method getCPS()");
        return rand.nextInt(65536);

    }

    @Override
    public float getVoltage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public boolean getSerialStatus() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setSerialOpen(){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
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
    public float getTemp(){
        this.temp = rand.nextInt(4095)/10;
        return this.temp;
    }
}
