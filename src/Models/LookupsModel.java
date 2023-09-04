/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import OICApi.OicRestApi;
import java.util.Map;

/**
 *
 * @author Maurcio Herrera
 */
public class LookupsModel extends Model {

    private String id;
    private String name;
    private String status;
    private Map<String, String> data;

    public LookupsModel() {

    }

    public Map<String, Object> getLookups(String env, String status) {
        OicRestApi oicra = new OicRestApi();
        return oicra.lookupsList(env, status);
    }

    public boolean exportLookup(String name, String env) {
        OicRestApi oicra = new OicRestApi();
        return oicra.exportLookups(name, env);
    }

    
    public int importLookup(String filename, String env, String method, String urlPart) {
        OicRestApi oicra = new OicRestApi();
        return oicra.importFileOIC(filename + ".csv", env, method, urlPart);
    }
   

}
