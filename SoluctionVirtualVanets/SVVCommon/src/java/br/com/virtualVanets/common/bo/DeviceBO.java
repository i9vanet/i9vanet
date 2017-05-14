/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.common.bo;

import br.com.virtualVanets.common.Device;
import br.com.virtualVanets.DataSource;
import br.com.virtualVanets.common.dao.DeviceDAO;
import java.sql.Connection;

/**
 *
 * @author georgejunior
 */
public class DeviceBO {
    private static DeviceBO deviceBO;
    private DeviceBO() {
        
    }
    public static DeviceBO getInstance() {
        if (deviceBO == null) {
            deviceBO = new DeviceBO();
        }
        return deviceBO;
    }
    
    public synchronized void insertDevice(Device device) throws Exception {
        Connection con = null;
        try {
            con = DataSource.getInstance().getConnection();
            DeviceDAO devDao = new DeviceDAO(con);
            device.setIdDB(System.currentTimeMillis());
            devDao.insert(device);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            con.close();
        }
    }
    
    public Device getDeviceByIdentification(String identification) throws Exception {
        Connection con = null;
        try {
            con = DataSource.getInstance().getConnection();
            DeviceDAO devDao = new DeviceDAO(con);
            Device device = devDao.getByIdentification(identification);
            return device;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            con.close();
        }
    }
}
