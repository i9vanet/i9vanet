/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.serverManager;

import br.com.virtualVanets.routingAlgorithm.Network;
import com.google.gson.Gson;
import com.vividsolutions.jts.geom.Polygon;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author georgejunior
 */
public class Server {
    
    private long serverId;
    private long srv_nr_idsuper;
    private String serverName, serverAddress, serverStatus="A";    
    private Map<Long, Network> mapNetwork = new HashMap<Long, Network>();
    private Polygon polygon; 
    
    public void addNetwork(Network network) {
        getMapNetwork().put(network.getNetworkId(), network);
    }
    
    public Network getNetwork(long networkId) {
        return getMapNetwork().get(networkId);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
    
    /**
     * @return the serverId
     */
    public long getServerId() {
        return serverId;
    }

    /**
     * @param serverId the serverId to set
     */
    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    /**
     * @return the serverName
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * @param serverName the serverName to set
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * @return the serverAddress
     */
    public String getServerAddress() {
        return serverAddress;
    }

    /**
     * @param serverAddress the serverAddress to set
     */
    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    /**
     * @return the serverStatus
     */
    public String getServerStatus() {
        return serverStatus;
    }

    /**
     * @param serverStatus the serverStatus to set
     */
    public void setServerStatus(String serverStatus) {
        this.serverStatus = serverStatus;
    }

    /**
     * @return the mapNetwork
     */
    private Map<Long, Network> getMapNetwork() {
        return mapNetwork;
    }

    /**
     * @param mapNetwork the mapNetwork to set
     */
    private void setMapNetwork(Map<Long, Network> mapNetwork) {
        this.mapNetwork = mapNetwork;
    }

    /**
     * @return the srv_nr_idsuper
     */
    public long getSrv_nr_idsuper() {
        return srv_nr_idsuper;
    }

    /**
     * @param srv_nr_idsuper the srv_nr_idsuper to set
     */
    public void setSrv_nr_idsuper(long srv_nr_idsuper) {
        this.srv_nr_idsuper = srv_nr_idsuper;
    }

    /**
     * @return the polygon
     */
    public Polygon getPolygon() {
        return polygon;
    }

    /**
     * @param polygon the polygon to set
     */
    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }
}
