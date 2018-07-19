/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import ejb.AreaConocimientoFacade;
import ejb.CampoEstudioFacade;
import entity.AreaConocimiento;
import entity.CampoEstudio;
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
@Path("entity.areaconocimiento")
public class AreaConocimientoFacadeREST {
    
    @EJB
    AreaConocimientoFacade areaConocimientoFacade;
    
    @EJB
    CampoEstudioFacade campoEstudioFacade;

    public AreaConocimientoFacadeREST() {
        
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(AreaConocimiento entity) {
        areaConocimientoFacade.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, AreaConocimiento entity) {
        areaConocimientoFacade.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        areaConocimientoFacade.remove(areaConocimientoFacade.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public AreaConocimiento find(@PathParam("id") Integer id) {
        return areaConocimientoFacade.find(id);
    }
    
    @GET
    @Path("campoID/{campoID}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<AreaConocimiento> findAreasByCampo(@PathParam("campoID") Integer campoID) {
        return areaConocimientoFacade.findAreasByCampo(campoID);
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<AreaConocimiento> findAll() {
        return areaConocimientoFacade.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<AreaConocimiento> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return areaConocimientoFacade.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(areaConocimientoFacade.count());
    }
    
}
