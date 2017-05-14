/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.rest;

import br.com.virtualVanets.common.Device;
import br.com.virtualVanets.routingAlgorithm.Network;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author georgejunior
 */
@Path("vvareanetwork")
public class VvareanetworkResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of VvareanetworkResource
     */
    public VvareanetworkResource() {
    }

    /**
     * Consulta a rede responsável pela área onde a coordenada está marcada
     * Retrieves representation of an instance of br.com.virtualVanets.rest.VvareanetworkResource
     * @return an instance of br.com.virtualVanets.routingAlgorithm.Network
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getNetworkByGPS/{latitude},{longitude}")
    public Network getNetworkByGPS(@PathParam("latitude") double lat, @PathParam("longitude" )double lon) {
        //TODO return proper representation object
        //Consultar a área e retornar o servidor responsável por ela
        
        return null;
    } 
    
    /**
     * Consulta a lista de redes de um sevidor
     * Retrieves representation of list an instance of br.com.virtualVanets.rest.VvareanetworkResource
     * @return an instance of br.com.virtualVanets.routingAlgorithm.Network
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getNetworksByServer/{serverId}")
    public List<Network> getNetworksByServer(@PathParam("serverId") long serverId) {
        //TODO return proper representation object
        //Consultar a área e retornar o servidor responsável por ela
        return null;
    } 
    
}