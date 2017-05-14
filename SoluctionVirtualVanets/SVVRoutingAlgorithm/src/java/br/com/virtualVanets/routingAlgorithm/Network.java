package br.com.virtualVanets.routingAlgorithm;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import br.com.virtualVanets.common.Command;
import br.com.virtualVanets.common.MobileAgent;
import br.com.virtualVanets.ServiceFactory;
import br.com.virtualVanets.common.Device;
import br.com.virtualVanets.common.OperationRequestDevice;
import br.com.virtualVanets.common.listener.SVVEventDevice;
import br.com.virtualVanets.common.listener.SVVListenerDevice;
import br.com.virtualVanets.common.security.ASecurityModel;
import br.com.virtualVanets.common.security.I9Key;
import br.com.virtualVanets.infracloud.OperationRequestInfraEquipament;
import br.com.virtualVanets.infracloud.communication.CommunicationI2A;
import br.com.virtualVanets.infracloud.listener.SVVEventInfraEquipament;
import br.com.virtualVanets.infracloud.listener.SVVListenerInfraEquipament;
import br.com.virtualVanets.vehiclecloud.OperationRequestVehicle;
import br.com.virtualVanets.common.Vehicle;
import br.com.virtualVanets.common.VirtualNetwork;
import br.com.virtualVanets.vehiclecloud.communication.CommunicationV2A;
import br.com.virtualVanets.vehiclecloud.listener.SVVEventVehicle;
import br.com.virtualVanets.vehiclecloud.listener.SVVListenerVehicle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.net.ServerSocketFactory;

/**
 * Define a rede virtual
 *
 * @author georgejunior
 */
public class Network {

    private long networkId, networkIdSuper;
    private long serverID, serverIDSuper;
    private String networkName, networkstatus;
    private Map<String, Host> mapHosts = new HashMap<String, Host>();
    private Network networkSuper;
    private Map<Long, Network> mapChildNetwork = new HashMap<Long, Network>();
    private CommunicationI2A comI2A;
    private CommunicationV2A comV2A;
    private Host hostManager;

    public Network() throws Exception {

        comI2A = CommunicationI2A.getInstance();
        comV2A = CommunicationV2A.getInstance();
    }

