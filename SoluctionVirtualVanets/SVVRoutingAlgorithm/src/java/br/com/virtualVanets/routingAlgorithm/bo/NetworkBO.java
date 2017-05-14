/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.routingAlgorithm.bo;

import br.com.virtualVanets.common.Device;
import br.com.virtualVanets.DataSource;
import br.com.virtualVanets.common.dao.DeviceDAO;
import br.com.virtualVanets.common.listener.SVVEventDevice;
import br.com.virtualVanets.routingAlgorithm.Network;
import br.com.virtualVanets.routingAlgorithm.dao.NetworkDAO;
import br.com.virtualVanets.common.Vehicle;
import br.com.virtualVanets.routingAlgorithm.RouteTable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author georgejunior
 */
public class NetworkBO {
    private List<Network> listNetworks;
    private static NetworkBO networkBO;
    private static Network network = new Network(1);
    private NetworkBO() {
        listNetworks = new ArrayList<Network>();
    }
    
    public static NetworkBO getInstance() {
        if (networkBO == null) {
            networkBO = new NetworkBO();
        }
        return networkBO;
    }
    
    /**
     * Localiza a rede pelas coodenadas geográficas
     * @param lat
     * @param lon
     * @return 
     */
    public synchronized Network getByArea(double lat, double lon) {        
        return network;
    }

    public synchronized Network getDefault() {        
        return network;
    }
    
    public synchronized Network getByArea(SVVEventDevice event) throws Exception {
        Vehicle vehicle = (Vehicle) event.getDevice();
        //Calcular caracteristicas comuns entre veículos, utilizando gps (Latitude, Longitude e Altitude), 
        //velocidade e direção
        return null;
    }
    
    /**
     * Insere um novo servidor
     * @param server
     * @throws Exception 
     */
    public synchronized void insertServer(Network network) throws Exception {
        Connection con = null;
        try {
            con = DataSource.getInstance().getConnection();
            NetworkDAO netDao = new NetworkDAO(con);
            network.setNetworkId(System.currentTimeMillis());
            netDao.insert(network);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            con.close();
        }
    }
    
    /**
     * Consulta um servidor pelo id
     * @param serverId
     * @return
     * @throws Exception 
     */
    public Network getServerById(long networkId) throws Exception {
        Connection con = null;
        try {
            con = DataSource.getInstance().getConnection();
            NetworkDAO netDao = new  NetworkDAO(con);
            Network network = netDao.getById(networkId);
            return network;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            con.close();
        }
    }
    
    /**
     * Consulta as redes de um servidor (somente as ativas)
     * @param serverId
     * @return
     * @throws Exception 
     */
    public List<Network> getListByServerId(long serverId) throws Exception {
        Connection con = null;
        try {
            con = DataSource.getInstance().getConnection();
            NetworkDAO netDao = new  NetworkDAO(con);
            return netDao.getByServerId(serverId);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            con.close();
        }
    }  
    
    /**
     * Consulta todas as redes de um servidor: ativas e inativas
     * @param serverId
     * @return
     * @throws Exception 
     */
    public List<Network> getListAllByServerId(long serverId) throws Exception {
        Connection con = null;
        try {
            con = DataSource.getInstance().getConnection();
            NetworkDAO netDao = new  NetworkDAO(con);
            return netDao.getAllByServerId(serverId);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            con.close();
        }
    }      
  
    /**
     * Obtém o servidor pai de um servidor, ou retorna nulo caso não haja pai
     * @param server
     * @return
     * @throws Exception 
     */
    public Network getNetworkSuper(Network network) throws Exception {
        if (network != null && network.getNetworkIdSuper() == 0) {
            return null;
        }
        return getServerById(network.getNetworkIdSuper());
    }
    
}
