/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.infraVehicle.communication.impl;

import br.com.virtualVanets.common.Command;
import br.com.virtualVanets.common.Device;
import br.com.virtualVanets.common.OperationRequestDevice;
import br.com.virtualVanets.common.Result;
import br.com.virtualVanets.common.bo.DeviceBO;
import br.com.virtualVanets.common.listener.SVVEventDevice;
import br.com.virtualVanets.common.listener.SVVListenerDevice;
import br.com.virtualVanets.common.security.ASecurityModel;
import br.com.virtualVanets.infraVehicle.communication.DeviceActive;
import br.com.virtualVanets.common.InfraEquipament;
import br.com.virtualVanets.infracloud.OperationRequestInfraEquipament;
import br.com.virtualVanets.infracloud.listener.SVVEventInfraEquipament;
import br.com.virtualVanets.infracloud.listener.SVVListenerInfraEquipament;
import br.com.virtualVanets.vehiclecloud.OperationRequestVehicle;
import br.com.virtualVanets.common.Vehicle;
import br.com.virtualVanets.common.security.I9Key;
import br.com.virtualVanets.vehiclecloud.listener.SVVEventVehicle;
import br.com.virtualVanets.vehiclecloud.listener.SVVListenerVehicle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

/**
 * Classe que implementa o WebSocket
 *
 * @author georgejunior
 */
public class WebSocket {

    public static String CONNECTED = "CONNECTED";
    protected static Logger logger = Logger.getLogger("WebSocket");
    private static Map<String, DeviceActive> mapPeers = Collections.synchronizedMap(new HashMap<String, DeviceActive>());
    private static Map<String, DeviceActive> mapPeersIdSocket = Collections.synchronizedMap(new HashMap<String, DeviceActive>());
    private Session peer;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static BufferedWriter out;
    private static long cont = 0;

