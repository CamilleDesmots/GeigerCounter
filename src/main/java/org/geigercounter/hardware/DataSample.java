/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geigercounter.hardware;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.geigercounter.entity.Cpm;
import org.geigercounter.entity.CpmPK;
import org.geigercounter.entity.Hardware;
import org.geigercounter.jsf.CpmController;
import org.geigercounter.jsf.HardwareController;
import org.geigercounter.sessionbean.CpmFacade;

/**
 * Create sample data if the database is empty
 *
 * @author camilledesmots
 */
@Named
@SessionScoped
public class DataSample implements Serializable {

    @Inject
    private CpmController cpmController;

    @Inject
    private HardwareController hardwareController;

    @Inject
    private CpmFacade cpmFacade;

    private static final Logger LOGGER = Logger.getLogger(DataSample.class.getName());
    private boolean populate;

    public void DataSample() {
        LOGGER.log(Level.INFO, "In the method init");

        this.init();

    }

    @PostConstruct
    public void init() {
        LOGGER.log(Level.INFO, "In the method DataSample");
        //TODO Check in the database if Hardware is empty
        List<Hardware> listHardware;
        listHardware = hardwareController.getItemsAvailableSelectMany();

        // Too much data in table 'Cpm' for retrieving a list of entity...
        EntityManager emCpm = cpmFacade.getEntityManager();
        Query query1 = emCpm.createNamedQuery("Cpm.countAll");
        Long cpmCount = (Long) query1.getSingleResult();

        if (listHardware.isEmpty()) {
            this.populate = Boolean.FALSE;
        } else {

            LOGGER.log(Level.INFO, "Number of records in 'Hardware' table: {0}", listHardware.size());
            LOGGER.log(Level.INFO, "Number of records in 'Cpm' table: {0}", cpmCount);
            if (cpmCount > 0) {
                this.populate = Boolean.TRUE;
            }
        }
    }

    /**
     * Check if the database is empty
     *
     * @return Boolean TRUE if database is empty.
     */
    public boolean isPopulate() {
        LOGGER.log(Level.INFO, "In the method getPopulate");
        if (this.populate == Boolean.FALSE) {
            this.init();
        }

        return this.populate;
    }

    /**
     * Populate the database
     * @param newValue true or false
     */
    public void setPopulate(boolean newValue) {
        /**
         * Create an occurence of hardware then create two years of data
         */
        LOGGER.log(Level.INFO, "In the method setPopulate({0})", newValue);
        LOGGER.log(Level.INFO, "In the method setPopulate this.populate {0}", this.populate);
        if (Objects.equals(this.populate, Boolean.FALSE) && newValue == Boolean.TRUE) {
            this.generateSampleData();
        }

    }

    private void generateSampleData() {
        LOGGER.log(Level.INFO, "In the method generateSampleData");
        Hardware hardware;
        hardware = hardwareController.prepareCreate();
        hardware.setHardwareid(Short.parseShort("0"));
        hardware.setVersion("Sample");
        hardware.setSerialnumber("Sample");
        hardwareController.create();

        /**
         * Now we create 1 month of data for every minutes
         */
        //TODO with a random CPM
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.add(Calendar.HOUR, -1);
        // Current time minus one hour
        LocalDateTime todayDate = LocalDateTime.now();

        LocalDateTime beginDate = LocalDateTime.of(todayDate.getYear(), todayDate.getMonth().minus(1), todayDate.getDayOfMonth(), 0, 0);

        Random rand1 = new Random();
        Random rand2 = new Random();
        Integer value1 = 10;
        boolean value2;

        while (beginDate.isBefore(todayDate)) {
            CpmPK cpmPk = new CpmPK();
            cpmPk.sethardwareid(hardware.getHardwareid());
            cpmPk.setTimestamp(beginDate);

            Cpm cpm;
            cpm = cpmController.prepareCreate();
            cpm.setCpmPK(cpmPk);
            //TODO Affect a realistic CPM value randomly obtained
            value2 = rand2.nextBoolean();
            if (value2 == Boolean.TRUE) {
                value1 = value1 + rand1.nextInt(5);
            } else {
                value1 = value1 - rand1.nextInt(5);
            }

            if (value1 <= 0) {
                value1 = 10;
            }

            if (value1 >= 60) {
                value1 = 58;
            }

            cpm.setCpm(value1);
            cpmController.create();

            beginDate = beginDate.plusMinutes(1);

        }

    }

}
