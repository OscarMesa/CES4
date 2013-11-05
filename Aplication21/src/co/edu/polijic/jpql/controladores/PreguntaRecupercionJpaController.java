/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.polijic.jpql.controladores;

import co.edu.polijic.jpql.controladores.exceptions.NonexistentEntityException;
import co.edu.polijic.jpql.entidades.PreguntaRecupercion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.edu.polijic.jpql.entidades.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author omesa
 */
public class PreguntaRecupercionJpaController implements Serializable {

    public PreguntaRecupercionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PreguntaRecupercion preguntaRecupercion) {
        if (preguntaRecupercion.getUsuarios() == null) {
            preguntaRecupercion.setUsuarios(new ArrayList<Usuario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Usuario> attachedUsuarios = new ArrayList<Usuario>();
            for (Usuario usuariosUsuarioToAttach : preguntaRecupercion.getUsuarios()) {
                usuariosUsuarioToAttach = em.getReference(usuariosUsuarioToAttach.getClass(), usuariosUsuarioToAttach.getPk());
                attachedUsuarios.add(usuariosUsuarioToAttach);
            }
            preguntaRecupercion.setUsuarios(attachedUsuarios);
            em.persist(preguntaRecupercion);
            for (Usuario usuariosUsuario : preguntaRecupercion.getUsuarios()) {
                PreguntaRecupercion oldRespuestaPreguntaOfUsuariosUsuario = usuariosUsuario.getRespuestaPregunta();
                usuariosUsuario.setRespuestaPregunta(preguntaRecupercion);
                usuariosUsuario = em.merge(usuariosUsuario);
                if (oldRespuestaPreguntaOfUsuariosUsuario != null) {
                    oldRespuestaPreguntaOfUsuariosUsuario.getUsuarios().remove(usuariosUsuario);
                    oldRespuestaPreguntaOfUsuariosUsuario = em.merge(oldRespuestaPreguntaOfUsuariosUsuario);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PreguntaRecupercion preguntaRecupercion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PreguntaRecupercion persistentPreguntaRecupercion = em.find(PreguntaRecupercion.class, preguntaRecupercion.getIdPreguntaRec());
            List<Usuario> usuariosOld = persistentPreguntaRecupercion.getUsuarios();
            List<Usuario> usuariosNew = preguntaRecupercion.getUsuarios();
            List<Usuario> attachedUsuariosNew = new ArrayList<Usuario>();
            for (Usuario usuariosNewUsuarioToAttach : usuariosNew) {
                usuariosNewUsuarioToAttach = em.getReference(usuariosNewUsuarioToAttach.getClass(), usuariosNewUsuarioToAttach.getPk());
                attachedUsuariosNew.add(usuariosNewUsuarioToAttach);
            }
            usuariosNew = attachedUsuariosNew;
            preguntaRecupercion.setUsuarios(usuariosNew);
            preguntaRecupercion = em.merge(preguntaRecupercion);
            for (Usuario usuariosOldUsuario : usuariosOld) {
                if (!usuariosNew.contains(usuariosOldUsuario)) {
                    usuariosOldUsuario.setRespuestaPregunta(null);
                    usuariosOldUsuario = em.merge(usuariosOldUsuario);
                }
            }
            for (Usuario usuariosNewUsuario : usuariosNew) {
                if (!usuariosOld.contains(usuariosNewUsuario)) {
                    PreguntaRecupercion oldRespuestaPreguntaOfUsuariosNewUsuario = usuariosNewUsuario.getRespuestaPregunta();
                    usuariosNewUsuario.setRespuestaPregunta(preguntaRecupercion);
                    usuariosNewUsuario = em.merge(usuariosNewUsuario);
                    if (oldRespuestaPreguntaOfUsuariosNewUsuario != null && !oldRespuestaPreguntaOfUsuariosNewUsuario.equals(preguntaRecupercion)) {
                        oldRespuestaPreguntaOfUsuariosNewUsuario.getUsuarios().remove(usuariosNewUsuario);
                        oldRespuestaPreguntaOfUsuariosNewUsuario = em.merge(oldRespuestaPreguntaOfUsuariosNewUsuario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = preguntaRecupercion.getIdPreguntaRec();
                if (findPreguntaRecupercion(id) == null) {
                    throw new NonexistentEntityException("The preguntaRecupercion with id " + id + " no longer exists.");
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
            PreguntaRecupercion preguntaRecupercion;
            try {
                preguntaRecupercion = em.getReference(PreguntaRecupercion.class, id);
                preguntaRecupercion.getIdPreguntaRec();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The preguntaRecupercion with id " + id + " no longer exists.", enfe);
            }
            List<Usuario> usuarios = preguntaRecupercion.getUsuarios();
            for (Usuario usuariosUsuario : usuarios) {
                usuariosUsuario.setRespuestaPregunta(null);
                usuariosUsuario = em.merge(usuariosUsuario);
            }
            em.remove(preguntaRecupercion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PreguntaRecupercion> findPreguntaRecupercionEntities() {
        return findPreguntaRecupercionEntities(true, -1, -1);
    }

    public List<PreguntaRecupercion> findPreguntaRecupercionEntities(int maxResults, int firstResult) {
        return findPreguntaRecupercionEntities(false, maxResults, firstResult);
    }

    private List<PreguntaRecupercion> findPreguntaRecupercionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PreguntaRecupercion.class));
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

    public PreguntaRecupercion findPreguntaRecupercion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PreguntaRecupercion.class, id);
        } finally {
            em.close();
        }
    }

    public int getPreguntaRecupercionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PreguntaRecupercion> rt = cq.from(PreguntaRecupercion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
