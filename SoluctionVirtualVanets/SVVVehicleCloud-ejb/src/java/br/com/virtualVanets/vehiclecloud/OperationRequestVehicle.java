/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.vehiclecloud;

import br.com.virtualVanets.common.OperationRequestDevice;
import br.com.virtualVanets.common.Vehicle;

/**
 * Classe que representa a requisição de uma operação pelo OBU
 * @author George Leite Junior
 */
public class OperationRequestVehicle extends OperationRequestDevice <Vehicle> {
    public final static int MOVIMENTATION_CODE=10;
    public final static int DISCONNECT_CODE=20;
    public final static int CONNECT_CODE=30;
    public final static int SEND_BROADCAST_CODE=40;
    public final static int SEND_CODE=200;

//    private double speed;
//    private double direction;

}
