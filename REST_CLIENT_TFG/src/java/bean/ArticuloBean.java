/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import entity.AreaConocimiento;
import entity.Articulo;
import entity.CampoEstudio;
import entity.Comentarios;
import entity.Usuario;
import entity.Valoracion;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import service.UsuarioClient;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import service.AreaClient;
import service.ArticuloClient;
import service.CampoClient;
import service.ComentarioClient;
import service.ValoracionClient;

/**
 *
 * @author Ignacio Marques
 */
@Named(value = "articuloBean")
@SessionScoped
public class ArticuloBean implements Serializable {
    
    @Inject
    private LoginBean session;
    
    @Inject
    private ValoracionBean valBean;
    
    private ArticuloClient articuloClient;
    private ValoracionClient valoracionClient = new ValoracionClient();
    private ComentarioClient comentarioClient = new ComentarioClient();
    private Usuario usu, revisor;
    private UsuarioClient usuarioClient = new UsuarioClient();
    private List<Articulo> articulos, articuloList;
    private List<Articulo> articuloBusList;
    private Integer articuloID = null;
    private Articulo articulo, articuloN;
    private String tituloA;
    private String textoA;
    private String busqueda = "";
    
    private List<CampoEstudio> campoList;
    private CampoClient campoClient;
    private AreaClient areaClient = new AreaClient();
    private List<AreaConocimiento> areaList;
    private Integer campoSel;
    private Integer areaSel;
    
    private List<Usuario> revisores, revisoresList;
    private AreaConocimiento conocimiento = null;
    private List<AreaConocimiento> conocimientoList;
    private CampoEstudio estudio = null;
    
    private List<Valoracion> listaVal, valList;
    private List<Comentarios> comentarios, comentList;
    
    private List<AreaConocimiento> listConocimiento = null;
     
    private boolean guardado = false; //guarda para distinguir entre 
    //Mis Articulos publicados y los Guardados en Mis Articulos
    private boolean terror = false; //guarda para mostrar error al crear nuevo Articulo
    private boolean berror = false; //guarda para mostrar notificacion de error al buscar
    private boolean miZona = false; //gurada para diferenciar entre Publicaciones y Mis Publicaciones
    private boolean publico = false; //guarda para la Gestion de Articulos Publicados
    private boolean noPublico = false; //guarda para la Gestion de Articulos No Publicados
    private boolean revision = false; //guarda para el pannelgrid de Nuevo Articulo
    private boolean edit = false; //guarda para diferenciar entre Edicion y Creación  
    private boolean pub = false;
    private boolean noArea = false;
    
    private List<Articulo> publicadosArt = new ArrayList<Articulo>();
    private List<Articulo> articulosBeta = new ArrayList<Articulo>();
    
    /**
     * Creates a new instance of ArticuloBean
     */
    public ArticuloBean() {
    }
    
    //Navegar a Mis Articulos Publicados
    public String irPublicados(){
        guardado = false;
        revision = false;
        miZona = true;
        
        if (!session.getUsuarioP().getNotificacionesList().isEmpty()){
            usuarioClient.delNotificaciones_JSON(usu, 4);
        }
        
        return "misArticulos";
    }
    
    //Navegar a Mis Articulos Guardados
    public String irGuardados(){
        guardado = true;
        revision = false;
        
        if (!session.getUsuarioP().getNotificacionesList().isEmpty()){
            usuarioClient.delNotificaciones_JSON(usu, 5);
        }
        
        return "misArticulos";
    }
    
    //Cargar Listado de Articulos
    public List<Articulo> cargarArticulos(){
        
        if (!guardado){
            return cargarArticulosP();
        }
        else{
            return cargarArticulosG();
        }  
    }
    
    //Cargar Articulos Publicados
    public List<Articulo> cargarArticulosP(){
        
        articuloList = findByRedactorP(usu.getIdUsuario().toString());
        
        return articuloList;
    }
    
    //Cargar Articulos Guardados
    public List<Articulo> cargarArticulosG(){
        
        articuloList = findByRedactorG(usu.getIdUsuario().toString());
        
        return articuloList;
    }
    
    //Leer Articulo desde Mis Articulos
    public String leerArticuloA(){
        articulo = findByID(articuloID.toString());
        miZona = true;
        
        return "leerArticulo"; 
    }
    
