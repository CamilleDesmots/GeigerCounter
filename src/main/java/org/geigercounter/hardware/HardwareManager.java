/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geigercounter.hardware;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author camilledesmots
 */
@Singleton
//@ApplicationScoped
//@SessionScoped
//@ManagedBean
@Named("hardwareManager")

public class HardwareManager implements Serializable {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private boolean emulation;
    // Nombre d'interrogations depuis le démarrage
    private long requestedCPM;
    private int cpm;
    private int cps;
    private String version;
    private String revision;
    private String serialNumber;
    private boolean countingCPM;
    
    private static final Logger logger = Logger.getLogger(Object.class.getName());
    private final GQGMCInterface myGQGMC;
    
    // GQ-GMC320
    //@Inject GQGMC320 myGQGMC320;
    //private final GQGMC320 myGQGMC320;
    
    @PersistenceContext
    EntityManager em;


    public HardwareManager() {
        this.requestedCPM = 0;
        this.countingCPM = Boolean.FALSE;
        logger.log(Level.INFO, "In HardwareManager() contrusctor this.countingCPM = {0}.",this.countingCPM);
        // By default we start in emulation mode
        this.emulation = Boolean.TRUE;
        myGQGMC = new GQGMCsim("/dev/gqgmc");
        
        //this.emulation = Boolean.FALSE;
        //myGQGMC = new GQGMC320("/dev/gqgmc");

        logger.log(Level.INFO, "Try to open the serial connection.");
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
        logger.log(Level.INFO, "this.version) [{0}]", this.version);
        
        this.serialNumber = myGQGMC.getSerialNumber();
        logger.log(Level.INFO, "this.serialNumber) [{0}]", this.serialNumber);        
    }

        /**
     *
     * @return False : no emulation, and True : Emulate a Geiger Counter
     */
    public boolean getEmulation() {
        logger.log(Level.INFO, "In the method getEmulation()");
        return this.emulation;
    }
    public boolean isEmulation() {
        logger.log(Level.INFO, "In the method isEmulation()");
        return this.emulation;
    }

    public void setEmulation(boolean newEmulation) {
        logger.log(Level.INFO, "In the method setEmulation()");
        this.emulation = newEmulation;
        /*
        if (this.emulation == FALSE) {
            myGQGMC = new GQGMC320("/dev/gqgmc");
        } */
    }

    @Lock(LockType.WRITE)
    public int getCPM() {
        logger.log(Level.INFO, "In the method getCPM()()");
        this.requestedCPM ++;
        this.cpm = myGQGMC.getCPM();
        return this.cpm;
    }

    /**
     *
     * @return
     */
    @Lock(LockType.WRITE)
    public int getCPS() {
        logger.log(Level.INFO, "In the method getCPS()");
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
    public void setCountingCPM(boolean newCountingCPM) throws InterruptedException {
        logger.log(Level.INFO, "In the method setCountingCPM({0})", newCountingCPM);
        logger.log(Level.INFO, "Current this.countingCPM = {0}", this.countingCPM);
        if (this.countingCPM !=  newCountingCPM ) 
        {
            // We ask for stopping or starting counting every minutes
            if (this.countingCPM == Boolean.TRUE) 
            {
                // We stop counting process
                logger.log(Level.INFO, "Stopping counting every minutes");
                // TODO : Imuplements starting TimerBean loop here. 
            }
            else
            {
                // We start counting process
                logger.log(Level.INFO, "Starting counting every minutes");
                // TODO : Imuplements starting TimerBean loop here. 
            }
            this.countingCPM = newCountingCPM;
        }
            
        
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
            logger.log(Level.INFO, "In the loop of countingCPM()");
        }
    }
    public long getRequestedCPM(){
        return this.requestedCPM;
    }
    
            
}
