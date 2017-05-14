/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.virtualVanets.serverManager.dao;

import br.com.virtualVanets.common.Device;
import br.com.virtualVanets.common.dao.DaoBase;
import br.com.virtualVanets.serverManager.Server;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.geotools.geometry.jts.spatialschema.geometry.geometry.JTSGeometryFactory;
import org.postgis.Point;
import org.postgresql.util.PGobject;

/**
 *
 * @author georgejunior
 */
public class ServerDAO extends DaoBase {

    private static List<Server> servers;
    private static GeometryFactory gf = new GeometryFactory();

    private synchronized static void loadPolygons(ServerDAO serverDao) throws Exception {
        if (servers == null) {
            servers = new ArrayList<Server>();
            servers = serverDao.getAll();
        }
    }

    private Polygon polygonPGToPolygonGT(GeometryFactory gf, org.postgis.Polygon polygon) {
        Coordinate[] coordinates = new Coordinate[polygon.numPoints()];
        for (int i = 0; i < polygon.numPoints(); i++) {
            Point p = polygon.getPoint(i);
            Coordinate coord = new Coordinate(p.getX(), p.getY(), p.getZ());
            coordinates[i] = coord;
        }
        Polygon polygonNew = gf.createPolygon(coordinates);
        return polygonNew;
    }

    public ServerDAO(Connection con) throws Exception {
        super(con);
        loadPolygons(this);
    }

