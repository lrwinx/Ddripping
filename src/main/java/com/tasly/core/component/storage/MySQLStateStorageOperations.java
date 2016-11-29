package com.tasly.core.component.storage;

import com.tasly.core.exception.MySQLValueTimeOutException;

import javax.sql.DataSource;
import java.io.*;
import java.sql.*;
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
            "                value BLOB NOT NULL);";

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
                Date now = new Date();
                PreparedStatement insertPreparedStatement = connection.prepareStatement(INSERT_SQL);
                insertPreparedStatement.setString(new Integer(1),key);
                insertPreparedStatement.setLong(new Integer(2), timeout);
                insertPreparedStatement.setTimestamp(new Integer(3), new java.sql.Timestamp(now.getTime()));
                insertPreparedStatement.setTimestamp(new Integer(4), new java.sql.Timestamp(now.getTime()));
                insertPreparedStatement.setBytes(new Integer(5), converObject("仍在操作"));
                insertPreparedStatement.executeUpdate();

                //TODO 这个定时需要再考虑
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
            preparedStatement.setBytes(new Integer(1), converObject(value));
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
                byte[] bytes = resultSet.getBytes("value");
                Timestamp update_time = resultSet.getTimestamp("update_time");
                Long timeout = resultSet.getLong("out_time");
                Long expireTime = update_time.getTime() + timeout;
                if(new Date().getTime() - expireTime > 0 ){//过期了
                    this.removeKey(key);//删除数据
                    throw new MySQLValueTimeOutException();
                }
                return getObject(bytes);
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


    private byte[] converObject(Object obj){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(baos);
            out.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return baos.toByteArray();
    }

    private Object getObject(byte[] bytes){
        ByteArrayInputStream bais;
        ObjectInputStream in = null;
        try{
            bais = new ByteArrayInputStream(bytes);
            in = new ObjectInputStream(bais);

            return in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
