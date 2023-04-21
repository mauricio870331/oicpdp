/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author mherrera
 */
public class Utils {

    public static int validarCampos(Object[] componentes) {
        int countErrors = 0;
        for (Object componente : componentes) {
            if (componente instanceof JTextField) {
                boolean equals = ((JTextField) componente).getText().equals("");
                if (equals) {
                    countErrors++;
                    ((JTextField) componente).setBorder(BorderFactory.createLineBorder(Color.decode("#EE1313")));  //2C6791                  
                } else {
                    ((JTextField) componente).setBorder(null);
                }
            }
            if (componente instanceof JComboBox) {
                boolean equals = false;
                if (((JComboBox) componente).getSelectedItem() instanceof String) {
                    equals = ((JComboBox) componente).getSelectedItem().equals("SELECCIONE");
                }
                if (equals) {
                    countErrors++;
                    ((JComboBox) componente).setBorder(BorderFactory.createLineBorder(Color.decode("#EE1313")));  //2C6791                  
                } else {
                    ((JComboBox) componente).setBorder(null);
                }
            }
            if (componente instanceof JPasswordField) {
                boolean equals =  String.valueOf(((JPasswordField) componente).getPassword()).equals("");
                if (equals) {
                    countErrors++;
                    ((JPasswordField) componente).setBorder(BorderFactory.createLineBorder(Color.decode("#EE1313")));  //2C6791                  
                } else {
                    ((JPasswordField) componente).setBorder(null);
                }
            }
        }
        return countErrors;
    }

}
