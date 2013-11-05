/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.polijic.jpql.controladores;

import co.edu.polijic.jpql.controladores.exceptions.NonexistentEntityException;
import co.edu.polijic.jpql.controladores.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.edu.polijic.jpql.entidades.Perfil;
import co.edu.polijic.jpql.entidades.Permiso;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author omesa
 */
public class PermisoJpaController implements Serializable {

    public PermisoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Permiso permiso) throws PreexistingEntityException, Exception {
        if (permiso.getPerfiles() == null) {
            permiso.setPerfiles(new ArrayList<Perfil>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Perfil> attachedPerfiles = new ArrayList<Perfil>();
            for (Perfil perfilesPerfilToAttach : permiso.getPerfiles()) {
                perfilesPerfilToAttach = em.getReference(perfilesPerfilToAttach.getClass(), perfilesPerfilToAttach.getIdPerfil());
                attachedPerfiles.add(perfilesPerfilToAttach);
            }
            permiso.setPerfiles(attachedPerfiles);
            em.persist(permiso);
            for (Perfil perfilesPerfil : permiso.getPerfiles()) {
                perfilesPerfil.getPermisos().add(permiso);
                perfilesPerfil = em.merge(perfilesPerfil);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPermiso(permiso.getIdPermiso()) != null) {
                throw new PreexistingEntityException("Permiso " + permiso + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Permiso permiso) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Permiso persistentPermiso = em.find(Permiso.class, permiso.getIdPermiso());
            List<Perfil> perfilesOld = persistentPermiso.getPerfiles();
            List<Perfil> perfilesNew = permiso.getPerfiles();
            List<Perfil> attachedPerfilesNew = new ArrayList<Perfil>();
            for (Perfil perfilesNewPerfilToAttach : perfilesNew) {
                perfilesNewPerfilToAttach = em.getReference(perfilesNewPerfilToAttach.getClass(), perfilesNewPerfilToAttach.getIdPerfil());
                attachedPerfilesNew.add(perfilesNewPerfilToAttach);
            }
            perfilesNew = attachedPerfilesNew;
            permiso.setPerfiles(perfilesNew);
            permiso = em.merge(permiso);
            for (Perfil perfilesOldPerfil : perfilesOld) {
                if (!perfilesNew.contains(perfilesOldPerfil)) {
                    perfilesOldPerfil.getPermisos().remove(permiso);
                    perfilesOldPerfil = em.merge(perfilesOldPerfil);
                }
            }
            for (Perfil perfilesNewPerfil : perfilesNew) {
                if (!perfilesOld.contains(perfilesNewPerfil)) {
                    perfilesNewPerfil.getPermisos().add(permiso);
                    perfilesNewPerfil = em.merge(perfilesNewPerfil);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = permiso.getIdPermiso();
                if (findPermiso(id) == null) {
                    throw new NonexistentEntityException("The permiso with id " + id + " no longer exists.");
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
            Permiso permiso;
            try {
                permiso = em.getReference(Permiso.class, id);
                permiso.getIdPermiso();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The permiso with id " + id + " no longer exists.", enfe);
            }
            List<Perfil> perfiles = permiso.getPerfiles();
            for (Perfil perfilesPerfil : perfiles) {
                perfilesPerfil.getPermisos().remove(permiso);
                perfilesPerfil = em.merge(perfilesPerfil);
            }
            em.remove(permiso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Permiso> findPermisoEntities() {
        return findPermisoEntities(true, -1, -1);
    }

    public List<Permiso> findPermisoEntities(int maxResults, int firstResult) {
        return findPermisoEntities(false, maxResults, firstResult);
    }

    private List<Permiso> findPermisoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Permiso.class));
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

    public Permiso findPermiso(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Permiso.class, id);
        } finally {
            em.close();
        }
    }

    public int getPermisoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Permiso> rt = cq.from(Permiso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
