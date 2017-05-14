/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.common;

/**
 *
 * @author geoleite
 */
public class VirtualNetwork {
    private long networkId, networkIdSuper;
    private long serverID, serverIDSuper;
    private String networkName, networkstatus;
    //Indica o Cluster Header (CH) da rede 
    private Device device;
    

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
     * @return the device
     */
    public Device getDevice() {
        return device;
    }

    /**
     * @param device the device to set
     */
    public void setDevice(Device device) {
        this.device = device;
    }
    
}
