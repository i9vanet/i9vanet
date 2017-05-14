/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.applicationModel.listener;

import br.com.virtualVanets.ServerFactory;
import br.com.virtualVanets.common.Device;
import br.com.virtualVanets.common.OperationRequestDevice;
import br.com.virtualVanets.common.listener.SVVEventDevice;
import br.com.virtualVanets.infracloud.OperationRequestInfraEquipament;
import br.com.virtualVanets.routingAlgorithm.Host;
import br.com.virtualVanets.routingAlgorithm.Network;
import br.com.virtualVanets.routingAlgorithm.RouteTable;
import br.com.virtualVanets.routingAlgorithm.bo.NetworkBO;
import br.com.virtualVanets.serverManager.Server;
import br.com.virtualVanets.vehiclecloud.OperationRequestVehicle;
import br.com.virtualVanets.common.Vehicle;
import br.com.virtualVanets.routingAlgorithm.RouterNetwork;
import br.com.virtualVanets.vehiclecloud.listener.SVVEventVehicle;
import br.com.virtualVanets.vehiclecloud.listener.SVVListenerVehicle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author georgejunior
 */
public class DefaultListenerVehicle extends SVVListenerVehicle {

    private static Gson gson = new GsonBuilder().setDateFormat(OperationRequestDevice.PARTTERN_DATE).create();
    private static long cont=0;
    
