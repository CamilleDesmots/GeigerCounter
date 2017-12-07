/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geigercounter.hardware;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author camilledesmots
 */
@Named
@SessionScoped
public class TimerManager implements Serializable {
   @EJB
    private TimerBean timerSession;

    private String lastProgrammaticTimeout;
    private String lastAutomaticTimeout;
 
    /** Creates a new instance of TimerManager */
    public TimerManager() {
        this.lastProgrammaticTimeout = "never";
        this.lastAutomaticTimeout = "never";
    }

    /**
     * @return the lastTimeout
     */
    public String getLastProgrammaticTimeout() {
        lastProgrammaticTimeout = timerSession.getLastProgrammaticTimeout();
        return lastProgrammaticTimeout;
    }

    /**
     * @param lastTimeout the lastTimeout to set
     */
    public void setLastProgrammaticTimeout(String lastTimeout) {
        this.lastProgrammaticTimeout = lastTimeout;
    }

    public void setTimer() {
        long timeoutDuration = 8000;
        timerSession.setTimer(timeoutDuration);
    }

    /**
     * @return the lastAutomaticTimeout
     */
    public String getLastAutomaticTimeout() {
        lastAutomaticTimeout = timerSession.getLastAutomaticTimeout();
        return lastAutomaticTimeout;
    }

    /**
     * @param lastAutomaticTimeout the lastAutomaticTimeout to set
     */
    public void setLastAutomaticTimeout(String lastAutomaticTimeout) {
        this.lastAutomaticTimeout = lastAutomaticTimeout;
    }

}
