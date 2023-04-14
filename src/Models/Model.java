/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import com.google.gson.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Mauricio Herrera
 */
public class Model {

    String url = "src/Models/Config.db";
    protected Connection connect;
    protected Statement query = null;
    protected ResultSet rs;
    private String table;   
    private String Model;
    private String sql;

    public Model() {
        this.connect();
    }

    private void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connect = DriverManager.getConnection("jdbc:sqlite:" + url);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public void disconnect() {
        try {
            sql = "";
            if (rs != null) {
                rs.close();
            }
            if (query != null) {
                query.close();
            }
            connect.close();
        } catch (SQLException ex) {
            System.out.println("error metodo disconnect " + ex);
        }
    }

    public void createTable(ArrayList<String> fields) {
        try {
            connect();
            query = connect.createStatement();
            sql = "CREATE TABLE IF NOT EXISTS  " + table + " (";
            for (int i = 0; i < fields.size(); i++) {
                sql += fields.get(i);
            }
            sql += ")";
            query.executeUpdate(sql);
        } catch (SQLException ex) {
            System.out.println("Error createTable() " + ex);
        }

    }

    public Model query(String sql) {
        try {
            this.query = connect.createStatement();
            rs = this.query.executeQuery(sql);
        } catch (SQLException ex) {
            System.out.println("Error metodo query; " + ex);
        }
        return this;
    }

    public int exec(String query) {
        int result = 0;
        try {
            this.query = this.connect.createStatement();
            this.query.executeUpdate(query);
            result = 1;
        } catch (SQLException ex) {
            System.out.println("error metodo exec " + ex.getMessage());
        }
        return result;
    }

    //Consultas
    public JsonObject first() {
        JsonObject obj = new JsonObject();
        try {
            if (this.rs.next()) {
                int totalColumns = this.rs.getMetaData().getColumnCount();
                for (int i = 0; i < totalColumns; i++) {
                    obj.addProperty(this.rs.getMetaData().getColumnLabel(i + 1)
                            .toLowerCase(), this.rs.getObject(i + 1).toString());
                }
            }
        } catch (SQLException ex) {
            System.out.println("error first() " + ex.getMessage());
        }
        return obj;
    }

    public JsonArray get() {
        try {
            return convert(this.rs);
        } catch (SQLException ex) {
            System.out.println("error get(): " + ex.getMessage());
            return null;
        }
    }

    public JsonArray all() {
        sql = "SELECT * FROM " + this.table;
        return this.query(sql).get();
    }

    public JsonObject find(String id) {
        sql = "SELECT * FROM " + this.table + " WHERE id = " + id;
        return this.query(sql).first();
    }

    public Model where(String column, String operator, String value) {
        if (value.equals("")) {
            value = operator;
            operator = "=";
        }
        sql = "SELECT * FROM " + this.table + " WHERE " + column + " " + operator + " '" + value + "'";
        this.query(sql);
        return this;
    }

    public JsonObject create(Map<String, String> data) throws SQLException {
        String columns = data.keySet().toString();
        columns = columns.replace("[", "").replace("]", "");
        String values = data.values().toString();
        values = values.replace("[", "'").replace("]", "'")
                .replace(",", "', '").replace("' ", " '");
        sql = "INSERT INTO " + this.table + " (" + columns + ") VALUES (" + values + ")";
        this.exec(sql);
        return this.find(data.get("id"));
    }

    //Utilidad
    public JsonArray convert(ResultSet resultSet) throws SQLException {
        JsonArray jsonArray = new JsonArray();
        while (resultSet.next()) {
            int totalColumns = resultSet.getMetaData().getColumnCount();
            JsonObject obj = new JsonObject();
            for (int i = 0; i < totalColumns; i++) {
                obj.addProperty(resultSet.getMetaData().getColumnLabel(i + 1)
                        .toLowerCase(), resultSet.getObject(i + 1).toString());
            }
            jsonArray.add(obj);
        }
        return jsonArray;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }
  
}
