/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.polijic.jpql.gui;

import co.edu.polijic.jpql.entidades.Permiso;
import co.edu.polijic.jpql.controladores.PermisoJpaController;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Pablo
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        Permiso permiso = new Permiso();
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("21PU");
        PermisoJpaController per = new PermisoJpaController(emf);
        
         for (Permiso p : per.findPermisoEntities()) {
               System.out.print("Permiso = "+p.getNombrePermiso()+"\n");  
        }
    
    }
}
