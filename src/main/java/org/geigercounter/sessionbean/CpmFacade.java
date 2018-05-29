/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geigercounter.sessionbean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.geigercounter.entity.Cpm;

/**
 *
 * @author camilledesmots
 */
@Stateless
public class CpmFacade extends AbstractFacade<Cpm> {

    @PersistenceContext(unitName = "org.geigercounter_GeigerCounter_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CpmFacade() {
        super(Cpm.class);
    }
    
}
