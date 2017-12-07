/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geigercounter.hardware;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.ejb.Asynchronous;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author camilledesmots
 */
@Singleton
@ApplicationScoped
//@SessionScoped
@ManagedBean
@Named("hardwareManager")

public class HardwareManager implements Serializable {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private boolean emulation;
    private int cpm;
    private int cps;
    private String version;
    private String revision;
    private boolean countingCPM;
    
    //private static final Logger logger = Logger.getLogger(HardwareManager.class.getName());
    private final GQGMCInterface myGQGMC;
    
    // GQ-GMC320
    //@Inject GQGMC320 myGQGMC320;
    //private final GQGMC320 myGQGMC320;


    public HardwareManager() {

        this.countingCPM = Boolean.FALSE;
        Logger.getLogger(HardwareManager.class.getName()).log(Level.INFO, "In HardwareManager() contrusctor this.countingCPM = {0}.",this.countingCPM);
        // By default we start in emulation mode
        this.emulation = Boolean.TRUE;
        myGQGMC = new GQGMCsim("/dev/gqgmc");
        
        //this.emulation = Boolean.FALSE;
        //myGQGMC = new GQGMC320("/dev/gqgmc");

        Logger.getLogger(HardwareManager.class.getName()).log(Level.INFO, "Try to open the serial connection.");
        //myGQGMC.serialOpen();
        /*
        if (myGQGMC320.getSerialStatus()) {
            Logger.getLogger(HardwareManager.class.getName()).log(Level.INFO, "Succesfully opened serial communication.");
        } else {
            Logger.getLogger(HardwareManager.class.getName()).log(Level.SEVERE, "Failed to open serial communication.");
        }
         */

        myGQGMC.setSerialOpen();
        this.version = myGQGMC.getVersion();
        Logger.getLogger(HardwareManager.class.getName()).log(Level.INFO, "this.version) [{0}]", this.version);
    }

        /**
     *
     * @return False : no emulation, and True : Emulate a Geiger Counter
     */
    public boolean getEmulation() {
        Logger.getLogger(HardwareManager.class.getName()).log(Level.INFO, "In the method getEmulation()");
        return this.emulation;
    }
    public boolean isEmulation() {
        Logger.getLogger(HardwareManager.class.getName()).log(Level.INFO, "In the method isEmulation()");
        return this.emulation;
    }

    public void setEmulation(boolean newEmulation) {
        Logger.getLogger(HardwareManager.class.getName()).log(Level.INFO, "In the method setEmulation()");
        this.emulation = newEmulation;
        /*
        if (this.emulation == FALSE) {
            myGQGMC = new GQGMC320("/dev/gqgmc");
        } */
    }

    @Lock(LockType.WRITE)
    public int getCPM() {
        Logger.getLogger(HardwareManager.class.getName()).log(Level.INFO, "In the method getCPM()()");
        this.cpm = myGQGMC.getCPM();
        return this.cpm;
    }

    /**
     *
     * @return
     */
    @Lock(LockType.WRITE)
    public int getCPS() {
        Logger.getLogger(HardwareManager.class.getName()).log(Level.INFO, "In the method getCPS()");
        this.cps = myGQGMC.getCPS();
        return this.cps;
    }

    /**
     *
     * @return
     */
    @Lock(LockType.WRITE)
    public String getVersion() {
        this.version = myGQGMC.getVersion();
        return this.version;
    }

    /**
     *
     * @param topCounting TRUE for turning on the loop for counting CPM every minutes
     */
    public void setCountingCPM(boolean topCounting) throws InterruptedException {
        this.countingCPM = topCounting;
        Logger.getLogger(HardwareManager.class.getName()).log(Level.INFO, "In the method setCountingCPM({0})", this.countingCPM);
        if (this.countingCPM){
            Logger.getLogger(HardwareManager.class.getName()).log(Level.INFO, "About to run coutingCPM()");
        //this.countingCPM();
        }
    }

    /**
     *
     * @return TRUE if a loop is counting CPM every minutes
     */
    public boolean isCountingCPM() {
        return this.countingCPM;
    }

    /**
     *
     * @return TRUE if a loop is counting CPM every minutes
     */
    public boolean getCountingCPM() {
        return this.countingCPM;
    }

    public String getSerialNumber() {
        return myGQGMC.getSerialNumber();
    }

    public String getRevision() {
        this.revision = myGQGMC.getRevision();
        return this.revision;
    }

    public float getVoltage() {
        return myGQGMC.getVoltage();
    }

    public float getTemp() {
        return myGQGMC.getTemp();
    }
    
    /**
     * Request CPM value every minutes
     * @throws java.lang.InterruptedException
     */
    
    @Asynchronous
    public void countingCPM() throws InterruptedException{
        this.countingCPM = Boolean.TRUE;
        while (this.countingCPM == Boolean.TRUE){
            Thread.sleep(250);
            this.cpm = myGQGMC.getCPM();
            Logger.getLogger(HardwareManager.class.getName()).log(Level.INFO, "In the loop of countingCPM()");
        }
        
    }
}
