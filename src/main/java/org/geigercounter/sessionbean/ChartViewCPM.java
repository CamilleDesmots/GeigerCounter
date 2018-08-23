/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geigercounter.sessionbean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.geigercounter.entity.Cpm;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

/**
 *
 * @author camilledesmots
 */
@Named
@ApplicationScoped

public class ChartViewCPM implements Serializable {

    // Data for the last hour refreshed every minutes
    private LineChartModel lineChartModel_lastMonth;
    private LineChartSeries lineChartSeries_lastMonth;
    private List<Cpm> listCpm_lastMonth;
    private Date date_lastMonth_begin;
    private Date date_lastMonth_end;
    private SimpleDateFormat dateFormat1;
    private Integer CPMmin, CPMmax;

    private LineChartModel lineChartModel_lastMonthStatisticsByHour;
    private LineChartSeries lineCharSeries_lastMonthMaxByHour;
    private LineChartSeries lineCharSeries_lastMonthMinByHour;
    private LineChartSeries lineCharSeries_lastMonthAverageByHour;

    private SimpleDateFormat dateFormat2;

    // HardwareID of the current Harware used
    private Short hardwareID;

    @Inject
    private CpmFacade cpmFacade;

    private static final Logger LOGGER = Logger.getLogger(ChartViewCPM.class.getName());

    public void ChartViewCPM() {
        LOGGER.log(Level.INFO, "In the method ChartViewCPM.");
        //this.init();
    }

    @PostConstruct
    public void init() {
        LOGGER.log(Level.INFO, "In the method init.");

        // TODO Look for the current ID of the active hardware (check HardwareManager).
        this.hardwareID = 0;
        LOGGER.log(Level.INFO, "Current hardware ID: {0} ", new Object[]{this.hardwareID});

        dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH");

        this.lineChartModel_lastMonth_init();
        this.lineChartSeries_lastMonth_init();

        this.lineChartModel_lastMonthStatisticsByHour_init();
        this.lineChartSeries_lastMonthStatisticsByHour_init();

    }

    public void lineChartModel_lastMonth_init() {
        LOGGER.log(Level.INFO, "In the method lineChartModel_lastMonth_init().");
        this.CPMmax = 0;
        this.CPMmin = 0;

        this.lineChartModel_lastMonth = new LineChartModel();
        this.lineChartModel_lastMonth.setTitle("One month CPM");
        this.lineChartModel_lastMonth.setLegendPosition("w");
        this.lineChartModel_lastMonth.setZoom(true);
        this.lineChartModel_lastMonth.getAxis(AxisType.Y).setLabel("CPM");

        DateAxis axis = new DateAxis("Date");
        axis.setTickAngle(60);
        // "%y/%m/%d %H:%M"
        axis.setTickFormat("%m/%d %H:%M");

        this.lineChartModel_lastMonth.getAxes().put(AxisType.X, axis);

        this.lineChartModel_lastMonth.addSeries(this.lineChartSeries_lastMonth);
    }

    public void lineChartModel_lastMonthStatisticsByHour_init() {
        LOGGER.log(Level.INFO, "In the method {0}", Thread.currentThread().getStackTrace()[1].getMethodName());
        this.lineChartModel_lastMonthStatisticsByHour = new LineChartModel();
        this.lineChartModel_lastMonthStatisticsByHour.setTitle("One month CPM statistics per hours");
        this.lineChartModel_lastMonthStatisticsByHour.setLegendPosition("w");
        this.lineChartModel_lastMonthStatisticsByHour.setZoom(true);
        this.lineChartModel_lastMonthStatisticsByHour.getAxis(AxisType.Y).setLabel("CPM");

        DateAxis axis = new DateAxis("Date");
        axis.setTickAngle(60);
        axis.setTickFormat("%m/%d %H");
        this.lineChartModel_lastMonth.getAxes().put(AxisType.X, axis);

        this.lineChartModel_lastMonthStatisticsByHour.addSeries(this.lineCharSeries_lastMonthMaxByHour);
        this.lineChartModel_lastMonthStatisticsByHour.addSeries(this.lineCharSeries_lastMonthMinByHour);
        this.lineChartModel_lastMonthStatisticsByHour.addSeries(this.lineCharSeries_lastMonthAverageByHour);

    }

    public LineChartModel getLineChartModelLastMonth() {
        LOGGER.log(Level.INFO, "In the method getLineChartLastMonth().");
        // Refresh series of data with data since last call
        this.lineChartSeries_lastMonth_refresh();
        return this.lineChartModel_lastMonth;
    }

    public LineChartModel getLineCharModelStatisticsByHour() {
        // TODO : Add a refresh here
        return this.lineChartModel_lastMonthStatisticsByHour;
    }

