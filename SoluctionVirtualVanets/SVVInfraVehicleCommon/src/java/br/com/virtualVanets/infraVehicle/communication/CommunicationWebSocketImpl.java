/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.infraVehicle.communication;

import br.com.virtualVanets.common.Command;
import br.com.virtualVanets.common.Device;
import br.com.virtualVanets.common.Result;
import br.com.virtualVanets.infraVehicle.communication.impl.WebSocket;
import br.com.virtualVanets.common.ICommunication;

/**
 *
 * @author georgejunior
 */
public class CommunicationWebSocketImpl implements ICommunication{

    @Override
    public Result sendCommand(Device device, Command command) {
        return WebSocket.sendCommand(device, command);
    }    
}
