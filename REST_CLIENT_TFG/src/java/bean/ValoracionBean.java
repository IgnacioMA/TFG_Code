/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import entity.Articulo;
import entity.Comentarios;
import entity.Usuario;
import entity.Valoracion;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import service.ArticuloClient;
import service.ComentarioClient;
import service.UsuarioClient;
import service.ValoracionClient;

/**
 *
 * @author Ignacio Marques
 */
@Named(value = "valoracionBean")
@RequestScoped
public class ValoracionBean {
    
    @Inject
    private LoginBean session;
    
    @Inject
    private ArticuloBean artic;
    
    private ValoracionClient valoracionClient;
    private ComentarioClient comentarioClient;
    private ArticuloClient articuloClient = new ArticuloClient();
    private List<Comentarios> comentarios, comentList;
    private List<Articulo> articulos;
    private List<Valoracion> listaVal, valList;
    private Usuario usu;
    private Articulo articulo;
    private Integer valor;
    private String comentarioTxt;
    private Integer comentarioID = 0;
    private Comentarios comentario;
    private Valoracion valoracion;
    private UsuarioClient usuarioClient = new UsuarioClient();

    /**
     * Creates a new instance of ValoracionBean
     */
    public ValoracionBean() {
    }
    
    //Metodo para evitar autoPuntuar un Articulo
    public boolean condicionPuntua(){
        if(articulo.getRedactor().getIdUsuario() != usu.getIdUsuario()){
            return true;
        }
        else
            return false;
    }
    
    //Realizar Puntuacion (incluye actualizar Puntuacion si esta se modifica)
    public String puntuar(){
        
        if ((valor != 0) && (valoracion == null)){
            
            valoracion = new Valoracion(0);
            valoracion.setIdArticulo(artic.getArticulo());
            valoracion.setIdUsuario(usu);
            valoracion.setPuntuacion(valor);
            
            valoracionClient.create_JSON(valoracion);
            
            return "leerArticulo";
        }
        
        else if ((valor != 0) && (valoracion != null)){
            
            valoracion.setPuntuacion(valor);
            valoracionClient.edit_JSON(valoracion, valoracion.getId().toString());    
            
            return "leerArticulo";
        }
        
        else if ((valor == 0) && (valoracion != null)){
            
            valoracionClient.remove(valoracion.getId().toString());
            
            return "leerArticulo";
        }
        
        else
        
        return "leerArticulo";
    }
    
    //Cargar Lista de Comentarios de un Articulo
    public List<Comentarios> cargarComentarios(){
        articulo = artic.getArticulo();
        comentList = findComentByArticulo(articulo.getIdArticulo().toString());
        
        if (comentList.isEmpty()){
            comentList = null;
        }
        if ((comentList != null) && ((articulo.getRedactor().getIdUsuario()) == (session.getUsuarioP().getIdUsuario()))){
            for (Comentarios cm : comentList){
                if (cm.getVisto() == 0){
                    cm.setVisto(1);
                    comentarioClient.edit_JSON(cm, cm.getId().toString());
                }
            }
        }
        
        if ((articulo.getRedactor().getIdUsuario()) == (session.getUsuarioP().getIdUsuario())){        
            Boolean delNot = true;
            List<Comentarios> comentListB = new ArrayList<Comentarios>();
            List<Articulo> listaRedactados = new ArrayList<Articulo>();
            
            Usuario us = articulo.getRedactor();
            listaRedactados = findByRedactorP(usu.getIdUsuario().toString());
            listaRedactados.remove(articulo);
        
            if (listaRedactados != null){
                for (Articulo arti : listaRedactados){
                    comentListB = findComentByArticulo(arti.getIdArticulo().toString());
                    if (comentListB != null){
                        for (Comentarios cm : comentListB){
                            if (cm.getVisto() == 0){
                                delNot = false;  
                            }
                        }
                    }
                }
            }
            
            if (delNot){
                usuarioClient.delNotificaciones_JSON(us, 2);
            }
        }

        return comentList;
    }
    
    public boolean condicionElimComent(Comentarios coment){
        
        if ((coment.getIdUsuario().getIdUsuario() == usu.getIdUsuario()) || (usu.getIdRango().getIdRango() == 4)){
            
            return true;
        }
        else{
            return false;
        }
    }
    
    //Eliminar un comentario realizado por el usuario logueado
    public String eliminarComent(){
        
        if (comentarioID != 0){
            
            comentarioClient.remove(comentarioID.toString());
        }
        
        return "leerArticulo";
    }
    
    //Publicar un nuevo Comentario
    public String nuevoComent(){
        
        if (!comentarioTxt.equals("")){
            comentario = new Comentarios();
            comentario.setIdUsuario(usu);
            comentario.setTexto(comentarioTxt);
            comentario.setFecha(new Date());
            comentario.setVisto(0);
            
            articulo.setFecha(new Date());
            articuloClient.edit_JSON(articulo, articulo.getIdArticulo().toString());
            
            comentario.setIdArticulo(articulo);
        
            comentarioClient.create_JSON(comentario);
            
            usuarioClient.actNotificaciones_JSON(articulo.getRedactor(),2);
            comentarioTxt = "";
        }
        
        return "leerArticulo";
    }
    
