/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.bo;

import br.com.virtualVanets.routingAlgorithm.Network;
import br.com.virtualVanets.serverManager.Server;
import br.com.virtualVanets.ServerFactory;
import java.util.List;

/**
 *
 * @author georgejunior
 */
public class StartEnvironmentBO {
    
    private static StartEnvironmentBO startEnvironmentBO;    
    
    public static StartEnvironmentBO getInstance() {
        if (startEnvironmentBO == null) {
            startEnvironmentBO = new StartEnvironmentBO();
        }
        return startEnvironmentBO;
    }
    /**
     * Carrega o ambiente virtual
     */
    public void loadEnvironment() {
        try {
            //Carrega o servidor da base
            System.out.println("Iniciando ambiente I9Vanet");
            Server server = ServerFactory.getInstance().getServerDefault();
            System.out.println("Server " + server);
            //Carrega as redes do servidor
            ServerFactory.getInstance().loadNetworkInServer(server);
            System.out.println("Server Loaded: " + server);
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }
}
