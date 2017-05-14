/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.infracloud;

import br.com.virtualVanets.common.OperationRequestDevice;

/**
 *
 * @author georgejunior
 */
public class OperationRequestInfraEquipament extends OperationRequestDevice {
    public final static int SEND_CODE=200;
    public final static int CONNECT_CODE=300;
    public final static int SEND_BROADCAST_CODE=400;
    public final static int DISCONNECT_CODE=500;

    private double temperature, moisture, pressure;

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
