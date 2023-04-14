/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OICApi;

import Models.Model;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

/**
 *
 * @author mherrera
 */
public class OicRestApi extends Model {

    public OicRestApi() {
        setTable("APP_CREDENTIALS");
    }

    public void listIntegraciones() {
        try {
            URL url = new URL("https://tst2oic-epsainfraestructura-px.integration.ocp.oraclecloud.com/ic/api/integration/v1/integrations/");
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setRequestProperty("User-Agent", "insomnia/2023.1.0");
            httpCon.setRequestProperty("Acept", "*/*");
            httpCon.setRequestProperty("Content-Type", "application/json");
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("GET");
            String userCredentials = "mherrera@redclay.com:Welcome@123_";
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
            httpCon.setRequestProperty("Authorization", basicAuth);
//            try (OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream())) {
//                out.write(data);
//            }
//            System.out.println("Mauricio");
            System.out.println("Response Code: " + httpCon.getResponseCode());
//        System.out.print("Response Code: " + httpCon.getResponseMessage());

            StringBuilder respuesta;

            try (BufferedReader in = new BufferedReader(new InputStreamReader(httpCon.getInputStream()))) {
                String linea;
                respuesta = new StringBuilder();

                while ((linea = in.readLine()) != null) {
                    respuesta.append(linea);
                }
            }

            httpCon.disconnect();
//        System.exit(0);
            System.out.println(respuesta.toString());
        } catch (IOException e) {
            System.out.println("Error " + e.getMessage());
        }

    }

}
