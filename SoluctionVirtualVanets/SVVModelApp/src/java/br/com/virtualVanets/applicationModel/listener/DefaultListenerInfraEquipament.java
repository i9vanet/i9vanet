/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.applicationModel.listener;

import br.com.virtualVanets.common.Device;
import br.com.virtualVanets.common.listener.SVVEventDevice;
import br.com.virtualVanets.common.InfraEquipament;
import br.com.virtualVanets.common.OperationRequestDevice;
import br.com.virtualVanets.infracloud.OperationRequestInfraEquipament;
import br.com.virtualVanets.infracloud.listener.SVVEventInfraEquipament;
import br.com.virtualVanets.infracloud.listener.SVVListenerInfraEquipament;
import br.com.virtualVanets.routingAlgorithm.Host;
import br.com.virtualVanets.routingAlgorithm.Network;
import br.com.virtualVanets.routingAlgorithm.RouteTable;
import br.com.virtualVanets.routingAlgorithm.RouterNetwork;
import br.com.virtualVanets.routingAlgorithm.bo.NetworkBO;
import br.com.virtualVanets.vehiclecloud.OperationRequestVehicle;
import br.com.virtualVanets.vehiclecloud.listener.SVVEventVehicle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Date;

/**
 *
 * @author georgejunior
 */
public class DefaultListenerInfraEquipament extends SVVListenerInfraEquipament {
    private static Gson gson = new GsonBuilder().setDateFormat(OperationRequestDevice.PARTTERN_DATE).create();
    @Override
    public void onLoopback(SVVEventDevice svvEvent) {
        {
            System.out.println("OnLoopback");
            try {
                Host host = RouteTable.getIntance().getHostTableNetwork(svvEvent.getDevice().getId());
                if (host != null) {
                    Network network = (Network)host.getNetwork();
                    network.sendMsg(host, host, svvEvent.getMessage());
                    OperationRequestInfraEquipament orv = new OperationRequestInfraEquipament();
                    orv.setType(Device.RSU);
                    SVVEventInfraEquipament sVVEventInfraEquipament = (SVVEventInfraEquipament) svvEvent;
                    orv.setDevice(sVVEventInfraEquipament.getDevice());
                    //orv.setAltitude(svvEvent.getAltitude());
                    //orv.setLatitude(svvEvent.getLatitude());
                    //orv.setLongitude(svvEvent.getLongitude());
                    //orv.setSpeed(((SVVEventVehicle) svvEvent).getSpeed());
                    orv.setOperationCode(OperationRequestVehicle.LOOP_BACK);
                    orv.setDate(svvEvent.getDate());
                    orv.setMessage(svvEvent.getMessage());
                    
                    String str = gson.toJson(orv);
                    System.out.println("Command loopback " + str);
                    network.sendMsg(host, host, str);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRead(SVVEventDevice event) {
        System.out.println("OnRead");
    }

    @Override
    public void onWrite(SVVEventDevice svvEvent) {
            System.out.println("OnLoopback");
            try {
                Host host = RouteTable.getIntance().getHostTableNetwork(svvEvent.getDevice().getId());
                if (host != null) {
                    Network network = (Network)host.getNetwork();
                    network.sendMsg(host, host, svvEvent.getMessage());
                    OperationRequestInfraEquipament orv = new OperationRequestInfraEquipament();
                    orv.setType(Device.RSU);
                    SVVEventInfraEquipament sVVEventInfraEquipament = (SVVEventInfraEquipament) svvEvent;
                    InfraEquipament infraEquipament = (InfraEquipament)sVVEventInfraEquipament.getDevice().clone();
                    infraEquipament.setPublicKey(null);
                    orv.setDevice(infraEquipament);
                    //orv.setAltitude(svvEvent.getAltitude());
                    //orv.setLatitude(svvEvent.getLatitude());
                    //orv.setLongitude(svvEvent.getLongitude());
                    //orv.setSpeed(((SVVEventVehicle) svvEvent).getSpeed());
                    orv.setOperationCode(OperationRequestVehicle.WRITE);
                    orv.setDate(new Date());
                    orv.setMessage(svvEvent.getMessage());
                    String str = gson.toJson(orv);
                    System.out.println("Command loopback " + str);
                    network.sendMsg(host, host, str);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    
    

    @Override
    public void onConnect(SVVEventDevice event) {
        try {

            System.out.println("OnConnect");
            /*
        Verificar em que rede o esquipamento se encontra,
        deve ser feito pelas coordenadas GPS, a consulta
        deve ser feita na base georeferenciada
             */
            //Criando o host

            //Cria uma rede para cada equipamento RSU caso não exista
            SVVEventInfraEquipament sVVEventInfraEquipament = (SVVEventInfraEquipament) event;
            RouterNetwork routerNetwork = RouterNetwork.getInstance(RouterNetwork.ROUTER_NETWORK);
            Network network = routerNetwork.findNetwork(sVVEventInfraEquipament.getDevice());
            if (network == null) {
                network = routerNetwork.createNetwork(sVVEventInfraEquipament.getDevice());
            }
//            Network network = NetworkBO.getInstance().getByArea(
//                                svvEventVehicle.getDevice().getLatitude(), 
//                                svvEventVehicle.getDevice().getLongitude());
//            //Adiciona o host na red
//            network.addHost(host);
            //RouteTable.getIntance().addTableNetworkRSU((InfraEquipament)event.getDevice());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void onMessageBoreadCast(SVVEventDevice svvEvent) {
        System.out.println("OnMessageBoreadCast");
        try {
            //Host host = new Host();
            //host.setDevice(svvEvent.getDevice());
            Host host = RouteTable.getIntance().getHostTableNetwork(svvEvent.getDevice().getId());
            if (host != null) {
                Network network = (Network)host.getNetwork();
                //network.sendMsg(host, null, svvEvent.getMessage());
                network.sendMsgInternal(host, null, svvEvent.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisconnet(SVVEventDevice svvEvent) {
        System.out.println("OnDisconnect");
        Host host = RouteTable.getIntance().getHostTableNetwork(svvEvent.getDevice().getId());
        if (host != null) {
            if (host.getNetwork() != null) {
                ((Network)host.getNetwork()).removeHost(host);
            }
        }
    }

    @Override
    public void callEvent(SVVEventDevice svvEvent) {
        switch (svvEvent.getOperationCode()) {
            case OperationRequestVehicle.LOOP_BACK:
                onLoopback(svvEvent);
                break;
            case OperationRequestInfraEquipament.WRITE:
                onWrite(svvEvent);
                break;
            case OperationRequestInfraEquipament.SEND_CODE:
                onRead(svvEvent);
                break;
            case OperationRequestInfraEquipament.SEND_BROADCAST_CODE:
                onMessageBoreadCast(svvEvent);
                break;
            case OperationRequestInfraEquipament.DISCONNECT_CODE:
                onDisconnet(svvEvent);
                break;
            case OperationRequestInfraEquipament.CONNECT_CODE:
                onConnect(svvEvent);
                break;
        }
    }

}
