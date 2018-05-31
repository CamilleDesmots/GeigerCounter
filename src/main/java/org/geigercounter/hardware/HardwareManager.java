/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geigercounter.hardware;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author camilledesmots
 */
@Singleton
@ApplicationScoped
//@SessionScoped
//@ManagedBean
@Named
public class HardwareManager implements Serializable {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private boolean emulation;
    // Nombre d'interrogations depuis le démarrage
    private long requestedCPM;
    private int CPM;
    private int lastCPM;
    private Date lastCPMDate;
    private int cps;
    private String version;
    private String revision;
    private String serialNumber;
    private boolean countingCPM;
    private Timer timer;

    private static final Logger logger = Logger.getLogger(HardwareManager.class.getName());

    private final GQGMCInterface myGQGMC;

    // GQ-GMC320
    //@Inject GQGMC320 myGQGMC320;
    //private final GQGMC320 myGQGMC320;
    @PersistenceContext
    EntityManager em;
    

    public HardwareManager() {
        requestedCPM = 0;
        countingCPM = Boolean.FALSE;
        logger.log(Level.INFO, "Instantiate class \"{0}\"", HardwareManager.class.getName());
        // By default we start in emulation mode
        emulation = Boolean.TRUE;
        myGQGMC = new GQGMCsim("/dev/gqgmc");

        //emulation = Boolean.FALSE;
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

        version = myGQGMC.getVersion();
        logger.log(Level.INFO, "version) [{0}]", version);

        serialNumber = myGQGMC.getSerialNumber();
        logger.log(Level.INFO, "serialNumber) [{0}]", serialNumber);
    }

    /**
     *
     * @return False : no emulation, and True : Emulate a Geiger Counter
     */
    public boolean getEmulation() {
        logger.log(Level.INFO, "In the method getEmulation()");
        return emulation;
    }

    public boolean isEmulation() {
        logger.log(Level.INFO, "In the method isEmulation()");
        return emulation;
    }

    public void setEmulation(boolean newEmulation) {
        logger.log(Level.INFO, "In the method setEmulation()");
        emulation = newEmulation;
        /*
        if (emulation == FALSE) {
            myGQGMC = new GQGMC320("/dev/gqgmc");
        } */
    }

    @Lock(LockType.WRITE)
    public int getCPM() {
        logger.log(Level.INFO, "In the method getCPM()()");
        requestedCPM++;
        CPM = myGQGMC.getCPM();
        lastCPM = CPM;
        lastCPMDate = new Date();
        return CPM;
    }

    /**
     *
     * @return
     */
    @Lock(LockType.WRITE)
    public int getCPS() {
        logger.log(Level.INFO, "In the method getCPS()");
        cps = myGQGMC.getCPS();
        return cps;
    }

    /**
     *
     * @return
     */
    @Lock(LockType.WRITE)
    public String getVersion() {
        version = myGQGMC.getVersion();
        return version;
    }

    /**
     *
     * @param topCounting TRUE for turning on the loop for counting CPM every
     * minutes
     */
    public void setCountingCPM(boolean newCountingCPM) throws InterruptedException {
        logger.log(Level.INFO, "In the method setCountingCPM({0})", newCountingCPM);
        logger.log(Level.INFO, "Current countingCPM = {0}", countingCPM);
        if (countingCPM != newCountingCPM) {
            // We ask for stopping or starting counting every minutes
            if (countingCPM == Boolean.TRUE) {
                // We stop counting process
                logger.log(Level.INFO, "Stopping counting every minutes");
                // TODO : Imuplements starting TimerBean loop here. 
     
            } else {
                // We start counting process
                logger.log(Level.INFO, "Starting counting every minutes");
                //TODO: Implements starting TimerBean loop here.
                
            }
            countingCPM = newCountingCPM;
        }

        if (countingCPM) {
            Logger.getLogger(HardwareManager.class.getName()).log(Level.INFO, "About to run coutingCPM()");
            //countingCPM();
        }
    }

    /**
     *
     * @return TRUE if a loop is counting CPM every minutes
     */
    public boolean setCountingCPM() {
        return countingCPM;
    }

    /**
     *
     * @return TRUE if a loop is counting CPM every minutes
     */
    public boolean getCountingCPM() {
        return countingCPM;
    }

    public String getSerialNumber() {
        return myGQGMC.getSerialNumber();
    }

    public String getRevision() {
        revision = myGQGMC.getRevision();
        return revision;
    }

    public float getVoltage() {
        return myGQGMC.getVoltage();
    }

    public float getTemp() {
        return myGQGMC.getTemp();
    }

    public long getRequestedCPM() {
        return requestedCPM;
    }
    
    public int getLastCPM(){
        return lastCPM;
    }

    public String getLastCPMDate(){
        if (lastCPMDate != null) {
        return lastCPMDate.toString();
        } else{
            return "never";
        }
    }
    
}
