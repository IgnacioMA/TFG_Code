/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import ejb.ArticuloFacade;
import ejb.ValoracionFacade;
import entity.Valoracion;
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
@Path("entity.valoracion")
public class ValoracionFacadeREST {

    @EJB
    ValoracionFacade valoracionFacade;
    
    @EJB
    private ArticuloFacade articuloFacade;

    public ValoracionFacadeREST() {
        
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Valoracion entity) {
        valoracionFacade.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Valoracion entity) {
        valoracionFacade.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        valoracionFacade.remove(valoracionFacade.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Valoracion find(@PathParam("id") Integer id) {
        return valoracionFacade.find(id);
    }
    
    @GET
    @Path("articulo/{articulo}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Valoracion> findComentByArticulo(@PathParam("articulo") Integer articulo) {
        return valoracionFacade.findComentByArticulo(articuloFacade.find(articulo));
    }
    
    @GET
    @Path("articuloM/{articuloM}")
    @Produces({MediaType.TEXT_PLAIN})
    public String mediaPuntuacion(@PathParam("articuloM") Integer articuloM) {
        Double valor = valoracionFacade.mediaPuntuacion(articuloFacade.find(articuloM));
        if (valor!=null){
            return valor.toString();}
        else{
            return "0";
        }
    }
    
    @GET
    @Path("articuloC/{articuloC}")
    @Produces({MediaType.TEXT_PLAIN})
    public String cuentaVotos(@PathParam("articuloC") Integer articuloC) {
        Long valor = valoracionFacade.cuentaVotos(articuloFacade.find(articuloC));
        if (valor!=null){
            return valor.toString();}
        else{
            return "0";
        }
    }
  
    @GET
    @Path("puntuacion/{articulo}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Valoracion> findValoracion(@PathParam("articulo") Integer articulo){
        return valoracionFacade.findValoracion(articuloFacade.find(articulo));
    }


    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Valoracion> findAll() {
        return valoracionFacade.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Valoracion> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return valoracionFacade.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(valoracionFacade.count());
    }
    
}
