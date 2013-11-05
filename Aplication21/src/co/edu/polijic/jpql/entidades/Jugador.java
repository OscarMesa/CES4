/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.polijic.jpql.entidades;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author omesa
 */
@Entity
@Table(name = "TAP21_JUGADOR")
public class Jugador extends Usuario {
    
    @Column(name = "DS_AVATAR")
    private String avatar;
    @Column(name = "DS_SEUDONIMO")
    private String seudonimo;
    @Column(name = "NM_PUNTAJEJUGADOR")
    private Integer puntuajeAcomulado;
    @Column(name = "OP_ESTADOJUGADOR")
    private Estado estadoJugador;
    @Column(name = "DS_IPJUGADOR")
    private String ipJugador;
    @ManyToMany(mappedBy = "jugadores")
    private List<Sala> salas;
    @OneToMany(mappedBy = "jugador")
    private List<JugadorPartida> partidas;

    public List<JugadorPartida> getPartidas() {
        return partidas;
    }

    public void setPartidas(List<JugadorPartida> partidas) {
        this.partidas = partidas;
    }

    public enum Estado {

        ACTIVO_PAS, INNACTIVO_PAS, INNACTIVO_TOTAL
    };

    public Jugador() {
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSeudonimo() {
        return seudonimo;
    }

    public void setSeudonimo(String seudonimo) {
        this.seudonimo = seudonimo;
    }

    public Estado getEstadoJugador() {
        return estadoJugador;
    }

    public void setEstadoJugador(Estado estadoJugador) {
        this.estadoJugador = estadoJugador;
    }

    public String getIpJugador() {
        return ipJugador;
    }

    public void setIpJugador(String ipJugador) {
        this.ipJugador = ipJugador;
    }

    public void sumaPuntaje() {
    }

    public void restarPuntaje() {
    }

    public boolean validarExistenciaJugador() {
        return true;
    }

    public Integer getPuntuajeAcomulado() {
        return puntuajeAcomulado;
    }

    public void setPuntuajeAcomulado(Integer puntuajeAcomulado) {
        this.puntuajeAcomulado = puntuajeAcomulado;
    }

    public List<Sala> getSalas() {
        return salas;
    }

    public void setSalas(List<Sala> salas) {
        this.salas = salas;
    }
    
}