    //Leer Articulo desde Publicados
    public String leerArticuloB(){
        articulo = findByID(articuloID.toString());
        miZona = false;
        
        return "leerArticulo"; 
    }
    
    //Cargar pagina Nuevo Articulo
    public String nuevoArticulo(){
        articuloID = null;  
        tituloA = "";
        textoA = "";
        campoSel = 0;
        areaSel = 0;
        
        miZona = true;
        cargarCampo();
        
        return "nuevoArticulo";
    }
    
    //Cargar pagina Nuevo Articulo con datos de Articulo para Editar
    public String editarArticulo(){
        miZona = true;
        edit = true;
        articulo = findByID(articuloID.toString());
        
        areaSel = articulo.getAreaConocimientoList().get(0).getId();
        campoSel = articulo.getAreaConocimientoList().get(0).getCampoEstudioId().getId();
        
        tituloA = articulo.getTitulo();
        textoA = articulo.getTexto();
        cargarCampo();
        
        return "nuevoArticulo";
    }
    
    //Eliminar Articulo
    public void eliminar(){

        //eliminar puntuaciones
        valList = findPuntuacion(articuloID.toString());
        
        for (Valoracion val : valList){
            valoracionClient.remove(val.getId().toString());
        }
        
        //eliminar comentarios
        comentList = findComentByArticulo(articuloID.toString());
        
        for (Comentarios comt : comentList){
            comentarioClient.remove(comt.getId().toString());
        }
        
        //eliminar areas de conocimiento
        articulo = findByID(articuloID.toString());
        List<AreaConocimiento> al = articulo.getAreaConocimientoList();
        
        AreaConocimiento c = al.get(0);
        
        articuloClient.elimAreasConocimientoArt_JSON(articulo, c.getId());
        
        //eliminar artículo
        deleteArticulo(articuloID.toString()); 
    }
    
    //Gestiona el boton Editar en Guardados, ya que los Subidos hasta que no se 
    //publiquen estan Guardados pero estos no se pueden Editar, los Guardados si se pueden Editar.
    public boolean condicion(Articulo articulo){
        
        if (articulo!=null){       
            if (guardado && (articulo.getSubido() == 0)){
                return true;
        }
            else
                return false;
        }
        else
            return false;
    }
    
    //Guardar Articulo (nuevoArticulo.xhtml)
    public String enviarG(){
        
        cargarArticulosR();
        if ((campoSel == 0)||(areaSel == 0)){
            areaSel = 0;
            noArea = true;
            return "nuevoArticulo";
            
        }
        
        else{
            noArea = false;
            return enviar(0);
        }
    }
    
    //Subir Articulo (nuevoArticulo.xhtml)
    public String enviarS(){
        
        cargarArticulosR();
        if ((campoSel == 0)||(areaSel == 0)){
            areaSel = 0;
            noArea = true;
            return "nuevoArticulo";
        }
        
        else{
            noArea = false;
            return enviar(1);
        }
    }
    
    //Envia el Articulo y lo procesa segun sea enviarG (Guardar) o enviarS (Subir)
    @SuppressWarnings({"BoxedValueEquality", "NumberEquality"})
    public String enviar(Integer i){
        
        terror = false;
        edit = false;
        articuloN = null;
        String lugar = "misArticulos";
        Integer v = 0;
        
        if ((usu.getIdRango().getIdRango()>1)&&(usu.getIdRango().getIdRango()!=5)&&(i==1)){
            v = 1;
        }
        
        if (revision == true){
            lugar = "misRevisiones";
        }
        
        if (!tituloA.equals("")){
            articuloN = findByTitulo(tituloA);
            
            if((articuloID == null)&&(articuloN == null)){
                articulo = new Articulo();
                
                articulo.setTitulo(tituloA);
                
                HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                articulo.setTexto(request.getParameter("editor1"));
                
                articulo.setRedactor(usu);
                articulo.setFecha(new Date());
                articulo.setValidado(v);
                articulo.setSubido(i);
                
                articuloClient.create_JSON(articulo);
            
                tituloA = null;
                textoA = null;
                
                articuloClient.actAreasConocimientoArt_JSON(articulo, areaSel);
                
                cargarArticulos();
                
                return lugar;
            }
            
            else if((articuloID != null)&&(articuloN == null)){
                articulo.setTitulo(tituloA);
                
                HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                articulo.setTexto(request.getParameter("editor1"));
                
                articulo.setFecha(new Date());
                articulo.setValidado(v);
                articulo.setSubido(i);
                
                articuloClient.edit_JSON(articulo, articulo.getIdArticulo().toString());
                
                articuloClient.actAreasConocimientoArt_JSON(articulo, areaSel);
                
                tituloA = null;
                textoA = null;
                articuloID = null;
                
                cargarArticulos();
                
                return lugar;
            }
            
            else if((articuloID != null)&&(articuloN != null)){                
                if(articuloID.toString().equals(articuloN.getIdArticulo().toString())){
                    articulo.setTitulo(tituloA);
                    
                    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                    articulo.setTexto(request.getParameter("editor1"));
                    
                    articulo.setFecha(new Date());
                    articulo.setValidado(v);
                    articulo.setSubido(i);
                
                    articuloClient.edit_JSON(articulo, articulo.getIdArticulo().toString());
                    
                    articuloClient.actAreasConocimientoArt_JSON(articulo, areaSel);
                
                    tituloA = null;
                    textoA = null;
                    articuloID = null;
                    
                    cargarArticulos();
                    
                    return lugar;
                }
                
                else {
                    terror = true;
                    return "nuevoArticulo";
                }
            }
            
            else{
                terror = true;
                return "nuevoArticulo";
            }
        }
        
        else {
            terror = true;
            return "nuevoArticulo";          
        }
    }
    
