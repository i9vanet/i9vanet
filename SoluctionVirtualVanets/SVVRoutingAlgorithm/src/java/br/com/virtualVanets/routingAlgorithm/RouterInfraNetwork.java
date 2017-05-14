/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
br.com.virtualVanets.routingAlgorithm.RouterInfraNetwork
 */
package br.com.virtualVanets.routingAlgorithm;

import br.com.virtualVanets.DefaultLoadClassFactory;
import br.com.virtualVanets.common.Device;
import br.com.virtualVanets.common.Server1;
import br.com.virtualVanets.common.listener.SVVListenerDevice;
import br.com.virtualVanets.common.util.UtilGeo;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author geoleite
 */
public class RouterInfraNetwork extends RouterNetwork {

    private static final String objsinal = "synchronized";

    @Override
    public Network createNetwork(Device device) {
        Network network = null;
        synchronized (objsinal) {
            network = new Network(System.currentTimeMillis());
            Host host = new Host();
            host.setDevice(device);
            network.setHostManager(host);
            //network.setDevice(device);
            //device.setVirtualNetwork(network);
            //Cadastrando a nova rede na lista geral
            addNetwork(network);
        }
        return network;
    }

    /**
     * Descobrir em que rede o dispositivo deve fazer parte
     *
     * @param device
     * @return
     */
    @Override
    public Network joinNetwork(Device device) {
        //Verifica em que rede o device deve entrar
        Collection<Network> collection = networksAll();
        Iterator<Network> iter = collection.iterator();
        GeometryFactory gf = new GeometryFactory();
        Coordinate coordDevice = new Coordinate(device.getLatitude(), device.getLongitude());
        Point pDevice = gf.createPoint(coordDevice);
        double distanceMinima = Double.MAX_VALUE;
        Network networkIdeal = null;
        while (iter.hasNext()) {
            Network network = iter.next();
            //Calcula a distancia entre o dispositivo e o nó central da rede
            Device deviceNet = network.getHostManager().getDevice();
            Coordinate coordNet = new Coordinate(deviceNet.getLatitude(), deviceNet.getLongitude());
            Point pNet = gf.createPoint(coordNet);
            //double distance = pNet.distance(pDevice);
            double distance = UtilGeo.getDistancia(pDevice.getX(), pDevice.getY(), pNet.getX(), pNet.getY());
            System.out.println("Calculo distancia " + (6378140 * distance));
            System.out.println("Calculo distancia " + (distance));
            if (distance < distanceMinima) {
                distanceMinima = distance;
                networkIdeal = network;

            }
        }
        System.out.println("Menor distancia " + (6378140 * distanceMinima));
        Host host = RouteTable.getIntance().getHostTableNetwork(device.getId());
        //device.setVirtualNetwork(networkIdeal);
        if (host == null) {
            host = new Host();
            host.setDevice(device);
        }
        //host.setNetwork(networkIdeal);
        networkIdeal.addHost(host);
        return networkIdeal;
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
            return network;
        }
        return null;
    }

    public static void main(String[] p) {
        try {
            GeometryFactory gf = new GeometryFactory();
            Coordinate coord1 = new Coordinate(-10.90630d, -37.06234d);
            Coordinate coord2 = new Coordinate(-10.906181511598998d, -37.061850152271525d);
            Point p1 = gf.createPoint(coord1);
            Point p2 = gf.createPoint(coord2);
            double distance = p1.distance(p2);
            System.out.println(distance / 0.3048);
            System.out.println((distance * (Math.PI / 180) * 6378137));
            System.out.println(UtilGeo.getDistancia(p1.getX(), p1.getY(), p2.getX(), p2.getY()));
            //            
            calcularDirecao(-10.90630d, -37.06234d, -10.906181511598998d, -37.061850152271525d);
            //calcularDirecao(0,0, 10,1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static double calcularDirecao(double lat1, double lon1, double lat2, double lon2) {
        double catOposto = lat2-lat1;
        double catAdjacente = lon2-lon1;
        double tagAlfa = catOposto / catAdjacente;
        double rad = Math.atan(tagAlfa);
        System.out.println("" + rad);
        double graus = Math.toDegrees(rad);
        System.out.println("" + graus);
        return 0;
    }
    
}
