/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ignacio Marqués
 */
@Entity
@Table(name = "CAMPO_ESTUDIO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CampoEstudio.findAll", query = "SELECT c FROM CampoEstudio c")
    , @NamedQuery(name = "CampoEstudio.findById", query = "SELECT c FROM CampoEstudio c WHERE c.id = :id")
    , @NamedQuery(name = "CampoEstudio.findByNombre", query = "SELECT c FROM CampoEstudio c WHERE c.nombre = :nombre")})
public class CampoEstudio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 70)
    @Column(name = "NOMBRE")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "campoEstudioId")
    private List<AreaConocimiento> areaConocimientoList;

    public CampoEstudio() {
    }

    public CampoEstudio(Integer id) {
        this.id = id;
    }

    public CampoEstudio(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<AreaConocimiento> getAreaConocimientoList() {
        return areaConocimientoList;
    }

    public void setAreaConocimientoList(List<AreaConocimiento> areaConocimientoList) {
        this.areaConocimientoList = areaConocimientoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CampoEstudio)) {
            return false;
        }
        CampoEstudio other = (CampoEstudio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CampoEstudio[ id=" + id + " ]";
    }
    
}
