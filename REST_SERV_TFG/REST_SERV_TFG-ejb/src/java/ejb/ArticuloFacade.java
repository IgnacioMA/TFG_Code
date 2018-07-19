/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import entity.Articulo;
import entity.Usuario;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Ignacio Marqués
 */
@Stateless
public class ArticuloFacade extends AbstractFacade<Articulo> {

    @PersistenceContext(unitName = "REST_SERV_TFG-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ArticuloFacade() {
        super(Articulo.class);
    }
    
    public List<Articulo> findByAutorP(Usuario redactor) {
        try{
            Query q = em.createNamedQuery("Articulo.findByAutorP").setParameter("redactor", redactor);
            return q.getResultList();
        }
        catch(NoResultException e)
        {
            return null;
        }
    }
    
    public List<Articulo> findByAutorG(Usuario redactor) {
        try{
            Query q = em.createNamedQuery("Articulo.findByAutorG").setParameter("redactor", redactor);
            return q.getResultList();
        }
        catch(NoResultException e)
        {
            return null;
        }
    }
    
    public List<Articulo> findByRevisor(Usuario revisor) {
        try{
            Query q = em.createNamedQuery("Articulo.findByRevisor").setParameter("revisor", revisor);
            return q.getResultList();
        }
        catch(NoResultException e)
        {
            return null;
        }
    }
    
    public Articulo findByTitulo(String titulo) {
        
        try{
            Query q = em.createNamedQuery("Articulo.findByTitulo").setParameter("titulo", titulo);
            return (Articulo) q.getSingleResult();
        }
        catch(NoResultException e)
        {
            return null;
        }
    }
    
    public List<Articulo> findByTituloB(String tituloB) {
        
        try{
            Query q = em.createNamedQuery("Articulo.findByTituloB").setParameter("tituloB", tituloB);
            return q.getResultList();
        }
        catch(NoResultException e)
        {
            return null;
        }
    }
    
    public List<Articulo> findByTituloOrd(String titulo) {
        try{
            Query q = em.createNamedQuery("Articulo.findByTituloOrd").setParameter("titulo", titulo);
            return q.getResultList();
        }
        catch(NoResultException e)
        {
            return null;
        }
    }
    
    public List<Articulo> findAllArticulosOrd() {
        try{
            Query q = em.createNamedQuery("Articulo.findAllArticulosOrd");
            return q.getResultList();
        }
        catch(NoResultException e)
        {
            return null;
        }
    }
    
    public List<Articulo> findAllOrdFN() {
        try{
            Query q = em.createNamedQuery("Articulo.findByAllOrdFN");
            return q.getResultList();
        }
        catch(NoResultException e)
        {
            return null;
        }
    }
    
    public List<Articulo> findNPByTituloOrd(String titulo) {
        try{
            Query q = em.createNamedQuery("Articulo.findNPByTituloOrd").setParameter("titulo", titulo);
            return q.getResultList();
        }
        catch(NoResultException e)
        {
            return null;
        }
    }
    
    public List<Articulo> findAllNPArticulosOrd() {
        try{
            Query q = em.createNamedQuery("Articulo.findAllNPArticulosOrd");
            return q.getResultList();
        }
        catch(NoResultException e)
        {
            return null;
        }
    }
    
    public Long cuentaRevisiones(Usuario revisor) {
        
        try{
            Query q = em.createNamedQuery("Articulo.cuentaRevisiones").setParameter("revisor", revisor);
            return (Long) q.getSingleResult();
        }
        catch(NoResultException e)
        {
            return new Long (0);
        }
    } 
}
