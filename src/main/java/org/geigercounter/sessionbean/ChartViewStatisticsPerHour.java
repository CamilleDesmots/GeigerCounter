/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geigercounter.sessionbean;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.DoubleSummaryStatistics;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
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
public class ChartViewStatisticsPerHour implements Serializable {

    private LineChartModel lineChartModel;
    private LineChartSeries lineChartSeriesAveragePerHour;
    private LineChartSeries lineChartSeriesMaxPerHour;
    private LineChartSeries lineChartSeriesMinPerHour;
    private DateAxis axisX;

    // For the timestamp
    private static final DateTimeFormatter DATETIMEFORMATTER1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final DateTimeFormatter DATETIMEFORMATTER2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final Logger LOGGER = Logger.getLogger(ChartViewStatisticsPerHour.class.getName());

    @Inject
    private DataProvider13 dataProvider;

    void ChartViewStatisticsPerHour() {
        // LOGGER.log(Level.INFO, "In the method ChartViewLastMonth() of class ChartViewLastMonth.");
    }

    @PostConstruct
    void init() {
        // LOGGER.log(Level.INFO, "In the method init() of ChartViewLastMonth.");

        this.lineChartSeriesAveragePerHour = new LineChartSeries();
        this.lineChartSeriesAveragePerHour.setLabel("Average per hour of CPM");

        this.lineChartSeriesMaxPerHour = new LineChartSeries();
        this.lineChartSeriesMaxPerHour.setLabel("Max per hour of CPM");

        this.lineChartSeriesMinPerHour = new LineChartSeries();
        this.lineChartSeriesMinPerHour.setLabel("Min per hour of CPM");

        this.lineChartModel_init();
    }

    public void lineChartModel_init() {
        // LOGGER.log(Level.INFO, "In the method lineCharModel_init().");
        this.lineChartModel = new LineChartModel();
        this.lineChartModel.setTitle("Last 7 days statistics per hour about CPM");
        this.lineChartModel.setLegendPosition("w");
        this.lineChartModel.setZoom(true);
        this.lineChartModel.getAxis(AxisType.Y).setLabel("CPM");

        this.axisX = new DateAxis("Date");
        this.axisX.setTickAngle(85);
        this.axisX.setTickFormat("%m/%d %H");
        // We divide into into 7 verticals divisions 
        this.axisX.setTickCount(7);

        this.lineChartModel.getAxes().put(AxisType.X, this.axisX);

        this.lineChartModel.addSeries(this.lineChartSeriesAveragePerHour);
        this.lineChartModel.addSeries(this.lineChartSeriesMaxPerHour);
        this.lineChartModel.addSeries(this.lineChartSeriesMinPerHour);
    }

    public LineChartModel getLineChartModel() {
        // LOGGER.log(Level.INFO, "In the method getLineChartModel().");

        Map<LocalDateTime, DoubleSummaryStatistics> statisticsPerHour = dataProvider.getStatisticsPerHour();
        // LOGGER.log(Level.INFO, "Number of records retrieved by dataProvider.getLastTwohours() {0}", statisticsPerHour.size());

        // Average per hour
        Map<Object, Number> resultAveragePerHour;
        resultAveragePerHour = statisticsPerHour.entrySet().stream()
                .collect(Collectors.toMap(x -> x.getKey().format(DATETIMEFORMATTER1),
                        x -> x.getValue().getAverage()));

        if (!resultAveragePerHour.isEmpty()) {
            this.lineChartSeriesAveragePerHour.setData(resultAveragePerHour);
        }

        // Max per hour  
        Map<Object, Number> resultMaxPerHour;
        resultMaxPerHour = statisticsPerHour.entrySet().stream()
                .collect(Collectors.toMap(x -> x.getKey().format(DATETIMEFORMATTER1),
                        x -> x.getValue().getMax()));

        if (!resultMaxPerHour.isEmpty()) {
            this.lineChartSeriesMaxPerHour.setData(resultMaxPerHour);
        }

        // Min per hour  
        Map<Object, Number> resultMinPerHour;
        resultMinPerHour = statisticsPerHour.entrySet().stream()
                .collect(Collectors.toMap(x -> x.getKey().format(DATETIMEFORMATTER1),
                        x -> x.getValue().getMin()));

        if (!resultMinPerHour.isEmpty()) {
            this.lineChartSeriesMinPerHour.setData(resultMinPerHour);
        }

        //LOGGER.log(Level.INFO, "Number of records returned {0}", resultMap.size());
        this.axisX.setMin(LocalDateTime.now().minusMinutes(1).minusDays(7).format(DATETIMEFORMATTER2));

        return this.lineChartModel;
    }
}