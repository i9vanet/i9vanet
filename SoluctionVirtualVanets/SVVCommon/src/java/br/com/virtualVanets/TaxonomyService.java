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
public class TaxonomyService {
    
    public final static String TAXONOMY_PROPERTIES_FILE="taxonomy_services.properties";
    private static TaxonomyService taxonomyService;
    public final static String SEND_COMMAND_DEVICE="sendCommandDevice";
    
    private TaxonomyService() {
        
    }
    
    public static TaxonomyService getInstance() {
        if (taxonomyService == null) {
            taxonomyService = new TaxonomyService();
        }
        return taxonomyService;
    }
    /**
     * Carrega a classe pelo id definido no arquivo idclassload.properties
     * @param idClass
     * @return Instancia da classe definida no arquivo idclassload.properties
     * @throws Exception 
     */
    public  String getServiceUrl(String idService) throws Exception{
//        return (CommunicationV2A)Class.forName(className).newInstance();
        InputStream is = new DefaultLoadClassFactory().getClass().getResourceAsStream(TAXONOMY_PROPERTIES_FILE);
        //System.
        //FileInputStream is = new FileInputStream("config_class_v2a.properties");
        Properties props = new Properties();
        props.load(is);
        return props.getProperty(idService);
    }    
}
