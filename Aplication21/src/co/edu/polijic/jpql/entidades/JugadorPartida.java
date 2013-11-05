/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.polijic.jpql.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

/**
 *
 * @author omesa
 */
@Entity
@Table(name = "TAP21_JUGADORPARTIDA")
public class JugadorPartida implements Serializable {

    @TableGenerator(name = "JGDP_GEN",
            table = "TAP21_SEQ",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_VALUE",
            pkColumnValue = "JGDP_CON",
            initialValue = 0,
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "JGDP_GEN")
    @Column(name = "NM_IDJUGADORPARTIDA")
    @Id
    private Integer idJugadorPartida;
    @Transient
    private EstadoP estadoPartida;
    @Column(name = "NM_PUNTAJEPARTIDA")
    private Integer puntajePartida;
    @ManyToOne
    @JoinColumn(name = "CDJUEGO", referencedColumnName = "NM_IDJUEGO")
    private Juego juego;
    @ManyToMany
    @JoinTable(name = "AP21_JUGADORPARTIDA_CARTA",
            joinColumns =
            @JoinColumn(name = "CDJUGADORPARTIDA", referencedColumnName = "NM_IDJUGADORPARTIDA"),
            inverseJoinColumns = {
        @JoinColumn(name = "CDCARTA_OP_VALOR", referencedColumnName = "OP_VALOR"),
        @JoinColumn(name = "CDCARTA_OP_PINTA", referencedColumnName = "OP_PINTA"),
        @JoinColumn(name = "CDCARTA_OP_COLOR", referencedColumnName = "OP_COLOR")})
    private List<Carta> mazoAsignado;
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "CDJUGADOR_NM_IDUSUARIO", referencedColumnName = "NM_IDUSUARIO"),
        @JoinColumn(name = "CDJUGADOR_DS_NOMBRE", referencedColumnName = "DS_NOMBRE")})
    private Jugador jugador;

    public Integer getIdJugadorPartida() {
        return idJugadorPartida;
    }

    public void setIdJugadorPartida(Integer idJugadorPartida) {
        this.idJugadorPartida = idJugadorPartida;
    }

    public EstadoP getEstadoPartida() {
        return estadoPartida;
    }

    public void setEstadoPartida(EstadoP estadoPartida) {
        this.estadoPartida = estadoPartida;
    }

    public Integer getPuntajePartida() {
        return puntajePartida;
    }

    public void setPuntajePartida(Integer puntajePartida) {
        this.puntajePartida = puntajePartida;
    }

    public Juego getJuego() {
        return juego;
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
    }

    public List<Carta> getMazoAsignado() {
        return mazoAsignado;
    }

    public void setMazoAsignado(List<Carta> mazoAsignado) {
        this.mazoAsignado = mazoAsignado;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }
}

enum EstadoP {
    RECIBE, NORECIBE
};