/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.vehiclecloud.listener;

import br.com.virtualVanets.common.Vehicle;
import br.com.virtualVanets.common.listener.SVVEventDevice;

/**
 *
 * @author georgejunior
 */
public class SVVEventVehicle extends SVVEventDevice<Vehicle>{
    //private double speed;
    //private double direction;
    public SVVEventVehicle() {
        setDevice(new Vehicle());
    }


}
