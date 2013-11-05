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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 *
 * @author omesa
 */
@Entity
@Table(name="TAP21_PEGUNTARECUPERACION")
public class PreguntaRecupercion implements Serializable {
    @Id
    @TableGenerator(name = "PRG_GEN",
                    table = "TAP21_SEQ",
                    pkColumnName = "SEQ_NAME",
                    valueColumnName = "SEQ_VALUE",
                    pkColumnValue = "PRG_CON",
                    initialValue = 0,
                    allocationSize = 1
    )
    @Column(name="NM_IDPREGUNTAREC")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PRG_GEN")
    private Integer idPreguntaRec;
    @Column(name="NM_DESCRIPCIONPREGUNTAREC")
    private String descripcionPreguntaRec;   
    @OneToMany(mappedBy = "respuestaPregunta")
    private List<Usuario> usuarios;

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
    
    public PreguntaRecupercion() {
    }

    public Integer getIdPreguntaRec() {
        return idPreguntaRec;
    }

    public void setIdPreguntaRec(Integer idPreguntaRec) {
        this.idPreguntaRec = idPreguntaRec;
    }

    public String getDescripcionPreguntaRec() {
        return descripcionPreguntaRec;
    }

    public void setDescripcionPreguntaRec(String descripcionPreguntaRec) {
        this.descripcionPreguntaRec = descripcionPreguntaRec;
    }

    @Override
    public String toString() {
        return "PreguntaRecupercion{" + "idPreguntaRec=" + idPreguntaRec + ", descripcionPreguntaRec=" + descripcionPreguntaRec + ", usuarios=" + usuarios + '}';
    }
    
}
