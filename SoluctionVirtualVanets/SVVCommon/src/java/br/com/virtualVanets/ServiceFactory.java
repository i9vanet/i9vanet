/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets;

import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author georgejunior
 */
public class ServiceFactory {
    public final static String SERVICE_PROPERTIES_FILE = "service.properties";
    public static String SERVICE_URL = "service_url";
    
    private static ServiceFactory serviceFactory;
    private static Properties props;
    
    private ServiceFactory() {
        
    }
    
    public static ServiceFactory getInstance() {
        if (serviceFactory == null) {
            serviceFactory = new ServiceFactory();
        }
        return serviceFactory;
    }
    
    public String getServiceUrl() throws Exception {
        return (String)getPropertiesPackage().get(SERVICE_URL);
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
            InputStream is = new ServiceFactory().getClass().getResourceAsStream(SERVICE_PROPERTIES_FILE);
            props.load(is);
        }
        return props;
    }
    

}
