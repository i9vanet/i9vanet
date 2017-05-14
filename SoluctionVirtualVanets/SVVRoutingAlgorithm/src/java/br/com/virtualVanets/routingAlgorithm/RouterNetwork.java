/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.routingAlgorithm;

import br.com.virtualVanets.DefaultLoadClassFactory;
import br.com.virtualVanets.common.Device;
import br.com.virtualVanets.common.InfraEquipament;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Defini os métodos para o controle do roteamento
 * @author geoleite
 */
public abstract class RouterNetwork {
    public static int DISTANCE_MAX_NETWORK=1000;
    private static Map<Long, Network> mapNetwork = new HashMap<Long, Network>();
    public final static String ROUTER_NETWORK="RouterNetwork";
    private static DefaultLoadClassFactory<RouterNetwork> defaultLoadClassFactory;

    public static RouterNetwork getInstance() throws Exception {
        return getInstance(ROUTER_NETWORK);
    }
    
    public static RouterNetwork getInstance(String idClass) throws Exception {
        if (defaultLoadClassFactory == null) {
            defaultLoadClassFactory = new DefaultLoadClassFactory<RouterNetwork>();
        }
        return defaultLoadClassFactory.getObjectForIdClass(idClass);         
    }    
    //RouterNetwork=br.com.virtualVanets.routingAlgorithm.RouterInfraNetwork
    abstract public Network createNetwork(Device device);
    
    abstract public Network joinNetwork(Device device);
    
    abstract public void getOutNetwork(Device device);
    
    abstract public Network findNetwork(Device device);
    
    /**
     * Adiciona uma rede na lista de redes do servidor
     * @param network 
     */
    public synchronized void addNetwork(Network network) {
        mapNetwork.put(network.getNetworkId(), network);        
    }

    /**
     * Retira a rede, indicando que esta rede não existe mais
     * @param network 
     */
    public synchronized void removeNetwork(Network network) {
        mapNetwork.remove(network.getNetworkId());
    }
    
    public Collection<Network> networksAll() {
        return mapNetwork.values();
    }
    
    public double getDistanciaGeometry(double latitude, double longitude, double latitudePto, double longitudePto) {
        GeometryFactory gf = new GeometryFactory();
        Coordinate coord1 = new Coordinate(latitude, longitude);
        Coordinate coord2 = new Coordinate(latitudePto, longitudePto);
        Point p1 = gf.createPoint(coord1);
        Point p2 = gf.createPoint(coord2);
        double distance = p1.distance(p2);

        return (distance * (Math.PI / 180) * 6378137);
        /* 6378140 is the radius of the Earth in meters*/
    }
    
}
