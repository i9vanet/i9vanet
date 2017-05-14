/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.routingAlgorithm;

import br.com.virtualVanets.common.Device;
import br.com.virtualVanets.common.InfraEquipament;
import br.com.virtualVanets.common.Vehicle;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author georgejunior
 */
public class RouteTable {

    private Map<String, Host> table = new HashMap<String, Host>();
    //private Map<Long, Host> tableOBU = new HashMap<Long, Host>();
    //private Map<Long, Host> tableRSU = new HashMap<Long, Host>();
    private static RouteTable routeTable;

    /**
     * Instanciamento Singleton
     *
     * @return
     */
    public static RouteTable getIntance() {
        if (routeTable == null) {
            routeTable = new RouteTable();
        }
        return routeTable;
    }

    public synchronized Host getHostTableNetwork(String address) {
        Host host = table.get(address);
        return host;
    }
    /**
     * Adiciona um host na tabela de roteamento do servidor
     *
     * @param host
     */
    public synchronized void addHostTableNetwork(Host host) {
        table.put(host.getAddress(), host);
    }

    public synchronized void removeHostTableNetwork(Host host) {
        if (table.containsKey(host.getAddress())) {
            table.remove(host.getAddress());
        }        
    }

    /**
     * Adiciona
     *
     * @param device
     */
//    public void addTableNetworkOBU(Vehicle vehicle) {
//    }
//    public void addTableNetworkRSU(InfraEquipament infraEquipament) {
//    }
}
