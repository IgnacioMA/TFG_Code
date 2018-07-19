/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ignacio Marqués
 */
@Entity
@Table(name = "MENSAJE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Mensaje.findAll", query = "SELECT m FROM Mensaje m")
    , @NamedQuery(name = "Mensaje.findById", query = "SELECT m FROM Mensaje m WHERE m.id = :id")
    ,   @NamedQuery(name = "Mensaje.findByIdReceptor", query = "SELECT m FROM Mensaje m WHERE m.idReceptor = :idReceptor AND m.contadorR = 0 ORDER BY m.id DESC")
    ,   @NamedQuery(name = "Mensaje.findByIdEmisor", query = "SELECT m FROM Mensaje m WHERE m.idEmisor = :idEmisor AND m.contadorE = 0 ORDER BY m.id DESC")
    ,   @NamedQuery(name = "Mensaje.findNLByIdReceptor", query = "SELECT m FROM Mensaje m WHERE m.idReceptor = :idReceptor AND m.leido = 0")
    , @NamedQuery(name = "Mensaje.findByContadorE", query = "SELECT m FROM Mensaje m WHERE m.contadorE = :contadorE")
    , @NamedQuery(name = "Mensaje.findByContadorR", query = "SELECT m FROM Mensaje m WHERE m.contadorR = :contadorR")
    , @NamedQuery(name = "Mensaje.findByTitulo", query = "SELECT m FROM Mensaje m WHERE m.titulo = :titulo")
    , @NamedQuery(name = "Mensaje.findByContenido", query = "SELECT m FROM Mensaje m WHERE m.contenido = :contenido")
    , @NamedQuery(name = "Mensaje.findByLeido", query = "SELECT m FROM Mensaje m WHERE m.leido = :leido")})
public class Mensaje implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "SEQ_MENSAJE_ID", sequenceName = "SEQ_MENSAJE_ID", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MENSAJE_ID")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_RECEPTOR")
    private int idReceptor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CONTADOR_E")
    private int contadorE;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CONTADOR_R")
    private int contadorR;
    @Size(max = 100)
    @Column(name = "TITULO")
    private String titulo;
    @Size(max = 300)
    @Column(name = "CONTENIDO")
    private String contenido;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LEIDO")
    private int leido;
    @JoinColumn(name = "ID_EMISOR", referencedColumnName = "ID_USUARIO")
    @ManyToOne(optional = false)
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
