package com.pwwiur.util.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.SQLException;


public class Jedoo {
    public final static Jedoo database = new Jedoo();

    private HikariDataSource datasource;
    private final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private final String HOST = "jdbc:mysql://127.0.0.1";
    private final String PORT = "3306";
    private final String DATABASE = "betwin90_melo";
    private final String USER = "pwwiur";
    private final String PASSWORD = "F9U8NSzfRX8cYdWv";

    public Jedoo(){
        init();
    }
    private void init(){
        try {
            Class.forName(DRIVER);

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(HOST + ":" + PORT + "/" + DATABASE + "?autoReconnect=true&tcpKeepAlive=true&useUnicode=true&characterEncoding=utf-8");
            config.setUsername(USER);
            config.setPassword(PASSWORD);
            datasource = new HikariDataSource( config );
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ResultSetHandler getResultSetHandler() throws SQLException {
        return new ResultSetHandler(datasource.getConnection());
    }

    public int modify(String query, Object[] shits) throws SQLException {
        try(ResultSetHandler resultSetHandler = getResultSetHandler()) {
            resultSetHandler.prepareStatement(query, new String[]{"id"});
            for (int i = 0; i < shits.length; i++) {
                resultSetHandler.statement.setObject(i + 1, shits[i]);
            }
            return resultSetHandler.executeUpdate();
        }
    }
    public int modify(String query, Object shit) throws SQLException {
        try(ResultSetHandler resultSetHandler = getResultSetHandler()) {
            resultSetHandler.prepareStatement(query, new String[]{"id"});
            resultSetHandler.statement.setObject(1, shit);
            return resultSetHandler.executeUpdate();
        }
    }
    public int modify(String query) throws SQLException {
        try(ResultSetHandler resultSetHandler = getResultSetHandler()) {
            resultSetHandler.prepareStatement(query, new String[]{"id"});
            return resultSetHandler.executeUpdate();
        }
    }
    public int delete(String table, String where, Object[] shits) throws SQLException {
        return modify("DELETE FROM " + table + (where.isEmpty() ? "" : " WHERE " + where), shits);
    }
    public int delete(String table, String where, Object shit) throws SQLException {
        return delete(table, where, new Object[]{shit});
    }
    public long insert(String table, String[] order, Object[] shits) throws SQLException {
        try(ResultSetHandler resultSetHandler = getResultSetHandler()) {
            String marks = new String(new char[shits.length]).replace("\0", "?, ");
            String query = "INSERT INTO " + table + "(" + String.join(", ", order) + ") VALUES(" + marks.substring(0, marks.length() - 2) + ");";

            resultSetHandler.prepareStatement(query, new String[]{"id"});

            for (int i = 0; i < shits.length; i++) {
                resultSetHandler.statement.setObject(i + 1, shits[i]);
            }

            if(resultSetHandler.executeUpdate() == 0) {
                throw new SQLException("Insert failed.");
            }

            resultSetHandler.getGeneratedKeys();
            if(resultSetHandler.resultset.next()){
                return resultSetHandler.resultset.getLong(1);
            }
            else {
                return 1;
            }
        }
    }
    public long insert(String table, String order, Object shit) throws SQLException {
        return insert(table, new String[] {order}, new Object[] {shit});
    }
    public long insert(String table, Object[][] items) throws SQLException {
        String[] order = new String[items.length];
        Object[] shits = new Object[items.length];
        for(int i = 0; i < items.length; i++) {
            order[i] = (String) items[i][0];
            shits[i] = items[i][1];
        }
        return insert(table, order, shits);
    }
    public int update(String table, String[] columns, Object[] columnsShits, String where, Object[] whereShits) throws SQLException {
        try(ResultSetHandler resultSetHandler = getResultSetHandler()) {
            String query = "UPDATE " + table + " SET " + String.join(" = ?, ", columns) + " = ? " + (where.isEmpty() ? "" : " WHERE " + where);
            resultSetHandler.prepareStatement(query);

            int i = 0;
            for (; i < columnsShits.length; i++) {
                resultSetHandler.statement.setObject(i + 1, columnsShits[i]);
            }
            for (; i < columnsShits.length + whereShits.length; i++) {
                resultSetHandler.statement.setObject(i + 1, whereShits[i]);
            }
            return resultSetHandler.executeUpdate();
        }
    }
    public int update(String table, String[] columns, Object[] columnsShits, String where, Object shit) throws SQLException {
        return update(table, columns, columnsShits, where, new Object[]{shit});
    }
    public int update(String table, String[] columns, Object[] columnsShits, long id) throws SQLException {
        return update(table, columns, columnsShits, "id = ?", id);
    }
    public int update(String table, String[] columns, Object[] columnsShits, String where) throws SQLException {
        return update(table, columns, columnsShits, where, new Object[0]);
    }
    public int update(String table, String[] columns, Object[] columnsShits) throws SQLException {
        return update(table, columns, columnsShits, "", new Object[0]);
    }
    public int update(String table, String column, Object columnShit, String where, Object[] whereShits) throws SQLException {
        return update(table, new String[]{column}, new Object[]{columnShit}, where, whereShits);
    }
    public int update(String table, String column, Object columnShit, String where, Object whereShit) throws SQLException {
        return update(table, new String[]{column}, new Object[]{columnShit}, where, new Object[] {whereShit});
    }
    public int update(String table, String column, Object columnShit, long id) throws SQLException {
        return update(table, new String[]{column}, new Object[]{columnShit}, id);
    }
    public int update(String table, String column, Object columnShit, String where) throws SQLException {
        return update(table, new String[]{column}, new Object[]{columnShit}, where);
    }
    public int update(String table, String column, Object columnShit) throws SQLException {
        return update(table, new String[]{column}, new Object[]{columnShit});
    }
    public int update(String table, Object[][] items, String where, Object[] whereShits) throws SQLException {
        String[] column = new String[items.length];
        Object[] columnShits = new Object[items.length];
        for(int i = 0; i < items.length; i++) {
            column[i] = (String) items[i][0];
            columnShits[i] = items[i][1];
        }

        return update(table, column, columnShits, where, whereShits);
    }
    public int update(String table, Object[][] items, String where, Object whereShits) throws SQLException {
        return update(table, items, where, new Object[]{whereShits});
    }
    public int update(String table, Object[][] items, long id) throws SQLException {
        return update(table, items, "id = ?", id);
    }
    public int update(String table, Object[][] items, String where) throws SQLException {
        return update(table, items, where, new Object[0]);
    }
    public int update(String table, Object[][] items) throws SQLException {
        return update(table, items, "", new Object[0]);
    }
    public int setLong(String table, String column, long value, long id) throws SQLException {
        try(ResultSetHandler resultSetHandler = getResultSetHandler()) {
            String query = "UPDATE " + table + " SET " + column + " = ? WHERE id = " + id;
            resultSetHandler.prepareStatement(query);
            resultSetHandler.statement.setLong(1, value);
            return resultSetHandler.executeUpdate();
        }
    }
    public long getLong(String table, String column, long id) throws SQLException {
        try(ResultSetHandler resultSetHandler = query("SELECT " + column + " FROM " + table + " WHERE id = " + id)) {
            return resultSetHandler.first().getResultSet().getLong(column);
        }
    }
    public int setInt(String table, String column, int value, long id) throws SQLException {
        try(ResultSetHandler resultSetHandler = getResultSetHandler()) {
            String query = "UPDATE " + table + " SET " + column + " = ? WHERE id = " + id;
            resultSetHandler.prepareStatement(query);
            resultSetHandler.statement.setInt(1, value);
            return resultSetHandler.executeUpdate();
        }
    }
    public int getInt(String table, String column, long id) throws SQLException {
        try(ResultSetHandler resultSetHandler = query("SELECT " + column + " FROM " + table + " WHERE id = " + id)) {
            return resultSetHandler.first().getResultSet().getInt(column);
        }
    }
    public int setString(String table, String column, String value, long id) throws SQLException {
        try(ResultSetHandler resultSetHandler = getResultSetHandler()) {
            String query = "UPDATE " + table + " SET " + column + " = ? WHERE id = " + id;

            resultSetHandler.prepareStatement(query);
            resultSetHandler.statement.setString(1, value);
            return resultSetHandler.executeUpdate();
        }
    }
    public String getString(String table, String column, long id) throws SQLException {
        try(ResultSetHandler resultSetHandler = query("SELECT " + column + " FROM " + table + " WHERE id = " + id)) {
            return resultSetHandler.first().getResultSet().getString(column);
        }
    }
    public int setBytes(String table, String column, byte[] bytes, long id) throws SQLException {
        try(ResultSetHandler resultSetHandler = getResultSetHandler()) {
            String query = "UPDATE " + table + " SET " + column + " = ? WHERE id = " + id;

            resultSetHandler.prepareStatement(query);
            resultSetHandler.statement.setBytes(1, bytes);
            return resultSetHandler.executeUpdate();
        }
    }
    public byte[] getBytes(String table, String column, long id) throws SQLException {
        try(ResultSetHandler resultSetHandler = query("SELECT " + column + " FROM " + table + " WHERE id = " + id)) {
            return resultSetHandler.first().getResultSet().getBytes(column);
        }
    }
    public ResultSetHandler query(String query, Object[] shits) throws SQLException {
        ResultSetHandler resultSetHandler = getResultSetHandler();
        resultSetHandler.prepareStatement(query);
        System.out.println(query);
        for (int i = 0; i < shits.length; i++) {
            resultSetHandler.statement.setObject(i + 1, shits[i]);
        }
        resultSetHandler.executeQuery();

        return resultSetHandler;
    }
    public ResultSetHandler query(String query, Object shit) throws SQLException {
        return query(query, new Object[]{shit});
    }
    public ResultSetHandler query(String query) throws SQLException {
        return query(query, new Object[0]);
    }
    public ResultSetHandler select(String table, String columns, String where, Object[] shits) throws SQLException {
        return query("SELECT " + columns + " FROM " + table + (where.isEmpty() ? "" : " WHERE " + where), shits);
    }
    public ResultSetHandler select(String table, String columns, String where, Object shit) throws SQLException {
        return select(table, columns, where, new Object[] {shit});
    }
    public ResultSetHandler select(String table, String columns, String where) throws SQLException {
        return select(table, columns, where, new Object[0]);
    }
    public ResultSetHandler select(String table, String columns) throws SQLException {
        return select(table, columns, "");
    }
    public ResultSetHandler select(String table) throws SQLException {
        return select(table, "*", "");
    }
    public ResultSetHandler all(String table) throws SQLException {
        return select(table);
    }
    public ResultSetHandler get(String table, long id) throws SQLException{
        return select(table, "*", "id = " + id).first();
    }
    public int count(String table, String where, Object[] shits) throws SQLException {
        try(ResultSetHandler resultSetHandler = select(table, "COUNT(*) as count", where, shits)) {
            return resultSetHandler.first().getResultSet().getInt("count");
        }
    }
    public int count(String table, String where, Object shit) throws SQLException {
        return count(table, where, new Object[]{shit});
    }
    public int count(String table, String where) throws SQLException {
        return count(table, where, new Object[0]);
    }
    public int count(String table) throws SQLException {
        return count(table, "");
    }
    public int max(String table, String column, String where, Object[] shits) throws SQLException {
        try(ResultSetHandler resultSetHandler = select(table, "Max(" + column + ") as max", where, shits)) {
            return resultSetHandler.first().getResultSet().getInt("max");
        }
    }
    public int max(String table, String column, String where, Object shit) throws SQLException {
        return max(table, column, where, new Object[]{shit});
    }
    public int max(String table, String column, String where) throws SQLException {
        return max(table, column, where, new Object[0]);
    }
    public int max(String table, String column) throws SQLException {
        return max(table, column, "");
    }
    public int min(String table, String column, String where, Object[] shits) throws SQLException {
        try(ResultSetHandler resultSetHandler = select(table, "Min(" + column + ") as max", where, shits)) {
            return resultSetHandler.first().getResultSet().getInt("max");
        }
    }
    public int min(String table, String column, String where, Object shit) throws SQLException {
        return min(table, column, where, new Object[]{shit});
    }
    public int min(String table, String column, String where) throws SQLException {
        return min(table, column, where, new Object[0]);
    }
    public int min(String table, String column) throws SQLException {
        return min(table, column, "");
    }
    public int avg(String table, String column, String where, Object[] shits) throws SQLException {
        try(ResultSetHandler resultSetHandler = select(table, "AVG(" + column + ") as max", where, shits)) {
            return resultSetHandler.first().getResultSet().getInt("max");
        }
    }
    public int avg(String table, String column, String where, Object shit) throws SQLException {
        return avg(table, column, where, new Object[]{shit});
    }
    public int avg(String table, String column, String where) throws SQLException {
        return avg(table, column, where);
    }
    public int avg(String table, String column) throws SQLException {
        return avg(table, column, "");
    }
    public int sum(String table, String column, String where, Object[] shits) throws SQLException {
        try(ResultSetHandler resultSetHandler = select(table, "SUM(" + column + ") as sum", where, shits)) {
            return resultSetHandler.first().getResultSet().getInt("sum");
        }
    }
    public int sum(String table, String column, String where, Object shit) throws SQLException {
        return sum(table, column, where, new Object[]{shit});
    }
    public int sum(String table, String column, String where) throws SQLException {
        return sum(table, column, where, new Object[0]);
    }
    public int sum(String table, String column) throws SQLException {
        return sum(table, column, "");
    }
    public boolean has(String table, String where, Object[] shits) throws SQLException {
        try(ResultSetHandler resultSetHandler = query("SELECT EXISTS (SELECT 1 FROM " + table + (where.isEmpty() ? "" : " WHERE " + where) + ") as has", shits)) {
            return resultSetHandler.first().getResultSet().getInt("has") == 1;
        }
    }
    public boolean has(String table, String where, Object shit) throws SQLException {
        return has(table, where, new Object[] {shit});
    }
    public boolean has(String table, String where) throws SQLException {
        return has(table, where, new Object[0]);
    }
    public boolean has(String table,  long id) throws SQLException {
        return has(table, "id = " + id);
    }
    public boolean has(String table) throws SQLException {
        try(ResultSetHandler resultSetHandler = query("SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'kok' AND table_name = '" + table + "') as has")) {
            return resultSetHandler.first().getResultSet().getInt("has") == 1;
        }
    }

}
