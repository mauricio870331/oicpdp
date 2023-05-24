/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OICApi;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.apache.http.util.EntityUtils;

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
                try (OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream())) {
                    out.write(gsonObj.toJson(data));
                }
            }
            resp.put("response_code", httpCon.getResponseCode());
//            System.out.println("Response Code: " + httpCon.getResponseCode());
            StringBuilder respuesta;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(httpCon.getInputStream()))) {
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

    public boolean exportIntegration(String integration, String fileName, String envSrc) {
        boolean response = false;
        int BUFFER_SIZE = 4096;
        String fileURL = getEnviromentUrl(envSrc) + "/integration/v1/integrations/" + integration + "/archive";
        try {
            URL url = new URL(fileURL);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            String userCredentials = OicRestApi.user + ":" + OicRestApi.pass;
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
            httpConn.setRequestProperty("Authorization", basicAuth);
            int responseCode = httpConn.getResponseCode();
            // always check HTTP response code first
            if (responseCode == HttpURLConnection.HTTP_OK) {
//                String fileName = "";
                String disposition = httpConn.getHeaderField("Content-Disposition");
                String contentType = httpConn.getContentType();
                int contentLength = httpConn.getContentLength();
                System.out.println("Content-Type = " + contentType);
                System.out.println("Content-Disposition = " + disposition);
                System.out.println("Content-Length = " + contentLength);
                System.out.println("fileName = " + fileName);
                // opens input stream from the HTTP connection
                InputStream inputStream = httpConn.getInputStream();
                String saveFilePath = "src/downloads/" + File.separator + fileName;
                // opens an output stream to save into file
                FileOutputStream outputStream = new FileOutputStream(saveFilePath);
                int bytesRead = -1;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                inputStream.close();
                System.out.println("File downloaded");
                response = true;
            } else {
                System.out.println("No file to download. Server replied HTTP code: " + responseCode);
            }
            httpConn.disconnect();
        } catch (MalformedURLException ex) {
            System.out.println("error " + ex);
        } catch (IOException ex) {
            System.out.println("error " + ex);
        }
        return response;
    }

    public boolean importIntegration(String env) {

        String userCredentials = OicRestApi.user + ":" + OicRestApi.pass;
        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
        String filePath = "src/downloads/ESFERA-INT17_C2M-OSC_SINCRONIZACION.iar";
        String requestUrl = getEnviromentUrl(env) + "/integration/v1/integrations/archive";

        String fileFieldName = "file";

        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        BufferedReader reader = null;
        String responsecode = "";

        try {
            URL url = new URL(requestUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", basicAuth);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=---------------------------1234567890");

            outputStream = new DataOutputStream(connection.getOutputStream());

            // Escribir los datos del archivo
            outputStream.writeBytes("-----------------------------1234567890\r\n");
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + fileFieldName + "\"; filename=\"" + new File(filePath).getName() + "\"\r\n");
            outputStream.writeBytes("Content-Type: application/octet-stream\r\n");
            outputStream.writeBytes("\r\n");

            File file = new File(filePath);
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            outputStream.writeBytes("\r\n");
            outputStream.writeBytes("-----------------------------1234567890--\r\n");

            // Leer la respuesta del servicio
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            System.out.println(response.toString());
        } catch (IOException e) {
            String error = e.getMessage();
            int indexInit = error.indexOf("code: ")+6;
            System.out.println(error.substring(indexInit, indexInit+3));

            System.out.println("error " + e.getMessage());
//            System.out.println("responsecode " + e);
        } finally {
            // Cerrar las conexiones y recursos
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                }
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }

            if (connection != null) {
                connection.disconnect();
            }
        }

        return false;
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

}
