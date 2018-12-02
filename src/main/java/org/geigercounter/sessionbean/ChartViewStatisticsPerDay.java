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
public class ChartViewStatisticsPerDay implements Serializable {

    private LineChartModel lineChartModel;
    private LineChartSeries lineChartSeriesAveragePerDay;
    private LineChartSeries lineChartSeriesMaxPerDay;
    private LineChartSeries lineChartSeriesMinPerDay;
    private DateAxis axisX;

    // For the timestamp
    private static final DateTimeFormatter DATETIMEFORMATTER1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final DateTimeFormatter DATETIMEFORMATTER2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final Logger LOGGER = Logger.getLogger(ChartViewStatisticsPerDay.class.getName());

    @Inject
    private DataProvider14 dataProvider;

    void ChartViewStatisticsPerDay() {
        // LOGGER.log(Level.INFO, "In the method ChartViewLastMonth() of class ChartViewLastMonth.");
    }

    @PostConstruct
    void init() {
        // LOGGER.log(Level.INFO, "In the method init() of ChartViewLastMonth.");

        this.lineChartSeriesAveragePerDay = new LineChartSeries();
        this.lineChartSeriesAveragePerDay.setLabel("Average per day of CPM");

        this.lineChartSeriesMaxPerDay = new LineChartSeries();
        this.lineChartSeriesMaxPerDay.setLabel("Max per day of CPM");

        this.lineChartSeriesMinPerDay = new LineChartSeries();
        this.lineChartSeriesMinPerDay.setLabel("Min per day of CPM");

        this.lineChartModel_init();
    }

    public void lineChartModel_init() {
        // LOGGER.log(Level.INFO, "In the method lineCharModel_init().");
        this.lineChartModel = new LineChartModel();
        this.lineChartModel.setTitle("Last month statistics per day about CPM");
        this.lineChartModel.setLegendPosition("w");
        this.lineChartModel.setZoom(true);
        this.lineChartModel.getAxis(AxisType.Y).setLabel("CPM");

        this.axisX = new DateAxis("Date");
        this.axisX.setTickAngle(85);
        this.axisX.setTickFormat("%y/%m/%d");
        // We divide into into 7 verticals divisions 
        this.axisX.setTickCount(31);

        this.lineChartModel.getAxes().put(AxisType.X, this.axisX);

        this.lineChartModel.addSeries(this.lineChartSeriesAveragePerDay);
        this.lineChartModel.addSeries(this.lineChartSeriesMaxPerDay);
        this.lineChartModel.addSeries(this.lineChartSeriesMinPerDay);
    }

    public LineChartModel getLineChartModel() {
        // LOGGER.log(Level.INFO, "In the method getLineChartModel().");

        Map<LocalDateTime, DoubleSummaryStatistics> statisticsPerDay = dataProvider.getStatisticsPerDay();
        // LOGGER.log(Level.INFO, "Number of records retrieved by dataProvider.getLastTwohours() {0}", statisticsPerDay.size());

        // Average per hour
        Map<Object, Number> resultAveragePerDay;
        resultAveragePerDay = statisticsPerDay.entrySet().stream()
                .collect(Collectors.toMap(x -> x.getKey().format(DATETIMEFORMATTER1),
                        x -> x.getValue().getAverage()));

        if (!resultAveragePerDay.isEmpty()) {
            this.lineChartSeriesAveragePerDay.setData(resultAveragePerDay);
        }

        // Max per hour  
        Map<Object, Number> resultMaxPerDay;
        resultMaxPerDay = statisticsPerDay.entrySet().stream()
                .collect(Collectors.toMap(x -> x.getKey().format(DATETIMEFORMATTER1),
                        x -> x.getValue().getMax()));

        if (!resultMaxPerDay.isEmpty()) {
            this.lineChartSeriesMaxPerDay.setData(resultMaxPerDay);
        }

        // Min per hour  
        Map<Object, Number> resultMinPerDay;
        resultMinPerDay = statisticsPerDay.entrySet().stream()
                .collect(Collectors.toMap(x -> x.getKey().format(DATETIMEFORMATTER1),
                        x -> x.getValue().getMin()));

        if (!resultMinPerDay.isEmpty()) {
            this.lineChartSeriesMinPerDay.setData(resultMinPerDay);
        }

        //LOGGER.log(Level.INFO, "Number of records returned {0}", resultMap.size());
        this.axisX.setMin(LocalDateTime.now().minusMinutes(1).minusDays(30).format(DATETIMEFORMATTER2));

        return this.lineChartModel;
    }
}