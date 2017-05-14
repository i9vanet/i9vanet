/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.routingAlgorithm;

import br.com.virtualVanets.common.Device;
import br.com.virtualVanets.common.MobileAgent;
import com.google.gson.Gson;

/**
 * Classe que representa um veículo ou um equipamento de infra, fazendo a ligação
 * da rede virtual com os equipamentos 
 * @author georgejunior
 */
public class Host {
    //private MobileAgent<Device> mobileAgent;
    private Network network;
    private Device device;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    
    /**
     * @return the network
     */
    public Network getNetwork() {
        return network;
    }

    /**
     * @param network the network to set
     */
    public void setNetwork(Network network) {
        this.network = network;
    }

    /**
     * @return the device
     */
    public Device getDevice() {
        return device;
    }

    /**
     * @param device the device to set
     */
    public void setDevice(Device device) {
        this.device = device;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return device.getId();
    }

    
}