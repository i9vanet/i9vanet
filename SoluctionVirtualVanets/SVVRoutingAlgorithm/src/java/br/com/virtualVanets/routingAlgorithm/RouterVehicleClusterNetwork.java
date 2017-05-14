/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.routingAlgorithm;

import br.com.virtualVanets.common.Device;
import br.com.virtualVanets.common.util.UtilGeo;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author geoleite
 */
public class RouterVehicleClusterNetwork extends RouterNetwork {

    private static final String objsinal = "synchronized";

    @Override
    public Network createNetwork(Device device) {
        //System.out.println("Criando nova rede " + device.getId());
        Network network = null;
        synchronized (objsinal) {
            network = new Network(System.currentTimeMillis());
            Host host = new Host();
            host.setDevice(device);
            network.setHostManager(host);
            //Cadastrando a nova rede na lista geral
            addNetwork(network);
        }
        return network;
    }

    @Override
    public Network joinNetwork(Device device) {
        Collection<Network> collection = networksAll();
        Iterator<Network> iter = collection.iterator();
        //GeometryFactory gf = new GeometryFactory();
        //Coordinate coordDevice = new Coordinate(device.getLatitude(), device.getLongitude());
        //Point pDevice = gf.createPoint(coordDevice);
        double distanceMinima = Double.MAX_VALUE;
        Network networkIdeal = null;
        while (iter.hasNext()) {
            Network network = iter.next();
            //Calcula a distancia entre o dispositivo e o nó central da rede
            Device deviceNet = network.getHostManager().getDevice();
            //Coordinate coordNet = new Coordinate(deviceNet.getLatitude(), deviceNet.getLongitude());
            //Point pNet = gf.createPoint(coordNet);
            //double distance = pNet.distance(pDevice);
            double distance = getDistanciaGeometry(device.getLatitude(), device.getLongitude(), deviceNet.getLatitude(), deviceNet.getLongitude());
            //System.out.println(device.getLatitude() +  "-" + device.getLongitude() +  "-" + deviceNet.getLatitude() +  "-" + deviceNet.getLongitude());
            //System.out.println("Calculo distancia " + (distance));
            if (distance < distanceMinima && distance < DISTANCE_MAX_NETWORK) {
                distanceMinima = distance;
                networkIdeal = network;
            }
        }
        if (networkIdeal == null) {
            return createNetwork(device);
        } else {
            //System.out.println("Menor distancia " + (6378140 * distanceMinima));
            Host host = RouteTable.getIntance().getHostTableNetwork(device.getId());
            if (host == null) {
                host = new Host();
                host.setDevice(device);
            }
            networkIdeal.addHost(host);
            return networkIdeal;
        }
    }

    @Override
    public void getOutNetwork(Device device) {
        Host host = RouteTable.getIntance().getHostTableNetwork(device.getId());
        if (host != null) {
            Network network = (Network) host.getNetwork();
            if (network != null) {
                network.removeHostByAddress(device.getId());
                host.setNetwork(null);
            }
        }
    }

    @Override
    public Network findNetwork(Device device) {
        Host host = RouteTable.getIntance().getHostTableNetwork(device.getId());
        if (host != null) {
            Network network = (Network) host.getNetwork();
            if (network != null && network.getHostManager().getAddress().equals(device.getId())) {
                if (network.getMapHosts().size() == 1) {
                    network.removeHost(host);
                    removeNetwork(network);
                    joinNetwork(device);
                }
            }
            return network;
        }
        return null;
    }

}
