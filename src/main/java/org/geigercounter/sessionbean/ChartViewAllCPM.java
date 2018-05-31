/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geigercounter.sessionbean;

import java.io.Serializable;
import java.util.List;
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
public class ChartViewAllCPM implements Serializable  {

    private LineChartModel lineModel1;
    private LineChartModel lineModel2;
    
    @Inject
    private CpmController cpmController;
    
    @Inject
    private CpmFacade cpmFacade;
   
    private static final Logger LOGGER = Logger.getLogger(ChartViewAllCPM.class.getName());
    
        public void ChartViewAllCPM () {
        LOGGER.log(Level.INFO, "In the method ChartViewAllCPM.");
        this.createLineModel1();
        this.createLineModel2();
   
    }
    
    @PostConstruct
    public void init(){
        LOGGER.log(Level.INFO, "In the method init.");
        createLineModel1();
        createLineModel2();
        
    }
    
    private void createLineModel1(){
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


    
        private void createLineModel2(){
        LOGGER.log(Level.INFO, "In the method createLineModel2.");
        lineModel2 = new LineChartModel();
        
        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Count Per Minutes");
        
        // Now we have to load the all data of the table CPM into the series
        
        //EntityManager em = cpmFacade.getEntityManager();
        //Query query1 = em.createNamedQuery("Cpm.findByHarwareid");
        //query1.setParameter("harwareid", 0);
        //List<Cpm> listCpm = query1.getResultList();
        
       LOGGER.log(Level.INFO, "In the method createLineModel2 before loop.");
        //for (Cpm aCpm : listCpm){
        //    series1.set(aCpm.getCpmPK().getTimestamp(), aCpm.getCpm());
           
        //}
        
        // Looking for the smallest CPM
//        EntityManager em = cpmFacade.getEntityManager();
//        Query query1 = em.createNamedQuery("Cpm.findMinCpm");
//        query1.setParameter("harwareID", 0);
//        Integer min = (Integer) query1.getSingleResult();
//        LOGGER.log(Level.INFO, "Min value is {0}",min);
        
        series1.set("2014-01-01", 51);
        series1.set("2014-01-06", 22);
        series1.set("2014-01-12", 65);
        series1.set("2014-01-18", 74);
        series1.set("2014-01-24", 24);
        series1.set("2014-01-30", 51);
        
        lineModel2.addSeries(series1);
        
        lineModel2.setTitle("Radiation Count Per Minutes (Zoom for details)");
        // Put the labels of the serie on the south "s" (middle bottom)
        lineModel2.setLegendPosition("s");
        lineModel2.setZoom(true);
        
        // Y axe
        lineModel2.getAxis(AxisType.Y).setLabel("CPM");
        // X axe
        DateAxis axis = new DateAxis("Dates");
        axis.setTickAngle(-50);
        axis.setMax("2018-06-01");
        axis.setTickFormat("%b %#d, %y");
         
        lineModel2.getAxes().put(AxisType.X, axis);   
    }

          public LineChartModel getLineModel2() {
        LOGGER.log(Level.INFO, "In the method getLineModel2.");
       
        //this.createLineModel2();
        return lineModel2;
    }

}
