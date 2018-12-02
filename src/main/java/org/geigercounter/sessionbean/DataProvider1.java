/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geigercounter.sessionbean;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.geigercounter.entity.Cpm;

/**
 *
 * @author camilledesmots
 */
@Named
@Singleton
@ApplicationScoped
public class DataProvider1 implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DataProvider1.class.getName());

    private final List<Cpm> listCpm;
    private LocalDateTime dateBegin;
    private LocalDateTime dateEnd;
    private final Period period;

    // HardwareID of the current Harware used
    private final Short hardwareID;

    @Inject
    private CpmFacade cpmFacade;
    
    @PersistenceContext
    EntityManager em;

    public DataProvider1() {
        LOGGER.log(Level.FINEST, "In the method DataProvider1().");
        
        // TODO Look for the current ID of the active hardware (check HardwareManager).
        this.hardwareID = 0;

        period = Period.ofYears(5);

        dateEnd = null;
        dateBegin = null;

        this.listCpm = new ArrayList<>();
    }

    //@PostConstruct
    private void init() {
        LOGGER.log(Level.FINEST, "In the method init().");

    }

    /**
     * Update the list of the last five years of CPM
     * @return true if datas have been updated. 
     */
    private void update() {
        LOGGER.log(Level.FINEST, "In the method update().");
//        LOGGER.log(Level.INFO, "Number of records for the period [{0}...{1}] {2}", new Object[]{this.dateBegin, this.dateEnd, this.listCpm.stream().count()});

        LocalDateTime newDateBegin;
        
        LocalDateTime newDateEnd;
        newDateEnd = LocalDateTime.now();
        
        if (this.dateEnd == null) {
            newDateBegin = newDateEnd.minus(period);
            this.dateBegin = newDateBegin;
        } else {
            newDateBegin = this.dateEnd;
        }

        if (newDateEnd.minusMinutes(1).isAfter(newDateBegin)) {
//            LOGGER.log(Level.FINEST, "About to retrieve CPM for the period [{0}...{1}]", new Object[]{newDateBegin, newDateEnd});

            Query query1;
            query1 = em.createNamedQuery("Cpm.findDateBetweenAndHardwarid");
            //TODO Ask for the hardware ID with timerBeanCPM.getHardwareId()
            query1.setParameter("hardwareid", this.hardwareID);
            query1.setParameter("dateBegin", Date.from(newDateBegin.toInstant(ZoneOffset.UTC)));
            query1.setParameter("dateEnd", Date.from(newDateEnd.toInstant(ZoneOffset.UTC)));

            List<Cpm> queryResult;
            queryResult = query1.getResultList();

//            LOGGER.log(Level.FINEST, "DataProvider1 Number of records found by the query for the period [{0}...{1}] {2}", new Object[]{newDateBegin, newDateEnd, queryResult.stream().count()});

            this.listCpm.addAll(query1.getResultList());

            /* Delete data in listCpm where data belong 
             * to the period before newDateBegin
             */
            
            this.dateBegin = newDateEnd.minus(period);
            this.dateEnd = newDateEnd;
           
            // Delete data before date dateMinimum
            boolean removed;
            removed = this.listCpm.removeIf(p -> {
                return p.getCpmPK().getTimestamp().compareTo(this.dateBegin) < 0;
            });
        }
//        LOGGER.log(Level.FINEST, "Finaly the list has {2} records for the period [{0}...{1}]", new Object[]{this.dateBegin, this.dateEnd, this.listCpm.stream().count()});  
    }

    /**
     * Get the list of the last five years CPM data
     * @return List of Cpm List of Cpm objects since five years 
     */
    public List<Cpm> getList() {
        LOGGER.log(Level.FINEST, "getList()");
        this.update();
        return this.listCpm;
    }
    
    /**
     * Get the list of data CPM between the date 'dateBegin' and 'dateEnd'specified
     * @param newDateBegin LocalDateTime The beginning of the period we want to extract.
     * @param newDateEnd LocalDateTime the end of the period we want to extract. 
     * If not specified, assumed to 
     * @return 
     */
     public List<Cpm> getListBetween(LocalDateTime newDateBegin, LocalDateTime newDateEnd) {
//         LOGGER.log(Level.FINEST, "DataProvider1::getListBetween({0}, {1})", new Object[]{newDateBegin, newDateEnd});
         boolean removed;
         
//         LOGGER.log(Level.FINEST, "DataProvider1::getListBetween - Current window is ({0}, {1})", new Object[]{this.dateBegin, this.dateEnd});
         if (this.dateEnd == null){
             // We have to update the list
              this.update();
         }
         else {
            if (newDateEnd.isAfter(this.dateEnd)){
                // We have to update the list
                this.update();
            }
         }
        List<Cpm> reducedList;
        
        reducedList = this.listCpm.stream()
                .filter(p -> p.getCpmPK().getTimestamp().compareTo(newDateBegin) >= 0)
                .filter(p -> p.getCpmPK().getTimestamp().compareTo(newDateEnd) <= 0)
                .collect(Collectors.toList());
//        LOGGER.log(Level.FINEST, "getListBetween({0},{1}) will return {2} elemenets in the list.", new Object[]{newDateBegin, newDateEnd, reducedList.stream().count()});
        return reducedList;
    }

     /**
      * Return a List of CPM after the date given.
      * @param dateBegin LocalDateTime
      * @return List of Cpm
      */
     public List<Cpm> getListAfter(LocalDateTime dateBegin ) {
          LocalDateTime newDateEnd;
          newDateEnd = LocalDateTime.now();
          return this.getListBetween(dateBegin, newDateEnd);
      }
}