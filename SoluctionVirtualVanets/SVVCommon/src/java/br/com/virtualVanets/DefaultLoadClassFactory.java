/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets;

import br.com.virtualVanets.common.FactoryClass;
import br.com.virtualVanets.common.FactoryClass;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class responsável por carregar as classes de implementação dinamicamente. 
 * @author George Leite Junior
 */
public class DefaultLoadClassFactory<T> {
    
    public final static String CLASS_LOAD_PROPERTIES_FILE = "idclassload.properties";
    /**
     * Carega a classe pelo nome completo dela.
     * @param className
     * @return
     * @throws Exception 
     */
    public  T getObjectForClassName(String className) throws Exception{
//        return (CommunicationV2A)Class.forName(className).newInstance();
        return new FactoryClass<T>().getComponent(className);
    }
    /**
     * Carrega a classe pelo id definido no arquivo idclassload.properties
     * @param idClass
     * @return Instancia da classe definida no arquivo idclassload.properties
     * @throws Exception 
     */
    public  T getObjectForIdClass(String idClass) throws Exception{
//        return (CommunicationV2A)Class.forName(className).newInstance();
        InputStream is = new DefaultLoadClassFactory().getClass().getResourceAsStream(CLASS_LOAD_PROPERTIES_FILE);
        //System.
        //FileInputStream is = new FileInputStream("config_class_v2a.properties");
        Properties props = new Properties();
        props.load(is);
        String className = props.getProperty(idClass);
        return getObjectForClassName(className);
    }
    
    public  String getValueForNameParameter(String nameParameter) throws Exception{
//        return (CommunicationV2A)Class.forName(className).newInstance();
        InputStream is = new DefaultLoadClassFactory().getClass().getResourceAsStream(CLASS_LOAD_PROPERTIES_FILE);
        //System.
        //FileInputStream is = new FileInputStream("config_class_v2a.properties");
        Properties props = new Properties();
        props.load(is);
        String parameterValue = props.getProperty(nameParameter);
        return parameterValue;
    }    
}
