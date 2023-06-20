/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import OICApi.OicRestApi;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author Maurcio Herrera
 */
public class IntegrationModel extends Model {

    private String id;
    private String code;
    private String name;
    private String version;
    private Map<String, String> data;

    public IntegrationModel() {

    }

    public Map<String, Object> getIntegrations(String env, String status) {
        ArrayList<IntegrationModel> lista = new ArrayList<>();
        OicRestApi oicra = new OicRestApi();              
        return oicra.integrationsList(env, status);
    }

    public void addTable(String table) {
        setTable(table);
        ArrayList<String> fields = new ArrayList<>();
        fields.add("id CHAR(100) PRIMARY KEY NOT NULL,");
        fields.add(" code CHAR(100) NOT NULL,");
        fields.add(" name CHAR(100) NOT NULL,");
        fields.add(" version CHAR(100) NOT NULL,");
        fields.add(" env CHAR(50) NOT NULL");
        createTable(fields);
    }

    public void addRecord(Map<String, String> data) throws SQLException {
        System.out.println(create(data));
        disconnect();
    }

    public void getFirst() {
        super.query("select * from APP_CREDENTIALS where id = '1681412959'").first();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

}