    //Gestion de las diferentes Navegaciones del boton Volver
    public String volver(){
        tituloA = null;
        textoA = null;
        articulo = null;
        edit = false;
       
        if (revision == true){
            return "misRevisiones";
        }
        
        else if (revision == false && miZona == true){
            return "misArticulos";
        }
        
        else{
            return "publicaciones";
        }
    }
    
    //Log Out
    public String salir(){
        usu = null;
        articuloList = null;
        articuloBusList = null;
        
        return session.salir();
    }
    
    //Mostrar la puntuación media de un Articulo
    public String cargarMedia (Articulo articuloS){
        
        if (articuloS != null){
            return valoracionClient.findMedia_JSON(articuloS.getIdArticulo());
        }
        else 
            return null;
    }
    
    //Mostrar el numero de votos de un Articulo
    public String cargarNumVotos (Articulo articuloS){
        
        if (articuloS != null){
            return valoracionClient.cuentaVotos_JSON(articuloS.getIdArticulo());
        }
        else
            return null;
    }
    
    public boolean accesoNuevoartic (){
        
        if ((usu.getIdRango().getIdRango()>1)&&(usu.getIdRango().getIdRango()!=4)){
            return true;
        }
        
        else{
            return false;
        }
    }
    
//Publicaciones---------------------------------------------------------------
    
    public String irPublicaciones(){
        pub = true;
        cargarCampo();
        miZona = false;
        
        areaSel = 0;
        campoSel = 0;
        busqueda = "";
        
        publicadosArt = findAllOrdFN();
        
        return "publicaciones";
    }

    public String realizarBusqueda(){
        
        berror = false;
        
        if (!busqueda.equals("")){
            publicadosArt = findByTituloB(busqueda);
        }
        
        else{
            publicadosArt = findAllOrdFN();
        }

        if (!publicadosArt.isEmpty()){
            
            if (campoSel == 0){
                areaSel = 0;
            }
            
            if (areaSel != 0){                
                conocimiento = findByAreaID(areaSel.toString());
                
                for(Articulo ar : publicadosArt){
                    if (ar.getAreaConocimientoList().contains(conocimiento)){
                        articulosBeta.add(ar);
                        
                    }
                }
                conocimiento = null;
                publicadosArt = articulosBeta;
            }
            
            else if ((areaSel == 0) && (campoSel != 0)){
                
                listConocimiento = findAreasByCampo(campoSel.toString());
                
                for (AreaConocimiento area : listConocimiento){
                    
                    for(Articulo ar : publicadosArt){
                        if (ar.getAreaConocimientoList().contains(area)){
                            articulosBeta.add(ar);
                        }
                    }   
                }
                publicadosArt = articulosBeta;
            }
        }

        if (publicadosArt.isEmpty()){
                berror = true;
        }
        
        articulosBeta = new ArrayList<Articulo>();
        return "publicaciones";
    }
    
//Gestion Articulos------------------------------------------------------------------
    
    //Gestion de Articulos Publicados
    public String gestion(){
        session.setgUsu(true);
        publico = true;
        session.setgUsu(false);
        noPublico = false;
        busqueda = "";
        berror = false;
        return "gestion";
    }
    
