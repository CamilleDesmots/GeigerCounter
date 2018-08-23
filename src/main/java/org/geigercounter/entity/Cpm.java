/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geigercounter.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author camilledesmots
 */
@Entity
@Table(name = "CPM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cpm.findAll", 
            query = "SELECT c FROM Cpm c")
    , @NamedQuery(name = "Cpm.findByHardwareid", 
            query = "SELECT c FROM Cpm c WHERE c.cpmPK.hardwareid = :hardwareid")
    , @NamedQuery(name = "Cpm.findByTimestamp", 
            query = "SELECT c FROM Cpm c WHERE c.cpmPK.timestamp = :timestamp")
    , @NamedQuery(name = "Cpm.findByCpm", 
            query = "SELECT c FROM Cpm c WHERE c.cpm = :cpm")
    , @NamedQuery(name = "Cpm.countAll", 
            query ="SELECT count(c) FROM Cpm c") 
    , @NamedQuery(name = "Cpm.findTimestampBetweenAndHardwarid", 
            query ="SELECT c FROM Cpm c WHERE c.cpmPK.hardwareid = :hardwareid and C.cpmPK.timestamp BETWEEN :timestampBegin AND :timestampEnd")
    , @NamedQuery(name = "Cpm.findStatBetweenAndHardwarid",
            query ="SELECT SUBSTRING(c.cpmPK.timestamp,1,16), MIN(c.cpm), MAX(c.cpm), AVG(c.cpm) FROM Cpm c WHERE c.cpmPK.hardwareid = :hardwareid and C.cpmPK.timestamp BETWEEN :timestampBegin AND :timestampEnd GROUP BY SUBSTRING(c.cpmPK.timestamp,1,16)")    
})
public class Cpm implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CpmPK cpmPK;
    @Column(name = "CPM")
    private Integer cpm;

    public Cpm() {
    }

    public Cpm(CpmPK cpmPK) {
        this.cpmPK = cpmPK;
    }

    public Cpm(short hardwareid, Date timestamp) {
        this.cpmPK = new CpmPK(hardwareid, timestamp);
    }

    public CpmPK getCpmPK() {
        return cpmPK;
    }

    public void setCpmPK(CpmPK cpmPK) {
        this.cpmPK = cpmPK;
    }

    public Integer getCpm() {
        return cpm;
    }

    public void setCpm(Integer cpm) {
        this.cpm = cpm;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cpmPK != null ? cpmPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cpm)) {
            return false;
        }
        Cpm other = (Cpm) object;
        if ((this.cpmPK == null && other.cpmPK != null) || (this.cpmPK != null && !this.cpmPK.equals(other.cpmPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.geigercounter.entity.Cpm[ cpmPK=" + cpmPK + " ]";
    }
    
}
