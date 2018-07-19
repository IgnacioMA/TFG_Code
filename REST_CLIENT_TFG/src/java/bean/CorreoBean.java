/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import service.EmailClient;
import entity.Mensaje;
import entity.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import service.UsuarioClient;

/**
 *
 * @author Ignacio Marques
 */
@Named(value = "correoBean")
@SessionScoped
public class CorreoBean implements Serializable {
    
    @Inject
    private LoginBean session;
    
    private EmailClient emailClient;
    private UsuarioClient usuarioClient;
    private List<Mensaje> mensajes, mensajeList, mensajesNL;
    private Usuario usu, usuM;
    private Integer mailID;
    private Mensaje mail;
    private String aliasM, tituloM, textoM, nombRecep;
    private boolean aerror = false; 
    private boolean envio = false;
    private boolean lectura = false;
    
    /*
    NOTA:
    boolean envio me indica si estoy en la zona de recibidos (envio = false) o envios (envio = true)
    boolean lectura me indica si estoy leyendo un mensaje (lectura = true) o creando uno (lectura = false)
    boolean aerror me indica si el alias a donde mando el mensaje existe (aerorr = false) o no (aerror = true)
    */
    
    /**
     * Creates a new instance of CorreoBean
     */
    public CorreoBean() {
    }
        
    public String irRecibidos(){
        envio = false;
        return "correo";
    }
    
    public String irEnviados(){
        envio = true;
        return "correo";
    }
    
    public List<Mensaje> cargarMensajes(){
        
        if (!envio){
            return cargarMensajesR();
        }
        else{
            return cargarMensajesE();
        }
    }
    
    //Cargar Mensajes Recibidos
    public List<Mensaje> cargarMensajesR(){
        
        mensajeList = new ArrayList<Mensaje>();
        mensajeList = findByReceptor(usu.getIdUsuario().toString());
        
        return mensajeList;
    }
    
    //Cargar Mensajes Enviados
    public List<Mensaje> cargarMensajesE(){
        
        mensajeList = new ArrayList<Mensaje>();
        mensajeList = findByEmisor(usu.getIdUsuario().toString());
        
        return mensajeList;
    }
    
    public String leerMail(){
        lectura = true;
        
        mail = findByID(mailID.toString());
        nombRecep = buscarNombre(mail.getIdReceptor());
        
        if (!envio){
            if (mail.getLeido() == 0){
                mail.setLeido(1);
                emailClient.edit_JSON(mail, mail.getId().toString());
            
                mensajesNL = new ArrayList<Mensaje>();
                mensajesNL = findNLByReceptor(usu.getIdUsuario().toString());
                
                if (mensajesNL.isEmpty()){
                    usuarioClient.delNotificaciones_JSON(usu, 1);                    
                }
            }
        }  
       
        return "nuevoMail"; 
    }
    
    public void eliminar(){
        if (!envio){
            eliminarR();
        }
        else {
            eliminarE();
        }
    }
    
    //Eliminar Mensaje Recibido
    public void eliminarR(){
        mail = findByID(mailID.toString());
        if (mail.getContadorE() == 1){
            if (mail.getLeido() == 0){
                mail.setLeido(1);
                
                mensajesNL = new ArrayList<Mensaje>();
                mensajesNL = findNLByReceptor(usu.getIdUsuario().toString());
                
                if (mensajesNL.isEmpty()){
                    usuarioClient.delNotificaciones_JSON(usu, 1);                    
                }
            }
            deleteMail(mailID.toString());            
        }
        
        else{
            mail.setContadorR(1);
            mail.setLeido(1);
            emailClient.edit_JSON(mail, mailID.toString());
            
            mensajesNL = new ArrayList<Mensaje>();
            mensajesNL = findNLByReceptor(usu.getIdUsuario().toString());
                
            if (mensajesNL.isEmpty()){
                usuarioClient.delNotificaciones_JSON(usu, 1);                    
            }
        }
    }
    
    //Eiminar Mensaje Enviado
    public void eliminarE(){
        mail = findByID(mailID.toString());
        if (mail.getContadorR() == 1){
            deleteMail(mailID.toString());  
        }
        else{
            mail.setContadorE(1);
            emailClient.edit_JSON(mail, mailID.toString());
        }
    }
    
    public String nuevoMensaje(){
        aerror = false;
        lectura = false;
        nombRecep = "";
        
        return "nuevoMail";
    }
    
    public String volver(){
        aliasM = null;
        tituloM = null;
        textoM = null;
        mailID = null;
        return "correo";
    }
    
    public String enviar(){
        
        aerror = false;
        if (!aliasM.equals("")){
            usuM = findByAlias(aliasM);
            if ((usuM != null)&&(!aliasM.equals(usu.getAlias()))){
            
                mail= new Mensaje();
            
                if (tituloM.equals("")){
                    mail.setTitulo("Sin Asunto");
                }
                else{
                    mail.setTitulo(tituloM);
                }
            
                mail.setContenido(textoM);
                mail.setContadorE(0);
                mail.setContadorR(0);
                mail.setLeido(0);
                mail.setIdEmisor(usu);
                mail.setIdReceptor(usuM.getIdUsuario());
            
                usuarioClient.actNotificaciones_JSON(usuM, 1);
            
                emailClient.create_JSON(mail);
            
                aliasM = null;
                tituloM = null;
                textoM = null;
                mail = null;
                return "correo";
            }
            else{
                aliasM = null;
                aerror = true;
                return "nuevoMail";
            }
        }
        else{
            aerror = true;
            return "nuevoMail";
        }
    }
    
