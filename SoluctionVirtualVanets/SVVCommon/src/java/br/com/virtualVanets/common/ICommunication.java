/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.common;

/**
 *
 * @author georgejunior
 */
public interface ICommunication {
    public Result sendCommand(Device device, Command command);
}
