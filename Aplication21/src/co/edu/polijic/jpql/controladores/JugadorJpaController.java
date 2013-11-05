/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.polijic.jpql.controladores;

import co.edu.polijic.jpql.controladores.exceptions.NonexistentEntityException;
import co.edu.polijic.jpql.controladores.exceptions.PreexistingEntityException;
import co.edu.polijic.jpql.entidades.Jugador;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.edu.polijic.jpql.entidades.PreguntaRecupercion;
import co.edu.polijic.jpql.entidades.JugadorPartida;
import java.util.ArrayList;
import java.util.List;
import co.edu.polijic.jpql.entidades.Sala;
import co.edu.polijic.jpql.entidades.Perfil;
import co.edu.polijic.jpql.entidades.UsuarioPk;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author omesa
 */
public class JugadorJpaController implements Serializable {

    public JugadorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Jugador jugador) throws PreexistingEntityException, Exception {
        if (jugador.getPk() == null) {
            jugador.setPk(new UsuarioPk());
        }
        if (jugador.getPartidas() == null) {
            jugador.setPartidas(new ArrayList<JugadorPartida>());
        }
        if (jugador.getSalas() == null) {
            jugador.setSalas(new ArrayList<Sala>());
        }
        if (jugador.getPerfiles() == null) {
            jugador.setPerfiles(new ArrayList<Perfil>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PreguntaRecupercion respuestaPregunta = jugador.getRespuestaPregunta();
            if (respuestaPregunta != null) {
                respuestaPregunta = em.getReference(respuestaPregunta.getClass(), respuestaPregunta.getIdPreguntaRec());
                jugador.setRespuestaPregunta(respuestaPregunta);
            }
            List<JugadorPartida> attachedPartidas = new ArrayList<JugadorPartida>();
            for (JugadorPartida partidasJugadorPartidaToAttach : jugador.getPartidas()) {
                partidasJugadorPartidaToAttach = em.getReference(partidasJugadorPartidaToAttach.getClass(), partidasJugadorPartidaToAttach.getIdJugadorPartida());
                attachedPartidas.add(partidasJugadorPartidaToAttach);
            }
            jugador.setPartidas(attachedPartidas);
            List<Sala> attachedSalas = new ArrayList<Sala>();
            for (Sala salasSalaToAttach : jugador.getSalas()) {
                salasSalaToAttach = em.getReference(salasSalaToAttach.getClass(), salasSalaToAttach.getIdSala());
                attachedSalas.add(salasSalaToAttach);
            }
            jugador.setSalas(attachedSalas);
            List<Perfil> attachedPerfiles = new ArrayList<Perfil>();
            for (Perfil perfilesPerfilToAttach : jugador.getPerfiles()) {
                perfilesPerfilToAttach = em.getReference(perfilesPerfilToAttach.getClass(), perfilesPerfilToAttach.getIdPerfil());
                attachedPerfiles.add(perfilesPerfilToAttach);
            }
            jugador.setPerfiles(attachedPerfiles);
            em.persist(jugador);
            if (respuestaPregunta != null) {
                respuestaPregunta.getUsuarios().add(jugador);
                respuestaPregunta = em.merge(respuestaPregunta);
            }
            for (JugadorPartida partidasJugadorPartida : jugador.getPartidas()) {
                Jugador oldJugadorOfPartidasJugadorPartida = partidasJugadorPartida.getJugador();
                partidasJugadorPartida.setJugador(jugador);
                partidasJugadorPartida = em.merge(partidasJugadorPartida);
                if (oldJugadorOfPartidasJugadorPartida != null) {
                    oldJugadorOfPartidasJugadorPartida.getPartidas().remove(partidasJugadorPartida);
                    oldJugadorOfPartidasJugadorPartida = em.merge(oldJugadorOfPartidasJugadorPartida);
                }
            }
            for (Sala salasSala : jugador.getSalas()) {
                salasSala.getJugadores().add(jugador);
                salasSala = em.merge(salasSala);
            }
            for (Perfil perfilesPerfil : jugador.getPerfiles()) {
                perfilesPerfil.getUsuarios().add(jugador);
                perfilesPerfil = em.merge(perfilesPerfil);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findJugador(jugador.getPk()) != null) {
                throw new PreexistingEntityException("Jugador " + jugador + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Jugador jugador) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jugador persistentJugador = em.find(Jugador.class, jugador.getPk());
            PreguntaRecupercion respuestaPreguntaOld = persistentJugador.getRespuestaPregunta();
            PreguntaRecupercion respuestaPreguntaNew = jugador.getRespuestaPregunta();
            List<JugadorPartida> partidasOld = persistentJugador.getPartidas();
            List<JugadorPartida> partidasNew = jugador.getPartidas();
            List<Sala> salasOld = persistentJugador.getSalas();
            List<Sala> salasNew = jugador.getSalas();
            List<Perfil> perfilesOld = persistentJugador.getPerfiles();
            List<Perfil> perfilesNew = jugador.getPerfiles();
            if (respuestaPreguntaNew != null) {
                respuestaPreguntaNew = em.getReference(respuestaPreguntaNew.getClass(), respuestaPreguntaNew.getIdPreguntaRec());
                jugador.setRespuestaPregunta(respuestaPreguntaNew);
            }
            List<JugadorPartida> attachedPartidasNew = new ArrayList<JugadorPartida>();
            for (JugadorPartida partidasNewJugadorPartidaToAttach : partidasNew) {
                partidasNewJugadorPartidaToAttach = em.getReference(partidasNewJugadorPartidaToAttach.getClass(), partidasNewJugadorPartidaToAttach.getIdJugadorPartida());
                attachedPartidasNew.add(partidasNewJugadorPartidaToAttach);
            }
            partidasNew = attachedPartidasNew;
            jugador.setPartidas(partidasNew);
            List<Sala> attachedSalasNew = new ArrayList<Sala>();
            for (Sala salasNewSalaToAttach : salasNew) {
                salasNewSalaToAttach = em.getReference(salasNewSalaToAttach.getClass(), salasNewSalaToAttach.getIdSala());
                attachedSalasNew.add(salasNewSalaToAttach);
            }
            salasNew = attachedSalasNew;
            jugador.setSalas(salasNew);
            List<Perfil> attachedPerfilesNew = new ArrayList<Perfil>();
            for (Perfil perfilesNewPerfilToAttach : perfilesNew) {
                perfilesNewPerfilToAttach = em.getReference(perfilesNewPerfilToAttach.getClass(), perfilesNewPerfilToAttach.getIdPerfil());
                attachedPerfilesNew.add(perfilesNewPerfilToAttach);
            }
            perfilesNew = attachedPerfilesNew;
            jugador.setPerfiles(perfilesNew);
            jugador = em.merge(jugador);
            if (respuestaPreguntaOld != null && !respuestaPreguntaOld.equals(respuestaPreguntaNew)) {
                respuestaPreguntaOld.getUsuarios().remove(jugador);
                respuestaPreguntaOld = em.merge(respuestaPreguntaOld);
            }
            if (respuestaPreguntaNew != null && !respuestaPreguntaNew.equals(respuestaPreguntaOld)) {
                respuestaPreguntaNew.getUsuarios().add(jugador);
                respuestaPreguntaNew = em.merge(respuestaPreguntaNew);
            }
            for (JugadorPartida partidasOldJugadorPartida : partidasOld) {
                if (!partidasNew.contains(partidasOldJugadorPartida)) {
                    partidasOldJugadorPartida.setJugador(null);
                    partidasOldJugadorPartida = em.merge(partidasOldJugadorPartida);
                }
            }
            for (JugadorPartida partidasNewJugadorPartida : partidasNew) {
                if (!partidasOld.contains(partidasNewJugadorPartida)) {
                    Jugador oldJugadorOfPartidasNewJugadorPartida = partidasNewJugadorPartida.getJugador();
                    partidasNewJugadorPartida.setJugador(jugador);
                    partidasNewJugadorPartida = em.merge(partidasNewJugadorPartida);
                    if (oldJugadorOfPartidasNewJugadorPartida != null && !oldJugadorOfPartidasNewJugadorPartida.equals(jugador)) {
                        oldJugadorOfPartidasNewJugadorPartida.getPartidas().remove(partidasNewJugadorPartida);
                        oldJugadorOfPartidasNewJugadorPartida = em.merge(oldJugadorOfPartidasNewJugadorPartida);
                    }
                }
            }
            for (Sala salasOldSala : salasOld) {
                if (!salasNew.contains(salasOldSala)) {
                    salasOldSala.getJugadores().remove(jugador);
                    salasOldSala = em.merge(salasOldSala);
                }
            }
            for (Sala salasNewSala : salasNew) {
                if (!salasOld.contains(salasNewSala)) {
                    salasNewSala.getJugadores().add(jugador);
                    salasNewSala = em.merge(salasNewSala);
                }
            }
            for (Perfil perfilesOldPerfil : perfilesOld) {
                if (!perfilesNew.contains(perfilesOldPerfil)) {
                    perfilesOldPerfil.getUsuarios().remove(jugador);
                    perfilesOldPerfil = em.merge(perfilesOldPerfil);
                }
            }
            for (Perfil perfilesNewPerfil : perfilesNew) {
                if (!perfilesOld.contains(perfilesNewPerfil)) {
                    perfilesNewPerfil.getUsuarios().add(jugador);
                    perfilesNewPerfil = em.merge(perfilesNewPerfil);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                UsuarioPk id = jugador.getPk();
                if (findJugador(id) == null) {
                    throw new NonexistentEntityException("The jugador with id " + id + " no longer exists.");
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
            Jugador jugador;
            try {
                jugador = em.getReference(Jugador.class, id);
                jugador.getPk();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The jugador with id " + id + " no longer exists.", enfe);
            }
            PreguntaRecupercion respuestaPregunta = jugador.getRespuestaPregunta();
            if (respuestaPregunta != null) {
                respuestaPregunta.getUsuarios().remove(jugador);
                respuestaPregunta = em.merge(respuestaPregunta);
            }
            List<JugadorPartida> partidas = jugador.getPartidas();
            for (JugadorPartida partidasJugadorPartida : partidas) {
                partidasJugadorPartida.setJugador(null);
                partidasJugadorPartida = em.merge(partidasJugadorPartida);
            }
            List<Sala> salas = jugador.getSalas();
            for (Sala salasSala : salas) {
                salasSala.getJugadores().remove(jugador);
                salasSala = em.merge(salasSala);
            }
            List<Perfil> perfiles = jugador.getPerfiles();
            for (Perfil perfilesPerfil : perfiles) {
                perfilesPerfil.getUsuarios().remove(jugador);
                perfilesPerfil = em.merge(perfilesPerfil);
            }
            em.remove(jugador);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Jugador> findJugadorEntities() {
        return findJugadorEntities(true, -1, -1);
    }

    public List<Jugador> findJugadorEntities(int maxResults, int firstResult) {
        return findJugadorEntities(false, maxResults, firstResult);
    }

    private List<Jugador> findJugadorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Jugador.class));
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

    public Jugador findJugador(UsuarioPk id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Jugador.class, id);
        } finally {
            em.close();
        }
    }

    public int getJugadorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Jugador> rt = cq.from(Jugador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
