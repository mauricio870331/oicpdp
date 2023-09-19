/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hilos;

import Models.IntegrationModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Maurcio Herrera
 */
public class HiloIntegraciones extends Thread {

    String ambiente;
    String status;
    JTable table;
    JLabel lbl;
    JLabel lblinfotst2;
    JProgressBar pb;

    public HiloIntegraciones(String ambiente, JTable table, JLabel lbl, JProgressBar pb, String status, JLabel lblinfotst2) {
        this.ambiente = ambiente;
        this.table = table;
        this.lbl = lbl;
        this.pb = pb;
        this.status = status;
        this.lblinfotst2 = lblinfotst2;
    }

    @Override
    public void run() {
        System.out.println("consultando integraciones");
        cargarIntegraciones(ambiente, table, lbl, pb, status);
    }

    private void cargarIntegraciones(String env, JTable table, JLabel lbl, JProgressBar pb, String status) {
        pb.setVisible(true);
        IntegrationModel im = new IntegrationModel();
        Map<String, Object> respuesta = im.getIntegrations(env, status);
        int total = (int) respuesta.get("total");
        lbl.setText("Total Integraciones: " + total);

        JsonArray list = (JsonArray) respuesta.get("integraciones");
//        System.out.println("list " + list);
        DefaultTableModel modeloTabla = new DefaultTableModel();
        // Agregar columnas a nuestro modelo de tabla
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("NAME");
        modeloTabla.addColumn("CODE");
        modeloTabla.addColumn("STATUS");
        modeloTabla.addColumn("ENV");
        modeloTabla.addColumn("VERSION");
        for (JsonElement jsonElement : list) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String ID = jsonObject.get("id").getAsString();
            String NAME = jsonObject.get("name").getAsString();
            String STATUS = jsonObject.get("status").getAsString();
            String CODE = jsonObject.get("code").getAsString();
            String VERSION = jsonObject.get("version").getAsString();
            modeloTabla.addRow(new Object[]{ID, NAME, CODE, STATUS, env, VERSION});
        }
// Asignar el modelo de tabla a nuestro JTable
        table.setModel(modeloTabla);
        table.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        table.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(3).setMaxWidth(150);
        table.getColumnModel().getColumn(3).setMinWidth(150);
        table.getColumnModel().getColumn(4).setMaxWidth(120);
        table.getColumnModel().getColumn(4).setMinWidth(120);

        table.setModel(modeloTabla);
        pb.setVisible(false);
        lblinfotst2.setText("");

    }
}
