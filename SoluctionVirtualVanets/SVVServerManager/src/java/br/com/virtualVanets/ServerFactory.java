/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets;

import static br.com.virtualVanets.DataSource.DATASOURCE_PROPERTIES_FILE;
import br.com.virtualVanets.common.listener.SVVEventDevice;
import br.com.virtualVanets.routingAlgorithm.Network;
import br.com.virtualVanets.routingAlgorithm.bo.NetworkBO;
import br.com.virtualVanets.serverManager.Server;
import br.com.virtualVanets.serverManager.bo.ServerBO;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author georgejunior
 */
public class ServerFactory {

    public final static String SERVER_PROPERTIES_FILE = "server_identification.properties";
    public final static String SERVER_ID = "serverid";

    private static ServerFactory serverFactory;
    private static Properties props;

    private ServerFactory() {

    }

    /**
     * Cria o singleton
     *
     * @return
     */
    public static ServerFactory getInstance() {
        if (serverFactory == null) {
            serverFactory = new ServerFactory();
        }
        System.out.println("ServerFactory " + serverFactory);
        return serverFactory;
    }

    /**
     * Consulta o servidor pelo ID
     *
     * @param serverId
     * @return
     * @throws Exception
     */
    public Server getByServerId(long serverId) throws Exception {
        return ServerBO.getInstance().getServerById(serverId);
    }

    public Server serverCheck(SVVEventDevice event) throws Exception {
        return ServerBO.getInstance().serverCheck(event);
    }

    public boolean serverCheck(SVVEventDevice event, Server server) throws Exception {
        return ServerBO.getInstance().serverCheck(event, server);
    }
    

    /**
     * Consulta o servidor padrão definido no arquivo
     * server_identification.properties
     *
     * @return
     * @throws Exception
     */
    public Server getServerDefault() throws Exception {
        Properties props = getPropertiesPackage();
        long serverId = Long.parseLong(props.getProperty(SERVER_ID));
        return getByServerId(serverId);
    }

    /**
     * Consulta a lista de todas as redes ativas do servidor
     *
     * @param server
     * @return
     * @throws Exception
     */
    public List<Network> getNetworksByServer(Server server) throws Exception {
        return NetworkBO.getInstance().getListByServerId(server.getServerId());
    }

    /**
     * Carrega as redes de um servidor
     *
     * @param server
     */
    public void loadNetworkInServer(Server server) throws Exception {
        List<Network> listNet = getNetworksByServer(server);
        for (int i = 0; i < listNet.size(); i++) {
            Network ntw = listNet.get(i);
            server.addNetwork(ntw);
        }
    }

    /**
     * Consulta as propriedades do arquivo server_identification.properties
     *
     * @return
     * @throws Exception
     */
    public static Properties getPropertiesPackage() throws Exception {
        if (props == null) {
            props = new Properties();
            InputStream is = new ServerFactory().getClass().getResourceAsStream(SERVER_PROPERTIES_FILE);
            props.load(is);
            System.out.println("Server Properties " + props);
        }
        return props;
    }

}
