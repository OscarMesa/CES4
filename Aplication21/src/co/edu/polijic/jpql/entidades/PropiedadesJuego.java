/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.polijic.jpql.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 *
 * @author omesa
 */
@Entity
@Table(name="TAP21_PROPIEDADESJUEGO")
public class PropiedadesJuego implements Serializable {
    @Id
    @GeneratedValue
    @Column(name="NM_IDPROPIEDADJUEGO")
    private Integer idPropiedadJuego;
    @Column(name="DS_NOMBREJUEGO")
    private String nombreJuego;
    @Column(name="DS_VERSIONJUEGO")
    private String versionJuego;
    private List<String> autores;
    @Column(name="OP_IDIOMAJUEGO")
    @Enumerated(EnumType.STRING)
    private Idiomas idiomaJuego;

    
    public PropiedadesJuego() {
    }

    public PropiedadesJuego(Integer idPropiedadJuego, String nombreJuego, String versionJuego, List<String> autores, Idiomas idiomaJuego) {
        this.idPropiedadJuego = idPropiedadJuego;
        this.nombreJuego = nombreJuego;
        this.versionJuego = versionJuego;
        this.autores = autores;
        this.idiomaJuego = idiomaJuego;
    }
    
    public String getNombreJuego() {
        return nombreJuego;
    }

    public void setNombreJuego(String nombreJuego) {
        this.nombreJuego = nombreJuego;
    }

    public String getVersionJuego() {
        return versionJuego;
    }

    public void setVersionJuego(String versionJuego) {
        this.versionJuego = versionJuego;
    }

    public List<String> getAutores() {
        return autores;
    }

    public void setAutores(List<String> autores) {
        this.autores = autores;
    }

    public Integer getIdPropiedadJuego() {
        return idPropiedadJuego;
    }

    public void setIdPropiedadJuego(Integer idPropiedadJuego) {
        this.idPropiedadJuego = idPropiedadJuego;
    }

    public Idiomas getIdiomaJuego() {
        return idiomaJuego;
    }

    public void setIdiomaJuego(Idiomas idiomaJuego) {
        this.idiomaJuego = idiomaJuego;
    }


    
}
enum Idiomas{ESPAÑOL,INGLES};