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
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author omesa
 */
@Entity
@Table(name="TAP21_PERMISO")
public class Permiso implements Serializable {
    @TableGenerator(name = "PRM_GEN",
                    table = "TAP21_SEQ",
                    pkColumnName = "SEQ_NAME",
                    valueColumnName = "SEQ_VALUE",
                    pkColumnValue = "PRM_CON",
                    initialValue = 0,
                    allocationSize = 1
    )
    @Id
    @Column(name="NM_IDPERMISO")
    private Integer idPermiso;
    @Column(name="DS_NOMBREPERMISO")
    private String nombrePermiso;
    @Column(name="OP_ESTADOPERMISO")
    @Enumerated(EnumType.STRING)
    private Estado estadoPermiso; 
    @ManyToMany(mappedBy = "permisos")
    @Lob
    private List<Perfil> perfiles;

    public List<Perfil> getPerfiles() {
        return perfiles;
    }

    public void setPerfiles(List<Perfil> perfiles) {
        this.perfiles = perfiles;
    }
    
    
    public enum Estado{ACTIVO, INNACTIVO};
    
    public Permiso() {
    }

    public Integer getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(Integer idPermiso) {
        this.idPermiso = idPermiso;
    }

    public String getNombrePermiso() {
        return nombrePermiso;
    }

    public void setNombrePermiso(String nombrePermiso) {
        this.nombrePermiso = nombrePermiso;
    }

    public Estado getEstadoPermiso() {
        return estadoPermiso;
    }

    public void setEstadoPermiso(Estado estadoPermiso) {
        this.estadoPermiso = estadoPermiso;
    }

}
