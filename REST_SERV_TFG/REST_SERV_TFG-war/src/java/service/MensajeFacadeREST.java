/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import ejb.MensajeFacade;
import ejb.UsuarioFacade;
import entity.Mensaje;
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
@Path("entity.mensaje")
public class MensajeFacadeREST {

    @EJB
    MensajeFacade mensajeFacade;
    
    @EJB
    private UsuarioFacade usuarioFacade;

    public MensajeFacadeREST() {
        
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Mensaje entity) {
        mensajeFacade.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Mensaje entity) {
        mensajeFacade.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        mensajeFacade.remove(mensajeFacade.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Mensaje find(@PathParam("id") Integer id) {
        return mensajeFacade.find(id);
    }
    
    @GET
    @Path("idE/{idE}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Mensaje> findByEmisor(@PathParam("idE") Integer idE) {
        return mensajeFacade.findByEmisor(usuarioFacade.find(idE));
    }
    
    @GET
    @Path("idR/{idR}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Mensaje> findByReceptor(@PathParam("idR") Integer idR) {
        return mensajeFacade.findByReceptor(idR);
    }
    
    @GET
    @Path("idRNL/{idRNL}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Mensaje> findNLByReceptor(@PathParam("idRNL") Integer idRNL) {
        return mensajeFacade.findNLByReceptor(idRNL);
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Mensaje> findAll() {
        return mensajeFacade.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Mensaje> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return mensajeFacade.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(mensajeFacade.count());
    }
    
}
