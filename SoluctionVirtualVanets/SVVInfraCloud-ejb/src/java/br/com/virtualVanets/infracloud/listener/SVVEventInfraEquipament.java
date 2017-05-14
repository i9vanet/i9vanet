/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.infracloud.listener;

import br.com.virtualVanets.common.InfraEquipament;
import br.com.virtualVanets.common.listener.SVVEventDevice;

/**
 *
 * @author georgejunior
 */
public class SVVEventInfraEquipament extends SVVEventDevice<InfraEquipament> {
    //private double temperature, moisture, pressure;
    
    public SVVEventInfraEquipament() {
        setDevice(new InfraEquipament());
    }
}
