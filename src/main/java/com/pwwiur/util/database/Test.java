package com.pwwiur.util;

import java.sql.SQLException;
import com.pwwiur.util.database.ResultSetHandler;
import static com.pwwiur.util.database.Jedoo.database;

public class Test {
    public static void main(String[] args) {
        try {
            if(database.count("users") < 100) {
                long id = database.insert("users", new Object[][]{
                        {"name", "Amir"},
                        {"username", "pwwuir"}
                });
                database.setString("users", "name", "Amir Forsati", id);


                try(ResultSetHandler rsh = database.select("users", "*", "username = ?", "pwwiur")) {
                    String name = rsh.first().resultset.getString("name");
                    System.out.println(name);
                }
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
