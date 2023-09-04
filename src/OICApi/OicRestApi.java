/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OICApi;

import Models.AppCredentialsModel;
import Models.PropertiesModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
import java.util.ArrayList;

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
        Map<String, Object> respuesta = (Map<String, Object>) apiOIC(getEnviromentUrl(OicRestApi.enviroment) + "/integration/v1/connections?orderBy=name&offset=0&limit=1", "GET", null, null);
        return respuesta;
    }

    public Map<String, Object> integrationsList(String env, String status) {
        JsonParser parser = new JsonParser();
        StringBuilder sb = new StringBuilder();
        sb.append("/integration/v1/integrations/");
        if (!status.equals("Seleccione Estado")) {
            sb.append("?q=%7Bstatus%20%3A'");
            sb.append(status);
            sb.append("'%7D");
        }
        Map<String, Object> respuesta = (Map<String, Object>) apiOIC(getEnviromentUrl(env) + sb.toString(), "GET", null, null);
        Map<String, Object> resp = new HashMap<>();
        JsonElement element = (JsonElement) parser.parse((String) respuesta.get("response"));
        JsonObject jsonObject = element.getAsJsonObject();
        resp.put("integraciones", jsonObject.get("items").getAsJsonArray());
        resp.put("total", jsonObject.get("totalResults").getAsInt());
        return resp;
    }

    public Map<String, Object> conectoresList(String env, String status) {
        JsonParser parser = new JsonParser();
        StringBuilder sb = new StringBuilder();
        sb.append("/integration/v1/connections/");
        if (!status.equals("Seleccione Estado") && !status.equals("")) {
            sb.append("?q=%7Bstatus%20%3A'");
            sb.append(status);
            sb.append("'%7D");
        }
        Map<String, Object> respuesta = (Map<String, Object>) apiOIC(getEnviromentUrl(env) + sb.toString(), "GET", null, null);
        Map<String, Object> resp = new HashMap<>();
        JsonElement element = (JsonElement) parser.parse((String) respuesta.get("response"));
        JsonObject jsonObject = element.getAsJsonObject();
        resp.put("conectores", jsonObject.get("items").getAsJsonArray());
        resp.put("total", jsonObject.get("totalResults").getAsInt());
        return resp;
    }

    public Map<String, Object> lookupsList(String env, String status) {
        JsonParser parser = new JsonParser();
        StringBuilder sb = new StringBuilder();
        sb.append("/integration/v1/lookups/");
        if (!status.equals("Seleccione Estado") && !status.equals("")) {
            sb.append("?q=%7B%20status%3A%20");
            sb.append("'");
            sb.append(status);
            sb.append("'");
            sb.append("%20%7D");
        }
        Map<String, Object> respuesta = (Map<String, Object>) apiOIC(getEnviromentUrl(env) + sb.toString(), "GET", null, null);
        Map<String, Object> resp = new HashMap<>();
        JsonElement element = (JsonElement) parser.parse((String) respuesta.get("response"));
        JsonObject jsonObject = element.getAsJsonObject();
        resp.put("lookups", (jsonObject.has("items")) ? jsonObject.get("items").getAsJsonArray() : new JsonArray());
        resp.put("total", jsonObject.get("totalResults").getAsInt());
        return resp;
    }

    public Map<String, Object> updateCredentialsConector(String env, String conectorId) {
        Map<String, Object> respuesta = new HashMap<>();
        System.out.println("Conector id = " + conectorId);
        AppCredentialsModel ct = new AppCredentialsModel();
        JsonObject jsonObject = ct.getByFieldName(conectorId, env);
//         System.out.println("jsonObject " + jsonObject.toString());
        if (!jsonObject.toString().equals("{}")) {//           
            Map<String, String> headers = new HashMap<>();
            headers.put("X-HTTP-Method-Override", "PATCH");
            //Data    
            ArrayList<PropertiesModel> lista = new ArrayList<>();
            lista.add(new PropertiesModel("username", jsonObject.get("user").getAsString()));
            lista.add(new PropertiesModel("password", jsonObject.get("pass").getAsString()));
            Map<String, String> data = new HashMap<>();
            data.put("securityProperties", lista.toString());
            Gson gsonObj = new Gson();
            String srtJson = gsonObj.toJson(data).replaceAll("\\\\", "").replace("\"[", "[").replace("}\"", "}").replace("\"]\"", "]").replace("]\"", "]");
            respuesta = (Map<String, Object>) apiOIC(getEnviromentUrl(env) + "/integration/v1/connections/" + conectorId, "POST", srtJson, headers);
        } else {
            respuesta.put("response_code", "-404");
        }
        return respuesta;
    }

    public Map<String, Object> activateDeactivateIntg(String intg, String env) {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-HTTP-Method-Override", "PATCH");
        //Data
        Map<String, String> data = new HashMap<>();
        data.put("status", "ACTIVATED");
        Gson gsonObj = new Gson();
        String srtJson = gsonObj.toJson(data);
        Map<String, Object> respuesta = (Map<String, Object>) apiOIC(getEnviromentUrl(env) + "/integration/v1/integrations/" + intg, "POST", srtJson, headers);
        return respuesta;
    }

    public Object apiOIC(String route, String method, String data, Map<String, String> headers) {
        Map<String, Object> resp = new HashMap<>();
        try {
            URL url = new URL(route);
//            System.out.println("route: "+route);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setRequestProperty("User-Agent", "insomnia/2023.1.0");
            httpCon.setRequestProperty("Acept", "*/*");
            httpCon.setRequestProperty("Content-Type", "application/json");
            if (headers != null) {
                System.out.println("headers send");
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpCon.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod(method);
            String userCredentials = OicRestApi.user + ":" + OicRestApi.pass;
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
            httpCon.setRequestProperty("Authorization", basicAuth);
            if (data != null) {
//                System.out.println("gsonObj.toJson(data) " + data);
                try ( OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream())) {
                    out.write(data);
                }
            }
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

    //Integracion d eprueba
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

    public boolean exportIntegration(String integration, String fileName, String envSrc) {
        boolean response = false;
        int BUFFER_SIZE = 4096;
        System.out.println("Id " + integration);
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
//                String disposition = httpConn.getHeaderField("Content-Disposition");
//                String contentType = httpConn.getContentType();
//                int contentLength = httpConn.getContentLength();
//                System.out.println("Content-Type = " + contentType);
//                System.out.println("Content-Disposition = " + disposition);
//                System.out.println("Content-Length = " + contentLength);
//                System.out.println("fileName = " + fileName);
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
//                System.out.println("File downloaded");
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

    public boolean exportLookups(String name, String envSrc) {
        boolean response = false;
        int BUFFER_SIZE = 4096;
        String fileURL = getEnviromentUrl(envSrc) + "/integration/v1/lookups/" + name + "/archive";
        try {
            URL url = new URL(fileURL);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            String userCredentials = OicRestApi.user + ":" + OicRestApi.pass;
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
            httpConn.setRequestProperty("Authorization", basicAuth);
            httpConn.setRequestMethod("GET");
            int responseCode = httpConn.getResponseCode();
            // always check HTTP response code first
            if (responseCode == HttpURLConnection.HTTP_OK) {
//                String fileName = "";
//                String disposition = httpConn.getHeaderField("Content-Disposition");
//                String contentType = httpConn.getContentType();
//                int contentLength = httpConn.getContentLength();
//                System.out.println("Content-Type = " + contentType);
//                System.out.println("Content-Disposition = " + disposition);
//                System.out.println("Content-Length = " + contentLength);
//                System.out.println("fileName = " + fileName);
                // opens input stream from the HTTP connection
                InputStream inputStream = httpConn.getInputStream();
                String saveFilePath = "src/downloads/" + File.separator + name + ".csv";
                // opens an output stream to save into file
                FileOutputStream outputStream = new FileOutputStream(saveFilePath);
                int bytesRead = -1;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                inputStream.close();
//                System.out.println("File downloaded");
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

    public int importFileOIC(String fileN, String env, String method, String apiPart) {
        System.out.println("nombre " + fileN);
        System.out.println("env " + env);
        String userCredentials = OicRestApi.user + ":" + OicRestApi.pass;
        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
        String filePath = "src/downloads/" + fileN;
        String requestUrl = getEnviromentUrl(env) + "/integration/v1/" + apiPart + "/archive";
        System.out.println(requestUrl);
        String fileFieldName = "file";
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        BufferedReader reader = null;
        int responsecode = -1;
        try {
            URL url = new URL(requestUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
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
            try ( FileInputStream fileInputStream = new FileInputStream(file)) {
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
            responsecode = connection.getResponseCode();
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            System.out.println(response.toString());
        } catch (IOException e) {
            System.out.println("e.getMessage() " + e.getMessage());
            responsecode = Utils.Utils.getErrorHttpImport(e.getMessage());
        } finally {
            // Cerrar las conexiones y recursos
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    System.out.println("error " + e.getMessage());
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("error " + e.getMessage());
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return responsecode;
    }

    public String getEnviromentUrl(String env) {
        return Utils.Utils.leerProperties(env);
    }

    public Map<String, Object> testConector(String env, String conectorId) {
        Map<String, Object> respuesta = (Map<String, Object>) apiOIC(getEnviromentUrl(env) + "/integration/v1/connections/" + conectorId + "/test", "POST", "", null);
        return respuesta;
    }

}