    public String accesoGestionUsu(){
        publico = false;
        noPublico=false;
        busqueda = "";
        berror = false;
        return session.accesoGestionUsu();
    }
    
    //Gestion de Articulos Subidos
    public String gestionB(){
        session.setgUsu(true);
        publico = false;
        noPublico = true;
        busqueda = "";
        berror = false;
        session.setgUsu(false);
        return "gestion";
    }
    
    public List<Articulo> cargarBusquedaOrd(){
        return buscarOrd();    
    }
    
    public List<Articulo> buscarOrd(){
        
        berror = false;
        articuloList = new ArrayList<Articulo>();
        
        if (publico == true){
            if (!busqueda.equals("")){           
                articuloList = findByTituloOrd (busqueda);
                if (articuloList.isEmpty()){
                    berror = true;
                }
            }
            else{
                articuloList = findAllArticulosOrd();
            }
        }
        
        else if (publico == false){
            if (!busqueda.equals("")){          
                articuloList = findNPByTituloOrd (busqueda);
                if (articuloList.isEmpty()){
                    berror = true;
                }
            }
            else{
                articuloList = findAllNPArticulosOrd();
            }
        }
        
        return articuloList;
    }
    
    public String realizarBusquedaOrd(){
        
        buscarOrd();
        return "gestion";
    }
    
    //Despublicar un Articulo
    public String despublicar(){
        
        articulo = findByID(articuloID.toString());
        articulo.setValidado(0);
        articulo.setSubido(0);
        
        articuloClient.edit_JSON(articulo, articulo.getIdArticulo().toString());
        
        return "gestion";
    }
    
//Asignación de Revisiones
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public String asignarRevision(){
        
        return "revisiones";
    }
    
    public boolean controlGestion(Articulo arti){
        
        return ((publico == false) && (arti.getRevisor()== null));
    }
    
    public List<Usuario> cargarRevisores(){
        
        revisoresList = new ArrayList<Usuario>();
        articulo = findByID(articuloID.toString());
        
        conocimiento = articulo.getAreaConocimientoList().get(0);
        
        Integer t = 3;
        System.out.println(t);
        revisores = findRevisores(t.toString());
        
        for (Usuario u: revisores){
            if (u.getAreaConocimientoList().contains(conocimiento)){
                revisoresList.add(u);
            }
        }
        
        if (revisoresList.isEmpty()){
            estudio = conocimiento.getCampoEstudioId();
            
            conocimientoList = findAreasByCampo(estudio.getId().toString());
            
            for (AreaConocimiento c : conocimientoList){
                for (Usuario u: revisores){
                    if (u.getAreaConocimientoList().contains(c)){
                        revisoresList.add(u);
                    }
                }   
            }  
        }
       
        return revisoresList;
    }
    
    //Mostrar el numero de Revisines Asignadas a un Usuario
    public String cargarNumRevisiones (Usuario usuarioS){
        
        if (usuarioS != null){
            return articuloClient.cuentaRevisiones_JSON(usuarioS.getIdUsuario());
        }
        else
            return null;
    }
    
    public String cargarConocimiento(Usuario user){
        
        return (user.getAreaConocimientoList().get(0)).getNombre();
    }
    
    public String cargarEstudio (Usuario user){
        
        AreaConocimiento areaC = user.getAreaConocimientoList().get(0);
        
        return areaC.getCampoEstudioId().getNombre();  
    }
    
    public String cargarConocimientoArt(Articulo art){
        
        return (art.getAreaConocimientoList().get(0)).getNombre();
    }
    
    public String cargarEstudioArt (Articulo art){
        
        AreaConocimiento areaC = art.getAreaConocimientoList().get(0);
        
        return areaC.getCampoEstudioId().getNombre();  
    }
   
    public String aplicarRevision(){
        
        articulo.setRevisor(revisor);
        articuloClient.edit_JSON(articulo, articulo.getIdArticulo().toString());
        
        usuarioClient.actNotificaciones_JSON(revisor, 3);
        
        return session.volverGestion();
    }
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
//Realización de Revisiones---------------------------------------------------------------
    
    //Cargar Articulos asignados para Revisar
    public List<Articulo> cargarArticulosR(){
        
        articuloList = findByRevisor(usu.getIdUsuario().toString());
        
        return articuloList;
    }

