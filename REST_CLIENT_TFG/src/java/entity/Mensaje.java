/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;

/**
 *
 * @author Usuario
 */
public class Mensaje implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private int idReceptor;
    private int contadorE;
    private int contadorR;
    private String titulo;
    private String contenido;
    private int leido;
    private Usuario idEmisor;

    public Mensaje() {
    }

    public Mensaje(Integer id) {
        this.id = id;
    }

    public Mensaje(Integer id, int idReceptor, int contadorE, int contadorR, int leido) {
        this.id = id;
        this.idReceptor = idReceptor;
        this.contadorE = contadorE;
        this.contadorR = contadorR;
        this.leido = leido;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getIdReceptor() {
        return idReceptor;
    }

    public void setIdReceptor(int idReceptor) {
        this.idReceptor = idReceptor;
    }

    public int getContadorE() {
        return contadorE;
    }

    public void setContadorE(int contadorE) {
        this.contadorE = contadorE;
    }

    public int getContadorR() {
        return contadorR;
    }

    public void setContadorR(int contadorR) {
        this.contadorR = contadorR;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public int getLeido() {
        return leido;
    }

    public void setLeido(int leido) {
        this.leido = leido;
    }

    public Usuario getIdEmisor() {
        return idEmisor;
    }

    public void setIdEmisor(Usuario idEmisor) {
        this.idEmisor = idEmisor;
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
        if (!(object instanceof Mensaje)) {
            return false;
        }
        Mensaje other = (Mensaje) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Mensaje[ id=" + id + " ]";
    }
    
}
