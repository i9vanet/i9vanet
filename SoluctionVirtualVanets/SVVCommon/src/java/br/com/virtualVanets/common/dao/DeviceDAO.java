/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.common.dao;

import br.com.virtualVanets.common.Device;
import br.com.virtualVanets.common.InfraEquipament;
import br.com.virtualVanets.common.Vehicle;
import br.com.virtualVanets.common.security.I9Key;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.PublicKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author georgejunior
 */
public class DeviceDAO extends DaoBase {

    public DeviceDAO(Connection con) {
        super(con);
    }

    public void insert(Device device) throws Exception {
        String sql = "insert into public.dev_device (dev_nr_id, dev_tx_identification, dev_tx_type, dev_bt_publickey) values (? ,? ,?, ? )";
        PreparedStatement ps = null;
        try {
            ps = createPreparedStatement(sql);
            ps.setObject(1, device.getIdDB());
            ps.setObject(2, device.getId());
            ps.setObject(3, device.getType());
            byte[] publicKey = null;
            if (device.getPublicKey() != null) {
                //Convertendo a chave publica para array de bytes
                I9Key i9Key = new I9Key();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                i9Key.saveKeyGenerate(device.getPublicKey(), baos);
                publicKey = baos.toByteArray();
            }
            //Inserindo a chave publica na base
            ps.setObject(4, publicKey);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
            }
        }
    }

    public Device getByIdentification(String identification) throws Exception {
        String sql = "select * from public.dev_device where dev_tx_identification = ?";
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = createPreparedStatement(sql);
            ps.setObject(1, identification);
            rs = ps.executeQuery();
            List<Device> list = resultSetToObject(rs);
            return list.size() > 0 ? list.get(0) : null;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
            try {
                ps.close();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public List<Device> resultSetToObject(ResultSet rs) throws Exception {
        List<Device> dataList = new ArrayList<Device>();
        while (rs.next()) {
            String type = rs.getString("dev_tx_type");
            Device device = null;
            if (Device.OBU.equals(type)) {
                device = new Vehicle();
            } else {
                device = new InfraEquipament();
            }
            device.setIdDB(rs.getLong("dev_nr_id"));
            device.setId(rs.getString("dev_tx_identification"));
            device.setType(type);
            byte[] publicKeyBytes = rs.getBytes("dev_bt_publickey");
            if (publicKeyBytes != null) {
                I9Key i9Key = new I9Key();
                ByteArrayInputStream bais = new ByteArrayInputStream(publicKeyBytes);
                PublicKey publicKey = i9Key.loadPublicKey(bais, publicKeyBytes.length);
                device.setPublicKey(publicKey);
            }
            dataList.add(device);
        }
        return dataList;
    }
}