    //Publicar Articulo Revisado (nuevoArticulo.xhtml)
    public String enviarP(){
        enviar(1);
        
        if (terror == false){
            
            articulo.setValidado(1);
            articulo.setRevisor(null);
            articuloClient.edit_JSON(articulo, articulo.getIdArticulo().toString());
            
            Usuario usr = articulo.getRedactor();
            
            usuarioClient.actNotificaciones_JSON(usr, 4);
            
            return "misRevisiones";
        }
        
        else {
            return "nuevoArticulo";
        }
    }
    
    //Navegar a Mis Revisiones
    public String irRevisiones(){
        revision = true;
        usuarioClient.delNotificaciones_JSON(usu, 3);
        
        return "misRevisiones";
    }
    
    //Rechazar Articulo (nuevoArticulo.xhtml)
    public String rechazar(){
        
        enviarG();
        articulo.setSubido(0);
        articulo.setRevisor(null);
    
        articuloClient.edit_JSON(articulo, articulo.getIdArticulo().toString());
        
        Usuario usr = articulo.getRedactor();
       
        usuarioClient.actNotificaciones_JSON(usr, 5);
    
        return "misRevisiones";
    }


//Cargar Area de Estudio y Campo de Conocimiento al acceder a la creación/edicion
//de un articulo
    
    public String cargarCampo(){
        
        campoSel = 0;
        areaSel = 0;
        
        campoClient = new CampoClient();
        campoList = new ArrayList<CampoEstudio>();
        campoList = findAllCampos();
        
        if (articulo != null && pub == false){
            if (!articulo.getAreaConocimientoList().isEmpty()){
                areaSel = articulo.getAreaConocimientoList().get(0).getId();
            
                conocimiento = findByAreaID(areaSel.toString());
            
                for (CampoEstudio c: campoList){
                    areaList = new ArrayList<AreaConocimiento>();
                    areaList = findAreasByCampo(c.getId().toString());
                
                    if (areaList.contains(conocimiento)){
                        campoSel = c.getId();    
                    }
                }
            }
        
            areaList = findAreasByCampo(campoSel.toString());
            conocimiento = null;
        }
        
        else {  
            if (campoList.isEmpty()){
            campoList=null;
            }
        }
        
        return "nuevoArticulo";
    }
    
    //Buscar Area de Conocimiento a partir del Campo seleccionado
    public String seleccionaCampo()
    {
        areaList = new ArrayList<AreaConocimiento>();
        
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        textoA = request.getParameter("editor1");
        
        if (campoSel != 0){
            areaList = findAreasByCampo(campoSel.toString());
        }
        
        if (miZona == true){
            return "nuevoArticulo";
        }
        
        else{
            return "publicaciones";
        }
    }    

//Getter and Setter--------------------------------------------------------

    public LoginBean getSession() {
        return session;
    }

    public void setSession(LoginBean session) {
        this.session = session;
    }

    public Usuario getUsu() {
        return usu;
    }

    public void setUsu(Usuario usu) {
        this.usu = usu;
    }

    public UsuarioClient getUsuarioClient() {
        return usuarioClient;
    }

