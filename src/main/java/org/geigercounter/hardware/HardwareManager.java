/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geigercounter.hardware;

import java.io.Serializable;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author camilledesmots
 */
@ApplicationScoped
@ManagedBean
@Named("hardwareManager")

public class HardwareManager implements Serializable {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private boolean emulation;
    private int cpm;
    private int cps;
    private Logger logger = Logger.getLogger(HardwareManager.class.getName());

// GQ-GMC320
    //@Inject GQGMC320 myGQGMC320;
    //private final GQGMC320 myGQGMC320;
    private GQGMCInterface myGQGMC;
    private String version;
    private String revision;
    private boolean countingLoop = FALSE;

    public HardwareManager() {
        

        // By default we start in emulation mode
        //emulation = Boolean.TRUE;
        //myGQGMC = new GQGMCsim("/dev/gqgmc");
        this.emulation = Boolean.FALSE;
        myGQGMC = new GQGMC320("/dev/gqgmc");

        logger.getLogger(HardwareManager.class.getName()).log(Level.INFO, "Try to open the serial connection.");
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
        logger.getLogger(HardwareManager.class.getName()).log(Level.INFO, "this.version) [{0}]", this.version);
    }

    /**
     *
     * @return False : no emulation
     * @return True : Emulate a Geiger Counter
     */
    public boolean getEmulation() {
        logger.getLogger(HardwareManager.class.getName()).log(Level.INFO, "In the method getEmulation()");
        return this.emulation;
    }
    
      public boolean isEmulation() {
        logger.getLogger(HardwareManager.class.getName()).log(Level.INFO, "In the method isEmulation()");
        return this.emulation;
    }

    @Lock(LockType.WRITE)
    public void setEmulation(boolean newEmulation) {
        logger.getLogger(HardwareManager.class.getName()).log(Level.INFO, "In the method setEmulation()");
        this.emulation = newEmulation;
        /*
        if (this.emulation == FALSE) {
            myGQGMC = new GQGMC320("/dev/gqgmc");
        } */
    }

    @Lock(LockType.WRITE)
    public int getCPM() {
        logger.getLogger(HardwareManager.class.getName()).log(Level.INFO, "In the method getCPM()()");
        this.cpm = myGQGMC.getCPM();
        return this.cpm;
    }

    /**
     *
     * @return
     */
    @Lock(LockType.WRITE)
    public int getCPS() {
         logger.getLogger(HardwareManager.class.getName()).log(Level.INFO, "In the method getCPS()");
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
     * @param topCounting
     */
    public void setCounting(boolean topCounting) {
        this.countingLoop = topCounting;

    }

    /**
     *
     * @return
     */
    public boolean isCounting() {
        return this.countingLoop;

    }

    /**
     *
     * @return
     */
    public boolean getCounting() {
        return this.countingLoop;

    }

    public String getSerialNumber() {
        return myGQGMC.getSerialNumber();
    }
    
    public String getRevision(){
        this.revision = myGQGMC.getRevision();
        return this.revision;
    }
    
    public float getVoltage(){
        return myGQGMC.getVoltage();
    }
    
    public float getTemp(){
        return myGQGMC.getTemp();
    }
}

