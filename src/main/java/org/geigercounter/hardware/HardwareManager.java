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
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Timer;
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

    private static final Logger LOGGER = Logger.getLogger(HardwareManager.class.getName());

  
    //@Inject 
    private GQGMC320 GQGMC320;

    //@Inject 
    private GQGMCsim GQGMCsim;    
    
    private GQGMCInterface myGQGMC;
    
    @PersistenceContext
    EntityManager em;
    

    public HardwareManager() {
        LOGGER.log(Level.INFO, "Instantiate class \"{0}\"", HardwareManager.class.getName());
        
        this.requestedCPM = 0;
        countingCPM = Boolean.FALSE;
        
        // By default we start in emulation mode
        emulation = Boolean.TRUE;
        myGQGMC =  new GQGMCsim();
        myGQGMC.setDevicePath("/dev/gqgmc");
        myGQGMC.setConfig();

        //emulation = Boolean.FALSE;
        //myGQGMC = new GQGMC320("/dev/gqgmc");
        LOGGER.log(Level.INFO, "Try to open the serial connection.");
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
        LOGGER.log(Level.INFO, "version) [{0}]", version);

        serialNumber = myGQGMC.getSerialNumber();
        LOGGER.log(Level.INFO, "serialNumber) [{0}]", serialNumber);
    }

    /**
     *
     * @return False : no emulation, and True : Emulate a Geiger Counter
     */
    public boolean getEmulation() {
        LOGGER.log(Level.INFO, "In the method getEmulation()");
        return emulation;
    }

//    public boolean isEmulation() {
//        LOGGER.log(Level.INFO, "In the method isEmulation()");
//        return emulation;
//    }

    public void setEmulation(boolean newEmulation) {
        LOGGER.log(Level.INFO, "In the method setEmulation() old value :{0} new value :{1}", new Object[]{Boolean.toString(this.emulation), Boolean.toString(newEmulation)} );
        
        if (newEmulation != this.emulation) {
            if (newEmulation == Boolean.FALSE) {
                this.myGQGMC = new GQGMC320();
                this.myGQGMC.setDevicePath("/dev/gqgmc");
                this.myGQGMC.setConfig();

                LOGGER.log(Level.INFO, "Try to open the serial connection.");
                this.myGQGMC.setSerialOpen();

                if (this.myGQGMC.getSerialStatus()) {
                    LOGGER.log(Level.INFO, "Succesfully opened serial communication.");

                } else {
                    LOGGER.log(Level.SEVERE, "Failed to open serial communication.");
                }
            } else {
                this.myGQGMC = new GQGMCsim();
                this.myGQGMC.setDevicePath("/dev/gqgmc");
                this.myGQGMC.setConfig();
            }
            this.version = this.myGQGMC.getVersion();
            this.serialNumber = myGQGMC.getSerialNumber();

        }

        emulation = newEmulation;
        
    }

    @Lock(LockType.WRITE)
    public int getCPM() {
        LOGGER.log(Level.INFO, "In the method getCPM()()");
        requestedCPM++;
        CPM = myGQGMC.getCPM();
        lastCPM = CPM;
        lastCPMDate = new Date();
        return CPM;
    }

    /**
     *
     * @return int
     */
    @Lock(LockType.WRITE)
    public int getCPS() {
        LOGGER.log(Level.INFO, "In the method getCPS()");
        cps = myGQGMC.getCPS();
        return cps;
    }

    /**
     *
     * @return String
     */
    @Lock(LockType.WRITE)
    public String getVersion() {
        version = myGQGMC.getVersion();
        return version;
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
