/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geigercounter.data;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cdesmots-ext
 */
@Entity
@Table(name = "HARDWARE", catalog = "", schema = "GEIGERCOUNTER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Hardware.findAll", query = "SELECT h FROM Hardware h")
    , @NamedQuery(name = "Hardware.findByHardwareid", query = "SELECT h FROM Hardware h WHERE h.hardwareid = :hardwareid")
    , @NamedQuery(name = "Hardware.findByVersion", query = "SELECT h FROM Hardware h WHERE h.version = :version")
    , @NamedQuery(name = "Hardware.findBySerialnumber", query = "SELECT h FROM Hardware h WHERE h.serialnumber = :serialnumber")})
public class Hardware implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "HARDWAREID")
    private Short hardwareid;
    @Size(max = 14)
    @Column(name = "VERSION")
    private String version;
    @Size(max = 25)
    @Column(name = "SERIALNUMBER")
    private String serialnumber;

    public Hardware() {
    }

    public Hardware(Short hardwareid) {
        this.hardwareid = hardwareid;
    }

    public Short getHardwareid() {
        return hardwareid;
    }

    public void setHardwareid(Short hardwareid) {
        this.hardwareid = hardwareid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSerialnumber() {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hardwareid != null ? hardwareid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Hardware)) {
            return false;
        }
        Hardware other = (Hardware) object;
        if ((this.hardwareid == null && other.hardwareid != null) || (this.hardwareid != null && !this.hardwareid.equals(other.hardwareid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.geigercounter.data.Hardware[ hardwareid=" + hardwareid + " ]";
    }
    
}
