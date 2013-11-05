/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.polijic.jpql.entidades;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author omesa
 */
public class Aplication21 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Aplication21PU");
        EntityManager em = emf.createEntityManager();
        
       /*
               PreguntaRecupercion p = new PreguntaRecupercion();
               
       
       p.setDescripcionPreguntaRec("descripcion");
       p.setUsuarios(new ArrayList<Usuario>());
       
       em.getTransaction().begin();
       
       em.persist(p);
       
       em.getTransaction().commit();*/
        List<PreguntaRecupercion> preguntas = em.createQuery("SELECT P FROM PreguntaRecupercion P", PreguntaRecupercion.class).getResultList();
        for (PreguntaRecupercion preguntaRecupercion : preguntas) {
            System.out.println(preguntaRecupercion);
        }
    }
}
