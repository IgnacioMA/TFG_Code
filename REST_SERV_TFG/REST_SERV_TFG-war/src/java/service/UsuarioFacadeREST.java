/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import ejb.AreaConocimientoFacade;
import ejb.CategoriaFacade;
import ejb.NotificacionesFacade;
import ejb.SancionFacade;
import ejb.UsuarioFacade;
import entity.AreaConocimiento;
import entity.Notificaciones;
import entity.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Ignacio Marqués
 */
@Stateless
@Path("entity.usuario")
public class UsuarioFacadeREST {

    @EJB
    private UsuarioFacade usuarioFacade;
    
    @EJB
    private CategoriaFacade categoriaFacade;
    
    @EJB
    private SancionFacade sancionFacade;
    
    @EJB
    private AreaConocimientoFacade conocimientoFacade;
    
    @EJB
    private NotificacionesFacade notificacionFacade;

    public UsuarioFacadeREST() {
        
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Usuario entity) {
        entity.setIdRango(categoriaFacade.find(1));
        entity.setSancionId(sancionFacade.find(1));
        usuarioFacade.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Usuario entity) {
        usuarioFacade.edit(entity);
    }
   
    @PUT
    @Path("areas/{area}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void editArea(@PathParam("area") Integer areaU, Usuario entity) {
       
        List<AreaConocimiento> listaArea = new ArrayList<AreaConocimiento>();
        Usuario u = usuarioFacade.find(entity.getIdUsuario());

        if (!u.getAreaConocimientoList().isEmpty()){
            AreaConocimiento aB;
            aB = u.getAreaConocimientoList().get(0);
            aB.getUsuarioList().remove(u);
        }

        AreaConocimiento ac = conocimientoFacade.find(areaU);
        
        List <Usuario> ul = ac.getUsuarioList();
        
        if (ul.isEmpty()){
            ul = new ArrayList<Usuario>();
            ul.add(entity);
            ac.setUsuarioList(ul);
            conocimientoFacade.edit(ac);
        }
        
        else if (!ul.contains(entity)){
            ul.add(entity);
            ac.setUsuarioList(ul);
            conocimientoFacade.edit(ac);
        }

        listaArea.add(ac);
        entity.setAreaConocimientoList(listaArea);
        usuarioFacade.edit(entity);
    }
    
    @PUT
    @Path("notificaciones/{notificacion}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void asignarNotificacion(@PathParam("notificacion") Integer notificacion, Usuario entity) {
       
        List<Notificaciones> listaNotif = new ArrayList<Notificaciones>();
        Notificaciones notif = notificacionFacade.find(notificacion);
        Usuario u = usuarioFacade.find(entity.getIdUsuario());
       
        if (!u.getNotificacionesList().isEmpty()){
            listaNotif = u.getNotificacionesList(); 
            if (!listaNotif.contains(notif)){
                notif.getUsuarioList().add(entity);
                notificacionFacade.edit(notif);
                listaNotif.add(notif);
                entity.setNotificacionesList(listaNotif);
                usuarioFacade.edit(entity);
            }
        }
        
        else{
            notif.getUsuarioList().add(entity);
            notificacionFacade.edit(notif);
            listaNotif.add(notif);
            entity.setNotificacionesList(listaNotif);
            usuarioFacade.edit(entity);
        }
    }
    
    @PUT
    @Path("notificacionesDel/{notificacionDel}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void eliminarNotificacion(@PathParam("notificacionDel") Integer notificacionDel, Usuario entity) {
       
        List<Notificaciones> listaNotif;
        Notificaciones notif = notificacionFacade.find(notificacionDel);
        Usuario u = usuarioFacade.find(entity.getIdUsuario());

        if (!u.getNotificacionesList().isEmpty()){
            listaNotif = u.getNotificacionesList(); 
            if (listaNotif.contains(notif)){
                notif.getUsuarioList().remove(entity);
                notificacionFacade.edit(notif);
                listaNotif.remove(notif);
                entity.setNotificacionesList(listaNotif);
                usuarioFacade.edit(entity);
            }
        }
    }
 
    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        usuarioFacade.remove(usuarioFacade.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Usuario find(@PathParam("id") Integer id) {
        return usuarioFacade.find(id);
    }

    @GET
    @Path("alias/{alias}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Usuario findByAlias(@PathParam("alias") String alias) {
        return usuarioFacade.findByAlias(alias);
    }
    
    @GET
    @Path("aliasOrd/{alias}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Usuario> findByAliasOrd(@PathParam("alias") String alias) {
        return usuarioFacade.findByAliasOrd(alias);
    }
    
    @GET
    @Path("revisores/{rango}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Usuario> findRevisores(@PathParam("rango") String rango) {
        return usuarioFacade.findRevisores(categoriaFacade.find(Integer.parseInt(rango)));
    }
    
    @GET
    @Path("email/{email}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Usuario findByEmail(@PathParam("email") String email) {
        return usuarioFacade.findByEmail(email);
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Usuario> findAll() {
        return usuarioFacade.findAll();
    }
    
    @GET
    @Path("ordenado/")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Usuario> findAllUsuariosOrd() {
        return usuarioFacade.findAllUsuariosOrd();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Usuario> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return usuarioFacade.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(usuarioFacade.count());
    }
    
}
