/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import entity.Notificaciones;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ignacio Marqués
 */
@Stateless
public class NotificacionesFacade extends AbstractFacade<Notificaciones> {

    @PersistenceContext(unitName = "REST_SERV_TFG-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NotificacionesFacade() {
        super(Notificaciones.class);
    }

    @Override
    public void edit(Notificaciones entity) {
        super.edit(entity);
        this.getEntityManager().flush();
    }
  
}
