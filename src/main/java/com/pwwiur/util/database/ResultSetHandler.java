package com.pwwiur.util.database;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ResultSetHandler implements AutoCloseable {
    public Connection connection;
    public PreparedStatement statement;
    public ResultSet resultset;

    ResultSetHandler(Connection connection){
        this.connection = connection;
    }
    public void prepareStatement(String query) throws SQLException {
        statement = connection.prepareStatement(query);
    }
    public void prepareStatement(String query, String[] strings) throws SQLException {
        statement = connection.prepareStatement(query, strings);
    }
    public void executeQuery() throws SQLException {
        resultset = statement.executeQuery();
    }
    public void getGeneratedKeys() throws SQLException {
        resultset = statement.getGeneratedKeys();
    }
    public int executeUpdate() throws SQLException {
        return statement.executeUpdate();
    }
    public boolean next() throws SQLException {
        return resultset.next();
    }
    public ResultSetHandler navigate(int index) throws SQLException {
        if(resultset == null) {
            return null;
        }
        resultset.absolute(index);
        return this;
    }
    public ResultSetHandler first() throws SQLException {
        return navigate(1);
    }

    public void close() throws SQLException {
        if(resultset != null) resultset.close();
        if(statement != null) statement.close();
        if(connection != null) connection.close();
    }

    public JSONArray toJSON(){
        try {
            if(resultset == null) {
                return null;
            }
            JSONArray json = new JSONArray();
            ResultSetMetaData rsmd =  resultset.getMetaData();
            while(next()) {
                int columnsCount = rsmd.getColumnCount();
                JSONObject obj = new JSONObject();
                for (int i = 1; i <= columnsCount; i++) {
                    String column_name = rsmd.getColumnName(i);
                    obj.put(column_name, resultset.getObject(column_name));
                }
                json.put(obj);
            }

            return json;
        }
        catch (Throwable e) {
            return null;
        }

    }

    @Override
    public String toString() {
        return toJSON().toString();
    }
}
