/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geigercounter.sessionbean;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
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
import org.geigercounter.entity.Cpm;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

/**
 * Display last two hours CPM
 *
 * @author Camille Desmots
 */
@ApplicationScoped
@Named
@Singleton
public class ChartViewLastWeekCPM implements Serializable {

    private LineChartModel lineChartModel;
    private LineChartSeries lineChartSeries;
    private DateAxis axisX;

    // For the timestamp
    private static final DateTimeFormatter DATETIMEFORMATTER1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final DateTimeFormatter DATETIMEFORMATTER2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final Logger LOGGER = Logger.getLogger(ChartViewLastWeekCPM.class.getName());

    @Inject
    private DataProvider12 dataProvider;

    void ChartViewLastWeekCPM() {
        LOGGER.log(Level.INFO, "In the method ChartViewLastMonth().");
    }

    @PostConstruct
    void init() {
        LOGGER.log(Level.INFO, "In the method init().");

        this.lineChartSeries = new LineChartSeries();
        this.lineChartSeries.setLabel("Count Per Minutes");

        this.lineChartModel_init();
    }

    public void lineChartModel_init() {
        LOGGER.log(Level.INFO, "In the method lineCharModel_init().");
        this.lineChartModel = new LineChartModel();
        this.lineChartModel.setTitle("Last 7 days statitics per hour of CPM");
        this.lineChartModel.setLegendPosition("w");
        this.lineChartModel.setZoom(true);
        this.lineChartModel.getAxis(AxisType.Y).setLabel("CPM");

        this.axisX = new DateAxis("Date");
        this.axisX.setTickAngle(85);
        this.axisX.setTickFormat("%y/%m/%d %H:%M");
        // We divide into into 7 verticals divisions 
        this.axisX.setTickCount(7);

        this.lineChartModel.getAxes().put(AxisType.X, this.axisX);

        this.lineChartModel.addSeries(this.lineChartSeries);
    }

    public LineChartModel getLineChartModel() {
        LOGGER.log(Level.INFO, "In the method getLineChartModel().");

        List<Cpm> test = dataProvider.getList();
//        LOGGER.log(Level.INFO, "Number of records retrieved by dataProvider.getLastTwohours() {0}", test.stream().count());

        Map<Object, Number> resultMap = test
                .stream()
                .sorted(Comparator.comparing(cpm -> cpm.getCpmPK().getTimestamp().format(DATETIMEFORMATTER1)))
                .collect(Collectors.toMap(cpm -> cpm.getCpmPK().getTimestamp().format(DATETIMEFORMATTER1), cpm -> cpm.getCpm()));

//        LOGGER.log(Level.INFO,"Number of records in resultMap {0}", resultMap.size());
        if (!resultMap.isEmpty()) {
            this.lineChartSeries.setData(resultMap);
        }

//        LOGGER.log(Level.INFO, "Number of records returned {0}", resultMap.size());
        this.axisX.setMin(LocalDateTime.now().minusMinutes(1).minusDays(7).format(DATETIMEFORMATTER2));

        return this.lineChartModel;
    }
}