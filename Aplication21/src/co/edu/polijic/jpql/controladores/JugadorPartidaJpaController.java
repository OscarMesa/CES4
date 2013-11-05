/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.polijic.jpql.controladores;

import co.edu.polijic.jpql.controladores.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.edu.polijic.jpql.entidades.Juego;
import co.edu.polijic.jpql.entidades.Jugador;
import co.edu.polijic.jpql.entidades.Carta;
import co.edu.polijic.jpql.entidades.JugadorPartida;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author omesa
 */
public class JugadorPartidaJpaController implements Serializable {

    public JugadorPartidaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(JugadorPartida jugadorPartida) {
        if (jugadorPartida.getMazoAsignado() == null) {
            jugadorPartida.setMazoAsignado(new ArrayList<Carta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Juego juego = jugadorPartida.getJuego();
            if (juego != null) {
                juego = em.getReference(juego.getClass(), juego.getIdJuego());
                jugadorPartida.setJuego(juego);
            }
            Jugador jugador = jugadorPartida.getJugador();
            if (jugador != null) {
                jugador = em.getReference(jugador.getClass(), jugador.getPk());
                jugadorPartida.setJugador(jugador);
            }
            List<Carta> attachedMazoAsignado = new ArrayList<Carta>();
            for (Carta mazoAsignadoCartaToAttach : jugadorPartida.getMazoAsignado()) {
                mazoAsignadoCartaToAttach = em.getReference(mazoAsignadoCartaToAttach.getClass(), mazoAsignadoCartaToAttach.getLlave());
                attachedMazoAsignado.add(mazoAsignadoCartaToAttach);
            }
            jugadorPartida.setMazoAsignado(attachedMazoAsignado);
            em.persist(jugadorPartida);
            if (juego != null) {
                juego.getJugadorPartida().add(jugadorPartida);
                juego = em.merge(juego);
            }
            if (jugador != null) {
                jugador.getPartidas().add(jugadorPartida);
                jugador = em.merge(jugador);
            }
            for (Carta mazoAsignadoCarta : jugadorPartida.getMazoAsignado()) {
                mazoAsignadoCarta.getJugadorPartida().add(jugadorPartida);
                mazoAsignadoCarta = em.merge(mazoAsignadoCarta);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(JugadorPartida jugadorPartida) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            JugadorPartida persistentJugadorPartida = em.find(JugadorPartida.class, jugadorPartida.getIdJugadorPartida());
            Juego juegoOld = persistentJugadorPartida.getJuego();
            Juego juegoNew = jugadorPartida.getJuego();
            Jugador jugadorOld = persistentJugadorPartida.getJugador();
            Jugador jugadorNew = jugadorPartida.getJugador();
            List<Carta> mazoAsignadoOld = persistentJugadorPartida.getMazoAsignado();
            List<Carta> mazoAsignadoNew = jugadorPartida.getMazoAsignado();
            if (juegoNew != null) {
                juegoNew = em.getReference(juegoNew.getClass(), juegoNew.getIdJuego());
                jugadorPartida.setJuego(juegoNew);
            }
            if (jugadorNew != null) {
                jugadorNew = em.getReference(jugadorNew.getClass(), jugadorNew.getPk());
                jugadorPartida.setJugador(jugadorNew);
            }
            List<Carta> attachedMazoAsignadoNew = new ArrayList<Carta>();
            for (Carta mazoAsignadoNewCartaToAttach : mazoAsignadoNew) {
                mazoAsignadoNewCartaToAttach = em.getReference(mazoAsignadoNewCartaToAttach.getClass(), mazoAsignadoNewCartaToAttach.getLlave());
                attachedMazoAsignadoNew.add(mazoAsignadoNewCartaToAttach);
            }
            mazoAsignadoNew = attachedMazoAsignadoNew;
            jugadorPartida.setMazoAsignado(mazoAsignadoNew);
            jugadorPartida = em.merge(jugadorPartida);
            if (juegoOld != null && !juegoOld.equals(juegoNew)) {
                juegoOld.getJugadorPartida().remove(jugadorPartida);
                juegoOld = em.merge(juegoOld);
            }
            if (juegoNew != null && !juegoNew.equals(juegoOld)) {
                juegoNew.getJugadorPartida().add(jugadorPartida);
                juegoNew = em.merge(juegoNew);
            }
            if (jugadorOld != null && !jugadorOld.equals(jugadorNew)) {
                jugadorOld.getPartidas().remove(jugadorPartida);
                jugadorOld = em.merge(jugadorOld);
            }
            if (jugadorNew != null && !jugadorNew.equals(jugadorOld)) {
                jugadorNew.getPartidas().add(jugadorPartida);
                jugadorNew = em.merge(jugadorNew);
            }
            for (Carta mazoAsignadoOldCarta : mazoAsignadoOld) {
                if (!mazoAsignadoNew.contains(mazoAsignadoOldCarta)) {
                    mazoAsignadoOldCarta.getJugadorPartida().remove(jugadorPartida);
                    mazoAsignadoOldCarta = em.merge(mazoAsignadoOldCarta);
                }
            }
            for (Carta mazoAsignadoNewCarta : mazoAsignadoNew) {
                if (!mazoAsignadoOld.contains(mazoAsignadoNewCarta)) {
                    mazoAsignadoNewCarta.getJugadorPartida().add(jugadorPartida);
                    mazoAsignadoNewCarta = em.merge(mazoAsignadoNewCarta);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = jugadorPartida.getIdJugadorPartida();
                if (findJugadorPartida(id) == null) {
                    throw new NonexistentEntityException("The jugadorPartida with id " + id + " no longer exists.");
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
            JugadorPartida jugadorPartida;
            try {
                jugadorPartida = em.getReference(JugadorPartida.class, id);
                jugadorPartida.getIdJugadorPartida();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The jugadorPartida with id " + id + " no longer exists.", enfe);
            }
            Juego juego = jugadorPartida.getJuego();
            if (juego != null) {
                juego.getJugadorPartida().remove(jugadorPartida);
                juego = em.merge(juego);
            }
            Jugador jugador = jugadorPartida.getJugador();
            if (jugador != null) {
                jugador.getPartidas().remove(jugadorPartida);
                jugador = em.merge(jugador);
            }
            List<Carta> mazoAsignado = jugadorPartida.getMazoAsignado();
            for (Carta mazoAsignadoCarta : mazoAsignado) {
                mazoAsignadoCarta.getJugadorPartida().remove(jugadorPartida);
                mazoAsignadoCarta = em.merge(mazoAsignadoCarta);
            }
            em.remove(jugadorPartida);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<JugadorPartida> findJugadorPartidaEntities() {
        return findJugadorPartidaEntities(true, -1, -1);
    }

    public List<JugadorPartida> findJugadorPartidaEntities(int maxResults, int firstResult) {
        return findJugadorPartidaEntities(false, maxResults, firstResult);
    }

    private List<JugadorPartida> findJugadorPartidaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(JugadorPartida.class));
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

    public JugadorPartida findJugadorPartida(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(JugadorPartida.class, id);
        } finally {
            em.close();
        }
    }

    public int getJugadorPartidaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<JugadorPartida> rt = cq.from(JugadorPartida.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
