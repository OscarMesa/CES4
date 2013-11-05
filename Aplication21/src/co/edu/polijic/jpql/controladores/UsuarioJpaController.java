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
import co.edu.polijic.jpql.entidades.PreguntaRecupercion;
import co.edu.polijic.jpql.entidades.Perfil;
import co.edu.polijic.jpql.entidades.Usuario;
import co.edu.polijic.jpql.entidades.UsuarioPk;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author omesa
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        if (usuario.getPk() == null) {
            usuario.setPk(new UsuarioPk());
        }
        if (usuario.getPerfiles() == null) {
            usuario.setPerfiles(new ArrayList<Perfil>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PreguntaRecupercion respuestaPregunta = usuario.getRespuestaPregunta();
            if (respuestaPregunta != null) {
                respuestaPregunta = em.getReference(respuestaPregunta.getClass(), respuestaPregunta.getIdPreguntaRec());
                usuario.setRespuestaPregunta(respuestaPregunta);
            }
            List<Perfil> attachedPerfiles = new ArrayList<Perfil>();
            for (Perfil perfilesPerfilToAttach : usuario.getPerfiles()) {
                perfilesPerfilToAttach = em.getReference(perfilesPerfilToAttach.getClass(), perfilesPerfilToAttach.getIdPerfil());
                attachedPerfiles.add(perfilesPerfilToAttach);
            }
            usuario.setPerfiles(attachedPerfiles);
            em.persist(usuario);
            if (respuestaPregunta != null) {
                respuestaPregunta.getUsuarios().add(usuario);
                respuestaPregunta = em.merge(respuestaPregunta);
            }
            for (Perfil perfilesPerfil : usuario.getPerfiles()) {
                perfilesPerfil.getUsuarios().add(usuario);
                perfilesPerfil = em.merge(perfilesPerfil);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuario(usuario.getPk()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getPk());
            PreguntaRecupercion respuestaPreguntaOld = persistentUsuario.getRespuestaPregunta();
            PreguntaRecupercion respuestaPreguntaNew = usuario.getRespuestaPregunta();
            List<Perfil> perfilesOld = persistentUsuario.getPerfiles();
            List<Perfil> perfilesNew = usuario.getPerfiles();
            if (respuestaPreguntaNew != null) {
                respuestaPreguntaNew = em.getReference(respuestaPreguntaNew.getClass(), respuestaPreguntaNew.getIdPreguntaRec());
                usuario.setRespuestaPregunta(respuestaPreguntaNew);
            }
            List<Perfil> attachedPerfilesNew = new ArrayList<Perfil>();
            for (Perfil perfilesNewPerfilToAttach : perfilesNew) {
                perfilesNewPerfilToAttach = em.getReference(perfilesNewPerfilToAttach.getClass(), perfilesNewPerfilToAttach.getIdPerfil());
                attachedPerfilesNew.add(perfilesNewPerfilToAttach);
            }
            perfilesNew = attachedPerfilesNew;
            usuario.setPerfiles(perfilesNew);
            usuario = em.merge(usuario);
            if (respuestaPreguntaOld != null && !respuestaPreguntaOld.equals(respuestaPreguntaNew)) {
                respuestaPreguntaOld.getUsuarios().remove(usuario);
                respuestaPreguntaOld = em.merge(respuestaPreguntaOld);
            }
            if (respuestaPreguntaNew != null && !respuestaPreguntaNew.equals(respuestaPreguntaOld)) {
                respuestaPreguntaNew.getUsuarios().add(usuario);
                respuestaPreguntaNew = em.merge(respuestaPreguntaNew);
            }
            for (Perfil perfilesOldPerfil : perfilesOld) {
                if (!perfilesNew.contains(perfilesOldPerfil)) {
                    perfilesOldPerfil.getUsuarios().remove(usuario);
                    perfilesOldPerfil = em.merge(perfilesOldPerfil);
                }
            }
            for (Perfil perfilesNewPerfil : perfilesNew) {
                if (!perfilesOld.contains(perfilesNewPerfil)) {
                    perfilesNewPerfil.getUsuarios().add(usuario);
                    perfilesNewPerfil = em.merge(perfilesNewPerfil);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                UsuarioPk id = usuario.getPk();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(UsuarioPk id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getPk();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            PreguntaRecupercion respuestaPregunta = usuario.getRespuestaPregunta();
            if (respuestaPregunta != null) {
                respuestaPregunta.getUsuarios().remove(usuario);
                respuestaPregunta = em.merge(respuestaPregunta);
            }
            List<Perfil> perfiles = usuario.getPerfiles();
            for (Perfil perfilesPerfil : perfiles) {
                perfilesPerfil.getUsuarios().remove(usuario);
                perfilesPerfil = em.merge(perfilesPerfil);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(UsuarioPk id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
