/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import ejb.ArticuloFacade;
import ejb.ComentariosFacade;
import entity.Comentarios;
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
@Path("entity.comentarios")
public class ComentariosFacadeREST {

    @EJB
    ComentariosFacade comentariosFacade;
    
    @EJB
    private ArticuloFacade articuloF;

    public ComentariosFacadeREST() {
        
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Comentarios entity) {
        comentariosFacade.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Comentarios entity) {
        comentariosFacade.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        comentariosFacade.remove(comentariosFacade.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Comentarios find(@PathParam("id") Integer id) {
        return comentariosFacade.find(id);
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Comentarios> findAll() {
        return comentariosFacade.findAll();
    }
    
    @GET
    @Path("articulo/{articulo}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Comentarios> findComentByArticulo(@PathParam("articulo") Integer articulo) {
        return comentariosFacade.findComentByArticulo(articuloF.find(articulo));
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Comentarios> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return comentariosFacade.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(comentariosFacade.count());
    }
    
}
