/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import ejb.AreaConocimientoFacade;
import ejb.ArticuloFacade;
import ejb.UsuarioFacade;
import entity.AreaConocimiento;
import entity.Articulo;
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
@Path("entity.articulo")
public class ArticuloFacadeREST {

    @EJB
    ArticuloFacade articuloFacade;
    
    @EJB
    private UsuarioFacade usuarioFacade;
    
    @EJB
    private AreaConocimientoFacade conocimientoFacade;

    public ArticuloFacadeREST() {
        
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Articulo entity) {
        articuloFacade.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Articulo entity) {
        articuloFacade.edit(entity);
    }
    
    @PUT
    @Path("areasArt/{areaArt}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void editArea(@PathParam("areaArt") Integer areaU, Articulo entity) {
        
        List<AreaConocimiento> listaArea = new ArrayList<AreaConocimiento>();
        Articulo a = articuloFacade.findByTitulo(entity.getTitulo());

        if (!a.getAreaConocimientoList().isEmpty()){
            AreaConocimiento aB;
            aB = a.getAreaConocimientoList().get(0);
            aB.getArticuloList().remove(a);
        }
        
        AreaConocimiento ac = conocimientoFacade.find(areaU);
        List <Articulo> al = ac.getArticuloList();
        
        if (al.isEmpty()){
            al = new ArrayList<Articulo>();
            al.add(a);
            ac.setArticuloList(al);
            conocimientoFacade.edit(ac);
        }
        
        else if (!al.contains(entity)){
            al.add(a);
            ac.setArticuloList(al);
            conocimientoFacade.edit(ac);
        }

        listaArea.add(ac);
        a.setAreaConocimientoList(listaArea);
        articuloFacade.edit(a);
    }
    
    @PUT
    @Path("areasArtElim/{areaArtElim}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void elimArea(@PathParam("areaArtElim") Integer areaU, Articulo entity) {

        Articulo a = articuloFacade.findByTitulo(entity.getTitulo());

        if (!a.getAreaConocimientoList().isEmpty()){
            AreaConocimiento aB;
            aB = a.getAreaConocimientoList().get(0);
            aB.getArticuloList().remove(a);
            conocimientoFacade.edit(aB);
        }   
        
        a.setAreaConocimientoList(null);   
        articuloFacade.edit(a);
    }
    
    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        articuloFacade.remove(articuloFacade.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Articulo find(@PathParam("id") Integer id) {
        return articuloFacade.find(id);
    }
    
    @GET
    @Path("redactorP/{redactorP}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Articulo> findByRedactorP(@PathParam("redactorP") Integer redactorP) {
        return articuloFacade.findByAutorP(usuarioFacade.find(redactorP));
    }
    
    @GET
    @Path("redactorG/{redactorG}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Articulo> findByRedactorG(@PathParam("redactorG") Integer redactorG) {
        return articuloFacade.findByAutorG(usuarioFacade.find(redactorG));
    }
    
    @GET
    @Path("revisor/{revisor}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Articulo> findByRevisor(@PathParam("revisor") Integer revisor) {
        return articuloFacade.findByRevisor(usuarioFacade.find(revisor));
    }
    
    @GET
    @Path("titulo/{titulo}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Articulo findByTitulo(@PathParam("titulo") String titulo) {
        return articuloFacade.findByTitulo(titulo);
    }
    
    @GET
    @Path("numRevisiones/{revisor}")
    @Produces({MediaType.TEXT_PLAIN})
    public String cuentaRevisiones(@PathParam("revisor") Integer revisor) {
        Long valor = articuloFacade.cuentaRevisiones(usuarioFacade.find(revisor));
        if (valor!=null){
            return valor.toString();}
        else{
            return "0";
        }
    }
    
    @GET
    @Path("tituloB/{tituloB}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Articulo> findByTituloB(@PathParam("tituloB") String tituloB) {
        return articuloFacade.findByTituloB(tituloB);
    }
    
    @GET
    @Path("tituloOrd/{titulo}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Articulo> findByTituloOrd(@PathParam("titulo") String titulo) {
        return articuloFacade.findByTituloOrd(titulo);
    }
    
    @GET
    @Path("tituloNPOrd/{titulo}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Articulo> findNPByTituloOrd(@PathParam("titulo") String titulo) {
        return articuloFacade.findNPByTituloOrd(titulo);
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Articulo> findAll() {
        return articuloFacade.findAll();
    }
    
    @GET
    @Path("ordenado/")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Articulo> findAllArticulosOrd() {
        return articuloFacade.findAllArticulosOrd();
    }
    
    @GET
    @Path("ordenadoFN/")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Articulo> findAllOrdFN() {
        return articuloFacade.findAllOrdFN();
    }
    
    @GET
    @Path("ordenadoNP/")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Articulo> findAllNPArticulosOrd() {
        return articuloFacade.findAllNPArticulosOrd();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Articulo> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return articuloFacade.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(articuloFacade.count());
    }
    
}
