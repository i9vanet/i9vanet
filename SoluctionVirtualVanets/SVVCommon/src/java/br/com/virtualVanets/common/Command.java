/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.common;

/**
 *
 * @author George Leite Junior
 */
public class Command {
    public static int FREE_COMMAND=0;
    private int type=0;
    private String cmd;
    private byte[] signature;

    /**
     * @return the cmd
     */
    public String getCmd() {
        return cmd;
    }

    /**
     * @param cmd the cmd to set
     */
    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

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

    @Override
    public String toString() {
        return type + " " + cmd;
    }

    /**
     * @return the signature
     */
    public byte[] getSignature() {
        return signature;
    }

    /**
     * @param signature the signature to set
     */
    public void setSignature(byte[] signature) {
        this.signature = signature;
    }
}