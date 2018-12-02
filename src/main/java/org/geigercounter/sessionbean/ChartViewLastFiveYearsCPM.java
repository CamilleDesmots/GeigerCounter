/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geigercounter.sessionbean;

import java.io.Serializable;
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
 * Display last five years of CPM.
 * It was just for testing the capacity of LineChartModel with a big set.
 *
 * @author Camille Desmots
 */
@ApplicationScoped
@Named
@Singleton
public class ChartViewLastFiveYearsCPM implements Serializable {

    private LineChartModel lineChartModel;
    private LineChartSeries lineChartSeries;

    private static final DateTimeFormatter DATETIMEFORMATTER1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final Logger LOGGER = Logger.getLogger(ChartViewLastFiveYearsCPM.class.getName());

    @Inject
    private DataProvider1 dataProvider;

    void ChartViewLastFiveYearsCPM() {
//        LOGGER.log(Level.FINEST, "In the method ChartViewLastFiveYearsCPM()");
    }

    @PostConstruct
    void init() {
//        LOGGER.log(Level.FINEST, "In the method init()");

        this.lineChartSeries = new LineChartSeries();
        this.lineChartSeries.setLabel("Count Per Minutes");

        this.lineChartModel_init();
    }

    public void lineChartModel_init() {
//        LOGGER.log(Level.FINEST, "In the method lineChartModel_init().");
        this.lineChartModel = new LineChartModel();
        this.lineChartModel.setTitle("Last five years CPM");
        this.lineChartModel.setLegendPosition("w");
        this.lineChartModel.setZoom(true);
        this.lineChartModel.getAxis(AxisType.Y).setLabel("CPM");

        DateAxis axis = new DateAxis("Date");
        axis.setTickAngle(85);
        // "%y/%m/%d %H:%M"
        axis.setTickFormat("%y/%m/%d %H:%M:%S");

        this.lineChartModel.getAxes().put(AxisType.X, axis);

        this.lineChartModel.addSeries(this.lineChartSeries);
    }

    /**
     * Return last five years CPM on the LineCHarModel
     * @return 
     */
    public LineChartModel getLineChartModel() {
//        LOGGER.log(Level.FINEST, "In the method getLineChartModel().");
        List<Cpm> test = dataProvider.getList();
//        LOGGER.log(Level.FINEST, "Number of records retrieved by dataProvider.getLastFiveYears() {0}", test.stream().count());

        Map<Object, Number> resultMap = test
                .stream()
                .sorted(Comparator.comparing(cpm -> cpm.getCpmPK().getTimestamp().format(DATETIMEFORMATTER1)))
                .collect(Collectors.toMap(cpm -> cpm.getCpmPK().getTimestamp().format(DATETIMEFORMATTER1), cpm -> cpm.getCpm()));

        if (!resultMap.isEmpty()) {
            this.lineChartSeries.setData(resultMap);
        }

//        LOGGER.log(Level.INFO, "Number of records returned {0}", resultMap.size());

        return this.lineChartModel;
    }
}