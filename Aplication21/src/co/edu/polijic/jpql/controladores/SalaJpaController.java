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
import java.util.ArrayList;
import java.util.List;
import co.edu.polijic.jpql.entidades.Jugador;
import co.edu.polijic.jpql.entidades.Sala;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author omesa
 */
public class SalaJpaController implements Serializable {

    public SalaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Sala sala) {
        if (sala.getJuegos() == null) {
            sala.setJuegos(new ArrayList<Juego>());
        }
        if (sala.getJugadores() == null) {
            sala.setJugadores(new ArrayList<Jugador>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Juego> attachedJuegos = new ArrayList<Juego>();
            for (Juego juegosJuegoToAttach : sala.getJuegos()) {
                juegosJuegoToAttach = em.getReference(juegosJuegoToAttach.getClass(), juegosJuegoToAttach.getIdJuego());
                attachedJuegos.add(juegosJuegoToAttach);
            }
            sala.setJuegos(attachedJuegos);
            List<Jugador> attachedJugadores = new ArrayList<Jugador>();
            for (Jugador jugadoresJugadorToAttach : sala.getJugadores()) {
                jugadoresJugadorToAttach = em.getReference(jugadoresJugadorToAttach.getClass(), jugadoresJugadorToAttach.getPk());
                attachedJugadores.add(jugadoresJugadorToAttach);
            }
            sala.setJugadores(attachedJugadores);
            em.persist(sala);
            for (Juego juegosJuego : sala.getJuegos()) {
                Sala oldSalaOfJuegosJuego = juegosJuego.getSala();
                juegosJuego.setSala(sala);
                juegosJuego = em.merge(juegosJuego);
                if (oldSalaOfJuegosJuego != null) {
                    oldSalaOfJuegosJuego.getJuegos().remove(juegosJuego);
                    oldSalaOfJuegosJuego = em.merge(oldSalaOfJuegosJuego);
                }
            }
            for (Jugador jugadoresJugador : sala.getJugadores()) {
                jugadoresJugador.getSalas().add(sala);
                jugadoresJugador = em.merge(jugadoresJugador);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Sala sala) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sala persistentSala = em.find(Sala.class, sala.getIdSala());
            List<Juego> juegosOld = persistentSala.getJuegos();
            List<Juego> juegosNew = sala.getJuegos();
            List<Jugador> jugadoresOld = persistentSala.getJugadores();
            List<Jugador> jugadoresNew = sala.getJugadores();
            List<Juego> attachedJuegosNew = new ArrayList<Juego>();
            for (Juego juegosNewJuegoToAttach : juegosNew) {
                juegosNewJuegoToAttach = em.getReference(juegosNewJuegoToAttach.getClass(), juegosNewJuegoToAttach.getIdJuego());
                attachedJuegosNew.add(juegosNewJuegoToAttach);
            }
            juegosNew = attachedJuegosNew;
            sala.setJuegos(juegosNew);
            List<Jugador> attachedJugadoresNew = new ArrayList<Jugador>();
            for (Jugador jugadoresNewJugadorToAttach : jugadoresNew) {
                jugadoresNewJugadorToAttach = em.getReference(jugadoresNewJugadorToAttach.getClass(), jugadoresNewJugadorToAttach.getPk());
                attachedJugadoresNew.add(jugadoresNewJugadorToAttach);
            }
            jugadoresNew = attachedJugadoresNew;
            sala.setJugadores(jugadoresNew);
            sala = em.merge(sala);
            for (Juego juegosOldJuego : juegosOld) {
                if (!juegosNew.contains(juegosOldJuego)) {
                    juegosOldJuego.setSala(null);
                    juegosOldJuego = em.merge(juegosOldJuego);
                }
            }
            for (Juego juegosNewJuego : juegosNew) {
                if (!juegosOld.contains(juegosNewJuego)) {
                    Sala oldSalaOfJuegosNewJuego = juegosNewJuego.getSala();
                    juegosNewJuego.setSala(sala);
                    juegosNewJuego = em.merge(juegosNewJuego);
                    if (oldSalaOfJuegosNewJuego != null && !oldSalaOfJuegosNewJuego.equals(sala)) {
                        oldSalaOfJuegosNewJuego.getJuegos().remove(juegosNewJuego);
                        oldSalaOfJuegosNewJuego = em.merge(oldSalaOfJuegosNewJuego);
                    }
                }
            }
            for (Jugador jugadoresOldJugador : jugadoresOld) {
                if (!jugadoresNew.contains(jugadoresOldJugador)) {
                    jugadoresOldJugador.getSalas().remove(sala);
                    jugadoresOldJugador = em.merge(jugadoresOldJugador);
                }
            }
            for (Jugador jugadoresNewJugador : jugadoresNew) {
                if (!jugadoresOld.contains(jugadoresNewJugador)) {
                    jugadoresNewJugador.getSalas().add(sala);
                    jugadoresNewJugador = em.merge(jugadoresNewJugador);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = sala.getIdSala();
                if (findSala(id) == null) {
                    throw new NonexistentEntityException("The sala with id " + id + " no longer exists.");
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
            Sala sala;
            try {
                sala = em.getReference(Sala.class, id);
                sala.getIdSala();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sala with id " + id + " no longer exists.", enfe);
            }
            List<Juego> juegos = sala.getJuegos();
            for (Juego juegosJuego : juegos) {
                juegosJuego.setSala(null);
                juegosJuego = em.merge(juegosJuego);
            }
            List<Jugador> jugadores = sala.getJugadores();
            for (Jugador jugadoresJugador : jugadores) {
                jugadoresJugador.getSalas().remove(sala);
                jugadoresJugador = em.merge(jugadoresJugador);
            }
            em.remove(sala);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Sala> findSalaEntities() {
        return findSalaEntities(true, -1, -1);
    }

    public List<Sala> findSalaEntities(int maxResults, int firstResult) {
        return findSalaEntities(false, maxResults, firstResult);
    }

    private List<Sala> findSalaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Sala.class));
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

    public Sala findSala(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Sala.class, id);
        } finally {
            em.close();
        }
    }

    public int getSalaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Sala> rt = cq.from(Sala.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
