/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import Models.AppCredentialsModel;
import OICApi.OicRestApi;
import java.sql.SQLException;

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
        System.out.println(Utils.Utils.leerProperties("PRD"));

    }
}