    public Network(long networkId) {
        try {
            setNetworkId(networkId);
            comI2A = CommunicationI2A.getInstance();
            comV2A = CommunicationV2A.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Envia a mensagem para um destintario
     *
     * @param hostDst
     * @param msg
     */
    private void sendMessage(Host hostDst, String msg, byte[] signature) throws Exception {
//        String hostServer = "http://localhost:8080/SoluctionVirtualVanets-war/webresources";
        String hostServer = ServiceFactory.getInstance().getServiceUrl();
        Command cmd = new Command();
        cmd.setType(Command.FREE_COMMAND);
        cmd.setCmd(msg);
        cmd.setSignature(signature);
        System.out.println("Device " + hostDst.getDevice());
        //if (hostDst.getDevice() instanceof Vehicle) {
        if (Device.OBU.equalsIgnoreCase(hostDst.getDevice().getType())) {
            getComV2A().sendMsg(hostServer, hostDst.getDevice(), cmd);
        } else {
            getComI2A().sendMsg(hostServer, hostDst.getDevice(), cmd);
        }
    }

    /**
     * Envia a mensagem para um agente virtual do destinatário
     *
     * @param hostDst
     * @param msg
     */
    private void sendMessageInternal(Host hostDst, String message) throws Exception {
        Device device = hostDst.getDevice();
        SVVListenerDevice listenerDevice = null;
        SVVEventDevice eventDevice = null;
        if (Device.OBU.equalsIgnoreCase(device.getType())) {
            listenerDevice = SVVListenerDevice.getInstance(SVVListenerDevice.LISTENER_VEHICLE);
            eventDevice = SVVEventDevice.getInstance(SVVEventDevice.EVENT_VEHICLE);
            eventDevice.setDevice(device);
            SVVListenerVehicle svvLV = (SVVListenerVehicle) listenerDevice;
            SVVEventVehicle svvEV = (SVVEventVehicle) eventDevice;
            svvEV.setOperationCode(OperationRequestDevice.WRITE);
            //svvEV = readOperationRequestVehicle(message, svvEV);
            svvEV.setMessage(message);
            svvLV.callEvent(svvEV);
        } else {
            listenerDevice = SVVListenerDevice.getInstance(SVVListenerDevice.LISTENER_INFRA_EQUIPAMENT);
            eventDevice = SVVEventDevice.getInstance(SVVEventDevice.EVENT_INFRA_EQUIPAMENT);
            eventDevice.setDevice(device);
            SVVListenerInfraEquipament svvLIE = (SVVListenerInfraEquipament) listenerDevice;
            SVVEventInfraEquipament svvIE = (SVVEventInfraEquipament) eventDevice;
            //svvIE = readOperationRequestInfraEquipament(message, svvIE);
            svvIE.setOperationCode(OperationRequestDevice.WRITE);
            svvIE.setMessage(message);
            svvLIE.callEvent(svvIE);
        }
//        String hostServer = ServiceFactory.getInstance().getServiceUrl();
//        Command cmd = new Command();
//        cmd.setType(Command.FREE_COMMAND);
//        cmd.setCmd(msg);
//        System.out.println("Device " + hostDst.getDevice());
//        if (hostDst.getDevice() instanceof Vehicle) {
//            comV2A.sendMsg(hostServer, hostDst.getDevice(), cmd);
//        } else {
//            comI2A.sendMsg(hostServer, hostDst.getDevice(), cmd);
//        }
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
//        OperationRequestVehicle orv = (OperationRequestVehicle) gson.fromJson(message, OperationRequestVehicle.class);
        System.out.println("Send MSG Vehicle: " + message);
        SVVEventVehicle svvEVTemp = (SVVEventVehicle) gson.fromJson(message, svvEV.getClass());
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
     * Lê os parametros da requisição e passa para o objeto Evento
     *
     * @param message
     * @param svvIE
     * @return
     */
    private SVVEventInfraEquipament readOperationRequestInfraEquipament(String message, SVVEventInfraEquipament svvIE) {
        Gson gson = new GsonBuilder().setDateFormat(OperationRequestDevice.PARTTERN_DATE).create();
        //OperationRequestInfraEquipament orv = (OperationRequestInfraEquipament) gson.fromJson(message, OperationRequestInfraEquipament.class);
        System.out.println("Send MSG Infra: " + message);
        SVVEventInfraEquipament svvIETemp = (SVVEventInfraEquipament) gson.fromJson(message, svvIE.getClass());
        svvIETemp.setDevice(svvIE.getDevice());
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

    /**
     * Envia mensagem pra um host da rede, se o host de destino for null, envia
     * para todos os hosts da rede
     *
     * @param hostSource
     * @param hostDestination
     */
//    public void sendMsg(Host hostSource, Host hostDestination, String msg) throws Exception {
    public void sendMsg(Host hostSource, Host hostDestination, String msg) throws Exception {
        byte[] signature = null;
        if (hostSource == null) {
            throw new Exception("Host de origem não pode ser nulo!");
        } else if (hostDestination == null) {
            //Envia pra todos
            Collection<Host> collection = getMapHosts().values();
            Iterator<Host> iterator = collection.iterator();
            System.out.println("Enviando msg broadcast: " + msg);
            while (iterator.hasNext()) {
                Host hostDst = iterator.next();
                //Envia mensagens para todos os hosts diferentes da origem
                if (!hostDst.getAddress().equals(hostSource.getAddress())) {
                    System.out.println("host:" + hostDst.getDevice());
                    //Criptografar aqui  a mensagem
                    Device device = hostDst.getDevice();
                    //Se o dispositivo não tiver a chave publica, não irá criptografar

                    if (device.getPublicKey() != null) {
                        signature = getSign(I9Key.loadPrivateKeyServer(), msg);
                        //byte[] data = getEncript(device.getPublicKey(), msg);
                        //msg = new String(data, "UTF8");
                    }
                    sendMessage(hostDst, msg, signature);
                }
            }
        } else {
            //Envia pra um host específico
            System.out.println("Enviando msg: " + msg + " para:" + hostDestination.getDevice());
            //Criptografar aqui  a mensagem
            Device device = hostDestination.getDevice();
            //Se o dispositivo não tiver a chave publica, não irá criptografar
            if (device.getPublicKey() != null) {
                signature = getSign(I9Key.loadPrivateKeyServer(), msg);
                //byte[] data = getEncript(device.getPublicKey(), msg);
                //msg = new String(data, "UTF8");
            }
            sendMessage(hostDestination, msg, signature);
        }
    }

    private byte[] getEncript(PublicKey publicKey, String message) throws Exception {
        return ASecurityModel.getInstance().encrypt(publicKey, message.getBytes("UTF8"));
    }

    private byte[] getSign(PrivateKey privateKey, String message) throws Exception {
        return ASecurityModel.getInstance().sign(privateKey, message.getBytes("UTF8"));
    }

    public void sendMsgInternal(Host hostSource, Host hostDestination, String msg) throws Exception {
        if (hostSource == null) {
            throw new Exception("Host de origem não pode ser nulo!");
        } else if (hostDestination == null) {
            //Envia pra todos
            Collection<Host> collection = getMapHosts().values();
            Iterator<Host> iterator = collection.iterator();
            System.out.println("Enviando msg broadcast: " + msg);
            while (iterator.hasNext()) {
                Host hostDst = iterator.next();
                //Envia mensagens para todos os hosts diferentes da origem
                if (!hostDst.getAddress().equals(hostSource.getAddress())) {
                    System.out.println("host:" + hostDst.getDevice());
                    sendMessageInternal(hostDst, msg);
                }
            }
        } else {
            //Envia pra um host específico
            System.out.println("Enviando msg: " + msg + " para:" + hostDestination.getDevice());
            sendMessageInternal(hostDestination, msg);
        }
    }

    /**
     * Localiza se o dispositivo está nesta rede
     *
     * @param host
     * @return
     */
    public Device findHostNetwork(Host host) {
        Host hostTemp = getMapHosts().get(host.getAddress());
        return hostTemp.getDevice();
    }

    public synchronized void replaceNetwork(Host host, Network network) {
        removeHost(host);
        network.addHost(host);
    }

    /**
     * Adiciona sub-rede
     *
     * @param network
     */
    public synchronized void addChildNetwork(Network network) {
        getMapChildNetwork().put(network.getNetworkId(), network);
    }

    /**
     *
     * @param network
     * @return
     */
    public synchronized Network removeNetwork(Network network) {
        return getMapChildNetwork().remove(network.getNetworkId());
    }

    /**
     * Obtém a sub-red pelo id
     *
     * @param networkId
     * @return
     */
    public Network getSubNetwork(long networkId) {
        return getMapChildNetwork().get(networkId);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    /**
     * Adiciona host na rede
     *
     * @param host
     */
    public synchronized void addHost(Host host) {
        if (!mapHosts.containsKey(host.getAddress())) {
            getMapHosts().put(host.getAddress(), host);
        }
        //informa ao host a qual rede ele pertence
        host.setNetwork(this);
        //Adiciona o host na tabela de roteamento geral
        RouteTable.getIntance().addHostTableNetwork(host);
    }

    /**
     * Obtém um host da rede pelo seu endereço
     *
     * @param address
     * @return
     */
    public synchronized Host getHost(String address) {
        return getMapHosts().get(address);
    }

    /**
     * Remove o host da rede
     *
     * @param host
     */
    public synchronized void removeHost(Host host) {
        removeHostByAddress(host.getAddress());
    }

    /**
     * Remove o host da rede pelo address
     *
     * @param address
     */
    public synchronized void removeHostByAddress(String address) {
        if (getMapHosts().containsKey(address)) {
            Host host = getMapHosts().remove(address);
            Network network = (Network)host.getNetwork();
            if (network != null) {
                if (network.getHostManager().getAddress().equals(address)) {
                    //Remove todos os hosts desta rede
                    removeHostsNetwork(network);
                    try {
                        RouterNetwork routerNetwork = RouterNetwork.getInstance();
                        routerNetwork.removeNetwork(network);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            host.setNetwork(null);
            //RouteTable.getIntance().addHostTableNetwork(host);
            RouteTable.getIntance().removeHostTableNetwork(host);
        }
    }

    /**
     * Remove todos os hosts de uma rede
     *
     * @param network
     */
    private void removeHostsNetwork(Network network) {
        System.out.println("Removendo hosts da rede");
        Map<String, Host> map = network.getMapHosts();
        Set<String> set = map.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Host host = network.getHost(key);
            host.setNetwork(null);
            //network.removeHostByAddress(key);
        }
    }

    /**
     * @return the networkSuper
     */
    public Network getNetworkSuper() {
        return networkSuper;
    }

    /**
     * @param networkSuper the networkSuper to set
     */
    public void setNetworkSuper(Network networkSuper) {
        this.networkSuper = networkSuper;
    }

    /**
     * @return the networkId
     */
    public long getNetworkId() {
        return networkId;
    }

    /**
     * @param networkId the networkId to set
     */
    public void setNetworkId(long networkId) {
        this.networkId = networkId;
    }

    /**
     * @return the networkIdSuper
     */
    public long getNetworkIdSuper() {
        return networkIdSuper;
    }

    /**
     * @param networkIdSuper the networkIdSuper to set
     */
    public void setNetworkIdSuper(long networkIdSuper) {
        this.networkIdSuper = networkIdSuper;
    }

    /**
     * @return the serverID
     */
    public long getServerID() {
        return serverID;
    }

    /**
     * @param serverID the serverID to set
     */
    public void setServerID(long serverID) {
        this.serverID = serverID;
    }

    /**
     * @return the serverIDSuper
     */
    public long getServerIDSuper() {
        return serverIDSuper;
    }

    /**
     * @param serverIDSuper the serverIDSuper to set
     */
    public void setServerIDSuper(long serverIDSuper) {
        this.serverIDSuper = serverIDSuper;
    }

    /**
     * @return the networkName
     */
    public String getNetworkName() {
        return networkName;
    }

    /**
     * @param networkName the networkName to set
     */
    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    /**
     * @return the networkstatus
     */
    public String getNetworkstatus() {
        return networkstatus;
    }

    /**
     * @param networkstatus the networkstatus to set
     */
    public void setNetworkstatus(String networkstatus) {
        this.networkstatus = networkstatus;
    }

    /**
     * @return the mapHosts
     */
    public Map<String, Host> getMapHosts() {
        return mapHosts;
    }

    /**
     * @param mapHosts the mapHosts to set
     */
    public void setMapHosts(Map<String, Host> mapHosts) {
        this.mapHosts = mapHosts;
    }

    /**
     * @return the mapChildNetwork
     */
    public Map<Long, Network> getMapChildNetwork() {
        return mapChildNetwork;
    }

    /**
     * @param mapChildNetwork the mapChildNetwork to set
     */
    public void setMapChildNetwork(Map<Long, Network> mapChildNetwork) {
        this.mapChildNetwork = mapChildNetwork;
    }

    /**
     * @return the comI2A
     */
    public CommunicationI2A getComI2A() {
        return comI2A;
    }

    /**
     * @param comI2A the comI2A to set
     */
    public void setComI2A(CommunicationI2A comI2A) {
        this.comI2A = comI2A;
    }

    /**
     * @return the comV2A
     */
    public CommunicationV2A getComV2A() {
        return comV2A;
    }

    /**
     * @param comV2A the comV2A to set
     */
    public void setComV2A(CommunicationV2A comV2A) {
        this.comV2A = comV2A;
    }

    /**
     * @return the hostManager
     */
    public Host getHostManager() {
        return hostManager;
    }

    /**
     * @param hostManager the hostManager to set
     */
    public void setHostManager(Host hostManager) {
        this.hostManager = hostManager;
        //O host cluster da rede faz parte da rede tb
        addHost(hostManager);
    }
}
