/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import entity.Articulo;
import entity.Valoracion;
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
public class ValoracionFacade extends AbstractFacade<Valoracion> {

    @PersistenceContext(unitName = "REST_SERV_TFG-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ValoracionFacade() {
        super(Valoracion.class);
    }
    
    public List<Valoracion> findComentByArticulo(Articulo articulo) {
        try{
            Query q = em.createNamedQuery("Valoracion.findComentByArticulo").setParameter("articulo", articulo);
            return q.getResultList();
        }
        catch(NoResultException e)
        {
            return null;
        }
    }
    
    public List<Valoracion> findValoracion(Articulo articulo) {
        
        try{
            Query q = em.createNamedQuery("Valoracion.findValoracion").setParameter("articulo", articulo);
            return q.getResultList();
        }
        catch(NoResultException e)
        {
            return null;
        }
    }
    
    public Double mediaPuntuacion(Articulo articulo) {
        
        try{
            Query q = em.createNamedQuery("Valoracion.mediaPuntuacion").setParameter("articulo", articulo);
            return (Double) q.getSingleResult();
        }
        catch(NoResultException e)
        {
            return new Double(0);
        }
    }
    
    public Long cuentaVotos(Articulo articulo) {
        
        try{
            Query q = em.createNamedQuery("Valoracion.cuentaVotos").setParameter("articulo", articulo);
            return (Long) q.getSingleResult();
        }
        catch(NoResultException e)
        {
            return new Long(0);
        }
    }
}
