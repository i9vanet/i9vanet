/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.websocket;

import br.com.virtualVanets.common.Command;
import br.com.virtualVanets.common.Device;
import br.com.virtualVanets.common.Result;
import br.com.virtualVanets.infraVehicle.communication.impl.WebSocket;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * Implementação da comunicação utilizando WebSocket
 * @author George leite Junior
 */
@ServerEndpoint("/svvws")
public class SVVWS extends WebSocket{
/*
    protected static Logger logger = Logger.getLogger("SVVWS");
    private static Map<String, Session> mapPeers = Collections.synchronizedMap(new HashMap<String, Session>());
    //static Session peer;

    @OnOpen
    public void onOpen(Session peer) {
        try {
            String idDevice = peer.getPathParameters().get("idDevice");
            logger.info("onOpen ID:" + idDevice);
            peer.getBasicRemote().sendText("testando");
            mapPeers.put(idDevice, peer);
            //SVVWS.peer = peer;
        } catch (IOException ex) {
            Logger.getLogger(SVVWS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @OnMessage
    public String onMessage(String message) {
        logger.info("onMessage");
        return null;
    }

    @OnClose
    public void onClose(Session peer) {
        logger.info("onClose");
    }

    private static Session getSessionForIdDevice(Device device) {
        return mapPeers.get(device.getId());
    }
    /**
     * Envia o comando para o dispositivo usando websocket
     * @param device
     * @param cmd
     * @return 
     *
    public static Result sendCommand(Device device, Command cmd) {
        Result res = new Result();
        try {
            Session peer = getSessionForIdDevice(device);            
            peer.getBasicRemote().sendText(cmd.getCmd());
            res.setType(Result.SUCCESS);
            res.setMsg("Enviado com sucesso! para o dispositivo: " + device.getId() );
        } catch (IOException ex) {
            Logger.getLogger(SVVWS.class.getName()).log(Level.SEVERE, null, ex);
            res.setType(Result.ERROR);
            res.setMsg("Falha ao enviar comando: " + ex.getMessage());
        }
        return res;
    }*/
}
