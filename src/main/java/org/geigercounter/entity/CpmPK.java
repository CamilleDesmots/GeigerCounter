/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geigercounter.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
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
    @Column(name = "hardwareID")
    private short hardwareid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public CpmPK() {
    }

    public CpmPK(short hardwareid, LocalDateTime newLocalDateTime) {
        this.hardwareid = hardwareid;
        this.date = Date.from(newLocalDateTime.atZone(ZoneId.of("Z")).toInstant());
    }

    public short gethardwareid() {
        return hardwareid;
    }

    public void sethardwareid(short hardwareid) {
        this.hardwareid = hardwareid;
    }

    public LocalDateTime getTimestamp() {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.of("Z"));
    }

    public void setTimestamp(LocalDateTime newLocalDateTime) {
        this.date = Date.from(newLocalDateTime.toInstant(ZoneOffset.UTC));
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) hardwareid;
        hash += (date != null ? date.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CpmPK)) {
            return false;
        }
        CpmPK other = (CpmPK) object;
        if (this.hardwareid != other.hardwareid) {
            return false;
        }
        if ((this.date == null && other.date != null) || (this.date != null && !this.date.equals(other.date))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.geigercounter.entity.CpmPK[ hardwareid=" + hardwareid + ", timestamp=" + date + " ]";
    }
    
}
