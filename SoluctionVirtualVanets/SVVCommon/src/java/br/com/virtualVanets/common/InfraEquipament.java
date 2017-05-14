/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.common;

import br.com.virtualVanets.common.Device;

/**
 *
 * @author George Leite Junior
 */
public class InfraEquipament extends Device {
//
    private double temperature, moisture, pressure;
//
    public InfraEquipament() {
        setType(Device.RSU);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    /**
     * @return the temperature
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * @param temperature the temperature to set
     */
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    /**
     * @return the moisture
     */
    public double getMoisture() {
        return moisture;
    }

    /**
     * @param moisture the moisture to set
     */
    public void setMoisture(double moisture) {
        this.moisture = moisture;
    }

    /**
     * @return the pressure
     */
    public double getPressure() {
        return pressure;
    }

    /**
     * @param pressure the pressure to set
     */
    public void setPressure(double pressure) {
        this.pressure = pressure;
    }
}
