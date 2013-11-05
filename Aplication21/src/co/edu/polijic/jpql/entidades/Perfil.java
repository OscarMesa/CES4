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
@Table(name="TAP21_PERFIL")
public class Perfil implements Serializable {
    @TableGenerator(name = "PRF_GEN",
                    table = "TAP21_SEQ",
                    pkColumnName = "SEQ_NAME",
                    valueColumnName = "SEQ_VALUE",
                    pkColumnValue = "PRF_CON",
                    initialValue = 0,
                    allocationSize = 1
    )
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PRF_GEN")
    @Column(name="NM_IDPERFIL")
    private Integer idPerfil;
    @Column(name="DS_NOMBREPEFIL")
    private String nombrePerfil;
    @ManyToMany(mappedBy = "perfiles")
    private List<Usuario> usuarios;
    @ManyToMany
    @JoinTable(name = "TAP21_PERFIL_PERMISO",
            joinColumns = @JoinColumn(name="CDPERFIL",referencedColumnName = "NM_IDPERFIL"),
            inverseJoinColumns = @JoinColumn(name="CDPERMISO",referencedColumnName = "NM_IDPERMISO"))
    private List<Permiso> permisos;
    
    
    public Perfil() {
    }
    
    public Perfil(Integer idPerfil, String nombrePerfil, List<Usuario> usuarios, List<Permiso> permisos) {
        this.idPerfil = idPerfil;
        this.nombrePerfil = nombrePerfil;
        this.usuarios = usuarios;
        this.permisos = permisos;
    }

    
    public Integer getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(Integer idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getNombrePerfil() {
        return nombrePerfil;
    }

    public void setNombrePerfil(String nombrePerfil) {
        this.nombrePerfil = nombrePerfil;
    }

    public List<Permiso> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<Permiso> permisos) {
        this.permisos = permisos;
    }    

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
    
    
}
