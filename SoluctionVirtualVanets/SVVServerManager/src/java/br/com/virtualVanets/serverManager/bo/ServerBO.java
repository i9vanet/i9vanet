/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.serverManager.bo;

import br.com.virtualVanets.common.Device;
import br.com.virtualVanets.DataSource;
import br.com.virtualVanets.ServerFactory;
import br.com.virtualVanets.common.OperationRequestDevice;
import br.com.virtualVanets.common.dao.DeviceDAO;
import br.com.virtualVanets.common.listener.SVVEventDevice;
import br.com.virtualVanets.routingAlgorithm.Host;
import br.com.virtualVanets.routingAlgorithm.Network;
import br.com.virtualVanets.routingAlgorithm.RouteTable;
import br.com.virtualVanets.serverManager.Server;
import br.com.virtualVanets.serverManager.dao.ServerDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author georgejunior
 */
public class ServerBO {

    private static ServerBO serverBO;
    private static Map<Long, Server> mapServer = new HashMap<Long, Server>();

    private ServerBO() {

    }

    public static ServerBO getInstance() {
        if (serverBO == null) {
            serverBO = new ServerBO();
        }
        return serverBO;
    }

    /**
     * Checa se o dispositivo encontra dentro da cerca virtual do servidor
     *
     * @param svvEvent
     * @return
     * @throws Exception
     */
    public Server serverCheck(SVVEventDevice svvEvent) throws Exception {
        //Verifica se a localização pertence à área deste servidor

        double latitude = svvEvent.getDevice().getLatitude();
        double longitude = svvEvent.getDevice().getLongitude();

        Server server = getServerByArea(latitude, longitude);
        return server;
//        if (server == null) {            
//            return false;
//        } else {
//            if (serverCurrent.getServerId() != server.getServerId()) {
//                return false;
//            }
//        }
//        return true;
    }
    
    public boolean serverCheck(SVVEventDevice svvEvent, Server server) throws Exception {
        //Verifica se a localização pertence à área deste servidor

        double latitude = svvEvent.getDevice().getLatitude();
        double longitude = svvEvent.getDevice().getLongitude();

        server = mapServer.get(server.getServerId());
        Connection con = null;
        boolean result = false;
        try {
            con = DataSource.getInstance().getConnection();
            ServerDAO srvDao = new ServerDAO(con);
            result = srvDao.intecsecArea(server.getPolygon(), latitude, longitude);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            con.close();
        }
        
        return result;

    }    

    /**
     * Insere um novo servidor
     *
     * @param server
     * @throws Exception
     */
    public synchronized void insertServer(Server server) throws Exception {
        Connection con = null;
        try {
            con = DataSource.getInstance().getConnection();
            ServerDAO srvDao = new ServerDAO(con);
            server.setServerId(System.currentTimeMillis());
            srvDao.insert(server);
            mapServer.clear();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            con.close();
        }
    }

    /**
     * Consulta um servidor pelo id
     *
     * @param serverId
     * @return
     * @throws Exception
     */
    public synchronized Server getServerById(long serverId) throws Exception {
        if (mapServer.size() > 0) {
            return mapServer.get(serverId);
        } else {
            Connection con = null;
            try {
                System.out.println("Obtendo Conexao");
                DataSource ds = DataSource.getInstance();
                System.out.println("DS " + ds);
                con = ds.getConnection();
                System.out.println("Conexao " + con);
                ServerDAO srvDao = new ServerDAO(con);
                //Server server = srvDao.getById(serverId);
                List<Server> list = srvDao.getAll();
                mapServer.clear();
                for (int i = 0; i < list.size(); i++) {
                    Server srv = list.get(i);                    
                    mapServer.put(srv.getServerId(), srv);
                }
                return mapServer.get(serverId);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            } finally {
                if (con != null) {
                    con.close();
                }
            }
        }
    }

    /**
     * Checa se o servidor corente é responsável pela área informada pelo
     * dispositivo
     *
     * @param server
     * @param latitude
     * @param longitude
     * @return
     * @throws Exception
     */
    public Server getServerByArea(double latitude, double longitude) throws Exception {
        Connection con = null;
        try {
            con = DataSource.getInstance().getConnection();
            ServerDAO srvDao = new ServerDAO(con);
            Server server = null;
            server = srvDao.getByArea(latitude, longitude);
//            if (serverCurrent == null) {
//                server = srvDao.getByArea( latitude, longitude);
//            } else {
//                server = srvDao.getServerByArea(serverCurrent, latitude, longitude);
//            }
            //Para teste
            //Server server = srvDao.getById(1);
            return server;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            con.close();
        }
    }
    
    public Server getServerByAreaId(double latitude, double longitude, int serverId) throws Exception {
        Connection con = null;
        try {
            con = DataSource.getInstance().getConnection();
            ServerDAO srvDao = new ServerDAO(con);
            Server server = null;
            server = srvDao.getByArea(latitude, longitude);
//            if (serverCurrent == null) {
//                server = srvDao.getByArea( latitude, longitude);
//            } else {
//                server = srvDao.getServerByArea(serverCurrent, latitude, longitude);
//            }
            //Para teste
            //Server server = srvDao.getById(1);
            return server;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            con.close();
        }
    }    

    /**
     * Consulta um servdidor pelo endereço
     *
     * @param serverAddress
     * @return
     * @throws Exception
     */
    public Server getServerByAddress(String serverAddress) throws Exception {
        Connection con = null;
        try {
            con = DataSource.getInstance().getConnection();
            ServerDAO srvDao = new ServerDAO(con);
            Server server = srvDao.getByAddress(serverAddress);
            return server;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            con.close();
        }
    }

    /**
     * Obtém o servidor pai de um ervidor, ou retorna nulo caso não haja pai
     *
     * @param server
     * @return
     * @throws Exception
     */
    public Server getServerSuper(Server server) throws Exception {
        if (server.getSrv_nr_idsuper() == 0) {
            return null;
        }
        return getServerById(server.getSrv_nr_idsuper());
    }

    /**
     * Obtém a lista de servidores filhos de um servidor
     *
     * @param server
     * @return
     * @throws Exception
     */
    public List<Server> getChildServer(Server server) throws Exception {
        Connection con = null;
        try {
            con = DataSource.getInstance().getConnection();
            ServerDAO srvDao = new ServerDAO(con);
            return srvDao.getBySuper(server.getServerId());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            con.close();
        }
    }
}
