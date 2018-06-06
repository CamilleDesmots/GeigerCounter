/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geigercounter.sessionbean;

import java.io.Serializable;
import java.sql.Timestamp;
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
import org.geigercounter.hardware.TimerBeanCPM;
import org.geigercounter.jsf.CpmController;
import org.primefaces.model.chart.Axis;
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
public class ChartViewAllCPM implements Serializable {

    private LineChartModel lineModel1;
    private LineChartModel lineModel2;

    @Inject
    private CpmController cpmController;

    @Inject
    private CpmFacade cpmFacade;

    private static final Logger LOGGER = Logger.getLogger(ChartViewAllCPM.class.getName());

    public void ChartViewAllCPM() {
        LOGGER.log(Level.INFO, "In the method ChartViewAllCPM.");
        this.createLineModel1();
        this.createLineModel2();

    }

    @PostConstruct
    public void init() {
        LOGGER.log(Level.INFO, "In the method init.");
        createLineModel1();
        createLineModel2();

    }

    private void createLineModel1() {
        LOGGER.log(Level.INFO, "In the method createLineModel1.");
        lineModel1 = new LineChartModel();

        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Series 1");

        series1.set(1, 2);
        series1.set(2, 1);
        series1.set(3, 3);
        series1.set(4, 6);
        series1.set(5, 8);

        lineModel1.addSeries(series1);
        lineModel1.setTitle("Linear Chart");
        lineModel1.setLegendPosition("e");
        Axis yAxis = lineModel1.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(10);
    }

    public LineChartModel getLineModel1() {
        LOGGER.log(Level.INFO, "In the method getLineModel1.");

        //this.createLineModel1();
        return lineModel1;
    }

    private void createLineModel2() {
        LOGGER.log(Level.INFO, "In the method createLineModel2.");
        lineModel2 = new LineChartModel();

        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Count Per Minutes");

        // Now we have to load the all data of the table CPM into the series
      
       
        

        //}
        // Set the window of time for timestamp
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        
        //SimpleDateFormat formatTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        Date endDate = cal.getTime();
        // Current minus on month
        cal.add(Calendar.YEAR, -2);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date beginDate = cal.getTime();

        // Retrieve CPM for last month 
        EntityManager em = cpmFacade.getEntityManager();
        Query query1 = em.createNamedQuery("Cpm.findTimestampBetweenAndHardwarid");
        //TODO In the past it should be a parameter
        query1.setParameter("hardwareid", 0);
        query1.setParameter("timestampBegin", beginDate);
        query1.setParameter("timestampEnd", endDate);
        LOGGER.log(Level.INFO, "HardwareID {0}", query1.getParameterValue("hardwareid"));
        LOGGER.log(Level.INFO, "Timestamp window [{0}, {1}]", new Object[]{query1.getParameterValue("timestampBegin"),query1.getParameterValue("timestampEnd") });
        // We limite to 30 records
        query1.setMaxResults(800);
        List <Cpm> cpmList = query1.getResultList();
        LOGGER.log(Level.INFO, "Number of records in found: {0}", cpmList.size());
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        
        LOGGER.log(Level.INFO, "In the method createLineModel2 before loop.");
        for (Cpm aCpm : cpmList) {
            
            /* TODO : Detecter la valeur maximal de CPM
            et  la date max et la date min p pour les axes*/
            //String theDate = dateFormat.format(aCpm.getCpmPK().getTimestamp());
            //Integer theCpm = aCpm.getCpm();
            //LOGGER.log(Level.INFO,"CPm {0} {1}", new Object[]{theDate, theCpm.toString()});
            series1.set(dateFormat.format(aCpm.getCpmPK().getTimestamp()), aCpm.getCpm());
        }


        //series1.set("2014-01-01", 51);
//        series1.set("2014-01-06", 22);
//        series1.set("2014-01-12", 65);
//        series1.set("2014-01-18", 74);
//        series1.set("2014-01-24", 24);
//        series1.set("2014-01-30", 51);

        lineModel2.addSeries(series1);

        lineModel2.setTitle("Radiation Count Per Minutes (Zoom for details)");
        // Put the labels of the serie on the south "s" (middle bottom)
        lineModel2.setLegendPosition("w");
        lineModel2.setZoom(true);

        // Y axe
        lineModel2.getAxis(AxisType.Y).setLabel("CPM");
        // X axe
        DateAxis axis = new DateAxis("Dates");
        axis.setTickAngle(60);
        //axis.setMax("2016-06-02");
        //axis.setTickFormat("%b %#d, %y");
        axis.setTickFormat("%y/%m/%d %H:%M");
        

        lineModel2.getAxes().put(AxisType.X, axis);
    }

    public LineChartModel getLineModel2() {
        LOGGER.log(Level.INFO, "In the method getLineModel2.");

        //this.createLineModel2();
        return lineModel2;
    }

}
