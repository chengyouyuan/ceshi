package com.winhxd.b2c.detection.datasource;



import com.winhxd.b2c.common.domain.detection.model.DbSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 采用ThreadLocal封装Connection
 * 只要线程是活动的，没有结束，ThreadLocal是可访问的，就可以访问本线程的connection
 *
 * @author louis
 *
 */
public class ConnectionManager {

    private static final String DIRVER_NAME = "com.mysql.jdbc.Driver";
    //使用ThreadLocal保存Connection变量
    private static ThreadLocal<Map<Long,Connection>> connectionHolder = new ThreadLocal<Map<Long,Connection>>();

    /**
     * 连接Connection
     *
     * @return
     */
   public static Connection getConnection(DbSource dbSource) {
        //ThreadLocal取得当前线程的connection
       Map<Long,Connection> connMap = connectionHolder.get();
        //如果ThreadLocal没有绑定相应的Connection，创建一个新的Connection，
        //并将其保存到本地线程变量中。
       Connection conn = null;
        if (connMap == null) {
            connMap = new HashMap<Long, Connection>();
            connectionHolder.set(connMap);
        }else{
            conn = connMap.get(dbSource.getId());
        }
        if(conn == null){
            try {
                Class.forName(DIRVER_NAME);
                conn = DriverManager.getConnection(dbSource.getUri(), dbSource.getUserName(), dbSource.getPassword());
                //将当前线程的Connection设置到ThreadLocal
                connMap.put(dbSource.getId(),conn);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                //throw new Exception("系统错误，请联系系统管理员");
            }catch (SQLException e) {
                e.printStackTrace();
                //throw new Exception("系统错误，请联系系统管理员");
            }
        }
        return conn;

    }

    /**
     * 关闭Connection，清除集合中的Connection
     */
    public static void closeConnection(DbSource dbSource) {
        //ThreadLocal取得当前线程的connection
        Map<Long,Connection> connMap = connectionHolder.get();
        if(connMap!=null){
            Connection conn = connMap.get(dbSource.getId());
            //当前线程的connection不为空时，关闭connection.
            if (conn != null) {
                try {
                    conn.close();
                    //connection关闭之后，要从ThreadLocal的集合中清除Connection
                    connMap.remove(dbSource.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}