/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.common;

import java.io.Serializable;
import java.security.PublicKey;

/**
 *
 * @author George Leite Junior
 */
public class Device implements Serializable, Cloneable{
    public static final String OBU = "OBU";
    public static final String RSU = "RSU";
    private String id;
    private long idDB;
    private double latitude, longitude, altitude;
    private String type=OBU;
    private PublicKey publicKey;
    //private VirtualNetwork virtualNetwork;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

    
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the altitude
     */
    public double getAltitude() {
        return altitude;
    }

    /**
     * @param altitude the altitude to set
     */
    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(id).append(", ").append(latitude).append(", ")
                .append(longitude).append(",").append(altitude);
        return sb.toString();
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the idDB
     */
    public long getIdDB() {
        return idDB;
    }

    /**
     * @param idDB the idDB to set
     */
    public void setIdDB(long idDB) {
        this.idDB = idDB;
    }

    /**
     * @return the publicKey
     */
    public PublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * @param publicKey the publicKey to set
     */
    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

//    /**
//     * @return the virtualNetwork
//     */
//    public VirtualNetwork getVirtualNetwork() {
//        return virtualNetwork;
//    }
//
//    /**
//     * @param virtualNetwork the virtualNetwork to set
//     */
//    public void setVirtualNetwork(VirtualNetwork virtualNetwork) {
//        this.virtualNetwork = virtualNetwork;
//    }
    
}
