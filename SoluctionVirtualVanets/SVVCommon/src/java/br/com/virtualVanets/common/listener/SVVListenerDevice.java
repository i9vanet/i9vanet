/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.common.listener;

import br.com.virtualVanets.DefaultLoadClassFactory;
import br.com.virtualVanets.common.OperationRequestDevice;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Date;

/**
 * Evento padrão dos Dispositivos
 * @author George Leite Junior
 */
public abstract class SVVListenerDevice {
    
    public final static String LISTENER_VEHICLE="ListenerVehicle";
    public final static String LISTENER_INFRA_EQUIPAMENT="ListenerInfraEquipament";
    private static DefaultLoadClassFactory<SVVListenerDevice> defaultLoadClassFactory;
    
    public static SVVListenerDevice getInstance(String idClass) throws Exception {
        if (defaultLoadClassFactory == null) {
            defaultLoadClassFactory = new DefaultLoadClassFactory<SVVListenerDevice>();
        }
        return defaultLoadClassFactory.getObjectForIdClass(idClass);         
    }
    
    /**
     * Responsavel por executar o evento de acordo com a operação informada
     * @param svvEvent 
     */
    public abstract void callEvent(SVVEventDevice svvEvent);
    
    /**
     * Evento executado quando a conexão é estabelecida
     * @param event 
     */
    public abstract void onConnect(SVVEventDevice event);

    /**
     * Evento executado quando a conexão é desfeita
     * @param event 
     */
    public abstract void onDisconnet(SVVEventDevice event);
    
    public abstract void onLoopback(SVVEventDevice event);
    
    public abstract void onRead(SVVEventDevice event);    
    
    public abstract void onWrite(SVVEventDevice event);
}
