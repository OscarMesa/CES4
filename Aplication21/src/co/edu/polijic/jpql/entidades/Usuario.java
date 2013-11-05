/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.polijic.jpql.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author omesa
 */
@Entity
@Table(name="TAP21_USUARIO")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Usuario implements Serializable {
    
    /*@EmbeddedId
    protected UsuarioPk pk;*/
    @Id
    @Column(name="DS_CORREO")
    protected String correo;
    @Column(name="DS_CLAVE")
    protected String clave;
    @Column(name="DS_NOMBRE")
    protected String nombre;
    @Column(name="DS_APELLIDO1")
    protected String apellido1;
    @Column(name="DS_APELLIDO2")
    protected String apellido2;
    @Column(name="FE_FECHAREGISTRO")
    @Temporal(javax.persistence.TemporalType.DATE)
    protected Date fechaRegistro;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="FE_FECHANACIMIENTO")
    protected Date fechaNacimiento;
    @ManyToOne
    @JoinColumn(name = "CDRESPUESTAPREGUNTA")
    protected PreguntaRecupercion respuestaPregunta;
    @ManyToMany
    @JoinTable(name = "TAP21_USUARIO_PERFIL",
            joinColumns = @JoinColumn(name="CDUSUARIO_DS_CORREO",referencedColumnName = "DS_CORREO"),
            inverseJoinColumns = @JoinColumn(name="CDPERFIL",referencedColumnName = "NM_IDPERFIL"))
    protected List<Perfil> perfiles;
    @Transient
    protected Perfil perfil_activo;
    public Usuario() {
    
    }

    public Usuario(String correo, String clave, String nombre, String apellido1, String apellido2, Date fechaRegistro, Date fechaNacimiento, PreguntaRecupercion respuestaPregunta, List<Perfil> perfiles, Perfil perfil_activo) {
        this.correo = correo;
        this.clave = clave;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.fechaRegistro = fechaRegistro;
        this.fechaNacimiento = fechaNacimiento;
        this.respuestaPregunta = respuestaPregunta;
        this.perfiles = perfiles;
        this.perfil_activo = perfil_activo;
    }

    public Perfil getPerfil_activo() {
        return perfil_activo;
    }

    public void setPerfil_activo(Perfil perfil_activo) {
        this.perfil_activo = perfil_activo;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public List<Perfil> getPerfiles() {
        return perfiles;
    }

    public void setPerfiles(List<Perfil> perfiles) {
        this.perfiles = perfiles;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

   

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public PreguntaRecupercion getRespuestaPregunta() {
        return respuestaPregunta;
    }

    public void setRespuestaPregunta(PreguntaRecupercion respuestaPregunta) {
        this.respuestaPregunta = respuestaPregunta;
    }

    public List<Perfil> getPerfil() {
        return perfiles;
    }

    public void setPerfil(List<Perfil> perfiles) {
        this.perfiles = perfiles;
    }
    
}
