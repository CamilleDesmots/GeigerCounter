/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * ATTENTION !!!
 * For working with Timer your database pool must use the resource type "javax.sql.XADataSource"
 * See : https://javaee.github.io/glassfish/doc/5.0/application-development-guide.pdf
 */
package org.geigercounter.hardware;

import java.time.LocalDateTime;
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
import javax.naming.NamingException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import org.geigercounter.entity.exceptions.RollbackFailureException;
import org.geigercounter.jsf.CpmController;
import org.geigercounter.jsf.HardwareController;
import org.geigercounter.sessionbean.CpmFacade;
import org.geigercounter.sessionbean.HardwareFacade;
import org.geigercounter.sessionbean.StatelessEntityManipulation;

/**
 * @author camilledesmots Singletion that create a timer that ask for CPM every
 * minutes
 */
@Named
@Singleton
@ApplicationScoped
public class TimerBeanCPM {

    @Resource
    TimerService timerService;

    private Timer timerCPM;
    private TimerHandle timerCpmHandle;
    private String timerCpmInfo;

    private LocalDateTime lastProgrammaticTimeout;
    private int lastCPM;

    private boolean countingEveryMinutes;

    private final long oneMinuteDuration = 60000; // 60 000 = 1 minutes

    private static final Logger LOGGER = Logger.getLogger(TimerBeanCPM.class.getName());

    private Short hardwareId;

    @Inject
    private HardwareManager hardwareManager;

    @Inject
    private CpmController cpmController;

    @Inject
    HardwareController hardwareController;

    @Inject
    private HardwareFacade hardwareFacade;

    @Inject
    private CpmFacade cpmFacade;

    @Inject
    private StatelessEntityManipulation statelessEntityManipulation;

    public void TimerBeanCPM() {
        LOGGER.log(Level.INFO, "Instantiate class \"{0}\"", TimerBeanCPM.class.getName());
        // By default the counting loop is not started
        this.countingEveryMinutes = Boolean.FALSE;
        this.timerCpmInfo = "Counting CPM every minutes";
    }
    
    public short getHardwareId(){
        return this.hardwareId;
    }
    
    public void setTimer(long intervalDuration) {
        LOGGER.log(Level.INFO,
                "Setting a programmatic timeout for {0} milliseconds from now.",
                intervalDuration);
        Timer timer = timerService.createTimer(intervalDuration,
                "Created new programmatic timer");
    }

    @Timeout
    //@TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
    public void programmaticTimeout(Timer timer) throws IllegalStateException, SecurityException, SystemException, NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, NamingException {
        // We check if it's the CPM time out

        //LOGGER.log(Level.INFO, "Programmatic timeout occurred. Timer info : {0}", timer.getInfo().toString());
        LOGGER.log(Level.FINE, "Programmatic timeout occurred. This is the CPM timer.");

        this.lastProgrammaticTimeout = LocalDateTime.now();
        this.lastCPM = this.hardwareManager.getCPM();

        LOGGER.log(Level.FINE, "New record Cpm should be hardwareID:{0} Cpm:{1} Timestamp: {2}", new Object[]{this.hardwareId, this.lastCPM, this.lastProgrammaticTimeout});

        statelessEntityManipulation.CreateCpm(this.hardwareId, this.lastProgrammaticTimeout, this.lastCPM);
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
     * @param newValue true for counting every minutes, false otherwise
     * @throws RollbackFailureException If failed do a rollback.
     */
    public void setCountingEveryMinutes(boolean newValue) throws RollbackFailureException {

        LOGGER.log(Level.FINE, "Set countingEveryMinutes from {0} to {1} ", new Object[]{this.countingEveryMinutes, newValue});
        if (this.countingEveryMinutes != newValue) {
            // Either we stop or we start counting
            if (newValue == Boolean.TRUE) {

                // We initiate a programmatic timer for counting every minutes
                LOGGER.log(Level.INFO, "Starting countingEveryMinutes");

                // Create hardware entity
                this.hardwareId = statelessEntityManipulation.CreateHardware(hardwareManager.getVersion(), hardwareManager.getSerialNumber());

                this.timerCPM = timerService.createTimer(this.oneMinuteDuration, this.oneMinuteDuration, this.timerCpmInfo);
                this.timerCpmHandle = this.timerCPM.getHandle();
            } else {
                // We stop counting every minutes
                LOGGER.log(Level.INFO, "Stopping countingEveryMinutes");
                this.timerCPM.cancel();
            }
        }

        this.countingEveryMinutes = newValue;
    }
}
