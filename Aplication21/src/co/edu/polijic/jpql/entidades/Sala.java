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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 *
 * @author omesa
 */
@Entity
@Table(name="TAP21_SALA")
public class Sala implements Serializable {
    @Id
    @TableGenerator(name = "SAL_GEN",
                    table = "TAP21_SEQ",
                    pkColumnName = "SEQ_NAME",
                    valueColumnName = "SEQ_VALUE",
                    pkColumnValue = "SL_CON",
                    initialValue = 0,
                    allocationSize = 1
    )
       
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "SAL_GEN")
    @Column(name="NM_IDSALA")
    private Integer idSala;
    @OneToMany(mappedBy = "sala")
    private List<Juego> juegos;
    @ManyToMany
    @JoinTable(name = "TAP21_SALA_JUGADOR",
            joinColumns = @JoinColumn(name = "CDSALA",referencedColumnName = "NM_IDSALA"),
            inverseJoinColumns = {@JoinColumn(name = "CDJUGADOR_NM_IDUSUARIO",referencedColumnName = "NM_IDUSUARIO"),@JoinColumn(name = "CDJUGADOR_DS_NOMBRE",referencedColumnName = "DS_NOMBRE")})
    private List<Jugador> jugadores;

    public Sala() {
    }

    public Sala(Integer idSala, List<Juego> juegos, List<Jugador> jugadores) {
        this.idSala = idSala;
        this.juegos = juegos;
        this.jugadores = jugadores;
    }
    
    public Integer getIdSala() {
        return idSala;
    }

    public void setIdSala(Integer idSala) {
        this.idSala = idSala;   
    }

    public List<Juego> getJuegos() {
        return juegos;
    }

    public void setJuegos(List<Juego> juegos) {
        this.juegos = juegos;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }
    
    public void sacarJugador(){ 
    
    }
    
    public void IngresarJugador(Jugador x){
    
    }
}
