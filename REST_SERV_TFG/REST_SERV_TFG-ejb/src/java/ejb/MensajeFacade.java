/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import entity.Mensaje;
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
public class MensajeFacade extends AbstractFacade<Mensaje> {

    @PersistenceContext(unitName = "REST_SERV_TFG-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MensajeFacade() {
        super(Mensaje.class);
    }
    
    public List<Mensaje> findByReceptor(Integer id) {
        try{
            Query q = em.createNamedQuery("Mensaje.findByIdReceptor").setParameter("idReceptor", id);
            return q.getResultList();
        }
        catch(NoResultException e)
        {
            return null;
        }
    }
    
    public List<Mensaje> findNLByReceptor(Integer id) {
        try{
            Query q = em.createNamedQuery("Mensaje.findNLByIdReceptor").setParameter("idReceptor", id);
            return q.getResultList();
        }
        catch(NoResultException e)
        {
            return null;
        }
    }
    
    public List<Mensaje> findByEmisor(Usuario id) {
        try{
            Query q = em.createNamedQuery("Mensaje.findByIdEmisor").setParameter("idEmisor", id);
            return q.getResultList();
        }
        catch(NoResultException e)
        {
            return null;
        }
    }
}
