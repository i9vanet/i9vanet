/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.common.util;

/**
 *
 * @author geoleite
 */
public class UtilGeo {

    public static double getDistancia(double latitude, double longitude, double latitudePto, double longitudePto) {
        double dlon, dlat, a, distancia;
        dlon = longitudePto - longitude;
        dlat = latitudePto - latitude;
        a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(latitude) * Math.cos(latitudePto) * Math.pow(Math.sin(dlon / 2), 2);
        distancia = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 6378140 * distancia;
        /* 6378140 is the radius of the Earth in meters*/
    }

    public static double calcularDirecao(double lat1, double lon1, 
                                double lat2, double lon2) {
        try {
            double catOposto = lat2 - lat1;
            double catAdjacente = lon2 - lon1;
            if (catOposto==0 || catAdjacente ==0)
                return 0;
            double tagAlfa = catOposto / catAdjacente;
            double rad = Math.atan(tagAlfa);
            //System.out.println("" + rad);
            double graus = Math.toDegrees(rad);
            //System.out.println("" + graus);
            return graus;
        } catch (ArithmeticException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
