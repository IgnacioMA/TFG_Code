/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import entity.Categoria;
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
public class UsuarioFacade extends AbstractFacade<Usuario> {

    @PersistenceContext(unitName = "REST_SERV_TFG-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }

    @Override
    public void edit(Usuario entity) {
        super.edit(entity); 
        this.getEntityManager().flush();
    }
    
    public Usuario findByAlias(String alias) {
        
        try{
            Query q = em.createNamedQuery("Usuario.findByAlias").setParameter("alias", alias);
            return (Usuario) q.getSingleResult();
        }
        catch(NoResultException e)
        {
            return null;
        }
    }
   
    public List<Usuario> findByAliasOrd(String alias) {
        try{
            Query q = em.createNamedQuery("Usuario.findByAliasOrd").setParameter("alias", alias);
            return q.getResultList();
        }
        catch(NoResultException e)
        {
            return null;
        }
    }
    
    public Usuario findByEmail(String email) {
        
        try{
            Query q = em.createNamedQuery("Usuario.findByEmail").setParameter("email", email);
            return (Usuario) q.getSingleResult();
        }
        catch(NoResultException e)
        {
            return null;
        }
    }
    
    public List<Usuario> findAllUsuariosOrd() {
        try{
            Query q = em.createNamedQuery("Usuario.findAllUsuariosOrd");
            return q.getResultList();
        }
        catch(NoResultException e)
        {
            return null;
        }
    }
    
    public List<Usuario> findRevisores(Categoria rango) {
        try{
            Query q = em.createNamedQuery("Usuario.findRevisores").setParameter("rango", rango);
            return q.getResultList();
        }
        catch(NoResultException e)
        {
            return null;
        }
    }
    
}
