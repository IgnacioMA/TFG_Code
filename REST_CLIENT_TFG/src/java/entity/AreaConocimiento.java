/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Usuario
 */

public class AreaConocimiento implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String nombre;
    private List<Articulo> articuloList;
    private List<Usuario> usuarioList;
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

    public List<Articulo> getArticuloList() {
        return articuloList;
    }

    public void setArticuloList(List<Articulo> articuloList) {
        this.articuloList = articuloList;
    }

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
