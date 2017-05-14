/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.common.listener;

import br.com.virtualVanets.DefaultLoadClassFactory;
import br.com.virtualVanets.common.Device;
import java.util.Date;

/**
 *
 * @author georgejunior
 */
public class SVVEventDevice<T extends Device> {

    public final static String EVENT_INFRA_EQUIPAMENT = "EventInfraEquipament";
    public final static String EVENT_VEHICLE = "EventVehicle";

    private T device;
    private int operationCode;
    //private double latitude, longitude, altitude;
    private Date date;
    private String message;

    private static DefaultLoadClassFactory<SVVEventDevice> defaultLoadClassFactory;

    public static SVVEventDevice getInstance(String idClass) throws Exception {
        if (defaultLoadClassFactory == null) {
            defaultLoadClassFactory = new DefaultLoadClassFactory<SVVEventDevice>();
        }
        return defaultLoadClassFactory.getObjectForIdClass(idClass);
    }

    /**
     * @return the operationCode
     */
    public int getOperationCode() {
        return operationCode;
    }

    /**
     * @param operationCode the operationCode to set
     */
    public void setOperationCode(int operationCode) {
        this.operationCode = operationCode;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the device
     */
    public T getDevice() {
        return device;
    }

    /**
     * @param device the device to set
     */
    public void setDevice(T device) {
        this.device = device;
    }
}
