/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.common;

import java.util.Date;

/**
 * Classe que representa a requisição de operação de uma OBU e RSU
 * @author George Leite Junior
 */
public class OperationRequestDevice <T> {
    public final static String PARTTERN_DATE="dd/MM/yyyy HH:mm:ss.SSS";
    public final static int LOOP_BACK=0;
    public final static int CHANGE_SERVER=1;
    public final static int WRITE=7;
    private int operationCode;
    private String type;
    //private double latitude, longitude, altitude;
    private T device;
    private Date date;
    private String message="";

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
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
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
