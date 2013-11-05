/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.polijic.jpql.entidades;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 *
 * @author omesa
 */
@Embeddable
public class CartaPK implements Serializable {

    @Column(name = "OP_VALOR")
    @Enumerated(EnumType.STRING)
    private Valor valor;
    @Column(name = "OP_PINTA")
    @Enumerated(EnumType.STRING)
       private Pinta pinta;
    @Column(name = "OP_COLOR")
    @Enumerated(EnumType.ORDINAL)
    private Color color;

    public enum Valor {

        AS(1, 11), DOS(2), TRES(3), CUATRO(4), CINCO(5), SEIS(6),
        SIETE(7), OCHO(8), NUEVE(9), DIEZ(10), J(10), Q(10), K(10);
        private final List<Integer> valores;

        Valor(Integer... valores) {
            this.valores = Arrays.asList(valores);
        }

        public List<Integer> getValores() {
            return valores;
        }
    };

    public enum Pinta {
        PICA, TREBOL, DIAMANTE, CORAZON
    };

    public enum Color {
        NEGRA, ROJA
    };
}