    public String buscarNombre(Integer id){
        Usuario userB;
        userB = findByUID(id.toString());
        
        return userB.getAlias();
    }
    
    //Guarda para los rendered de nuevoMail.xhtml
    public boolean condicionA(){
        if (lectura && !envio){
            return true;
        }
        
        else{
            return false;
        }
    }
    
    public String salir(){
        usu = null;
        mensajeList = null;
        
        return session.salir();
    }
    
    //Getter and Setter--------------------------------------------------------

    public EmailClient getEmailClient() {
        return emailClient;
    }

    public void setEmailClient(EmailClient emailClient) {
        this.emailClient = emailClient;
    }

    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    public Usuario getUsu() {
        return usu;
    }

    public void setUsu(Usuario usu) {
        this.usu = usu;
    }

    public Integer getMailID() {
        return mailID;
    }

    public void setMailID(Integer mailID) {
        this.mailID = mailID;
    }
    
    public Mensaje getMail() {
        return mail;
    }

    public void setMail(Mensaje mail) {
        this.mail = mail;
    }

    public String getAliasM() {
        return aliasM;
    }

    public void setAliasM(String aliasM) {
        this.aliasM = aliasM;
    }

    public String getTituloM() {
        return tituloM;
    }

    public void setTituloM(String tituloM) {
        this.tituloM = tituloM;
    }

    public String getTextoM() {
        return textoM;
    }

    public void setTextoM(String textoM) {
        this.textoM = textoM;
    }

    public boolean isAerror() {
        return aerror;
    }

    public void setAerror(boolean aerror) {
        this.aerror = aerror;
    }

    public boolean isEnvio() {
        return envio;
    }

    public void setEnvio(boolean envio) {
        this.envio = envio;
    }
    
    public UsuarioClient getUsuarioClient() {
        return usuarioClient;
    }

    public void setUsuarioClient(UsuarioClient usuarioClient) {
        this.usuarioClient = usuarioClient;
    }

    public Usuario getUsuM() {
        return usuM;
    }

    public void setUsuM(Usuario usuM) {
        this.usuM = usuM;
    }

    public LoginBean getSession() {
        return session;
    }

    public void setSession(LoginBean sesion) {
        this.session = sesion;
    }

    public List<Mensaje> getMensajeList() {
        return mensajeList;
    }

    public void setMensajeList(List<Mensaje> mensajeList) {
        this.mensajeList = mensajeList;
    }

    public boolean isLectura() {
        return lectura;
    }

    public void setLectura(boolean lectura) {
        this.lectura = lectura;
    }

    public String getNombRecep() {
        return nombRecep;
    }

    public void setNombRecep(String nombRecep) {
        this.nombRecep = nombRecep;
    }
    
    //-------------------------------------------------------------------------
    
    @PostConstruct
    public void init() {
        usu = session.getUsuarioP(); 
        emailClient = new EmailClient();
        usuarioClient = new UsuarioClient();
    }
    
    private java.util.List<Mensaje> findByReceptor(java.lang.String idReceptor) {
        Response r = emailClient.findByReceptor_JSON(Response.class, idReceptor);
        if (r.getStatus() == 200) {
            GenericType<List<Mensaje>> genericType = new GenericType<List<Mensaje>>() {
            };
            return r.readEntity(genericType);
        } else {
            return mensajes;
        }
    }
    
    private java.util.List<Mensaje> findNLByReceptor(java.lang.String idReceptor) {
        Response r = emailClient.findNLByReceptor_JSON(Response.class, idReceptor);
        if (r.getStatus() == 200) {
            GenericType<List<Mensaje>> genericType = new GenericType<List<Mensaje>>() {
            };
            return r.readEntity(genericType);
        } else {
            return mensajes;
        }
    }
    
    private java.util.List<Mensaje> findByEmisor(java.lang.String idEmisor) {
        Response r = emailClient.findByEmisor_JSON(Response.class, idEmisor);       
        if (r.getStatus() == 200) {
            GenericType<List<Mensaje>> genericType = new GenericType<List<Mensaje>>() {
            };
            return r.readEntity(genericType);
        } else {
            return mensajes;
        }
    }
    
    private Mensaje findByID(java.lang.String mailID) {
        Response r = emailClient.find_JSON(Response.class, mailID);
        GenericType<Mensaje> genericType = new GenericType<Mensaje>() {
        };
        return r.readEntity(genericType);
    }
    
    private void deleteMail(java.lang.String mailID) {
        emailClient.remove(mailID);
    }
    
    private Usuario findByAlias(java.lang.String alias) {
        Response r = usuarioClient.findByAlias_JSON(Response.class, alias);
        GenericType<Usuario> genericType = new GenericType<Usuario>() {
        };
        return r.readEntity(genericType);
    }
    
    private Usuario findByUID(java.lang.String id) {
        Response r = usuarioClient.find_JSON(Response.class, id);
        GenericType<Usuario> genericType = new GenericType<Usuario>() {
        };
        return r.readEntity(genericType);
    }
    
}
