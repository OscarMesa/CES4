/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.polijic.jpql.entidades;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author omesa
 */
@Entity
@Table(name = "TAP21_JUEGO")
public class Juego implements Serializable {
    @TableGenerator(name = "JUG_GEN",
                    table = "TAP21_SEQ",
                    pkColumnName = "SEQ_NAME",
                    valueColumnName = "SEQ_VALUE",
                    pkColumnValue = "JUG_CON",
                    initialValue = 0,
                    allocationSize = 1
    )
    @Id
    @Column(name = "NM_IDJUEGO")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "JUG_GEN")
    private Integer idJuego;
    @OneToMany(mappedBy = "juego")
    private List<JugadorPartida> jugadorPartida;
    @ManyToMany
    @JoinTable(name="TAP21_JUEGO_CARTA",
             joinColumns = @JoinColumn(name = "CDJUEGO",referencedColumnName = "NM_IDJUEGO"),
             inverseJoinColumns = {@JoinColumn(name = "CDCARTA_OP_COLOR", referencedColumnName = "OP_COLOR"),@JoinColumn(name = "CDCARTA_OP_PINTA", referencedColumnName = "OP_PINTA"),@JoinColumn(name = "CDCARTA_OP_VALOR", referencedColumnName = "OP_VALOR")})
    private List<Carta> mazo;
    @ManyToOne
    @JoinColumn(name = "CDSALA",referencedColumnName = "NM_IDSALA")
    private Sala sala;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FE_PARTIDAINICIO")
    private Date partidaInicio;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FE_PARTIDAFIN")
    private Date partidaFin;
    @Column(name = "BL_IMAGENMODERADOR")
    @Lob
    private Byte[] imagenModerador;

    public Juego() {
    }

    public Juego(Integer idJuego, List<JugadorPartida> jugadorPartida, List<Carta> mazo, Sala sala, Timestamp partidaInicio, Timestamp partidaFin, Byte[] imagenModerador) {
        this.idJuego = idJuego;
        this.jugadorPartida = jugadorPartida;
        this.mazo = mazo;
        this.sala = sala;
        this.partidaInicio = partidaInicio;
        this.partidaFin = partidaFin;
        this.imagenModerador = imagenModerador;
    }
    
    public void setPartidaInicio(Timestamp partidaInicio) {
        this.partidaInicio = partidaInicio;
    }

    public void setPartidaFin(Timestamp partidaFin) {
        this.partidaFin = partidaFin;
    }
    
    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }
    
    public Integer getIdJuego() {
        return idJuego;
    }

    public void setIdJuego(Integer idJuego) {
        this.idJuego = idJuego;
    }

    public List<JugadorPartida> getJugadorPartida() {
        return jugadorPartida;
    }

    public void setJugadorPartida(List<JugadorPartida> jugadorPartida) {
        this.jugadorPartida = jugadorPartida;
    }

    public List<Carta> getMazo() {
        return mazo;
    }

    public void setMazo(List<Carta> mazo) {
        this.mazo = mazo;
    }

    public Date getPartidaInicio() {
        return partidaInicio;
    }

    public Date getPartidaFin() {
        return partidaFin;
    }
    
    public Byte[] getImagenModerador() {
        return imagenModerador;
    }

    public void setImagenModerador(Byte[] imagenModerador) {
        this.imagenModerador = imagenModerador;
    }

    public int ObtenerCantidadJugadores() {
        return 0;
    }

    public boolean verificarPosibilidadContinuarJugador() {
        return false;
    }

    public void agregarJugador(Jugador x) {
    }

    public void sacarJugador(Jugador x) {
    }

    public Carta entregarCartaAleatoria() {
        return null;
    }

    public Jugador obtenerJugador() {
        return null;
    }

    public Jugador validarGanadorPaertida() {
        return null;
    }

    public String obtenerTotalTiempoPartida() {
        return null;
    }
}
