/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geigercounter.sessionbean;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.geigercounter.entity.Cpm;

/**
 * Display last week CPM statistiques
 * @author camilledesmots
 */
@Named
@Singleton
@ApplicationScoped
public class DataProvider12 implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DataProvider12.class.getName());
 
    private final List<Cpm> listCpm;
    private LocalDateTime dateBegin;
    private LocalDateTime dateEnd;
    private final Duration duration;

    // HardwareID of the current Harware used
    private final Short hardwareID;

    @Inject
    private CpmFacade cpmFacade;
    
    @Inject
    private DataProvider1 dataProviderLastFiveYears; 
    
    @PersistenceContext
    EntityManager em;

    public DataProvider12() {
        LOGGER.log(Level.FINEST, "In the method DataProvider12().");
      
        // TODO Look for the current ID of the active hardware (check HardwareManager).
        this.hardwareID = 0;
  
        duration = Duration.ofDays(7);
     
        this.listCpm = new ArrayList<>();
    }

    //@PostConstruct
    private void init() {
        LOGGER.log(Level.FINEST, "In the method init().");
    }

    private void update() {
        LOGGER.log(Level.FINEST, "In the method update().");
        
        LocalDateTime newDateBegin;
        
        LocalDateTime newDateEnd;
        newDateEnd = LocalDateTime.now();

        if (this.dateEnd == null) {
            newDateBegin = newDateEnd.minus(this.duration);
        } else {
            newDateBegin = this.dateEnd;
        }

        if (newDateEnd.minusMinutes(1).isAfter(newDateBegin)) {
            LOGGER.log(Level.FINEST, "More than one minute between {0} and {1}. We can update data.", new Object[]{newDateBegin, newDateEnd});

            List<Cpm> newListCpm;
            newListCpm = this.dataProviderLastFiveYears.getListBetween(newDateBegin, newDateEnd);           
            
            if (newListCpm.isEmpty() == false) {
                this.listCpm.addAll(newListCpm);
            }
            
            this.dateBegin = newDateEnd.minus(duration);
            this.dateEnd = newDateEnd;
            
            // Delete data before date dateMinimum
            boolean removed;
            removed = this.listCpm.removeIf(p -> {
                return p.getCpmPK().getTimestamp().compareTo(this.dateBegin) < 0;
            });
        }
        LOGGER.log(Level.FINEST, "The list has {2} records for the period [{0}...{1}] {2}", new Object[]{this.dateBegin, this.dateEnd, this.listCpm.stream().count()});
    }

    /**
     * Return a list of CPM
     * @return 
     */
    public List<Cpm> getList() {
        this.update();
        return this.listCpm;
    }
}
