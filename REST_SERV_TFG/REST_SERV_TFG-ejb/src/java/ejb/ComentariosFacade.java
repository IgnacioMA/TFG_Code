/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import entity.Articulo;
import entity.Comentarios;
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
public class ComentariosFacade extends AbstractFacade<Comentarios> {

    @PersistenceContext(unitName = "REST_SERV_TFG-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ComentariosFacade() {
        super(Comentarios.class);
    }
    
    public List<Comentarios> findComentByArticulo(Articulo articulo) {
        try{
            Query q = em.createNamedQuery("Comentarios.findComentByArticulo").setParameter("articulo", articulo);
            return q.getResultList();
        }
        catch(NoResultException e)
        {
            return null;
        }
    }
    
}
