/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geigercounter.sessionbean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.geigercounter.entity.Cpm;
import org.geigercounter.entity.CpmPK;
import org.geigercounter.entity.Hardware;
import org.geigercounter.jsf.HardwareController;

/**
 *
 * @author camilledesmots
 */
@Stateless
public class StatelessEntityManipulation {
    
    // The id of the current hardware in table Hardware
    private Short hardwareId;

    //@Inject
    //private CpmController cpmController;
    @PersistenceContext
    protected EntityManager em1;
    
    @Inject
    private HardwareFacade hardwareFacade;
    
    @Inject 
    private HardwareController hardwareController;

    public void StatelessEntityManipulation() {

    }

    private static final Logger LOGGER = Logger.getLogger(StatelessEntityManipulation.class.getName());

    public void CreateCpm(Short hardwareId, LocalDateTime timestamp, Integer cpmValue) {
        LOGGER.log(Level.INFO, "in method CreateCpm");

        CpmPK cpmPK1;
        cpmPK1 = new CpmPK();
        cpmPK1.sethardwareid(hardwareId);
        cpmPK1.setTimestamp(timestamp);

        Cpm cpm1;
        cpm1 = new Cpm();

        cpm1.setCpm(cpmValue);
        cpm1.setCpmPK(cpmPK1);

        em1.persist(cpm1);
        
        LOGGER.log(Level.FINE,
                "New CPM created hardwareID:{0} timestamp:{1} CPM:{2}",
                new Object[]{hardwareId, timestamp, cpmValue});
        
    }
    
    public Short CreateHardware(String version, String serialNumber) {
        LOGGER.log(Level.FINE, "In method CreateHardware");
    

        EntityManager em = hardwareFacade.getEntityManager();
        Query query1 = em.createNamedQuery("Hardware.findByVersionAndSerialnumber");
        query1.setParameter("version", version);
        query1.setParameter("serialnumber", serialNumber);
        Hardware hardware1;
        List list1;
        list1 = query1.getResultList();

        if (list1.isEmpty()) {
            LOGGER.log(Level.FINE, "The hardware was not yet in the database");

            // Look for a new hardwareID
            Query query2 = em.createNamedQuery("Hardware.getMaxHardwareID");
            List list2;
            list2 = query2.getResultList();
            if (list2.isEmpty()) {
                LOGGER.log(Level.INFO, "No hardware found in the database.");
                this.hardwareId = 0;
            } else {
                LOGGER.log(Level.FINE, "list2: {0}", list2.toString());
                Short hardwareId1;
                hardwareId1 = (Short) list2.get(0);
                if (hardwareId1 == null) {
                    hardwareId1 = 0;
                }
                LOGGER.log(Level.FINE, "hardwareID1:{0}", hardwareId1);
                this.hardwareId = hardwareId1++;
            }
            hardware1 = hardwareController.prepareCreate();
            hardware1.setHardwareid(this.hardwareId);
            hardware1.setVersion(version);
            hardware1.setSerialnumber(serialNumber);
            hardwareController.create();

            LOGGER.log(Level.INFO, "The hardware succesfully created in database with hardwareID:{0}", this.hardwareId);
        } else {
            hardware1 = (Hardware) list1.get(0);
            this.hardwareId = hardware1.getHardwareid();
        }
        return this.hardwareId;
    }
}
