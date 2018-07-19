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
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer idRango;
    private String nombre;
    private List<Usuario> usuarioList;

    public Categoria() {
    }

    public Categoria(Integer idRango) {
        this.idRango = idRango;
    }

    public Categoria(Integer idRango, String nombre) {
        this.idRango = idRango;
        this.nombre = nombre;
    }

    public Integer getIdRango() {
        return idRango;
    }

    public void setIdRango(Integer idRango) {
        this.idRango = idRango;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRango != null ? idRango.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Categoria)) {
            return false;
        }
        Categoria other = (Categoria) object;
        if ((this.idRango == null && other.idRango != null) || (this.idRango != null && !this.idRango.equals(other.idRango))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Categoria[ idRango=" + idRango + " ]";
    }
    
}