    //Getter and Setter--------------------------------------------------------

    public LoginBean getSession() {
        return session;
    }

    public void setSession(LoginBean session) {
        this.session = session;
    }

    public ArticuloBean getArtic() {
        return artic;
    }

    public void setArtic(ArticuloBean artic) {
        this.artic = artic;
    }

    public ValoracionClient getValoracionClient() {
        return valoracionClient;
    }

    public void setValoracionClient(ValoracionClient valoracionClient) {
        this.valoracionClient = valoracionClient;
    }

    public ComentarioClient getComentarioClient() {
        return comentarioClient;
    }

    public void setComentarioClient(ComentarioClient comentarioClient) {
        this.comentarioClient = comentarioClient;
    }

    public ArticuloClient getArticuloClient() {
        return articuloClient;
    }

    public void setArticuloClient(ArticuloClient articuloClient) {
        this.articuloClient = articuloClient;
    }

    public List<Comentarios> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentarios> comentarios) {
        this.comentarios = comentarios;
    }

    public List<Comentarios> getComentList() {
        return comentList;
    }

    public void setComentList(List<Comentarios> comentList) {
        this.comentList = comentList;
    }

    public Usuario getUsu() {
        return usu;
    }

    public void setUsu(Usuario usu) {
        this.usu = usu;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public String getComentarioTxt() {
        return comentarioTxt;
    }

    public void setComentarioTxt(String comentarioTxt) {
        this.comentarioTxt = comentarioTxt;
    }

    public Integer getComentarioID() {
        return comentarioID;
    }

    public void setComentarioID(Integer comentarioID) {
        this.comentarioID = comentarioID;
    }

    public Comentarios getComentario() {
        return comentario;
    }

    public void setComentario(Comentarios comentario) {
        this.comentario = comentario;
    }

    public List<Valoracion> getListaVal() {
        return listaVal;
    }

    public void setListaVal(List<Valoracion> listaVal) {
        this.listaVal = listaVal;
    }

    public List<Valoracion> getValList() {
        return valList;
    }

    public void setValList(List<Valoracion> valList) {
        this.valList = valList;
    }

    public Valoracion getValoracion() {
        return valoracion;
    }

    public void setValoracion(Valoracion valoracion) {
        this.valoracion = valoracion;
    }

    public List<Articulo> getArticulos() {
        return articulos;
    }

    public void setArticulos(List<Articulo> articulos) {
        this.articulos = articulos;
    }

    public UsuarioClient getUsuarioClient() {
        return usuarioClient;
    }

    public void setUsuarioClient(UsuarioClient usuarioClient) {
        this.usuarioClient = usuarioClient;
    }
    
    //--------------------------------------------------------------------------
    @PostConstruct
    public void init() {
        
        usu = session.getUsuarioP();
        articulo = artic.getArticulo();
        valoracionClient = new ValoracionClient();
        comentarioClient = new ComentarioClient();
        
        valList = valList = findPuntuacion(articulo.getIdArticulo().toString());
        
        valor = 0;
        
        if (valList!=null){
            for (Valoracion v : valList){
                if (v.getIdUsuario().getIdUsuario() == usu.getIdUsuario()){
                    valor = v.getPuntuacion();
                    valoracion = v;
                }
            }
        }
    }
    
    //Busca los comentarios de un articulo
    private java.util.List<Comentarios> findComentByArticulo(java.lang.String articulo) {
        Response r = comentarioClient.findComentByArticulo_JSON(Response.class, articulo);
        if (r.getStatus() == 200) {
            GenericType<List<Comentarios>> genericType = new GenericType<List<Comentarios>>() {
            };
            return r.readEntity(genericType);
        } else {
            return comentarios;
        }
    }
    
    //Busca las puntuaciones de un articulo
    private java.util.List<Valoracion> findPuntuacion(java.lang.String articuloID) {
        Response r = valoracionClient.findPuntuacion_JSON(Response.class, articuloID);
        if (r.getStatus() == 200) {
            GenericType<List<Valoracion>> genericType = new GenericType<List<Valoracion>>() {
            };
            return r.readEntity(genericType);
        } else {
            return listaVal;
        }
    }
    
    private java.util.List<Articulo> findByRedactorP(java.lang.String idRedactor) {
        Response r = articuloClient.findByRedactorP_JSON(Response.class, idRedactor);
        if (r.getStatus() == 200) {
            GenericType<List<Articulo>> genericType = new GenericType<List<Articulo>>() {
            };
            return r.readEntity(genericType);
        } else {
            return articulos;
        }
    }
}
