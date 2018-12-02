/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geigercounter.sessionbean;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.geigercounter.entity.Cpm;

/**
 * Display statistics per Day
 *
 * @author camilledesmots
 */
@Named
@Singleton
@ApplicationScoped
public class DataProvider14 implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DataProvider14.class.getName());
    // For grouping by hour
    private static final DateTimeFormatter DATETIMEFORMATTER1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");

    private Map <LocalDateTime, DoubleSummaryStatistics> cpmStatisticsPerDay;
    private LocalDateTime dateBegin;
    private LocalDateTime dateEnd;
    private final Duration duration;

    // HardwareID of the current Harware used
    private final Short hardwareID;

    @Inject
    private CpmFacade cpmFacade;

    @Inject
    private DataProvider1 dataProvider1;

    @PersistenceContext
    EntityManager em;

    public DataProvider14() {
        LOGGER.log(Level.FINEST, "In the method DataProvider13().");

        // TODO Look for the current ID of the active hardware (check HardwareManager).
        this.hardwareID = 0;

        duration = Duration.ofDays(30);
        
        this.cpmStatisticsPerDay = new HashMap <>();
    }

    @PostConstruct
    private void init() {
        LOGGER.log(Level.FINEST, "In the method init().");
    }

    /**
     * Update the inner map with statistics from the last time
     */
    private void update() {
        // LOGGER.log(Level.FINEST, "In the method update().");

        LocalDateTime newDateBegin;

        LocalDateTime newDateEnd;
        newDateEnd = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);

        if (this.dateEnd == null) {
            newDateBegin = newDateEnd.minus(this.duration);
        } else {
            newDateBegin = this.dateEnd;
        }

        if (newDateEnd.minusMinutes(1).isAfter(newDateBegin)) {
            // LOGGER.log(Level.FINEST, "More than one minute between {0} and {1}. We can update data.", new Object[]{newDateBegin, newDateEnd});

            List<Cpm> newListCpm;
            newListCpm = this.dataProvider1.getListBetween(newDateBegin, newDateEnd);
            // LOGGER.log(Level.FINEST, "number of elements in newListCpm {0}.", new Object[]{newListCpm.size()});
            if (newListCpm.isEmpty() == false) {
                Map <LocalDateTime, DoubleSummaryStatistics> summaryStatisticsByHour;
                summaryStatisticsByHour = newListCpm
                        .stream()
                        .collect(Collectors.groupingBy(
                                cpm -> cpm.getCpmPK().getTimestamp().truncatedTo(ChronoUnit.HOURS),
                                Collectors.summarizingDouble(cpm -> cpm.getCpm())));
                
                // LOGGER.log(Level.FINEST, "Number of elements in summaryStatisticsByHour {0}.", new Object[]{summaryStatisticsByHour.size()});
                // LOGGER.log(Level.FINEST, "Content of map summaryStatisticsByHour {0}.", new Object[]{summaryStatisticsByHour.toString()});
                this.cpmStatisticsPerDay.putAll(summaryStatisticsByHour);
                // LOGGER.log(Level.FINEST, "After this.cpmStatisticsPerDay.putAll(summaryStatisticsByHour).");
            }

            this.dateBegin = newDateEnd.minus(this.duration);
            this.dateEnd = newDateEnd;

            // Delete data before date dateMinimum
            boolean removed;
            removed = this.cpmStatisticsPerDay
                    .keySet().removeIf(p -> {
                        return p.isBefore(this.dateBegin);
                    });

        }
        // LOGGER.log(Level.FINEST, "The list has {2} records for the period [{0}...{1}] {2}", new Object[]{this.dateBegin, this.dateEnd, this.cpmStatisticsPerDayAverage.keySet().stream().count()});
    }

    /**
     * Return statistics per hour with SummaryStatistics
     * @return Map &lt;LocalDateTime, DoubleSummaryStatistics&gt;
     */
    public Map <LocalDateTime, DoubleSummaryStatistics> getStatisticsPerDay(){
        this.update();
        // LOGGER.log(Level.FINEST, "Number of elements in this.cpmStatisticsPerDay {0}.", new Object[]{this.cpmStatisticsPerDay.size()});
        return this.cpmStatisticsPerDay;
    }
}
