/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import entity.AreaConocimiento;
import entity.CampoEstudio;
import entity.Categoria;
import entity.Notificaciones;
import entity.Sancion;
import entity.Usuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import service.AreaClient;
import service.CampoClient;
import service.RangoClient;
import service.SancionClient;
import service.UsuarioClient;


/**
 *
 * @author Ignacio Marques
 */
@Named(value = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {
    
    private Usuario usu = null;
    private Usuario usuarioP = null;
    private Usuario gestionUsu = null;
    private List<Usuario> usuarioList;
    private List<String> seleccionadosRango, seleccionadosSancion;
    private UsuarioClient usuarioClient;
    private AreaClient areaClient = new AreaClient();
    private CampoClient campoClient;
    private RangoClient rangoClient;
    private SancionClient sancionClient;
    private Integer areaSel;
    private AreaConocimiento areaCon;
    private List<AreaConocimiento> areaList;
    private Integer campoSel;
    private List<CampoEstudio> campoList;
    private List<Categoria> rangoList;
    private List<Sancion> sancionList;
    private String alias, pass, email, acpass, passConfirm;
    private String busqueda = "";
    private Integer rangoSel;
    private Integer sancionSel;
    private Integer gestionUsuID;
    private List<Notificaciones> notifList;
    
    
    private boolean er = false; //guarda para notificar error al iniciar sesión
    private boolean sr = false;
    private boolean vacios = false;
    private boolean ac = false; //guarda para notificar que la contraeña 
    //introducida para actualizar el perfil no coincide con la actual
    private boolean ml = false; //guarda para notificar que el email ya existe
    //al crear nueva cuenta
    private boolean al = false; //guarda para notificar que el alias ya existe
    //al crear nueva cuenta
    private boolean passl = false;
    private boolean gUsu = true; //variable usada para el pannelgrid de gestion
    private boolean berror = false; //guarda para notificar que la busqueda no
    //ha producido ningu resultado
    private boolean gestion = false;
    
    private boolean newMail;
    private boolean newComment;
    private boolean newRevision;
    private boolean newPublicado;
    private boolean newRechazado;

    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
    }
    
    //LOGIN, LOGOUT, ACCESO Y MODIF PERFIL -------------------------------------
    
    //Loguearse en la web
    public String login(){
        String salida = "login";
        usu = null;
        al = false;
        ml = false;
         
        if ((!alias.equals("")) && (!pass.equals(""))){
            usu = findByAlias(alias);
            er = false;
            if ((usu!=null)&&(usu.getContrasena().equals(pass))){
                if (usu.getBloqueado()== 0){
                    usuarioP = usu;
                    sr = false;
                    salida = "principal";
                }
                else {
                    usu = null;
                    sr = true;
                    alias = null;
                    pass = null;
                }
            }
            else{
                er = true;
                sr = false;
                alias = null;
                pass = null;
            }
        }
        else {
            er = true;
            sr = false;
            alias = null;
            pass = null;
        }
        
        return salida;
    }
    
    //Darse de alta en la web
    public String nuevaAlta() {
        al = false;
        ml = false;
        passl = false;
        er = false;
        
        if ((!alias.equals(""))&&(!email.equals(""))&&(!pass.equals(""))){
        
        usu = findByAlias(alias);
        
        if (usu != null){
            alias = null;
            al = true;
        }
        
        usu = findByEmail(email);
        
        if (usu != null){
            email = null;
            ml = true;
        }
        
        if (!pass.equals(passConfirm)){
            passl = true;
        }
        
        if (ml || al || passl){
            usu = null;
            pass = null;
            return "alta";
        }
        
       else {
            Usuario usuN = new Usuario();
            usuN.setAlias(alias);
            usuN.setEmail(email);
            usuN.setContrasena(pass);
            usuN.setBloqueado(0);
            usuarioClient.create_JSON(usuN);
            
            alias = null;
            email = null;
            pass = null;
        }
        return "login";
        }
        
        else {
            vacios = true;
            return "alta";
        }
    }
    
    //Volver atras desde registrarse en la web (alta)
    public String volverLog(){
        alias = null;
        pass = null;
        email = null;
        
        return "login";
    }
    
    //Volver a la pagina principal
    public String volver(){
        return "principal";
    }
    
    //Acceder a la pagina de alta
    public String newUser(){
        alias = "";
        email = "";
        pass = "";
        passConfirm = null;
        return "alta";
    }
    
    //Actualizar perfil del usuario
    public String actualizar(){
        
        ac = false;
        if (acpass.equals(usu.getContrasena())){
            if (!pass.equals("")){
                usu.setContrasena(pass);
                usuarioClient.edit_JSON(usu, usu.getIdUsuario().toString());
            }

            if (areaSel != 0){
                areaCon = findByAreaID(areaSel.toString()); 
                usuarioClient.actAreasConocimiento_JSON(usu, areaSel);
                usuarioP = findByAlias(usu.getAlias());
            }      
        }
        
        else{
            ac=true;
        }
        
        return "perfil";
    }
    
    //Log out
    public String salir(){
        usuarioP = null;
             
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        session.invalidate();
      
        return "login";
    }
    
    //Cargar Area de Estudio y Campo de Conocimiento al acceder al perfil
    public String cargarPerfil(){
        
        pass = null;
        gestion = false;
        campoSel = 0;
        areaSel = 0;

        campoClient = new CampoClient();
        
        campoList = new ArrayList<CampoEstudio>();
        campoList = findAllCampos();
        
        if (!usuarioP.getAreaConocimientoList().isEmpty()){
            areaSel = usuarioP.getAreaConocimientoList().get(0).getId();
            
            areaCon = findByAreaID(areaSel.toString());
            
            for (CampoEstudio c: campoList){
                areaList = new ArrayList<AreaConocimiento>();
                areaList = findAreasByCampo(c.getId().toString());
                
                if (areaList.contains(areaCon)){
                    campoSel = c.getId();    
                }
            }
        }
        
        areaList = findAreasByCampo(campoSel.toString());
        areaCon = null;
         
        return "perfil";
    }
    
    //Buscar Area de Conocimiento a partir del Campo seleccionado
    public String seleccionaCampo()
    {
        areaList = new ArrayList<AreaConocimiento>();
        if (campoSel != 0){
            areaList = findAreasByCampo(campoSel.toString());
        }
        
        return "perfil";
    }
    
    
    //GESTION USUARIO-----------------------------------------------------------
    
    public String accesoGestionUsu(){
        gUsu = true;
        busqueda = "";
        berror = false;
        return "gestion";
    }
    
    public String gestionarUsuario(){
        gestion = true;
        gestion();
        return "perfil";
    }
    
    //Cargar perfil para la gestion
    public String gestion(){
        rangoClient = new RangoClient();
        sancionClient = new SancionClient();
        
        alias = gestionUsu.getAlias();
        email = gestionUsu.getEmail();
        pass = null;
        rangoList = findAllRangos();
        sancionList = findAllSanciones();
        rangoSel = gestionUsu.getIdRango().getIdRango();
        sancionSel = gestionUsu.getSancionId().getId();
        
        return "perfil";
    }
    
    public List<Usuario> cargarBusqueda(){
        return buscar();    
    }
    
    public List<Usuario> buscar(){
        
        usuarioList = new ArrayList<Usuario>();
        if (!busqueda.equals("")){           
            usuarioList = findByAliasOrd (busqueda);
            if (usuarioList.isEmpty()){
                berror = true;
            }
        }
        else{
            usuarioList = findAllUsuariosOrd();
        }
        
        return usuarioList;
    }
    
    public String realizarBusqueda(){
        
        berror = false;
        buscar();
        return "gestion";
    }
    
    public String actualizarGestion(){
        
        usu = null;
        if (!gestionUsu.getAlias().equals(alias)){
            usu = findByAlias(alias);
            
            if (usu == null){
                gestionUsu.setAlias(alias);
            }
            else {
                al = true;
            }
        }
        
        if (!gestionUsu.getEmail().equals(email)){
            usu = findByEmail(email);
            if (usu==null){
                gestionUsu.setEmail(email);
            }
            else {
                ml = true;
            }
        }
        
        if (!pass.equals("")){
            gestionUsu.setContrasena(pass);
        }
        
        gestionUsu.setIdRango(findRango(rangoSel.toString()));
        gestionUsu.setSancionId(findSancion(sancionSel.toString()));
        Integer idSanc = (findSancion(sancionSel.toString()).getId());
        
        if (idSanc > 1){
            gestionUsu.setBloqueado(1);
        }
        
        else{
            gestionUsu.setBloqueado(0);   
        }
                
        usuarioClient.edit_JSON(gestionUsu, gestionUsu.getIdUsuario().toString()); 
        
        usu = null;
        alias = gestionUsu.getAlias();
        email = gestionUsu.getEmail();
        pass = null;
        
        return "perfil";
    }
    
    public String volverGestion(){
        
        usu = null;
        alias = null;
        email = null;
        pass = null;
        al = false;
        ml = false;
        
        return "gestion";
    }
    
    //NOTIFICACIONES -----------------------------------------------------------
    
    public String irNotificaciones(){
        
        Usuario usuAux = findByAlias(usuarioP.getAlias());

        notifList = usuAux.getNotificacionesList();
        
        if (!notifList.isEmpty()){
            for (Notificaciones m : notifList){
                switch (m.getId()){
                    case 1 : newMail = true; break;
                    case 2 : newComment = true; break;
                    case 3 : newRevision = true; break;
                    case 4 : newPublicado = true; break;
                    case 5 : newRechazado = true; break;
                }
            }
        }
        
        else{
            newMail = false;
            newComment = false;
            newRevision = false;
            newPublicado = false;
            newRechazado = false;    
        }
        
        return "notificaciones";
    }
    
    //GETTER AND SETTER -------------------------------------------------------
    
    public Usuario getUsu() {
        return usu;
    }

    public void setUsu(Usuario usu) {
        this.usu = usu;
    }

    public Usuario getUsuarioP() {
        return usuarioP;
    }

    public void setUsuarioP(Usuario usuarioP) {
        this.usuarioP = usuarioP;
    }

    public UsuarioClient getUsuarioClient() {
        return usuarioClient;
    }

    public void setUsuarioClient(UsuarioClient usuarioClient) {
        this.usuarioClient = usuarioClient;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public boolean isEr() {
        return er;
    }

    public void setEr(boolean er) {
        this.er = er;
    }

    public String getAcpass() {
        return acpass;
    }

    public void setAcpass(String acpass) {
        this.acpass = acpass;
    }

    public boolean isAc() {
        return ac;
    }

    public void setAc(boolean ac) {
        this.ac = ac;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isMl() {
        return ml;
    }

    public void setMl(boolean ml) {
        this.ml = ml;
    }

    public boolean isAl() {
        return al;
    }

    public void setAl(boolean al) {
        this.al = al;
    }

    public List<AreaConocimiento> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<AreaConocimiento> areaList) {
        this.areaList = areaList;
    }

    public List<CampoEstudio> getCampoList() {
        return campoList;
    }

    public void setCampoList(List<CampoEstudio> campoList) {
        this.campoList = campoList;
    }

    public AreaClient getAreaClient() {
        return areaClient;
    }

    public void setAreaClient(AreaClient areaClient) {
        this.areaClient = areaClient;
    }

    public CampoClient getCampoClient() {
        return campoClient;
    }

    public void setCampoClient(CampoClient campoClient) {
        this.campoClient = campoClient;
    }

    public Integer getCampoSel() {
        return campoSel;
    }

    public void setCampoSel(Integer campoSel) {
        this.campoSel = campoSel;
    }

    public Integer getAreaSel() {
        return areaSel;
    }

    public void setAreaSel(Integer areaSel) {
        this.areaSel = areaSel;
    }

    public AreaConocimiento getAreaCon() {
        return areaCon;
    }

    public void setAreaCon(AreaConocimiento areaCon) {
        this.areaCon = areaCon;
    }

    public boolean isgUsu() {
        return gUsu;
    }

    public void setgUsu(boolean gUsu) {
        this.gUsu = gUsu;
    }

    public String getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
    }

    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    public boolean isBerror() {
        return berror;
    }

    public void setBerror(boolean berror) {
        this.berror = berror;
    }

    public RangoClient getRangoClient() {
        return rangoClient;
    }

    public void setRangoClient(RangoClient rangoClient) {
        this.rangoClient = rangoClient;
    }

    public SancionClient getSancionClient() {
        return sancionClient;
    }

    public void setSancionClient(SancionClient sancionClient) {
        this.sancionClient = sancionClient;
    }

    public List<Categoria> getRangoList() {
        return rangoList;
    }

    public void setRangoList(List<Categoria> rangoList) {
        this.rangoList = rangoList;
    }

    public List<Sancion> getSancionList() {
        return sancionList;
    }

    public void setSancionList(List<Sancion> sancionList) {
        this.sancionList = sancionList;
    }

    public Integer getRangoSel() {
        return rangoSel;
    }

    public void setRangoSel(Integer rangoSel) {
        this.rangoSel = rangoSel;
    }

    public Integer getSancionSel() {
        return sancionSel;
    }

    public void setSancionSel(Integer sancionSel) {
        this.sancionSel = sancionSel;
    }

    public List<String> getSeleccionadosRango() {
        return seleccionadosRango;
    }

    public void setSeleccionadosRango(List<String> seleccionadosR) {
        this.seleccionadosRango = seleccionadosR;
    }

    public List<String> getSeleccionadosSancion() {
        return seleccionadosSancion;
    }

    public void setSeleccionadosSancion(List<String> seleccionadosSancion) {
        this.seleccionadosSancion = seleccionadosSancion;
    }

    public Usuario getGestionUsu() {
        return gestionUsu;
    }

    public void setGestionUsu(Usuario gestionUsu) {
        this.gestionUsu = gestionUsu;
    }

    public boolean isGestion() {
        return gestion;
    }

    public void setGestion(boolean gestion) {
        this.gestion = gestion;
    }

    public Integer getGestionUsuID() {
        return gestionUsuID;
    }

    public void setGestionUsuID(Integer gestionUsuID) {
        this.gestionUsuID = gestionUsuID;
    }

    public boolean isNewMail() {
        return newMail;
    }

    public void setNewMail(boolean newMail) {
        this.newMail = newMail;
    }

    public boolean isNewComment() {
        return newComment;
    }

    public void setNewComment(boolean newComment) {
        this.newComment = newComment;
    }

    public boolean isNewRevision() {
        return newRevision;
    }

    public void setNewRevision(boolean newRevision) {
        this.newRevision = newRevision;
    }

    public boolean isNewPublicado() {
        return newPublicado;
    }

    public void setNewPublicado(boolean newPublicado) {
        this.newPublicado = newPublicado;
    }

    public boolean isNewRechazado() {
        return newRechazado;
    }

    public void setNewRechazado(boolean newRechazado) {
        this.newRechazado = newRechazado;
    }

    public String getPassConfirm() {
        return passConfirm;
    }

    public void setPassConfirm(String passConfirm) {
        this.passConfirm = passConfirm;
    }

    public boolean isPassl() {
        return passl;
    }

    public void setPassl(boolean passl) {
        this.passl = passl;
    }

    public boolean isVacios() {
        return vacios;
    }

    public void setVacios(boolean vacios) {
        this.vacios = vacios;
    }

    public List<Notificaciones> getNotifList() {
        return notifList;
    }

    public void setNotifList(List<Notificaciones> notifList) {
        this.notifList = notifList;
    }

    public boolean isSr() {
        return sr;
    }

    public void setSr(boolean sr) {
        this.sr = sr;
    }
    
    //-------------------------------------------------------------------------
    
    @PostConstruct
    public void init() {
        usuarioClient = new UsuarioClient();
    }
    
    private Usuario findByAlias(java.lang.String alias) {
        Response r = usuarioClient.findByAlias_JSON(Response.class, alias);
        GenericType<Usuario> genericType = new GenericType<Usuario>() {
        };
        return r.readEntity(genericType);
    }
    
    private java.util.List<Usuario> findAllUsuariosOrd() {
        Response r = usuarioClient.findAllUsuariosOrd_JSON(Response.class);
        if (r.getStatus() == 200) {
            GenericType<List<Usuario>> genericType = new GenericType<List<Usuario>>() {
            };
            return r.readEntity(genericType);
        } else {
            return usuarioList;
        }
    }
    
    private java.util.List<Usuario> findByAliasOrd(java.lang.String alias) {
        Response r = usuarioClient.findByAliasOrd_JSON(Response.class, alias);
        if (r.getStatus() == 200) {
            GenericType<List<Usuario>> genericType = new GenericType<List<Usuario>>() {
            };
            return r.readEntity(genericType);
        } else {
            return usuarioList;
        }
    }
    
    private Usuario findByEmail(java.lang.String email) {
        Response r = usuarioClient.findByEmail_JSON(Response.class, email);
        GenericType<Usuario> genericType = new GenericType<Usuario>() {
        };
        return r.readEntity(genericType);
    }
    
    private java.util.List<CampoEstudio> findAllCampos() {
        Response r = campoClient.findAll_JSON(Response.class);
        if (r.getStatus() == 200) {
            GenericType<List<CampoEstudio>> genericType = new GenericType<List<CampoEstudio>>() {
            };
            return r.readEntity(genericType);
        } else {
            return campoList;
        }
    }
    
    private java.util.List<AreaConocimiento> findAreasByCampo(java.lang.String campoID) {
        Response r = areaClient.findAreasByCampo_JSON(Response.class, campoID);
        if (r.getStatus() == 200) {
            GenericType<List<AreaConocimiento>> genericType = new GenericType<List<AreaConocimiento>>() {
            };
            return r.readEntity(genericType);
        } else {
            return areaList;
        }
    }
    
    private AreaConocimiento findByAreaID(java.lang.String areaSel) {
        Response r = areaClient.find_JSON(Response.class, areaSel);
        GenericType<AreaConocimiento> genericType = new GenericType<AreaConocimiento>() {
        };
        return r.readEntity(genericType);
    }
    
    private java.util.List<Categoria> findAllRangos() {
        Response r = rangoClient.findAll_JSON(Response.class);
        if (r.getStatus() == 200) {
            GenericType<List<Categoria>> genericType = new GenericType<List<Categoria>>() {
            };
            return r.readEntity(genericType);
        } else {
            return rangoList;
        }
    }
    
    private java.util.List<Sancion> findAllSanciones() {
        Response r = sancionClient.findAll_JSON(Response.class);
        if (r.getStatus() == 200) {
            GenericType<List<Sancion>> genericType = new GenericType<List<Sancion>>() {
            };
            return r.readEntity(genericType);
        } else {
            return sancionList;
        }
    }
    
    private Categoria findRango(java.lang.String rangoSel) {
        Response r = rangoClient.find_JSON(Response.class, rangoSel);
        GenericType<Categoria> genericType = new GenericType<Categoria>() {
        };
        return r.readEntity(genericType);
    }
    
    private Sancion findSancion(java.lang.String sancionSel) {
        Response r = sancionClient.find_JSON(Response.class, sancionSel);
        GenericType<Sancion> genericType = new GenericType<Sancion>() {
        };
        return r.readEntity(genericType);
    }
}
