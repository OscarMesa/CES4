/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.awt.event.KeyEvent;

/**
 *
 * @author Pablo
 */
public class validador {
    
    public validador(boolean required)
    {
        
    }
    
    public void  numerico(java.awt.event.KeyEvent evt)
    {
        char caracter = evt.getKeyChar();
        
        if(((caracter < '0') || (caracter > '9')) && (caracter != KeyEvent.VK_BACK_SPACE))
            {
                evt.consume();
            }
        
    }
    
    public void caracter(java.awt.event.KeyEvent evt)
    {
       int caracter =  (int)evt.getKeyChar();
        if(caracter >= 97 && caracter <= 122 || caracter >= 65 && caracter <= 90)
            {
                evt.consume();
            }
        
    }
    
    
}
