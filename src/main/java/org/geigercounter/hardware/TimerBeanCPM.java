/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geigercounter.hardware;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerHandle;
import javax.ejb.TimerService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.geigercounter.entity.Cpm;
import org.geigercounter.entity.CpmPK;
import org.geigercounter.entity.Hardware;
import org.geigercounter.entity.exceptions.RollbackFailureException;
import org.geigercounter.jsf.CpmController;
import org.geigercounter.jsf.HardwareController;

/**
 * @author camilledesmots Singletion that create a timer that ask for CPM every
 * minutes
 */
@Named
@Singleton
@ApplicationScoped
//@Startup // If you want it to run at the beginning of the deployement.
public class TimerBeanCPM {

    @Resource
    TimerService timerService;

    private Timer timerCPM;
    private TimerHandle timerCpmHandle;
    private String timerCpmInfo;

    private Date lastProgrammaticTimeout;
    private Date lastAutomaticTimeout;
    private int lastCPM;

    private boolean countingEveryMinutes;

    private final long oneMinuteDuration = 6000;

    private static final Logger LOGGER = Logger.getLogger(TimerBeanCPM.class.getName());

    private Hardware hardware;
    private Cpm cpm;
    private CpmPK cpmPK;

    @Inject
    private HardwareManager hardwareManager;

    @Inject
    private CpmController cpmController;

    @Inject
    HardwareController hardwareController;

    public void TimerBean() {
        LOGGER.log(Level.INFO, "Instantiate class \"{0}\"", Object.class.getName());
        // By default the counting loop is not started
        this.countingEveryMinutes = Boolean.FALSE;
        this.timerCpmInfo = "Counting CPM every minutes";
    }

    public void setTimer(long intervalDuration) {
        LOGGER.log(Level.INFO,
                "Setting a programmatic timeout for {0} milliseconds from now.",
                intervalDuration);
        Timer timer = timerService.createTimer(intervalDuration,
                "Created new programmatic timer");
    }

    @Timeout
    public void programmaticTimeout(Timer timer) {
        // We check if it's the CPM time out
        //LOGGER.log(Level.INFO, "Programmatic timeout occurred. Timer info : {0}", timer.getInfo().toString());
        //if (timer.getHandle() == this.timerCpmHandle) {
        LOGGER.log(Level.INFO, "Programmatic timeout occurred. This is the CPM timer.");

        this.createCPM();
        //}
    }

    //@Schedule(minute = "*/1", hour = "*", persistent = false)
    public void automaticTimeout() {
        this.lastAutomaticTimeout = new Date();
        LOGGER.info("Automatic timeout occurred");

        if (this.countingEveryMinutes == Boolean.TRUE) {
            LOGGER.info("Continue counting loop.");
            this.lastCPM = hardwareManager.getCPM();
            LOGGER.log(Level.INFO, "Last CPM : {0} at {1}", new Object[]{this.lastCPM, this.lastAutomaticTimeout});
            // TODO : insert the serialisation of the data here.

            this.setTimer(this.oneMinuteDuration);
        } else {
            LOGGER.info("Stop counting loop.");
        }
    }

    /**
     * @return the lastTimeout
     */
    public String getLastProgrammaticTimeout() {
        if (lastProgrammaticTimeout != null) {
            return lastProgrammaticTimeout.toString();
        } else {
            return "never";
        }
    }

    /**
     * @param lastTimeout the lastTimeout to set
     */
    public void setLastProgrammaticTimeout(Date lastTimeout) {
        this.lastProgrammaticTimeout = lastTimeout;
    }

    /**
     * @return the lastAutomaticTimeout
     */
    public String getLastAutomaticTimeout() {
        if (lastAutomaticTimeout != null) {
            return lastAutomaticTimeout.toString();
        } else {
            return "never";
        }
    }

    /**
     * @param lastAutomaticTimeout the lastAutomaticTimeout to set
     */
    public void setLastAutomaticTimeout(Date lastAutomaticTimeout) {
        this.lastAutomaticTimeout = lastAutomaticTimeout;
    }

    /**
     *
     * @return boolean TRUE if counting FALSE otherwise
     */
    public boolean getCountingEveryMinutes() {
        return this.countingEveryMinutes;
    }

    /**
     * Enable or Disable the loop for counting every minutes.
     *
     * @param newValue
     */
    public void setCountingEveryMinutes(boolean newValue) throws RollbackFailureException {

        LOGGER.log(Level.INFO, "Set countingEveryMinutes to {0} ", newValue);
        LOGGER.log(Level.INFO, "Set countingEveryMinutes from {0}", this.countingEveryMinutes);

        if (this.countingEveryMinutes != newValue) {
            // Either we stop or we start counting
            if (newValue == Boolean.TRUE) {

                // We start counting every minutes
                LOGGER.log(Level.INFO, "Starting countingEveryMinutes");

                // Create hardware entity
                this.createHardware();

                // TODO : Pourquoi je plante ici ? 
                this.timerCPM = timerService.createTimer(this.oneMinuteDuration, this.oneMinuteDuration, this.timerCpmInfo);
                this.timerCpmHandle = this.timerCPM.getHandle();
            } else {
                // We stop counting every minutes
                LOGGER.log(Level.INFO, "Stopping countingEveryMinutes");
                //TODO: Implements stopping TimerBeanCPM loop here.
                this.timerCPM.cancel();
            }
        }

        this.countingEveryMinutes = newValue;
    }

    private void createHardware() {
        //TODO Check if the harware already exist before creating one...

        //Query query = em.createNamedQuery("Hardware.findByVersionAndSerialnumber");
        //query.setParameter("version", version);
        //query.setParameter("serialnumber", serialNumber);
        //hardware = (Hardware) query.getSingleResult();
        hardware = hardwareController.prepareCreate();
        hardware.setHardwareid(Short.parseShort("2"));
        hardware.setVersion(hardwareManager.getVersion());
        hardware.setSerialnumber(hardwareManager.getSerialNumber());
        hardwareController.create();

    }

    private void createCPM() {
        this.lastProgrammaticTimeout = new Date();

        LOGGER.log(Level.INFO, "Try to create cpm Entity");
        this.lastCPM = this.hardwareManager.getCPM();
        cpm = cpmController.prepareCreate();
        cpm.setCpm(lastCPM);
        cpmPK = new CpmPK();
        LOGGER.log(Level.INFO, "Hardware ID is {0}",hardware.getHardwareid());
        cpmPK.setHarwareid(hardware.getHardwareid());
        cpmPK.setTimestamp(lastAutomaticTimeout);
        cpm.setCpmPK(cpmPK);
        cpmController.create();
    }

}
