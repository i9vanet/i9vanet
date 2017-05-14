/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets;

import br.com.virtualVanets.common.Command;
import br.com.virtualVanets.common.Device;
import br.com.virtualVanets.common.ICommunication;

/**
 *
 * @author georgejunior
 */
public class CommunicationFactory {
    private static DefaultLoadClassFactory<ICommunication> defaultLoadClassFactory;
    public static ICommunication getInstance() throws Exception {
        if (defaultLoadClassFactory == null) {
            defaultLoadClassFactory = new DefaultLoadClassFactory<ICommunication>();
        }
        return defaultLoadClassFactory.getObjectForIdClass("ICommunication");         
    }
}
