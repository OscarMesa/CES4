/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.polijic.jpql.controladores;

import co.edu.polijic.jpql.controladores.exceptions.NonexistentEntityException;
import co.edu.polijic.jpql.controladores.exceptions.PreexistingEntityException;
import co.edu.polijic.jpql.entidades.Carta;
import co.edu.polijic.jpql.entidades.CartaPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.edu.polijic.jpql.entidades.JugadorPartida;
import java.util.ArrayList;
import java.util.List;
import co.edu.polijic.jpql.entidades.Juego;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author omesa
 */
public class CartaJpaController implements Serializable {

    public CartaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Carta carta) throws PreexistingEntityException, Exception {
        if (carta.getLlave() == null) {
            carta.setLlave(new CartaPK());
        }
        if (carta.getJugadorPartida() == null) {
            carta.setJugadorPartida(new ArrayList<JugadorPartida>());
        }
        if (carta.getJuego() == null) {
            carta.setJuego(new ArrayList<Juego>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<JugadorPartida> attachedJugadorPartida = new ArrayList<JugadorPartida>();
            for (JugadorPartida jugadorPartidaJugadorPartidaToAttach : carta.getJugadorPartida()) {
                jugadorPartidaJugadorPartidaToAttach = em.getReference(jugadorPartidaJugadorPartidaToAttach.getClass(), jugadorPartidaJugadorPartidaToAttach.getIdJugadorPartida());
                attachedJugadorPartida.add(jugadorPartidaJugadorPartidaToAttach);
            }
            carta.setJugadorPartida(attachedJugadorPartida);
            List<Juego> attachedJuego = new ArrayList<Juego>();
            for (Juego juegoJuegoToAttach : carta.getJuego()) {
                juegoJuegoToAttach = em.getReference(juegoJuegoToAttach.getClass(), juegoJuegoToAttach.getIdJuego());
                attachedJuego.add(juegoJuegoToAttach);
            }
            carta.setJuego(attachedJuego);
            em.persist(carta);
            for (JugadorPartida jugadorPartidaJugadorPartida : carta.getJugadorPartida()) {
                jugadorPartidaJugadorPartida.getMazoAsignado().add(carta);
                jugadorPartidaJugadorPartida = em.merge(jugadorPartidaJugadorPartida);
            }
            for (Juego juegoJuego : carta.getJuego()) {
                juegoJuego.getMazo().add(carta);
                juegoJuego = em.merge(juegoJuego);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCarta(carta.getLlave()) != null) {
                throw new PreexistingEntityException("Carta " + carta + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Carta carta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Carta persistentCarta = em.find(Carta.class, carta.getLlave());
            List<JugadorPartida> jugadorPartidaOld = persistentCarta.getJugadorPartida();
            List<JugadorPartida> jugadorPartidaNew = carta.getJugadorPartida();
            List<Juego> juegoOld = persistentCarta.getJuego();
            List<Juego> juegoNew = carta.getJuego();
            List<JugadorPartida> attachedJugadorPartidaNew = new ArrayList<JugadorPartida>();
            for (JugadorPartida jugadorPartidaNewJugadorPartidaToAttach : jugadorPartidaNew) {
                jugadorPartidaNewJugadorPartidaToAttach = em.getReference(jugadorPartidaNewJugadorPartidaToAttach.getClass(), jugadorPartidaNewJugadorPartidaToAttach.getIdJugadorPartida());
                attachedJugadorPartidaNew.add(jugadorPartidaNewJugadorPartidaToAttach);
            }
            jugadorPartidaNew = attachedJugadorPartidaNew;
            carta.setJugadorPartida(jugadorPartidaNew);
            List<Juego> attachedJuegoNew = new ArrayList<Juego>();
            for (Juego juegoNewJuegoToAttach : juegoNew) {
                juegoNewJuegoToAttach = em.getReference(juegoNewJuegoToAttach.getClass(), juegoNewJuegoToAttach.getIdJuego());
                attachedJuegoNew.add(juegoNewJuegoToAttach);
            }
            juegoNew = attachedJuegoNew;
            carta.setJuego(juegoNew);
            carta = em.merge(carta);
            for (JugadorPartida jugadorPartidaOldJugadorPartida : jugadorPartidaOld) {
                if (!jugadorPartidaNew.contains(jugadorPartidaOldJugadorPartida)) {
                    jugadorPartidaOldJugadorPartida.getMazoAsignado().remove(carta);
                    jugadorPartidaOldJugadorPartida = em.merge(jugadorPartidaOldJugadorPartida);
                }
            }
            for (JugadorPartida jugadorPartidaNewJugadorPartida : jugadorPartidaNew) {
                if (!jugadorPartidaOld.contains(jugadorPartidaNewJugadorPartida)) {
                    jugadorPartidaNewJugadorPartida.getMazoAsignado().add(carta);
                    jugadorPartidaNewJugadorPartida = em.merge(jugadorPartidaNewJugadorPartida);
                }
            }
            for (Juego juegoOldJuego : juegoOld) {
                if (!juegoNew.contains(juegoOldJuego)) {
                    juegoOldJuego.getMazo().remove(carta);
                    juegoOldJuego = em.merge(juegoOldJuego);
                }
            }
            for (Juego juegoNewJuego : juegoNew) {
                if (!juegoOld.contains(juegoNewJuego)) {
                    juegoNewJuego.getMazo().add(carta);
                    juegoNewJuego = em.merge(juegoNewJuego);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                CartaPK id = carta.getLlave();
                if (findCarta(id) == null) {
                    throw new NonexistentEntityException("The carta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(CartaPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Carta carta;
            try {
                carta = em.getReference(Carta.class, id);
                carta.getLlave();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The carta with id " + id + " no longer exists.", enfe);
            }
            List<JugadorPartida> jugadorPartida = carta.getJugadorPartida();
            for (JugadorPartida jugadorPartidaJugadorPartida : jugadorPartida) {
                jugadorPartidaJugadorPartida.getMazoAsignado().remove(carta);
                jugadorPartidaJugadorPartida = em.merge(jugadorPartidaJugadorPartida);
            }
            List<Juego> juego = carta.getJuego();
            for (Juego juegoJuego : juego) {
                juegoJuego.getMazo().remove(carta);
                juegoJuego = em.merge(juegoJuego);
            }
            em.remove(carta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Carta> findCartaEntities() {
        return findCartaEntities(true, -1, -1);
    }

    public List<Carta> findCartaEntities(int maxResults, int firstResult) {
        return findCartaEntities(false, maxResults, firstResult);
    }

    private List<Carta> findCartaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Carta.class));
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

    public Carta findCarta(CartaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Carta.class, id);
        } finally {
            em.close();
        }
    }

    public int getCartaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Carta> rt = cq.from(Carta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
