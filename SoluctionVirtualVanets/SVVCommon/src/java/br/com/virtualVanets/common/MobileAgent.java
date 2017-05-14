/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.common;

import java.io.Serializable;

/**
 *
 * @author George Leite Junior
 */
public class MobileAgent<T> implements Serializable{
    private T device;

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
