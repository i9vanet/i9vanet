/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.common.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author georgejunior
 */
public abstract class DaoBase<T> {
    private Connection connection;

    private List<Field> getFieldsClass(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        List<Field> listFields = new ArrayList<Field>();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            listFields.add(field);
            System.out.println(field.getName());
        }
        return listFields;
    }
    
    private void setFieldObject(Object obj, Field field, Object value) throws Exception {
        Class[] param = {field.getType()};
        String methodName = "set" + field.getName();
        Method method = obj.getClass().getMethod(methodName, param);
        Object[] args = {value};
        method.invoke(obj, args);
    }
    
    public List<T> resultSetToObjet(ResultSet rs, Class className) throws Exception {
        List<T> dataList = new ArrayList<T>();
        while (rs.next()) {
            Object obj = className.newInstance();
            List<Field> listFields = getFieldsClass(obj);
            for (int i = 0; i < listFields.size(); i++) {
                Field field = listFields.get(i);
                Object objValue = rs.getObject(field.getName());
                setFieldObject(obj, field, objValue);
            }
            dataList.add((T)obj);
        }
        return dataList;
    }
    
    public abstract List<T> resultSetToObject(ResultSet rs) throws Exception;
    
    public DaoBase(Connection con) {
        setConnetion(con);
    }
    
    public PreparedStatement createPreparedStatement(String sql) throws Exception {
        return connection.prepareCall(sql);
    }
    
    public void close() {
        try {
            connection.close();
        } catch (Exception e) {
        }
    }
    /**
     * @return the con
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * @param con the con to set
     */
    public void setConnetion(Connection con) {
        this.connection = con;
    }
}
