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
public class Result {
    public final static int SUCCESS = 0;
    public final static int ERROR = 1;
    private int type=SUCCESS;
    private String msg;

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return type + ": " + msg; 
    }
    
    
}
