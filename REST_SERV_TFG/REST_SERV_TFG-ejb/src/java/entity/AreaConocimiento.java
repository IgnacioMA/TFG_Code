/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "AREA_CONOCIMIENTO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AreaConocimiento.findAll", query = "SELECT a FROM AreaConocimiento a")
    , @NamedQuery(name = "AreaConocimiento.findById", query = "SELECT a FROM AreaConocimiento a WHERE a.id = :id")
    ,     @NamedQuery(name = "AreaConocimiento.findAreasByCampo", query = "SELECT a FROM AreaConocimiento a WHERE a.campoEstudioId.id = :campoID")
    , @NamedQuery(name = "AreaConocimiento.findByNombre", query = "SELECT a FROM AreaConocimiento a WHERE a.nombre = :nombre")})
public class AreaConocimiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NOMBRE")
    private String nombre;
    @JoinTable(name = "AREA", joinColumns = {
        @JoinColumn(name = "AREA_CONOCIMIENTO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "ID_ARTICULO", referencedColumnName = "ID_ARTICULO")})
    @ManyToMany
    private List<Articulo> articuloList;
    @JoinTable(name = "ESPECIALIZADO_EN", joinColumns = {
        @JoinColumn(name = "AREA_CONOCIMIENTO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO")})
    @ManyToMany
    private List<Usuario> usuarioList;
    @JoinColumn(name = "CAMPO_ESTUDIO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CampoEstudio campoEstudioId;

    public AreaConocimiento() {
    }

    public AreaConocimiento(Integer id) {
        this.id = id;
    }

    public AreaConocimiento(Integer id, String nombre) {
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
    public List<Articulo> getArticuloList() {
        return articuloList;
    }

    public void setArticuloList(List<Articulo> articuloList) {
        this.articuloList = articuloList;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    public CampoEstudio getCampoEstudioId() {
        return campoEstudioId;
    }

    public void setCampoEstudioId(CampoEstudio campoEstudioId) {
        this.campoEstudioId = campoEstudioId;
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
        if (!(object instanceof AreaConocimiento)) {
            return false;
        }
        AreaConocimiento other = (AreaConocimiento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AreaConocimiento[ id=" + id + " ]";
    }
    
}