    private void lineChartSeries_lastMonth_init() {
        LOGGER.log(Level.INFO, "In the method lineChartSeries_lastMonth_init().");
        this.lineChartSeries_lastMonth = new LineChartSeries();
        this.lineChartSeries_lastMonth.setLabel("Count Per Minutes");

        // Set the end of the one month window.
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        this.date_lastMonth_end = cal.getTime();

        // Set the beginning of the one month window.
        cal.add(Calendar.MONTH, -1);
        this.date_lastMonth_begin = cal.getTime();

        // Look for one month data in database.
        EntityManager em = cpmFacade.getEntityManager();
        Query query1 = em.createNamedQuery("Cpm.findTimestampBetweenAndHardwarid");
        //TODO Ask for the hardware ID with timerBeanCPM.getHardwareId()
        query1.setParameter("hardwareid", this.hardwareID);
        query1.setParameter("timestampBegin", this.date_lastMonth_begin);
        query1.setParameter("timestampEnd", this.date_lastMonth_end);
        // DEBUG 
        //query1.setMaxResults(500);
        this.listCpm_lastMonth = query1.getResultList();
        LOGGER.log(Level.INFO, "Number of records found: {0} for the period [{1},{2}] in table CPM", new Object[]{this.listCpm_lastMonth.size(), this.date_lastMonth_begin, this.date_lastMonth_end});

        for (Cpm aCpm : this.listCpm_lastMonth) {
            if (this.CPMmax < aCpm.getCpm()) {
                this.CPMmax = aCpm.getCpm();
            }

            if (this.CPMmin > aCpm.getCpm()) {
                this.CPMmin = aCpm.getCpm();
            }

            this.lineChartSeries_lastMonth.set(dateFormat1.format(aCpm.getCpmPK().getTimestamp()), aCpm.getCpm());
        }

    }

    private void lineChartSeries_lastMonth_refresh() {
        LOGGER.log(Level.INFO, "In the method lineChartSeries_lastMonth_refresh().");

        Date newDateEnd;
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        newDateEnd = cal.getTime();

        if (newDateEnd.getTime() - this.date_lastMonth_end.getTime() > 6000) {

            LOGGER.log(Level.INFO, "We have to retrieve data for the period [{0},{1}] in table CPM", new Object[]{this.date_lastMonth_end, newDateEnd});

            EntityManager em = cpmFacade.getEntityManager();
            Query query1 = em.createNamedQuery("Cpm.findTimestampBetweenAndHardwarid");
            query1.setParameter("hardwareid", this.hardwareID);
            query1.setParameter("timestampBegin", this.date_lastMonth_end);
            query1.setParameter("timestampEnd", newDateEnd);
            this.listCpm_lastMonth = query1.getResultList();
            LOGGER.log(Level.INFO, "Number of records found: {0} for the period [{1},{2}] in table CPM", new Object[]{this.listCpm_lastMonth.size(), this.date_lastMonth_end, newDateEnd});

            for (Cpm aCpm : this.listCpm_lastMonth) {
                if (this.CPMmax < aCpm.getCpm()) {
                    this.CPMmax = aCpm.getCpm();
                }

                if (this.CPMmin > aCpm.getCpm()) {
                    this.CPMmin = aCpm.getCpm();
                }

                this.lineChartSeries_lastMonth.set(dateFormat1.format(aCpm.getCpmPK().getTimestamp()), aCpm.getCpm());
            }

            this.date_lastMonth_end = newDateEnd;
        } else {
            LOGGER.log(Level.INFO, "No need yet to retrieve data for the period [{0},{1}] in table CPM", new Object[]{this.date_lastMonth_end, newDateEnd});
        }
    }

    /**
     * Intialize the LineChartSeries "lineChartSeries_lastMonthAverageByHour"
     * with last month data. Begin and end date and time should have been
     * already set by method lineChartSeries_lastMonth_init().
     */
    private void lineChartSeries_lastMonthStatisticsByHour_init() {
        LOGGER.log(Level.INFO, "In the method lineChartSeries_lastMonthStatisticsByHour_init().");
        this.lineCharSeries_lastMonthMinByHour = new LineChartSeries();
        this.lineCharSeries_lastMonthMinByHour.setLabel("Min CPM count per hour");

        this.lineCharSeries_lastMonthMaxByHour = new LineChartSeries();
        this.lineCharSeries_lastMonthMaxByHour.setLabel("Max CPM count per hour");

        this.lineCharSeries_lastMonthAverageByHour = new LineChartSeries();
        this.lineCharSeries_lastMonthAverageByHour.setLabel("Average CPM count per hour");

        EntityManager em = cpmFacade.getEntityManager();
        Query query1 = em.createNamedQuery("Cpm.findStatBetweenAndHardwarid");
        //TODO Ask for the hardware ID with timerBeanCPM.getHardwareId()
        query1.setParameter("hardwareid", this.hardwareID);
        query1.setParameter("timestampBegin", this.date_lastMonth_begin);
        query1.setParameter("timestampEnd", this.date_lastMonth_end);

        //Local Class that describe the result of the named query
        class CpmStatisticsByHour {

            Date date;
            //Integer hour;
            Integer min;
            Integer max;
            Double average;

            public Date getDate() {
                return this.date;
            }

//            public Integer getHour() {
//                return this.hour;
//            }

            public Integer getMin() {
                return this.min;
            }

            public Integer getMax() {
                return this.max;
            }

            public Double getAverage() {
                return this.average;
            }
        }

        List<CpmStatisticsByHour> resultList;
        resultList = query1.getResultList();
        LOGGER.log(Level.INFO, "Number of records found: {0} for the period [{1},{2}] in table CPM wtih a group by hour", new Object[]{this.listCpm_lastMonth.size(), this.date_lastMonth_begin, this.date_lastMonth_end});

        for (CpmStatisticsByHour aResult : resultList) {
            // We create 3 series
            Calendar aCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            aCal.setTime(aResult.getDate());
            //aCal.set(Calendar.HOUR_OF_DAY, aResult.getHour());
            Date aDate = aCal.getTime();
            this.lineCharSeries_lastMonthMinByHour.set(dateFormat2.format(aDate), aResult.getMin());
            this.lineCharSeries_lastMonthMaxByHour.set(dateFormat2.format(aDate), aResult.getMax());
            this.lineCharSeries_lastMonthAverageByHour.set(dateFormat2.format(aDate), aResult.getAverage());

        }

    }
}
