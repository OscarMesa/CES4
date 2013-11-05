/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.polijic.jpql.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.TableGenerator;

/**
 *
 * @author omesa
 */
@Embeddable
public class UsuarioPk implements Serializable {
    @TableGenerator(name = "USA_GEN",
                    table = "TAP21_SEQ",
                    pkColumnName = "SEQ_NAME",
                    valueColumnName = "SEQ_VALUE",
                    pkColumnValue = "USA_CON",
                    initialValue = 0,
                    allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "USA_GEN")
    @Column(name = "NM_IDUSUARIO")
    private Integer idUsuario;
    @Column(name="DS_CORREO")
    protected String correo;

    public UsuarioPk() {
    }

    public UsuarioPk(Integer idUsuario, String correo) {
        this.idUsuario = idUsuario;
        this.correo = correo;
    }
    
    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
} 
