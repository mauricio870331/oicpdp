/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import Models.IntegrationModel;

/**
 *
 * @author Maurcio Herrera
 */
public class IntegrationsUtils implements Runnable{

    @Override
    public void run() {
        IntegrationModel im = new IntegrationModel();
        im.addTable("INTEGRATIONS_TST2");
        im.getIntegrations("TST2");
    }
    
}
