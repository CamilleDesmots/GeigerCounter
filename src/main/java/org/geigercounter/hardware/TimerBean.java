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
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.inject.Inject;

/**
 * @author camilledesmots Singletion that create a timer that ask for CPM every
 * minutes
 */
@Singleton
@Startup
public class TimerBean {

    @Resource
    TimerService timerService;

    private Date lastProgrammaticTimeout;
    private Date lastAutomaticTimeout;
    private int lastCPM;

    private static final Logger logger = Logger.getLogger(TimerBean.class.getName());
    
       
    @Inject HardwareManager hardwareManager;

    public void setTimer(long intervalDuration) {
        logger.log(Level.INFO,
                "Setting a programmatic timeout for {0} milliseconds from now.",
                intervalDuration);
        Timer timer = timerService.createTimer(intervalDuration,
                "Created new programmatic timer");
    }

    @Timeout
    public void programmaticTimeout(Timer timer) {
        this.setLastProgrammaticTimeout(new Date());
        logger.info("Programmatic timeout occurred.");
    }

    @Schedule(minute = "*/1", hour = "*", persistent = false)
    public void automaticTimeout() {
        this.setLastAutomaticTimeout(new Date());
        logger.info("Automatic timeout occurred");
        logger.info("Restart");
        //TODO: Insert here the call to getCPM here 
        this.lastCPM = hardwareManager.getCPM();
        logger.log(Level.INFO, "Last CPM : {0} at {1}", new Object[]{this.lastCPM, this.getLastAutomaticTimeout()});
        this.setTimer(8000);
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

}
