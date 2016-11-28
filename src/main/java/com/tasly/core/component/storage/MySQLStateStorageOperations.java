package com.tasly.core.component.storage;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by dulei on 11/27/16.
 * Email:dreamfly@126.com
 * Site:http://lrwinx.github.io/
 * Github:https://github.com/lrwinx
 * Author:Lrwin
 * Description:MYSQL存储方式
 */
public class MySQLStateStorageOperations implements StateStorageOperations{

    DataSource dataSource ;
    public MySQLStateStorageOperations(DataSource dataSource)  {
        this.dataSource = dataSource;
        try {
            initDDL();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    final String DDL="create table if not exists ddripping_idempotency(id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,\n" +
            "                the_key VARCHAR(255) NOT NULL,\n" +
            "                out_time INT NOT NULL,\n" +
            "                create_time DATETIME NOT NULL,\n" +
            "                update_time DATETIME NOT NULL,\n" +
            "                value LONGTEXT NOT NULL);";

    String SELECT_BY_KEY_SQL = "select * from ddripping_idempotency where the_key = ?";
    String DELETE_BY_KEY_SQL = "delete from ddripping_idempotency where the_key = ?";
    String INSERT_SQL = "insert into ddripping_idempotency(the_key,out_time,create_time,update_time,value) values(?,?,?,?,?)";
    String UPDATE_BY_KEY_SQL = "update  ddripping_idempotency set value=? where the_key = ?";
    public void initDDL() throws Exception{
        if(null == dataSource){
            throw new RuntimeException("dataSource is require!");
        }
        Connection connection = dataSource.getConnection();
        try {
            connection.createStatement().executeUpdate(DDL);
        }catch (Exception e){
            throw e;
        }finally {
            if(null != connection){
                connection.close();
            }
        }
    }

    @Override
    public boolean setKeyIfAbsence(String key, long timeout) {

        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_KEY_SQL);
            preparedStatement.setString(new Integer(1),key);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {//如果存在
                return false;
            }

            if(timeout != STATE_STORAGE_TIMEOUT.NEVER_TIME_OUT){
//                map.put(key,"仍在操作");
                Date now = new Date();
                PreparedStatement insertPreparedStatement = connection.prepareStatement(INSERT_SQL);
                insertPreparedStatement.setString(new Integer(1),key);
                insertPreparedStatement.setLong(new Integer(2), timeout);
                insertPreparedStatement.setTimestamp(new Integer(3), new java.sql.Timestamp(new Date().getTime()));
                insertPreparedStatement.setTimestamp(new Integer(4), new java.sql.Timestamp(new Date().getTime()));
                insertPreparedStatement.setString(new Integer(5), "仍在操作");
                insertPreparedStatement.executeUpdate();


                //TODO 这个定时需要再考虑
//                Timer timer= new Timer();
//                final Connection finalConnection = connection;
//                TimerTask task  = new TimerTask(){    //创建一个新的计时器任务。
//                    @Override
//                    public void run() {
//                        try {
//                            PreparedStatement delPreparedStatement = finalConnection.prepareStatement(DELETE_BY_KEY_SQL);
//                            delPreparedStatement.setString(new Integer(1),key);
//                            delPreparedStatement.execute();
//                        } catch (SQLException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                };
//                timer.schedule(task, timeout);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(null != connection){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    public void resetValue(String key, Object value) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BY_KEY_SQL);
            //TODO value需要序列化
            preparedStatement.setString(new Integer(1), value.toString());
            preparedStatement.setString(new Integer(2), key);
            preparedStatement.execute();
        }catch (Exception e){

        }finally {
            if(null != connection){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void removeKey(String key) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement delPreparedStatement = connection.prepareStatement(DELETE_BY_KEY_SQL);
            delPreparedStatement.setString(new Integer(1),key);
            delPreparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(null != connection){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Object getKey(String key) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_KEY_SQL);
            preparedStatement.setString(new Integer(1), key);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {//如果存在
                return resultSet.getString("value");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(null != connection){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


}
