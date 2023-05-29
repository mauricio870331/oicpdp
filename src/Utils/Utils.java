/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author mherrera
 */
public class Utils {

    public static String filePropertiesPath = "src/FilesSystem/file.properties";

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
                boolean equals = String.valueOf(((JPasswordField) componente).getPassword()).equals("");
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

    public static int getErrorHttpImport(String error) {
        int response = 200;
        if (error.contains("400")) {
            //No file is uploaded
            response = 400;
        }
        if (error.contains("409")) {
            //Integration already exists
            response = 409;
        }
        if (error.contains("500")) {
            //Server error
            response = 500;
        }
        return response;
    }

    public static int crearArchivoProperties() {
        //-1 Sin acción
        // 0 Error
        // 1 Archivo Creado
        // 2 El archivo ya existe
        int r = -1;
        File archivo = new File(filePropertiesPath);
        if (!archivo.exists()) {
            Properties properties = new Properties();
            try ( OutputStream output = new FileOutputStream(filePropertiesPath)) {
                // Guardar las propiedades en el archivo
                properties.store(output, "Archivo de propiedades");
                r = 1;
                System.out.println("Archivo .properties creado correctamente.");
            } catch (IOException e) {
                r = -0;
                e.printStackTrace();
            }
        } else {
            r = 2;
        }
        return r;
    }

    public static boolean modificarArchivoProperties(String ambiente, String url) {
        boolean r = true;
        try ( InputStream input = new FileInputStream(filePropertiesPath)) {
            Properties properties = new Properties();
            // Cargar las propiedades desde el archivo existente
            properties.load(input);
            // Modificar una propiedad
            for (Map.Entry<String, String> entry : leerArchivoProperties().entrySet()) {
                String key = entry.getKey();
                String val = entry.getValue();
                properties.setProperty(key, val);
            }

            properties.setProperty(ambiente, url);

            try ( OutputStream output = new FileOutputStream(filePropertiesPath)) {
                // Guardar las propiedades modificadas en el archivo
                properties.store(output, "Archivo de propiedades modificado");
                System.out.println("Archivo .properties modificado correctamente.");
            }
        } catch (IOException e) {
            r = false;
            e.printStackTrace();
        }
        return r;
    }

    public static Map<String, String> leerArchivoProperties() {
        Properties properties = new Properties();
        Map<String, String> urlsMap = new HashMap<>();
        try ( InputStream input = new FileInputStream(filePropertiesPath)) {
            // Cargar las propiedades desde el archivo
            properties.load(input);
            // Leer todas las propiedades
            properties.forEach((clave, val) -> urlsMap.put(clave.toString(), val.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urlsMap;
    }

    public static String leerProperties(String key) {
        String r = "";
        Properties properties = new Properties();
        try ( InputStream input = new FileInputStream(filePropertiesPath)) {
            // Cargar las propiedades desde el archivo
            properties.load(input);
            r = properties.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return r;
    }

}