    @OnOpen
    public void onOpen(Session peer) throws Exception {
        try {
            try {
                if (out == null) {
                    out = new BufferedWriter(new FileWriter("saida_srv.csv", true));
                }
            } catch (Exception e) {
            }
            this.peer = peer;
            //Obtendo os parâmetros da requisição
            String idDevice = peer.getPathParameters().get("idDevice");
            String typeDevice = peer.getPathParameters().get("type");
            String latitude = peer.getPathParameters().getOrDefault("latitude", "0");
            String longitude = peer.getPathParameters().getOrDefault("longitude", "0");
            if (typeDevice == null) {
                typeDevice = "OBU";
            }
            if (isActiveDeviceConnection(idDevice)) {
                peer.getBasicRemote().sendText("Another device connected with this id.");
                //return;
            }
            logger.info("onOpen ID:" + idDevice + " session id: " + peer.getId());
            peer.getBasicRemote().sendText(CONNECTED);
            //Device device = new Device();
            //Verifica se o Dispositivo já está cadastrado
            Device device = DeviceBO.getInstance().getDeviceByIdentification(idDevice);
            if (device == null) {
                //Indica que o dispositivo não está cadastrado
                //Inserir o Dispositivo na base
                if (Device.OBU.equals(typeDevice)) {
                    device = new Vehicle();
                } else {
                    device = new InfraEquipament();
                }
                //
                device.setId(idDevice);
                device.setType(typeDevice);
                DeviceBO.getInstance().insertDevice(device);
            }
            device.setLatitude(Double.parseDouble(latitude));
            device.setLongitude(Double.parseDouble(longitude));
            //Define um dispositivo como Ativo
            DeviceActive deviceActive = new DeviceActive(device, peer);
            //Armazena o Dispositivo Ativo para ser localizado pelo seu ID 
            mapPeers.put(idDevice, deviceActive);
            //Armazena o Dispositivo Ativo para ser localizado pelo id da sessão
            mapPeersIdSocket.put(peer.getId(), deviceActive);
            SVVListenerDevice listenerDevice = null;
            SVVEventDevice eventDevice = null;
            if (Device.OBU.equalsIgnoreCase(device.getType())) {
                //Obtém as classes de Event e Listener para o disositivo OBU,
                // definidas no arquivo de propriedades
                listenerDevice = SVVListenerDevice.getInstance(SVVListenerDevice.LISTENER_VEHICLE);
                eventDevice = SVVEventDevice.getInstance(SVVEventDevice.EVENT_VEHICLE);
            } else {
                //Obtém as classes de Event e Listener para o disositivo RSU
                // definidas no arquivo de propriedades
                listenerDevice = SVVListenerDevice.getInstance(SVVListenerDevice.LISTENER_INFRA_EQUIPAMENT);
                eventDevice = SVVEventDevice.getInstance(SVVEventDevice.EVENT_INFRA_EQUIPAMENT);
            }
            eventDevice.setDevice(device);
            //Informa que o dispositivo está conectado
            listenerDevice.onConnect(eventDevice);
        } catch (IOException ex) {
            Logger.getLogger(WebSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Lê os parametros da requisição e passa para o objeto do Evento
     *
     * @param message
     * @param svvEV
     * @return
     */
    private SVVEventVehicle readOperationRequestVehicle(String message, SVVEventVehicle svvEV) {
        Gson gson = new GsonBuilder().setDateFormat(OperationRequestDevice.PARTTERN_DATE).create();
        //OperationRequestVehicle orv = (OperationRequestVehicle) gson.fromJson(message, OperationRequestVehicle.class);
        //System.out.println("Vehicle: " + message);
        SVVEventVehicle svvEVTemp = (SVVEventVehicle) gson.fromJson(message, svvEV.getClass());
        svvEV.getDevice().setAltitude(svvEVTemp.getDevice().getAltitude());
        svvEV.getDevice().setLatitude(svvEVTemp.getDevice().getLatitude());
        svvEV.getDevice().setLongitude(svvEVTemp.getDevice().getLongitude());
        svvEV.getDevice().setSpeed(svvEVTemp.getDevice().getSpeed());
        svvEV.getDevice().setDirection(svvEVTemp.getDevice().getDirection());
        svvEV.getDevice().setSpeed(svvEVTemp.getDevice().getSpeed());
        svvEVTemp.setDevice(svvEV.getDevice());
        //svvEV.setAltitude(orv.getAltitude());
        //svvEV.setLatitude(orv.getLatitude());
        //svvEV.setLongitude(orv.getLongitude());
        //svvEV.setSpeed(orv.getSpeed());
        //svvEV.setOperationCode(orv.getOperationCode());
        //svvEV.setDate(orv.getDate());
        //svvEV.setMessage(orv.getMessage()); 
        return svvEVTemp;
    }

    /**
     * Lê os parametros da requisição e passa para o obujeto Evento
     *
     * @param message
     * @param svvIE
     * @return
     */
    private SVVEventInfraEquipament readOperationRequestInfraEquipament(String message, SVVEventInfraEquipament svvIE) {
        Gson gson = new GsonBuilder().setDateFormat(OperationRequestDevice.PARTTERN_DATE).create();
        //OperationRequestInfraEquipament orv = (OperationRequestInfraEquipament) gson.fromJson(message, OperationRequestInfraEquipament.class);
        //System.out.println("Infra: " + message);
        SVVEventInfraEquipament svvIETemp = (SVVEventInfraEquipament) gson.fromJson(message, svvIE.getClass());
        svvIE.getDevice().setAltitude(svvIETemp.getDevice().getAltitude());
        svvIE.getDevice().setLatitude(svvIETemp.getDevice().getLatitude());
        svvIE.getDevice().setLongitude(svvIETemp.getDevice().getLongitude());
        svvIE.getDevice().setMoisture(svvIETemp.getDevice().getMoisture());
        svvIE.getDevice().setPressure(svvIETemp.getDevice().getPressure());
        svvIE.getDevice().setTemperature(svvIETemp.getDevice().getTemperature());

        svvIETemp.setDevice(svvIE.getDevice());
        //InfraEquipament infraEquipament = (InfraEquipament)svvIETemp.getDevice();
        //svvIE.setAltitude(orv.getAltitude());
        //svvIE.setLatitude(orv.getLatitude());
        //svvIE.setLongitude(orv.getLongitude());
        //svvIE.setMoisture(orv.getMoisture());
        //svvIE.setPressure(orv.getPressure());
        //svvIE.setOperationCode(orv.getOperationCode());
        //svvIE.setTemperature(orv.getTemperature());
        //svvIE.setDate(orv.getDate());
        //svvIE.setMessage(orv.getMessage());
        return svvIETemp;
    }

    @OnMessage
    public byte[] onMessage(byte[] dataEncrypted) {
        try {
            String message = "";
            SecretKey secretKey = ASecurityModel.getInstance().getSecretKey();
            byte[] data = ASecurityModel.getInstance().decrypt(secretKey, dataEncrypted);
            message = new String(data);
            message = onMessage(message);
            data = message.getBytes("UTF8");
            data = ASecurityModel.getInstance().encrypt(secretKey, dataEncrypted);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0".getBytes();
    }

    @OnMessage
    public String onMessage(String message) {
        //System.out.println("msg "  + message);
        long timeI = System.currentTimeMillis();
        String result = "0";
        try {
            Gson gson = new Gson();
            Command cmd = gson.fromJson(message, Command.class);
            message = cmd.getCmd();
            //logger.info("onMessage " + peer.getId());
            DeviceActive deviceActive = mapPeersIdSocket.get(peer.getId());
            Device device = deviceActive.getDevice();
            //Checa assinatura
            byte[] data = message.getBytes("UTF8");
            if (ASecurityModel.getInstance().verifySign(device.getPublicKey(), cmd.getSignature(), data)) {
                //logger.info("onMessage " + message);
                SVVListenerDevice listenerDevice = null;
                SVVEventDevice eventDevice = null;
                try {
                    message = URLDecoder.decode(message, "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (Device.OBU.equalsIgnoreCase(device.getType())) {
                    listenerDevice = SVVListenerDevice.getInstance(SVVListenerDevice.LISTENER_VEHICLE);
                    eventDevice = SVVEventDevice.getInstance(SVVEventDevice.EVENT_VEHICLE);
                    eventDevice.setDevice(device);
                    SVVListenerVehicle svvLV = (SVVListenerVehicle) listenerDevice;
                    SVVEventVehicle svvEV = (SVVEventVehicle) eventDevice;

                    svvEV = readOperationRequestVehicle(message, svvEV);
                    svvLV.callEvent(svvEV);
                } else {
                    listenerDevice = SVVListenerDevice.getInstance(SVVListenerDevice.LISTENER_INFRA_EQUIPAMENT);
                    eventDevice = SVVEventDevice.getInstance(SVVEventDevice.EVENT_INFRA_EQUIPAMENT);
                    eventDevice.setDevice(device);
                    SVVListenerInfraEquipament svvLIE = (SVVListenerInfraEquipament) listenerDevice;
                    SVVEventInfraEquipament svvIE = (SVVEventInfraEquipament) eventDevice;
                    svvIE = readOperationRequestInfraEquipament(message, svvIE);
                    svvLIE.callEvent(svvIE);
                }
                result = "1";
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = "0";
        }
        //Arquivo de saida do processamento
        long timeF = System.currentTimeMillis();
        try {
            long differ = timeF - timeI;
            StringBuffer sb = new StringBuffer();
            sb.append(sdf.format(new Date(timeI))).append(";").append(message.length()).append(";").append(differ).append(";").append(++cont);
            out.append(sb.toString());
            out.newLine();
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                out.append("ERROR: ").append(e.getMessage());
                out.newLine();
            } catch (Exception ex) {
            }
        }
        return result;
    }

    @OnClose
    public void onClose(Session peer) {
        logger.info("onClose " + peer);
        try {
            String idDevice = peer.getPathParameters().get("idDevice");
            //String typeDevice = peer.getPathParameters().get("type");

            mapPeers.remove(idDevice);
            DeviceActive deviceActive = mapPeersIdSocket.remove(peer.getId());
            if (deviceActive != null) {
                Device device = deviceActive.getDevice();
                SVVListenerDevice listenerDevice = null;
                SVVEventDevice eventDevice = null;
                if (Device.OBU.equalsIgnoreCase(device.getType())) {
                    listenerDevice = SVVListenerDevice.getInstance(SVVListenerDevice.LISTENER_VEHICLE);
                    eventDevice = SVVEventDevice.getInstance(SVVEventDevice.EVENT_VEHICLE);
                } else {
                    listenerDevice = SVVListenerDevice.getInstance(SVVListenerDevice.LISTENER_INFRA_EQUIPAMENT);
                    eventDevice = SVVEventDevice.getInstance(SVVEventDevice.EVENT_INFRA_EQUIPAMENT);
                }
                eventDevice.setDevice(device);
                listenerDevice.onDisconnet(eventDevice);
                //Removendo do map

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static DeviceActive getSessionForIdDevice(Device device) {
        return mapPeers.get(device.getId());
    }

    /**
     * Indica se a comunicação com o dispositivo está ativa
     *
     * @param device
     * @return
     */
    public static boolean isActiveDeviceConnection(String idDevice) {
        return mapPeers.containsKey(idDevice);
    }

    /**
     * Envia o comando para o dispositivo usando websocket
     *
     * @param device
     * @param cmd
     * @return
     */
    public static Result sendCommand(Device device, Command cmd) {
        Result res = new Result();
        try {
            DeviceActive deviceActive = getSessionForIdDevice(device);
            if (deviceActive != null) {
                Session peer = deviceActive.getPeer();
                Gson gson = new Gson();
                String str = gson.toJson(cmd);
                try {
                    if (!ASecurityModel.getInstance().isEncripted()) {
                        peer.getBasicRemote().sendText(str);
                    } else {
                        SecretKey secretKey = ASecurityModel.getInstance().getSecretKey();
                        byte[] data = str.getBytes("UTF-8");
                        data = ASecurityModel.getInstance().encrypt(secretKey, data);
                        ByteBuffer bb = ByteBuffer.wrap(data);
                        peer.getBasicRemote().sendBinary(bb);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(WebSocket.class.getName()).log(Level.SEVERE, null, ex);
                }
                res.setType(Result.SUCCESS);
                res.setMsg("Enviado com sucesso! para o dispositivo: " + device.getId());
            }
        } catch (Exception ex) {
            Logger.getLogger(WebSocket.class.getName()).log(Level.SEVERE, null, ex);
            res.setType(Result.ERROR);
            res.setMsg("Falha ao enviar comando: " + ex.getMessage());
        }
        return res;
    }
}
