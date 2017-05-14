/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.common;

/**
 *
 * @author George Leite Junior
 */
public class Server1 {
    private String addressIP;
    private VirtualFace virtualFace;

    /**
     * @return the addressIP
     */
    public String getAddressIP() {
        return addressIP;
    }

    /**
     * @param addressIP the addressIP to set
     */
    public void setAddressIP(String addressIP) {
        this.addressIP = addressIP;
    }

    /**
     * @return the virtualFace
     */
    public VirtualFace getVirtualFace() {
        return virtualFace;
    }

    /**
     * @param virtualFace the virtualFace to set
     */
    public void setVirtualFace(VirtualFace virtualFace) {
        this.virtualFace = virtualFace;
    }
}
