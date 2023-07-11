/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author Maurcio Herrera
 */
public class PropertiesModel {
    private String propertyName;
    private String propertyValue;

    public PropertiesModel(String propertyName, String propertyValue) {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }
    
    

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertieName) {
        this.propertyName = propertieName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    @Override
    public String toString() {
        return "{\"propertyName\":\"" + propertyName + "\", \"propertyValue\":\"" + propertyValue + "\"}\"";
    }
    
    
}
