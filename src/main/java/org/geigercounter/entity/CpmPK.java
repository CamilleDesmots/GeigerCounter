/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geigercounter.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author camilledesmots
 */
@Embeddable
public class CpmPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "HARWAREID")
    private short harwareid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    public CpmPK() {
    }

    public CpmPK(short harwareid, Date timestamp) {
        this.harwareid = harwareid;
        this.timestamp = timestamp;
    }

    public short getHarwareid() {
        return harwareid;
    }

    public void setHarwareid(short harwareid) {
        this.harwareid = harwareid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) harwareid;
        hash += (timestamp != null ? timestamp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CpmPK)) {
            return false;
        }
        CpmPK other = (CpmPK) object;
        if (this.harwareid != other.harwareid) {
            return false;
        }
        if ((this.timestamp == null && other.timestamp != null) || (this.timestamp != null && !this.timestamp.equals(other.timestamp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.geigercounter.entity.CpmPK[ harwareid=" + harwareid + ", timestamp=" + timestamp + " ]";
    }
    
}
