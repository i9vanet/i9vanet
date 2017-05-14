/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.vehiclecloud.communication;

import br.com.virtualVanets.common.Command;
import br.com.virtualVanets.DefaultLoadClassFactory;
import br.com.virtualVanets.common.Device;
import br.com.virtualVanets.common.MobileAgent;
import br.com.virtualVanets.common.Vehicle;

/**
 * Define a comunicação entre o Veículo e o Agente Móvel
 * @author George Leite Junior
 */
public abstract class CommunicationV2A {
    private static DefaultLoadClassFactory<CommunicationV2A> defaultLoadClassFactory;
    public static CommunicationV2A getInstance() throws Exception {
        if (defaultLoadClassFactory == null) {
            defaultLoadClassFactory = new DefaultLoadClassFactory<CommunicationV2A>();
        }
        return defaultLoadClassFactory.getObjectForIdClass("CommunicationV2A");         
    }
    public abstract void sendMsg(String hostServer, Device device, Command command) throws Exception;
}
