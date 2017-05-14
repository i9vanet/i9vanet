/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.common;

import br.com.virtualVanets.common.Device;

/**
 *
 * @author George Leite Junior
 */
public class Vehicle extends Device {
    private double speed;
    private double direction;
//
    public Vehicle() {
        setType(Device.OBU);
    }
//    /**
//     * @return the speed
//     */
//    public double getSpeed() {
//        return speed;
//    }
//
//    /**
//     * @param speed the speed to set
//     */
//    public void setSpeed(double speed) {
//        this.speed = speed;
//    }
//
//    /**
//     * @return the direction
//     */
//    public double getDirection() {
//        return direction;
//    }
//
//    /**
//     * @param direction the direction to set
//     */
//    public void setDirection(double direction) {
//        this.direction = direction;
//    }

    /**
     * @return the direction
     */
    public double getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(double direction) {
        this.direction = direction;
    }

    /**
     * @return the speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * @param speed the speed to set
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }
}