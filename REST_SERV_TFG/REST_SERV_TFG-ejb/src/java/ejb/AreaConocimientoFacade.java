/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import entity.AreaConocimiento;
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
public class AreaConocimientoFacade extends AbstractFacade<AreaConocimiento> {

    @PersistenceContext(unitName = "REST_SERV_TFG-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AreaConocimientoFacade() {
        super(AreaConocimiento.class);
    }
    
    public List<AreaConocimiento> findAreasByCampo(Integer campoID) {
        try{
            Query q = em.createNamedQuery("AreaConocimiento.findAreasByCampo").setParameter("campoID", campoID);
            return q.getResultList();
        }
        catch(NoResultException e)
        {
            return null;
        }
    }
    
}
