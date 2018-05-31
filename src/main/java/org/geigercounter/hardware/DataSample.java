/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geigercounter.hardware;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.geigercounter.entity.Cpm;
import org.geigercounter.entity.CpmPK;
import org.geigercounter.entity.Hardware;
import org.geigercounter.jsf.CpmController;
import org.geigercounter.jsf.HardwareController;

/**
 * Create sample data if the database is empty
 *
 * @author camilledesmots
 */
@Named
@SessionScoped
public class DataSample implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DataSample.class.getName());

    @Inject
    private CpmController cpmController;

    @Inject
    HardwareController hardwareController;

    /**
     * Check if the database is empty
     *
     * @return Boolean TRUE if database is empty.
     */
    public boolean getPopulate() {
        boolean answer = Boolean.FALSE;

        //TODO Check in the database if Hardware is empty
        List<Hardware> listHardware;
        listHardware = hardwareController.getItemsAvailableSelectMany();
        if (listHardware.isEmpty()){
            answer = Boolean.FALSE;
        } else {
            answer = Boolean.TRUE;
        }
                
        return answer;
    }

    /**
     * Populate the database
     */
    public void setPopulate(boolean newValue) {
        /**
         * Create an occurence of hardware then create two years of data
         */
        LOGGER.log(Level.INFO,"About to generate data sample.");
        Hardware hardware;
        hardware = hardwareController.prepareCreate();
        hardware.setHardwareid(Short.parseShort("0"));
        hardware.setVersion("Sample");
        hardware.setSerialnumber("Sample");
        hardwareController.create();
    
        /**
         * Now we create 2 years of data for every minutes of the past
         * two years
         */
        //TODO with a random CPM
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.add(Calendar.HOUR, -1);
        // Current time minus one hour
        Date todayDate = cal.getTime();
        cal.add(Calendar.YEAR, -2);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND, 0);
        // Two years ago day.
        Date beginDate =  cal.getTime();
        
        Random rand = new Random();
        
        while (beginDate.before(todayDate)){
            CpmPK cpmPk = new CpmPK();
            cpmPk.setHarwareid(hardware.getHardwareid());
            cpmPk.setTimestamp(beginDate);
            
            Cpm cpm;
            cpm = cpmController.prepareCreate();
            cpm.setCpmPK(cpmPk);
            //TODO Affect a realistic CPM value randomly obtained
            cpm.setCpm(rand.nextInt(60));
            cpmController.create();
            cal.add(Calendar.MINUTE, 1);
            beginDate = cal.getTime();
        }
        
    }

}