    public void insert(Server server) throws Exception {
        String sql = "insert into public.srv_servidor (srv_nr_id, srv_tx_name, srv_tx_address, srv_tx_status) values (?, ? ,? ,? )";
        PreparedStatement ps = null;
        try {
            ps = createPreparedStatement(sql);
            ps.setObject(1, server.getServerId());
            ps.setObject(2, server.getServerName());
            ps.setObject(3, server.getServerAddress());
            ps.setObject(4, server.getServerStatus());
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

    public List<Server> getAll() throws Exception {
        String sql = "select srv_nr_id, srv_tx_name, srv_tx_address, srv_tx_status, srv_nr_idsuper, ST_AsText(srv_geo_area) as srv_geo_area from public.srv_server where srv_tx_status='A' order by srv_nr_id";
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = createPreparedStatement(sql);
            rs = ps.executeQuery();
            List<Server> list = resultSetToObjectPolygon(rs);
            return list;
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

    public Server getByAreaOld(double latitude, double longitude) throws Exception {
        String sql = "select srv_nr_id, srv_tx_name, srv_tx_address, srv_tx_status, srv_nr_idsuper, ST_AsText(srv_geo_area) as srv_geo_area from public.srv_server srv  where ST_INTERSECTS(srv_geo_area, ST_GeomFromText('POINT(' || ? || ' ' || ? || ')'))";
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = createPreparedStatement(sql);
            ps.setObject(1, latitude);
            ps.setObject(2, longitude);
            rs = ps.executeQuery();
            List<Server> list = resultSetToObject(rs);
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

    public Server getByArea(double latitude, double longitude) throws Exception {
        for (int i = 0; i < servers.size(); i++) {
            Server server = servers.get(i);
            Polygon polygon = server.getPolygon();
            boolean existe = intecsecArea(polygon, latitude, longitude);
            System.out.println("server " + server.getServerAddress() + " achou " + existe);
            if (existe) {
                return server;
            }
        }
        return null;
    }

    public boolean intecsecArea(Polygon polygon, double latitude, double longitude) {
        Coordinate coordinate = new Coordinate(latitude, longitude);
        com.vividsolutions.jts.geom.Point point = gf.createPoint(coordinate);
        return polygon.intersects(point);
    }

//    public Server getServerByArea(Server serverCurrent, double latitude, double longitude) throws Exception {
//        String sql = "select * from public.srv_server srv  where srv_nr_id=? and ST_INTERSECTS(srv_geo_area, ST_GeomFromText('POINT(' || ? || ' ' || ? || ')'))";
//        ResultSet rs=null;
//        PreparedStatement ps = null;
//        try {
//            ps = createPreparedStatement(sql);
//            ps.setObject(1, serverCurrent.getServerId());
//            ps.setObject(2, latitude);
//            ps.setObject(3, longitude);
//            rs = ps.executeQuery();
//            List<Server> list = resultSetToObject(rs);
//            return list.size()>0?list.get(0):null;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        } finally {
//            try {
//                rs.close();
//            } catch (Exception e) {
//            }
//            try {
//                ps.close();
//            } catch (Exception e) {
//            }
//        }        
//    }    
    public Server getById(long serverId) throws Exception {
        String sql = "select srv_nr_id, srv_tx_name, srv_tx_address, srv_tx_status, srv_nr_idsuper, ST_AsText(srv_geo_area) as srv_geo_area from public.srv_server where srv_nr_id = ?";
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = createPreparedStatement(sql);
            ps.setObject(1, serverId);
            rs = ps.executeQuery();
            List<Server> list = resultSetToObject(rs);
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
    


    public Server getByAddress(String serverAddress) throws Exception {
        String sql = "select srv_nr_id, srv_tx_name, srv_tx_address, srv_tx_status, srv_nr_idsuper, ST_AsText(srv_geo_area) as srv_geo_area from public.srv_server where srv_tx_address = ?";
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = createPreparedStatement(sql);
            ps.setObject(1, serverAddress);
            rs = ps.executeQuery();
            List<Server> list = resultSetToObject(rs);
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

    /**
     * Consulta a lista de servidores que tem o id como pai.
     *
     * @param serverIdSuper
     * @return
     * @throws Exception
     */
    public List<Server> getBySuper(long serverIdSuper) throws Exception {
        String sql = "select srv_nr_id, srv_tx_name, srv_tx_address, srv_tx_status, srv_nr_idsuper, ST_AsText(srv_geo_area) as srv_geo_area from public.srv_server where srv_nr_idsuper = ?";
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = createPreparedStatement(sql);
            ps.setObject(1, serverIdSuper);
            rs = ps.executeQuery();
            List<Server> list = resultSetToObject(rs);
            return list;
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

    public Server getMyServerSuper(long serverId) throws Exception {
        String sql = "select srv_nr_id, srv_tx_name, srv_tx_address, srv_tx_status, srv_nr_idsuper, ST_AsText(srv_geo_area) as srv_geo_area from public.srv_server where srv_nr_id in (select srv_nr_idsuper from public.srv_server where srv_nr_id=?)";
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = createPreparedStatement(sql);
            ps.setObject(1, serverId);
            rs = ps.executeQuery();
            List<Server> list = resultSetToObject(rs);
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
    public List<Server> resultSetToObject(ResultSet rs) throws Exception {
        List<Server> dataList = new ArrayList<Server>();
        while (rs.next()) {
            Server server = new Server();
            server.setServerId(rs.getLong("srv_nr_id"));
            server.setSrv_nr_idsuper(rs.getLong("srv_nr_idsuper"));
            server.setServerName(rs.getString("srv_tx_name"));
            server.setServerAddress(rs.getString("srv_tx_address"));
            server.setServerStatus(rs.getString("srv_tx_status"));
            dataList.add(server);
        }
        return dataList;
    }

    public List<Server> resultSetToObjectPolygon(ResultSet rs) throws Exception {
        List<Server> dataList = new ArrayList<Server>();
        while (rs.next()) {
            Server server = new Server();
            server.setServerId(rs.getLong("srv_nr_id"));
            server.setSrv_nr_idsuper(rs.getLong("srv_nr_idsuper"));
            server.setServerName(rs.getString("srv_tx_name"));
            server.setServerAddress(rs.getString("srv_tx_address"));
            server.setServerStatus(rs.getString("srv_tx_status"));
            String geo = rs.getString("srv_geo_area");
            //PGobject pgObj = (PGobject) rs.getObject("srv_geo_area");
            System.out.println(geo);
            org.postgis.Polygon polygon = new org.postgis.Polygon(geo, false);
            Polygon poly = polygonPGToPolygonGT(gf, polygon);
            server.setPolygon(poly);
            dataList.add(server);
        }
        return dataList;
    }
}
