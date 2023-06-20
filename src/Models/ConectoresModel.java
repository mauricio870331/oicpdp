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
public class ConectoresModel extends Model {

    private String id; 
    private String name;
    private String status;
    private Map<String, String> data;

    public ConectoresModel() {

    }

    public Map<String, Object> getConections(String env, String status) {
        ArrayList<ConectoresModel> lista = new ArrayList<>();
        OicRestApi oicra = new OicRestApi();              
        return oicra.integrationsList(env, status);
    }

  
}
