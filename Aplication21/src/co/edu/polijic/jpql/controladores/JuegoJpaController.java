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
import co.edu.polijic.jpql.entidades.Sala;
import co.edu.polijic.jpql.entidades.JugadorPartida;
import java.util.ArrayList;
import java.util.List;
import co.edu.polijic.jpql.entidades.Carta;
import co.edu.polijic.jpql.entidades.Juego;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author omesa
 */
public class JuegoJpaController implements Serializable {

    public JuegoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Juego juego) {
        if (juego.getJugadorPartida() == null) {
            juego.setJugadorPartida(new ArrayList<JugadorPartida>());
        }
        if (juego.getMazo() == null) {
            juego.setMazo(new ArrayList<Carta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sala sala = juego.getSala();
            if (sala != null) {
                sala = em.getReference(sala.getClass(), sala.getIdSala());
                juego.setSala(sala);
            }
            List<JugadorPartida> attachedJugadorPartida = new ArrayList<JugadorPartida>();
            for (JugadorPartida jugadorPartidaJugadorPartidaToAttach : juego.getJugadorPartida()) {
                jugadorPartidaJugadorPartidaToAttach = em.getReference(jugadorPartidaJugadorPartidaToAttach.getClass(), jugadorPartidaJugadorPartidaToAttach.getIdJugadorPartida());
                attachedJugadorPartida.add(jugadorPartidaJugadorPartidaToAttach);
            }
            juego.setJugadorPartida(attachedJugadorPartida);
            List<Carta> attachedMazo = new ArrayList<Carta>();
            for (Carta mazoCartaToAttach : juego.getMazo()) {
                mazoCartaToAttach = em.getReference(mazoCartaToAttach.getClass(), mazoCartaToAttach.getLlave());
                attachedMazo.add(mazoCartaToAttach);
            }
            juego.setMazo(attachedMazo);
            em.persist(juego);
            if (sala != null) {
                sala.getJuegos().add(juego);
                sala = em.merge(sala);
            }
            for (JugadorPartida jugadorPartidaJugadorPartida : juego.getJugadorPartida()) {
                Juego oldJuegoOfJugadorPartidaJugadorPartida = jugadorPartidaJugadorPartida.getJuego();
                jugadorPartidaJugadorPartida.setJuego(juego);
                jugadorPartidaJugadorPartida = em.merge(jugadorPartidaJugadorPartida);
                if (oldJuegoOfJugadorPartidaJugadorPartida != null) {
                    oldJuegoOfJugadorPartidaJugadorPartida.getJugadorPartida().remove(jugadorPartidaJugadorPartida);
                    oldJuegoOfJugadorPartidaJugadorPartida = em.merge(oldJuegoOfJugadorPartidaJugadorPartida);
                }
            }
            for (Carta mazoCarta : juego.getMazo()) {
                mazoCarta.getJuego().add(juego);
                mazoCarta = em.merge(mazoCarta);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Juego juego) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Juego persistentJuego = em.find(Juego.class, juego.getIdJuego());
            Sala salaOld = persistentJuego.getSala();
            Sala salaNew = juego.getSala();
            List<JugadorPartida> jugadorPartidaOld = persistentJuego.getJugadorPartida();
            List<JugadorPartida> jugadorPartidaNew = juego.getJugadorPartida();
            List<Carta> mazoOld = persistentJuego.getMazo();
            List<Carta> mazoNew = juego.getMazo();
            if (salaNew != null) {
                salaNew = em.getReference(salaNew.getClass(), salaNew.getIdSala());
                juego.setSala(salaNew);
            }
            List<JugadorPartida> attachedJugadorPartidaNew = new ArrayList<JugadorPartida>();
            for (JugadorPartida jugadorPartidaNewJugadorPartidaToAttach : jugadorPartidaNew) {
                jugadorPartidaNewJugadorPartidaToAttach = em.getReference(jugadorPartidaNewJugadorPartidaToAttach.getClass(), jugadorPartidaNewJugadorPartidaToAttach.getIdJugadorPartida());
                attachedJugadorPartidaNew.add(jugadorPartidaNewJugadorPartidaToAttach);
            }
            jugadorPartidaNew = attachedJugadorPartidaNew;
            juego.setJugadorPartida(jugadorPartidaNew);
            List<Carta> attachedMazoNew = new ArrayList<Carta>();
            for (Carta mazoNewCartaToAttach : mazoNew) {
                mazoNewCartaToAttach = em.getReference(mazoNewCartaToAttach.getClass(), mazoNewCartaToAttach.getLlave());
                attachedMazoNew.add(mazoNewCartaToAttach);
            }
            mazoNew = attachedMazoNew;
            juego.setMazo(mazoNew);
            juego = em.merge(juego);
            if (salaOld != null && !salaOld.equals(salaNew)) {
                salaOld.getJuegos().remove(juego);
                salaOld = em.merge(salaOld);
            }
            if (salaNew != null && !salaNew.equals(salaOld)) {
                salaNew.getJuegos().add(juego);
                salaNew = em.merge(salaNew);
            }
            for (JugadorPartida jugadorPartidaOldJugadorPartida : jugadorPartidaOld) {
                if (!jugadorPartidaNew.contains(jugadorPartidaOldJugadorPartida)) {
                    jugadorPartidaOldJugadorPartida.setJuego(null);
                    jugadorPartidaOldJugadorPartida = em.merge(jugadorPartidaOldJugadorPartida);
                }
            }
            for (JugadorPartida jugadorPartidaNewJugadorPartida : jugadorPartidaNew) {
                if (!jugadorPartidaOld.contains(jugadorPartidaNewJugadorPartida)) {
                    Juego oldJuegoOfJugadorPartidaNewJugadorPartida = jugadorPartidaNewJugadorPartida.getJuego();
                    jugadorPartidaNewJugadorPartida.setJuego(juego);
                    jugadorPartidaNewJugadorPartida = em.merge(jugadorPartidaNewJugadorPartida);
                    if (oldJuegoOfJugadorPartidaNewJugadorPartida != null && !oldJuegoOfJugadorPartidaNewJugadorPartida.equals(juego)) {
                        oldJuegoOfJugadorPartidaNewJugadorPartida.getJugadorPartida().remove(jugadorPartidaNewJugadorPartida);
                        oldJuegoOfJugadorPartidaNewJugadorPartida = em.merge(oldJuegoOfJugadorPartidaNewJugadorPartida);
                    }
                }
            }
            for (Carta mazoOldCarta : mazoOld) {
                if (!mazoNew.contains(mazoOldCarta)) {
                    mazoOldCarta.getJuego().remove(juego);
                    mazoOldCarta = em.merge(mazoOldCarta);
                }
            }
            for (Carta mazoNewCarta : mazoNew) {
                if (!mazoOld.contains(mazoNewCarta)) {
                    mazoNewCarta.getJuego().add(juego);
                    mazoNewCarta = em.merge(mazoNewCarta);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = juego.getIdJuego();
                if (findJuego(id) == null) {
                    throw new NonexistentEntityException("The juego with id " + id + " no longer exists.");
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
            Juego juego;
            try {
                juego = em.getReference(Juego.class, id);
                juego.getIdJuego();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The juego with id " + id + " no longer exists.", enfe);
            }
            Sala sala = juego.getSala();
            if (sala != null) {
                sala.getJuegos().remove(juego);
                sala = em.merge(sala);
            }
            List<JugadorPartida> jugadorPartida = juego.getJugadorPartida();
            for (JugadorPartida jugadorPartidaJugadorPartida : jugadorPartida) {
                jugadorPartidaJugadorPartida.setJuego(null);
                jugadorPartidaJugadorPartida = em.merge(jugadorPartidaJugadorPartida);
            }
            List<Carta> mazo = juego.getMazo();
            for (Carta mazoCarta : mazo) {
                mazoCarta.getJuego().remove(juego);
                mazoCarta = em.merge(mazoCarta);
            }
            em.remove(juego);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Juego> findJuegoEntities() {
        return findJuegoEntities(true, -1, -1);
    }

    public List<Juego> findJuegoEntities(int maxResults, int firstResult) {
        return findJuegoEntities(false, maxResults, firstResult);
    }

    private List<Juego> findJuegoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Juego.class));
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

    public Juego findJuego(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Juego.class, id);
        } finally {
            em.close();
        }
    }

    public int getJuegoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Juego> rt = cq.from(Juego.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