    private static synchronized void inc() {
        cont++;
        System.out.println("Request: " + cont);
    }
    @Override
    public void onLoopback(SVVEventDevice svvEvent) {
        {
            System.out.println("OnLoopback " + svvEvent.getDate());
            try {
                Host host = new Host();
                host.setDevice(svvEvent.getDevice());
                host = RouteTable.getIntance().getHostTableNetwork(host.getAddress());
                if (host != null) {
                    Network network = (Network)host.getNetwork();
                    OperationRequestVehicle orv = new OperationRequestVehicle();
                    orv.setType(Device.OBU);
                    SVVEventVehicle svvEventVehicle = (SVVEventVehicle) svvEvent;
                    Vehicle vehicle = svvEventVehicle.getDevice();
                    vehicle = (Vehicle) vehicle.clone();
                    vehicle.setPublicKey(null);
                    orv.setDevice(vehicle);
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
    public void onMoviment(SVVEventDevice svvEvent) {
        inc();
        System.out.println("OnMoviment " );
        long t1 = System.currentTimeMillis();
        try {
            Server currentServer = ServerFactory.getInstance().getServerDefault();
            boolean result = ServerFactory.getInstance().serverCheck(svvEvent, currentServer);
            System.out.println("Result " + result );
            Server server = null;
            if (result) {
                server = currentServer;
            } else {
                server = ServerFactory.getInstance().serverCheck(svvEvent);
            }
            
            if (server == null) {
                //O Dispositivo não pertence a este servidor
                if (currentServer.getSrv_nr_idsuper() != 0) {
                    Server serverSuper = ServerFactory.getInstance().getByServerId(currentServer.getSrv_nr_idsuper());
                    onAreaOut(svvEvent);
                    sendCommandChangeServer(serverSuper, svvEvent);
                }
            } else if (server.getServerId() != currentServer.getServerId()) {
                //O servidor atual deve ser substituído
                onAreaOut(svvEvent);
                sendCommandChangeServer(server, svvEvent);
            } else {
                //Define o servidor no dispositivo
                //svvEvent.getDevice().setServer(server);
                //O dispositivo pertence a este servidor
                //Configurar a que rede que deve pertencer
                RouterNetwork routerNetwork = RouterNetwork.getInstance(RouterNetwork.ROUTER_NETWORK);
                Network network = routerNetwork.findNetwork(svvEvent.getDevice());
                if (network == null) {
                    network = routerNetwork.joinNetwork(svvEvent.getDevice());
                } else {
                    //Verificar se deve continuar nesta rede
                    Network networkNew = routerNetwork.joinNetwork(svvEvent.getDevice());
                    if (network != networkNew) {
                        //System.out.println("Saindo da rede para entrar em outra.");
                        Host host = RouteTable.getIntance().getHostTableNetwork(svvEvent.getDevice().getId());
                        network.replaceNetwork(host, networkNew);
                    }
                }
                //Network network = NetworkBO.getInstance().getByArea(svvEvent);
                onLoopback(svvEvent);
            }
        } catch (Exception ex) {
            Logger.getLogger(DefaultListenerVehicle.class.getName()).log(Level.SEVERE, null, ex);
        }

        long t2 = System.currentTimeMillis();
        System.out.println("Time OnMoviment: " + (t2 - t1));

    }

    /**
     * Envia comando de changeServer para o dispositivo
     *
     * @param server Servidor de destino
     * @param svvEvent Dispositivo que deve trocar de servidor
     * @throws Exception
     */
    private void sendCommandChangeServer(Server server, SVVEventDevice svvEvent) throws Exception {
        //Obtendo o endereço do Servidor Pai
        OperationRequestVehicle orv = new OperationRequestVehicle();
        orv.setOperationCode(OperationRequestVehicle.CHANGE_SERVER);
        orv.setDate(new Date());
        orv.setType(Device.OBU);
        orv.setMessage(server.getServerAddress());
        String str = gson.toJson(orv);
        //Enviando comando para o dispositivo
        Host host = RouteTable.getIntance().getHostTableNetwork(svvEvent.getDevice().getId());
        if (host != null ) {
            //NetworkBO.getInstance().getByArea(0, 0).sendMsg(host, host, str);
            Network  network = (Network)host.getNetwork();
            if (network != null) {
                network.sendMsg(host, host, str);
            }
        } else {
            host = new Host();
            host.setDevice(svvEvent.getDevice());
            NetworkBO.getInstance().getDefault().sendMsg(host, host, str);
        }
    }

    @Override
    public void onAreaIn(SVVEventDevice event) {
        System.out.println("OnAreaIn");
    }

    @Override
    public void onAreaOut(SVVEventDevice svvEvent) {
        System.out.println("OnAreaOut");
        Host host = new Host();
        host.setDevice(svvEvent.getDevice());
        host = RouteTable.getIntance().getHostTableNetwork(host.getAddress());
        if (host != null && host.getNetwork() != null) {
            ((Network)host.getNetwork()).removeHost(host);
        }
    }

    @Override
    public void onRead(SVVEventDevice event) {
        System.out.println("OnRead");
    }

    @Override
    public void onWrite(SVVEventDevice svvEvent) {
        System.out.println("OnWrite " + svvEvent.getDate());
        try {
            Host host = new Host();
            host.setDevice(svvEvent.getDevice());
            host = RouteTable.getIntance().getHostTableNetwork(host.getAddress());
            if (host != null) {
                Network network = (Network) host.getNetwork();
                OperationRequestVehicle orv = new OperationRequestVehicle();
                orv.setType(Device.OBU);
                SVVEventVehicle svvEventVehicle = (SVVEventVehicle) svvEvent;
                Vehicle vehicle = svvEventVehicle.getDevice();
                vehicle = (Vehicle) vehicle.clone();
                vehicle.setPublicKey(null);
                orv.setDevice(vehicle);
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
            System.out.println("OnConnect " + event.getDevice().getClass().getName());
            //Verifica se estar no servidor correto e a rede que deve conectar-se
            event.setDate(new Date());
            onMoviment(event);
            //Criando o host
//            Host host = new Host();
//            host.setDevice(event.getDevice());
//            //Por enquanto todos estão usando uma única rede de maneira forçada
//            //Depois deve mudar pra pegar do banco.
//            SVVEventVehicle svvEventVehicle = (SVVEventVehicle)event;
//            Network network = NetworkBO.getInstance()
//                    .getByArea(svvEventVehicle.getDevice().getLatitude(), 
//                               svvEventVehicle.getDevice().getLongitude());
//            //Adiciona o host na red
//            network.addHost(host);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisconnet(SVVEventDevice svvEvent) {
        System.out.println("OnDisconnect " + svvEvent.getDevice().getId());
        Host host = new Host();
        host.setDevice(svvEvent.getDevice());
        host = RouteTable.getIntance().getHostTableNetwork(host.getAddress());
        if (host != null) {
            if (host.getNetwork() != null) {
                ((Network)host.getNetwork()).removeHost(host);
            }
        }
    }

    private void onMessageBoreadCast(SVVEventDevice svvEvent) {
        System.out.println("OnMessageBoreadCast");
        try {
            Host host = RouteTable.getIntance().getHostTableNetwork(svvEvent.getDevice().getId());
            if (host != null) {
                Network network = (Network)host.getNetwork();
                network.sendMsgInternal(host, null, svvEvent.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callEvent(SVVEventDevice svvEvent) {
        switch (svvEvent.getOperationCode()) {
            case OperationRequestVehicle.LOOP_BACK:
                onLoopback(svvEvent);
                break;
            case OperationRequestVehicle.WRITE:
                onWrite(svvEvent);
                break;
            case OperationRequestVehicle.MOVIMENTATION_CODE:
                onMoviment(svvEvent);
                break;
            case OperationRequestVehicle.SEND_CODE:
                onRead(svvEvent);
                break;
            case OperationRequestVehicle.SEND_BROADCAST_CODE:
                onMessageBoreadCast(svvEvent);
                break;
            case OperationRequestVehicle.DISCONNECT_CODE:
                onDisconnet(svvEvent);
                break;
            case OperationRequestVehicle.CONNECT_CODE:
                onConnect(svvEvent);
                break;
        }
    }
}
