/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.vehiclecloud.listener;

import br.com.virtualVanets.common.Vehicle;
import br.com.virtualVanets.common.listener.SVVEventDevice;
import br.com.virtualVanets.common.listener.SVVListenerDevice;


/**
 *
 * @author George Leite Junior
 */
public abstract class SVVListenerVehicle extends SVVListenerDevice {
    
    public abstract void onMoviment(SVVEventDevice<Vehicle> event);

    public abstract void onAreaIn(SVVEventDevice<Vehicle> event);

    public abstract void onAreaOut(SVVEventDevice<Vehicle> event);
}
