/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.polijic.jpql.entidades;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
/**
 *
 * @author omesa
 */
@Entity
@Table(name="TAP21_CARTA")
public class Carta implements Serializable {
    @EmbeddedId
    private CartaPK llave;
    @Column(name="BL_IMAGEN")
    private BufferedImage imagen[];
    @ManyToMany(mappedBy = "mazoAsignado")
    private List<JugadorPartida> jugadorPartida;
    @ManyToMany(mappedBy = "mazo")
    private List<Juego> juego;

    public Carta() {    
    }

    public Carta(CartaPK llave, BufferedImage[] imagen, List<Juego> juego) {
        this.llave = llave;
        this.imagen = imagen;
        this.juego = juego;
    }

    public List<JugadorPartida> getJugadorPartida() {
        return jugadorPartida;
    }

    public void setJugadorPartida(List<JugadorPartida> jugadorPartida) {
        this.jugadorPartida = jugadorPartida;
    }
    
    public CartaPK getLlave() {
        return llave;
    }

    public void setLlave(CartaPK llave) {
        this.llave = llave;
    }

    public BufferedImage[] getImagen() {
        return imagen;
    }

    public void setImagen(BufferedImage[] imagen) {
        this.imagen = imagen;
    }

    public List<Juego> getJuego() {
        return juego;
    }

    public void setJuego(List<Juego> juego) {
        this.juego = juego;
    }
    
}


