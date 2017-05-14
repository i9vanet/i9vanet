/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.rest;

import br.com.virtualVanets.common.Command;
import br.com.virtualVanets.common.Device;
import br.com.virtualVanets.common.MobileAgent;
import br.com.virtualVanets.common.OperationRequestDevice;
import br.com.virtualVanets.common.Result;
import br.com.virtualVanets.common.Vehicle;
import br.com.virtualVanets.websocket.SVVWS;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author georgejunior
 */
@Path("svvrest")
public class SVVRest {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of SVVRest
     */
    public SVVRest() {
    }

    /**
     * Retrieves representation of an instance of
     * br.com.virtualVanets.rest.SVVRest
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        Command cmd = new Command();
        cmd.setType(0);
        cmd.setCmd("valor");
        Vehicle vehicle = new Vehicle();
        vehicle.setId("oeo4356");
        Result result = SVVWS.sendCommand(vehicle, cmd);
        String resultJson = new Gson().toJson(result);
        return resultJson;
    }
/*
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/sendCommandDevice/{device},{cmd}")
    public String sendCommandDevice(@PathParam("device")String device, @PathParam("cmd")String cmd) {
        Gson gson = new Gson();
        System.out.println("sendCommandDevice");
        Command command = gson.fromJson(cmd, Command.class);
        Device deviceObj = gson.fromJson(device, Vehicle.class);
        Result result = SVVWS.sendCommand(deviceObj, command);
        String resultJson = new Gson().toJson(result);
        return resultJson;
    }
*/    

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/sendCommandDevice/")
    public String sendCommandDevice(@FormParam("device")String device, @FormParam("cmd")String cmd) {
        Gson gson = new GsonBuilder().setDateFormat(OperationRequestDevice.PARTTERN_DATE).create();
        System.out.println("sendCommandDevice");
        Command command = gson.fromJson(cmd, Command.class);
        Device deviceObj = gson.fromJson(device, Vehicle.class);
        Result result = SVVWS.sendCommand(deviceObj, command);
        String resultJson = new Gson().toJson(result);
        return resultJson;
    }
    
    /**
     * PUT method for updating or creating an instance of SVVRest
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
