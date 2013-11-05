/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.polijic.jpql.controladores;

import co.edu.polijic.jpql.controladores.exceptions.NonexistentEntityException;
import co.edu.polijic.jpql.entidades.PropiedadesJuego;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author omesa
 */
public class PropiedadesJuegoJpaController implements Serializable {

    public PropiedadesJuegoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PropiedadesJuego propiedadesJuego) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(propiedadesJuego);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PropiedadesJuego propiedadesJuego) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            propiedadesJuego = em.merge(propiedadesJuego);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = propiedadesJuego.getIdPropiedadJuego();
                if (findPropiedadesJuego(id) == null) {
                    throw new NonexistentEntityException("The propiedadesJuego with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PropiedadesJuego propiedadesJuego;
            try {
                propiedadesJuego = em.getReference(PropiedadesJuego.class, id);
                propiedadesJuego.getIdPropiedadJuego();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The propiedadesJuego with id " + id + " no longer exists.", enfe);
            }
            em.remove(propiedadesJuego);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PropiedadesJuego> findPropiedadesJuegoEntities() {
        return findPropiedadesJuegoEntities(true, -1, -1);
    }

    public List<PropiedadesJuego> findPropiedadesJuegoEntities(int maxResults, int firstResult) {
        return findPropiedadesJuegoEntities(false, maxResults, firstResult);
    }

    private List<PropiedadesJuego> findPropiedadesJuegoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PropiedadesJuego.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public PropiedadesJuego findPropiedadesJuego(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PropiedadesJuego.class, id);
        } finally {
            em.close();
        }
    }

    public int getPropiedadesJuegoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PropiedadesJuego> rt = cq.from(PropiedadesJuego.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
