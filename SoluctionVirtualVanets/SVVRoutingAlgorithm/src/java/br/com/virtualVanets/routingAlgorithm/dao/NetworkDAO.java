/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.routingAlgorithm.dao;

import br.com.virtualVanets.common.Device;
import br.com.virtualVanets.common.dao.DaoBase;
import br.com.virtualVanets.routingAlgorithm.Network;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author georgejunior
 */
public class NetworkDAO extends DaoBase {
    
    public NetworkDAO(Connection con) {
        super(con);
    }
    
    
    public void insert(Network network) throws Exception {
        String sql = "insert into public.ntw_network (ntw_nr_id, ntw_tx_name, ntw_tx_status, srv_nr_id, srv_nr_idsuper, ntw_nr_idsuper) values (?, ? ,? ,?, ?, ? )";
        PreparedStatement ps = null;
        try {
            ps = createPreparedStatement(sql);
            ps.setObject(1, network.getNetworkId());
            ps.setObject(2, network.getNetworkName());
            ps.setObject(3, network.getNetworkstatus());
            ps.setObject(4, network.getServerID());
            ps.setObject(5, network.getServerIDSuper());
            ps.setObject(6, network.getNetworkIdSuper());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
            }
        }
    }
    
    public Network getById(long networkId) throws Exception {
        String sql = "select * from public.ntw_network where ntw_nr_id = ?";
        ResultSet rs=null;
        PreparedStatement ps = null;
        try {
            ps = createPreparedStatement(sql);
            ps.setObject(1, networkId);
            rs = ps.executeQuery();
            List<Network> list = resultSetToObject(rs);
            return list.size()>0?list.get(0):null;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
            try {
                ps.close();
            } catch (Exception e) {
            }
        }
    }
    
    /**
     * Consulta As redes ativas de um servidor
     * @param serverId
     * @return
     * @throws Exception 
     */
    public List<Network> getByServerId(long serverId) throws Exception {
        String sql = "select * from public.ntw_network where srv_nr_id = ? and ntw_tx_status='A' order by ntw_tx_name";
        ResultSet rs=null;
        PreparedStatement ps = null;
        try {
            ps = createPreparedStatement(sql);
            ps.setObject(1, serverId);
            rs = ps.executeQuery();
            return resultSetToObject(rs);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
            try {
                ps.close();
            } catch (Exception e) {
            }
        }
    }
    
    /**
     * Consulta todas as redes: ativas e inativas de um servidor
     * @param serverId
     * @return
     * @throws Exception 
     */
    public List<Network> getAllByServerId(long serverId) throws Exception {
        String sql = "select * from public.ntw_network where srv_nr_id = ? order by ntw_tx_name ";
        ResultSet rs=null;
        PreparedStatement ps = null;
        try {
            ps = createPreparedStatement(sql);
            ps.setObject(1, serverId);
            rs = ps.executeQuery();
            return resultSetToObject(rs);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
            try {
                ps.close();
            } catch (Exception e) {
            }
        }
    }    
        

    @Override
    public List<Network> resultSetToObject(ResultSet rs) throws Exception {
        List<Network> dataList = new ArrayList<Network>();
        while (rs.next()) {
            Network network = new Network();
            network.setNetworkId(rs.getLong("ntw_nr_id"));
            network.setNetworkIdSuper(rs.getLong("ntw_nr_idsuper"));
            network.setNetworkName(rs.getString("ntw_tx_name"));
            network.setNetworkstatus(rs.getString("ntw_tx_status"));
            network.setServerID(rs.getLong("srv_nr_id"));
            network.setServerIDSuper(rs.getLong("srv_nr_idsuper"));
            dataList.add(network);
        }
        return dataList;
    }
}
