/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.infraVehicle.communication;

import br.com.virtualVanets.common.Device;
import java.util.HashMap;
import java.util.Map;
import javax.websocket.Session;

/**
 *
 * @author georgejunior
 */
public class DeviceActive {
    private Device device;
    private Session peer;
    public DeviceActive(Device device, Session peer) {
        setDevice(device);
        setPeer(peer);
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
     * @return the peer
     */
    public Session getPeer() {
        return peer;
    }

    /**
     * @param peer the peer to set
     */
    public void setPeer(Session peer) {
        this.peer = peer;
    }
}