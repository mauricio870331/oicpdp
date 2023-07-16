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
public class ConectoresModel extends Model {

    private String id;
    private String name;
    private String status;
    private Map<String, String> data;

    public ConectoresModel() {

    }

    public Map<String, Object> getConections(String env, String status) {
        OicRestApi oicra = new OicRestApi();
        return oicra.conectoresList(env, status);
    }

    public Map<String, Object> updateConectionCredentials(String env, String conectorId) {
        OicRestApi oicra = new OicRestApi();
        return oicra.updateCredentialsConector(env, conectorId);
    }

    public Map<String, Object> testConection(String env, String conectorId) {
       OicRestApi oicra = new OicRestApi();
        return oicra.testConector(env, conectorId);
    }

}
