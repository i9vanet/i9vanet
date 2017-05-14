/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.infracloud.communication;

import br.com.virtualVanets.common.Command;
import br.com.virtualVanets.DefaultLoadClassFactory;
import br.com.virtualVanets.common.Device;
import br.com.virtualVanets.common.MobileAgent;
import br.com.virtualVanets.common.InfraEquipament;

/**
 * Define a comunicação entre o equipamento na Infraestrutura e o seu agente móvel
 * @author George Leite Junior
 */
public abstract class CommunicationI2A {
    private static DefaultLoadClassFactory<CommunicationI2A> defaultLoadClassFactory;
    public static CommunicationI2A getInstance() throws Exception {
        if (defaultLoadClassFactory == null) {
            defaultLoadClassFactory = new DefaultLoadClassFactory<CommunicationI2A>();
        }
        return defaultLoadClassFactory.getObjectForIdClass("CommunicationI2A");         
    }
    
    public abstract void sendMsg(String hostServer, Device device, Command command) throws Exception;
    
}
