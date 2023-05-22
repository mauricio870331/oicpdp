/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OICApi;

import Models.Model;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mherrera
 */
public class OicRestApi {

    public static String user;
    public static String pass;
    public static String enviroment;

    public OicRestApi() {

    }

    public Map<String, Object> login(String user, String pass, String enviroment) {
        if (OicRestApi.user == null && OicRestApi.pass == null && OicRestApi.enviroment == null) {
            System.out.println("nulos");
            OicRestApi.user = user;
            OicRestApi.pass = pass;
            OicRestApi.enviroment = enviroment;
        }
        Map<String, Object> respuesta = (Map<String, Object>) apiOIC(getEnviromentUrl(OicRestApi.enviroment) + "/integration/v1/connections?orderBy=name&offset=0&limit=1", "GET", null);
        return respuesta;
    }

    public Map<String, Object> integrationsList(String env) {
        JsonParser parser = new JsonParser();
        Map<String, Object> respuesta = (Map<String, Object>) apiOIC(getEnviromentUrl(env) + "/integration/v1/integrations/?q=%7Bstatus%20%3A'ACTIVATED'%7D", "GET", null);
        Map<String, Object> resp = new HashMap<>();
        JsonElement element = (JsonElement) parser.parse((String) respuesta.get("response"));
        JsonObject jsonObject = element.getAsJsonObject();
        resp.put("integraciones", jsonObject.get("items").getAsJsonArray());
        resp.put("total", jsonObject.get("totalResults").getAsInt());
        return resp;
    }

    public Object apiOIC(String route, String method, Map<String, String> data) {      
        Map<String, Object> resp = new HashMap<>();
        try {
            URL url = new URL(route);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setRequestProperty("User-Agent", "insomnia/2023.1.0");
            httpCon.setRequestProperty("Acept", "*/*");
            httpCon.setRequestProperty("Content-Type", "application/json");
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod(method);
            String userCredentials = OicRestApi.user + ":" + OicRestApi.pass;
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
            httpCon.setRequestProperty("Authorization", basicAuth);
            if (data != null) {
                Gson gsonObj = new Gson();
                try ( OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream())) {
                    out.write(gsonObj.toJson(data));
                }
            }
//            System.out.println("Mauricio");
            resp.put("response_code", httpCon.getResponseCode());
//            System.out.println("Response Code: " + httpCon.getResponseCode());
            StringBuilder respuesta;
            try ( BufferedReader in = new BufferedReader(new InputStreamReader(httpCon.getInputStream()))) {
                String linea;
                respuesta = new StringBuilder();
                while ((linea = in.readLine()) != null) {
                    respuesta.append(linea);
                }
            }
            httpCon.disconnect();
            resp.put("response", respuesta.toString());
//            System.out.println(respuesta.toString());
        } catch (IOException e) {
//            System.out.println("Error " + e.getLocalizedMessage());
            resp.put("error", e.getMessage());
        }
        return resp;
    }

    public String getEnviromentUrl(String env) {
        String url = "";
        switch (env) {
            case "TST2":
                url = "https://tst2oic-epsainfraestructura-px.integration.ocp.oraclecloud.com/ic/api";
                break;
            case "UAT2":
                url = "https://uat2oic-epsainfraestructura-px.integration.ocp.oraclecloud.com/ic/api";
                break;
            case "PRD":
                url = "https://prdoic-epsainfraestructura.integration.ocp.oraclecloud.com//ic/api";
                break;
        }
        return url;
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

            try ( BufferedReader in = new BufferedReader(new InputStreamReader(httpCon.getInputStream()))) {
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