    public void setUsuarioClient(UsuarioClient usuarioClient) {
        this.usuarioClient = usuarioClient;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public List<Articulo> getArticulos() {
        return articulos;
    }

    public void setArticulos(List<Articulo> articulos) {
        this.articulos = articulos;
    }

    public List<Articulo> getArticuloList() {
        return articuloList;
    }

    public void setArticuloList(List<Articulo> articuloList) {
        this.articuloList = articuloList;
    }

    public ArticuloClient getArticuloClient() {
        return articuloClient;
    }

    public void setArticuloClient(ArticuloClient articuloClient) {
        this.articuloClient = articuloClient;
    }

    public Integer getArticuloID() {
        return articuloID;
    }

    public void setArticuloID(Integer articuloID) {
        this.articuloID = articuloID;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    public boolean isTerror() {
        return terror;
    }

    public void setTerror(boolean terror) {
        this.terror = terror;
    }

    public String getTituloA() {
        return tituloA;
    }

    public void setTituloA(String tituloA) {
        this.tituloA = tituloA;
    }

    public String getTextoA() {
        return textoA;
    }

    public void setTextoA(String textoA) {
        this.textoA = textoA;
        System.out.println("---------------------- " + textoA);
    }

    public Articulo getArticuloN() {
        return articuloN;
    }

    public void setArticuloN(Articulo articuloN) {
        this.articuloN = articuloN;
    }

    public String getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
    }

    public boolean isBerror() {
        return berror;
    }

    public void setBerror(boolean berror) {
        this.berror = berror;
    }

    public List<Articulo> getArticuloBusList() {
        return articuloBusList;
    }

    public void setArticuloBusList(List<Articulo> articuloBusList) {
        this.articuloBusList = articuloBusList;
    }

    public boolean getMiZona() {
        return miZona;
    }

    public void setMiZona(boolean miZona) {
        this.miZona = miZona;
    }

    public boolean isPublico() {
        return publico;
    }

    public void setPublico(boolean publico) {
        this.publico = publico;
    }

    public ValoracionBean getValBean() {
        return valBean;
    }

    public void setValBean(ValoracionBean valBean) {
        this.valBean = valBean;
    }

    public boolean isRevision() {
        return revision;
    }

    public void setRevision(boolean revision) {
        this.revision = revision;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public ValoracionClient getValoracionClient() {
        return valoracionClient;
    }

    public void setValoracionClient(ValoracionClient valoracionClient) {
        this.valoracionClient = valoracionClient;
    }

    public List<CampoEstudio> getCampoList() {
        return campoList;
    }

    public void setCampoList(List<CampoEstudio> campoList) {
        this.campoList = campoList;
    }

    public CampoClient getCampoClient() {
        return campoClient;
    }

    public void setCampoClient(CampoClient campoClient) {
        this.campoClient = campoClient;
    }

    public List<AreaConocimiento> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<AreaConocimiento> areaList) {
        this.areaList = areaList;
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

    public AreaClient getAreaClient() {
        return areaClient;
    }

    public void setAreaClient(AreaClient areaClient) {
        this.areaClient = areaClient;
    }

    public List<Usuario> getRevisores() {
        return revisores;
    }

    public void setRevisores(List<Usuario> revisores) {
        this.revisores = revisores;
    }

    public List<Usuario> getRevisoresList() {
        return revisoresList;
    }

    public void setRevisoresList(List<Usuario> revisoresList) {
        this.revisoresList = revisoresList;
    }

    public AreaConocimiento getConocimiento() {
        return conocimiento;
    }

    public void setConocimiento(AreaConocimiento conocimiento) {
        this.conocimiento = conocimiento;
    }

    public CampoEstudio getEstudio() {
        return estudio;
    }

    public void setEstudio(CampoEstudio estudio) {
        this.estudio = estudio;
    }

    public List<AreaConocimiento> getConocimientoList() {
        return conocimientoList;
    }

    public void setConocimientoList(List<AreaConocimiento> conocimientoList) {
        this.conocimientoList = conocimientoList;
    }

    public Usuario getRevisor() {
        return revisor;
    }

    public void setRevisor(Usuario revisor) {
        this.revisor = revisor;
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

    public boolean isPub() {
        return pub;
    }

    public void setPub(boolean pub) {
        this.pub = pub;
    }

    public ComentarioClient getComentarioClient() {
        return comentarioClient;
    }

    public void setComentarioClient(ComentarioClient comentarioClient) {
        this.comentarioClient = comentarioClient;
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

    public boolean isNoArea() {
        return noArea;
    }

    public void setNoArea(boolean noArea) {
        this.noArea = noArea;
    }

    public List<AreaConocimiento> getListConocimiento() {
        return listConocimiento;
    }

    public void setListConocimiento(List<AreaConocimiento> listConocimiento) {
        this.listConocimiento = listConocimiento;
    }

    public List<Articulo> getArticulosBeta() {
        return articulosBeta;
    }

    public void setArticulosBeta(List<Articulo> articulosBeta) {
        this.articulosBeta = articulosBeta;
    }

    public List<Articulo> getPublicadosArt() {
        return publicadosArt;
    }

    public void setPublicadosArt(List<Articulo> publicadosArt) {
        this.publicadosArt = publicadosArt;
    }

    public boolean isNoPublico() {
        return noPublico;
    }

    public void setNoPublico(boolean noPublico) {
        this.noPublico = noPublico;
    }
    
//-------------------------------------------------------------------------
    
    @PostConstruct
    public void init() {
        if (usu == null){
            usu = session.getUsuarioP();
        }
        articuloClient = new ArticuloClient();
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
    
    private java.util.List<Articulo> findByRedactorG(java.lang.String idRedactor) {
        Response r = articuloClient.findByRedactorG_JSON(Response.class, idRedactor);
        if (r.getStatus() == 200) {
            GenericType<List<Articulo>> genericType = new GenericType<List<Articulo>>() {
            };
            return r.readEntity(genericType);
        } else {
            return articulos;
        }
    }
    
    private java.util.List<Articulo> findByRevisor(java.lang.String idRevisor) {
        Response r = articuloClient.findByRevisor_JSON(Response.class, idRevisor);
        if (r.getStatus() == 200) {
            GenericType<List<Articulo>> genericType = new GenericType<List<Articulo>>() {
            };
            return r.readEntity(genericType);
        } else {
            return articulos;
        }
    }
    
    private Articulo findByID(java.lang.String articuloID) {
        Response r = articuloClient.find_JSON(Response.class, articuloID);
        GenericType<Articulo> genericType = new GenericType<Articulo>() {
        };
        return r.readEntity(genericType);
    }
    
    private void deleteArticulo(java.lang.String articuloID) {
        articuloClient.remove(articuloID);
    }
    
    private Articulo findByTitulo(java.lang.String titulo) {
        Response r = articuloClient.findByTitulo_JSON(Response.class, titulo);
        GenericType<Articulo> genericType = new GenericType<Articulo>() {
        };
        return r.readEntity(genericType);
    }
   
    private java.util.List<Articulo> findByTituloB(java.lang.String titulo) {
        Response r = articuloClient.findByTituloB_JSON(Response.class, titulo);
        if (r.getStatus() == 200) {
            GenericType<List<Articulo>> genericType = new GenericType<List<Articulo>>() {
            };
            return r.readEntity(genericType);
        } else {
            return articulos;
        }
    }
    
    private java.util.List<Articulo> findAllArticulosOrd() {
        Response r = articuloClient.findAllArticulosOrd_JSON(Response.class);
        if (r.getStatus() == 200) {
            GenericType<List<Articulo>> genericType = new GenericType<List<Articulo>>() {
            };
            return r.readEntity(genericType);
        } else {
            return articulos;
        }
    }
    
    private java.util.List<Articulo> findAllOrdFN() {
        Response r = articuloClient.findAllOrdFN_JSON(Response.class);
        if (r.getStatus() == 200) {
            GenericType<List<Articulo>> genericType = new GenericType<List<Articulo>>() {
            };
            return r.readEntity(genericType);
        } else {
            return articulos;
        }
    }
    
    private java.util.List<Articulo> findAllNPArticulosOrd() {
        Response r = articuloClient.findAllNPArticulosOrd_JSON(Response.class);
        if (r.getStatus() == 200) {
            GenericType<List<Articulo>> genericType = new GenericType<List<Articulo>>() {
            };
            return r.readEntity(genericType);
        } else {
            return articulos;
        }
    }
    
    private java.util.List<Articulo> findByTituloOrd(java.lang.String titulo) {
        Response r = articuloClient.findByTituloOrd_JSON(Response.class, titulo);
        if (r.getStatus() == 200) {
            GenericType<List<Articulo>> genericType = new GenericType<List<Articulo>>() {
            };
            return r.readEntity(genericType);
        } else {
            return articulos;
        }
    }
    
    private java.util.List<Articulo> findNPByTituloOrd(java.lang.String titulo) {
        Response r = articuloClient.findNPByTituloOrd_JSON(Response.class, titulo);
        if (r.getStatus() == 200) {
            GenericType<List<Articulo>> genericType = new GenericType<List<Articulo>>() {
            };
            return r.readEntity(genericType);
        } else {
            return articulos;
        }
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
    
    private java.util.List<Usuario> findRevisores(java.lang.String rango) {
        Response r = usuarioClient.findRevisores_JSON(Response.class, rango);
        if (r.getStatus() == 200) {
            GenericType<List<Usuario>> genericType = new GenericType<List<Usuario>>() {
            };
            return r.readEntity(genericType);
        } else {
            return revisoresList;
        }
    }
    
    private AreaConocimiento findByAreaID(java.lang.String areaSel) {
        Response r = areaClient.find_JSON(Response.class, areaSel);
        GenericType<AreaConocimiento> genericType = new GenericType<AreaConocimiento>() {
        };
        return r.readEntity(genericType);
    }
    
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
}
