/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mherrera
 */
public final class AppCredentialsModel extends Model {

    public long timestamp = (new Date().getTime() / 1000);
    Map<String, String> data;

    public AppCredentialsModel() {
        addTable();
    }

    public void addTable() {
        setTable("APP_CREDENTIALS");
        ArrayList<String> fields = new ArrayList<>();
        fields.add("id CHAR(100) PRIMARY KEY NOT NULL,");
        fields.add(" USER CHAR(100) NOT NULL,");
        fields.add(" PASS CHAR(100) NOT NULL,");
        fields.add(" ENV CHAR(100) NOT NULL,");
        fields.add(" APP CHAR(50) NOT NULL");
        createTable(fields);
    }

    public void addRecord(Map<String, String> data) throws SQLException {        
        data.put("id", Long.toString(timestamp));       
        System.out.println(create(data));
        disconnect();
    }
    
    

    public void getFirst() {
        super.query("select * from APP_CREDENTIALS where id = '1681412959'").first();
    }

}
