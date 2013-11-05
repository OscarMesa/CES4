/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.polijic.jpql.controladores;

import co.edu.polijic.jpql.controladores.exceptions.NonexistentEntityException;
import co.edu.polijic.jpql.entidades.Perfil;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.edu.polijic.jpql.entidades.Permiso;
import java.util.ArrayList;
import java.util.List;
import co.edu.polijic.jpql.entidades.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author omesa
 */
public class PerfilJpaController implements Serializable {

    public PerfilJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Perfil perfil) {
        if (perfil.getPermisos() == null) {
            perfil.setPermisos(new ArrayList<Permiso>());
        }
        if (perfil.getUsuarios() == null) {
            perfil.setUsuarios(new ArrayList<Usuario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Permiso> attachedPermisos = new ArrayList<Permiso>();
            for (Permiso permisosPermisoToAttach : perfil.getPermisos()) {
                permisosPermisoToAttach = em.getReference(permisosPermisoToAttach.getClass(), permisosPermisoToAttach.getIdPermiso());
                attachedPermisos.add(permisosPermisoToAttach);
            }
            perfil.setPermisos(attachedPermisos);
            List<Usuario> attachedUsuarios = new ArrayList<Usuario>();
            for (Usuario usuariosUsuarioToAttach : perfil.getUsuarios()) {
                usuariosUsuarioToAttach = em.getReference(usuariosUsuarioToAttach.getClass(), usuariosUsuarioToAttach.getPk());
                attachedUsuarios.add(usuariosUsuarioToAttach);
            }
            perfil.setUsuarios(attachedUsuarios);
            em.persist(perfil);
            for (Permiso permisosPermiso : perfil.getPermisos()) {
                permisosPermiso.getPerfiles().add(perfil);
                permisosPermiso = em.merge(permisosPermiso);
            }
            for (Usuario usuariosUsuario : perfil.getUsuarios()) {
                usuariosUsuario.getPerfiles().add(perfil);
                usuariosUsuario = em.merge(usuariosUsuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Perfil perfil) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Perfil persistentPerfil = em.find(Perfil.class, perfil.getIdPerfil());
            List<Permiso> permisosOld = persistentPerfil.getPermisos();
            List<Permiso> permisosNew = perfil.getPermisos();
            List<Usuario> usuariosOld = persistentPerfil.getUsuarios();
            List<Usuario> usuariosNew = perfil.getUsuarios();
            List<Permiso> attachedPermisosNew = new ArrayList<Permiso>();
            for (Permiso permisosNewPermisoToAttach : permisosNew) {
                permisosNewPermisoToAttach = em.getReference(permisosNewPermisoToAttach.getClass(), permisosNewPermisoToAttach.getIdPermiso());
                attachedPermisosNew.add(permisosNewPermisoToAttach);
            }
            permisosNew = attachedPermisosNew;
            perfil.setPermisos(permisosNew);
            List<Usuario> attachedUsuariosNew = new ArrayList<Usuario>();
            for (Usuario usuariosNewUsuarioToAttach : usuariosNew) {
                usuariosNewUsuarioToAttach = em.getReference(usuariosNewUsuarioToAttach.getClass(), usuariosNewUsuarioToAttach.getPk());
                attachedUsuariosNew.add(usuariosNewUsuarioToAttach);
            }
            usuariosNew = attachedUsuariosNew;
            perfil.setUsuarios(usuariosNew);
            perfil = em.merge(perfil);
            for (Permiso permisosOldPermiso : permisosOld) {
                if (!permisosNew.contains(permisosOldPermiso)) {
                    permisosOldPermiso.getPerfiles().remove(perfil);
                    permisosOldPermiso = em.merge(permisosOldPermiso);
                }
            }
            for (Permiso permisosNewPermiso : permisosNew) {
                if (!permisosOld.contains(permisosNewPermiso)) {
                    permisosNewPermiso.getPerfiles().add(perfil);
                    permisosNewPermiso = em.merge(permisosNewPermiso);
                }
            }
            for (Usuario usuariosOldUsuario : usuariosOld) {
                if (!usuariosNew.contains(usuariosOldUsuario)) {
                    usuariosOldUsuario.getPerfiles().remove(perfil);
                    usuariosOldUsuario = em.merge(usuariosOldUsuario);
                }
            }
            for (Usuario usuariosNewUsuario : usuariosNew) {
                if (!usuariosOld.contains(usuariosNewUsuario)) {
                    usuariosNewUsuario.getPerfiles().add(perfil);
                    usuariosNewUsuario = em.merge(usuariosNewUsuario);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = perfil.getIdPerfil();
                if (findPerfil(id) == null) {
                    throw new NonexistentEntityException("The perfil with id " + id + " no longer exists.");
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
            Perfil perfil;
            try {
                perfil = em.getReference(Perfil.class, id);
                perfil.getIdPerfil();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The perfil with id " + id + " no longer exists.", enfe);
            }
            List<Permiso> permisos = perfil.getPermisos();
            for (Permiso permisosPermiso : permisos) {
                permisosPermiso.getPerfiles().remove(perfil);
                permisosPermiso = em.merge(permisosPermiso);
            }
            List<Usuario> usuarios = perfil.getUsuarios();
            for (Usuario usuariosUsuario : usuarios) {
                usuariosUsuario.getPerfiles().remove(perfil);
                usuariosUsuario = em.merge(usuariosUsuario);
            }
            em.remove(perfil);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Perfil> findPerfilEntities() {
        return findPerfilEntities(true, -1, -1);
    }

    public List<Perfil> findPerfilEntities(int maxResults, int firstResult) {
        return findPerfilEntities(false, maxResults, firstResult);
    }

    private List<Perfil> findPerfilEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Perfil.class));
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

    public Perfil findPerfil(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Perfil.class, id);
        } finally {
            em.close();
        }
    }

    public int getPerfilCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Perfil> rt = cq.from(Perfil.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
