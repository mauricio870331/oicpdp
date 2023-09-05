/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import Models.AppCredentialsModel;
import Models.PropertiesModel;
import OICApi.OicRestApi;
import com.google.gson.Gson;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mherrera
 */
public class test {

    public static void main(String[] args) throws SQLException {
//        AppCredentialsModel ct = new AppCredentialsModel();
////        System.out.println(ct);
////        ct.create();
//        System.out.println(ct.query("select * from APP_CREDENTIALS where id = '1681412959'").first());
//        System.out.println(ct.all());
//        ct.disconnect();
//        OicRestApi ora = new OicRestApi();
//        ora.listIntegraciones();
//        ora.login("mherrera@redclay.com", "Welcome@123_", "TST2");
//        System.out.println(Utils.Utils.leerProperties("PRD"));

// AppCredentialsModel ct = new AppCredentialsModel(); 
//        System.out.println(ct.getByFieldName("FIELD_SERVICE", "TST2"));
//        
//        ArrayList<PropertiesModel> lista = new ArrayList<>();
//        
//        lista.add(new PropertiesModel("prueba", "13"));
//        lista.add(new PropertiesModel("sdasfasf", "2222"));
// 
////      String fields = "[{\"propertyName\":\"username\",\"propertyValue\":\"esfera_epsa1test\"},{\"propertyName\":\"password\",\"propertyValue\":\"123456789\"}]";
////
////
//        Map<String, String> data = new HashMap<>();
////     
//        data.put("securityProperties",lista.toString());
////        
////        
////        
//        Gson gsonObj = new Gson();
//        System.out.println(gsonObj.toJson(data).replaceAll("\\\\", ""));
//System.out.println(Utils.Utils.leerProperties("UAT2"));
        String datos = "Mauricio";
        List<String> lista = new ArrayList<>();
        lista.add("perro");
        lista.add("gato");
        for (int i = 0; i < lista.size(); i++) {
            System.out.println("Letra "+lista.get(i));
        }
        
        for (String string : lista) {
            System.out.println("animal "+string);
        }
    }
}
