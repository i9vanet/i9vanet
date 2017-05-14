/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.vehiclecloud.communication.impl;

import br.com.virtualVanets.CommunicationFactory;
import br.com.virtualVanets.TaxonomyService;
import br.com.virtualVanets.common.Command;
import br.com.virtualVanets.common.Device;
import br.com.virtualVanets.common.ICommunication;
import br.com.virtualVanets.common.MobileAgent;
import br.com.virtualVanets.common.Result;
import br.com.virtualVanets.common.util.RestClient;
import br.com.virtualVanets.common.Vehicle;
import br.com.virtualVanets.vehiclecloud.communication.CommunicationV2A;
import com.google.gson.Gson;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author George Leite Junior
 */
public class CommunicationV2AImpl extends CommunicationV2A {

    @Override
    public void sendMsg(String hostServer, Device device, Command command) throws Exception {
        if (device == null ) {
            throw new Exception("Invalid Device!");
        } else if (command == null) {
            throw new Exception("Invalid Command communication!");
        }
        
        System.out.println("enviando msg para o device "  + device + " " + command);
        
        //Enviando msg para veículo através do WS
//http://localhost:8080/SoluctionVirtualVanets-war/webresources/svvrest        
        
//        String urlWS = hostServer + TaxonomyService.getInstance().getServiceUrl(TaxonomyService.SEND_COMMAND_DEVICE);
//        RestClient restClient = new RestClient();
//        Result result = restClient.sendCommandRest(urlWS, device, command);
        ICommunication communicaiton = CommunicationFactory.getInstance();
        Result result = communicaiton.sendCommand(device, command);
        System.out.println("Result " + result);
    }
    
}
