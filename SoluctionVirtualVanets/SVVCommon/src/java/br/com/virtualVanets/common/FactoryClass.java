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
public class FactoryClass<T> {
 public T getComponent(String className) throws Exception{ 
        return (T)Class.forName(className).newInstance();
    }    
}
