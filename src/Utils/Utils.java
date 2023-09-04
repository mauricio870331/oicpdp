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

    public static boolean clearDirectory(String directory) {
        boolean delete = false;
        File directorio = new File("src/" + directory);
        if (directorio.isDirectory()) {
            File[] archivos = directorio.listFiles();
            if (archivos != null) {
                for (File archivo : archivos) {
                    archivo.delete();
                    delete = true;
                }
            }
        }
        return delete;
    }
    
    public static String responseMessage(String code) {
        String message = "";
           switch (code) {
                case "200":
                    message = "Integración activada correctamente";
                    break;
                case "204-3":
                    message = "Lookup importada correctamente";
                    break;
                case "400-3":
                    message = "No se subio ningun archivo";
                    break;
                case "404":
                    message = "Integración no encontrada";
                    break;
                case "412":
                    message = "La integración ya esta activada o desactivada";
                    break;
                case "500":
                    message = "Ocurio un error al tratar de activar la integarción, realice lo siguiene:"
                            + "\n1.Verifique que las conexiones origen y destino funcionen correctamente."
                            + "\n2.Reconfigure las credenciales en el conetor Origen."
                            + "\n3.Haga una prueba del conector para confirmar su funcionalidad.";
                    break;
                case "200-1":
                   message = "Credenciales Actualizadas..!";
                   break;
                case "200-2":
                   message = "La conexión se encuentra configurada corrctamente..!";
                   break;
                case "400-1":
                   message = "Error en la solicitud..!";
                   break;
                case "404-2":
                   message = "Conexión no encontrada..!";
                   break;
                case "409-2":
                   message = "Conexión no configurada..!";
                   break;
                case "409-3":
                   message = "Lookup ya existe..!";
                   break;
                case "412-2":
                   message = "Conector Bloqueado, por favor desbloqueelo en OIC..!";
                   break;
                case "423-1":
                   message = "Conector Bloqueado, por favor desbloqueelo en OIC..!";
                   break;
                case "500-1":
                   message = "Error en el servidor..!";
                   break;
                case "500-2":
                   message = "Error en el servidor..!";
                   break;
                case "500-3":
                   message = "Error en el servidor..!";
                   break;
                default:
                   message = "No existen credenciales configuradas para el conector: &&.\n"
                           + "Configurelas en el menú: \"Configuración->Configurar credenciales de aplicación\"..!";
                   break;
           }
           return message;
    }

}
