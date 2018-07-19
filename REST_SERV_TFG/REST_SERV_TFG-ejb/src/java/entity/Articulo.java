/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ignacio Marqués
 */
@Entity
@Table(name = "ARTICULO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Articulo.findAll", query = "SELECT a FROM Articulo a")
    , @NamedQuery(name = "Articulo.findByIdArticulo", query = "SELECT a FROM Articulo a WHERE a.idArticulo = :idArticulo")
    , @NamedQuery(name = "Articulo.findByTitulo", query = "SELECT a FROM Articulo a WHERE a.titulo = :titulo")
    ,   @NamedQuery(name = "Articulo.findByTituloB", query = "SELECT a FROM Articulo a WHERE a.titulo like CONCAT('%',:tituloB,'%') AND a.validado = 1 ORDER BY a.fecha DESC, a.titulo ASC")
    ,   @NamedQuery(name = "Articulo.findByAllOrdFN", query = "SELECT a FROM Articulo a WHERE a.validado = 1 ORDER BY a.fecha DESC, a.titulo ASC")
    ,   @NamedQuery(name = "Articulo.findAllArticulosOrd", query = "SELECT a FROM Articulo a WHERE a.validado = 1 ORDER BY a.titulo ASC")
    ,   @NamedQuery(name = "Articulo.findByTituloOrd", query = "SELECT a FROM Articulo a WHERE a.titulo like CONCAT('%',:titulo,'%') AND a.validado = 1 ORDER BY a.titulo")
    ,   @NamedQuery(name = "Articulo.findAllNPArticulosOrd", query = "SELECT a FROM Articulo a WHERE a.validado = 0 AND a.subido = 1 ORDER BY a.titulo ASC")
    ,   @NamedQuery(name = "Articulo.findNPByTituloOrd", query = "SELECT a FROM Articulo a WHERE a.titulo like CONCAT('%',:titulo,'%') AND a.validado = 0 AND a.subido = 1 ORDER BY a.titulo")
    ,   @NamedQuery(name = "Articulo.findByAutorP", query = "SELECT a FROM Articulo a WHERE a.redactor = :redactor AND a.validado = 1 ORDER BY a.fecha DESC")
    ,   @NamedQuery(name = "Articulo.findByAutorG", query = "SELECT a FROM Articulo a WHERE a.redactor = :redactor AND a.validado = 0 ORDER BY a.fecha DESC")
    ,   @NamedQuery(name = "Articulo.findByRevisor", query = "SELECT a FROM Articulo a WHERE a.revisor = :revisor ORDER BY a.fecha DESC, a.titulo ASC")
    ,   @NamedQuery(name = "Articulo.cuentaRevisiones", query = "SELECT COUNT(a.idArticulo) FROM Articulo a WHERE a.revisor = :revisor")
    , @NamedQuery(name = "Articulo.findByFecha", query = "SELECT a FROM Articulo a WHERE a.fecha = :fecha")
    , @NamedQuery(name = "Articulo.findByTexto", query = "SELECT a FROM Articulo a WHERE a.texto = :texto")
    , @NamedQuery(name = "Articulo.findByValidado", query = "SELECT a FROM Articulo a WHERE a.validado = :validado")
    , @NamedQuery(name = "Articulo.findBySubido", query = "SELECT a FROM Articulo a WHERE a.subido = :subido")})
public class Articulo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "SEQ_ARTICULO_ID", sequenceName = "SEQ_ARTICULO_ID", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ARTICULO_ID")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_ARTICULO")
    private Integer idArticulo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "TITULO")
    private String titulo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Size(max = 4000)
    @Column(name = "TEXTO")
    private String texto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VALIDADO")
    private int validado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SUBIDO")
    private int subido;
    @ManyToMany(mappedBy = "articuloList")
    private List<AreaConocimiento> areaConocimientoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idArticulo")
    private List<Comentarios> comentariosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idArticulo")
    private List<Valoracion> valoracionList;
    @JoinColumn(name = "REVISOR", referencedColumnName = "ID_USUARIO")
    @ManyToOne
    private Usuario revisor;
    @JoinColumn(name = "REDACTOR", referencedColumnName = "ID_USUARIO")
    @ManyToOne(optional = false)
    private Usuario redactor;

    public Articulo() {
    }

    public Articulo(Integer idArticulo) {
        this.idArticulo = idArticulo;
    }

    public Articulo(Integer idArticulo, String titulo, Date fecha, int validado, int subido) {
        this.idArticulo = idArticulo;
        this.titulo = titulo;
        this.fecha = fecha;
        this.validado = validado;
        this.subido = subido;
    }

    public Integer getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(Integer idArticulo) {
        this.idArticulo = idArticulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getValidado() {
        return validado;
    }

    public void setValidado(int validado) {
        this.validado = validado;
    }

    public int getSubido() {
        return subido;
    }

    public void setSubido(int subido) {
        this.subido = subido;
    }

    public List<AreaConocimiento> getAreaConocimientoList() {
        return areaConocimientoList;
    }

    public void setAreaConocimientoList(List<AreaConocimiento> areaConocimientoList) {
        this.areaConocimientoList = areaConocimientoList;
    }

    @XmlTransient
    public List<Comentarios> getComentariosList() {
        return comentariosList;
    }

    public void setComentariosList(List<Comentarios> comentariosList) {
        this.comentariosList = comentariosList;
    }

    @XmlTransient
    public List<Valoracion> getValoracionList() {
        return valoracionList;
    }

    public void setValoracionList(List<Valoracion> valoracionList) {
        this.valoracionList = valoracionList;
    }

    public Usuario getRevisor() {
        return revisor;
    }

    public void setRevisor(Usuario revisor) {
        this.revisor = revisor;
    }

    public Usuario getRedactor() {
        return redactor;
    }

    public void setRedactor(Usuario redactor) {
        this.redactor = redactor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idArticulo != null ? idArticulo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Articulo)) {
            return false;
        }
        Articulo other = (Articulo) object;
        if ((this.idArticulo == null && other.idArticulo != null) || (this.idArticulo != null && !this.idArticulo.equals(other.idArticulo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Articulo[ idArticulo=" + idArticulo + " ]";
    }
    
}
