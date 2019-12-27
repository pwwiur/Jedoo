# Jedoo
Jedoo is a java jdbc wrapper for database query execution to accelerate development processes inspired from [Medoo](https://github.com/catfan/Medoo) project.

## Requirement
Jedoo is using [HikariCP v.3.4.1](https://github.com/brettwooldridge/HikariCP) database connection pooling library. You should add this library to your project.

## Getting started
To get started with Jedoo, Clone `com.pwwiur.util.database` package to your project and set database connection information at `Jedoo.java` file:
```java
    private final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private final String HOST = "jdbc:mysql://127.0.0.1";
    private final String PORT = "3306";
    private final String DATABASE = "database_name";
    private final String USER = "username";
    private final String PASSWORD = "password";
```

Now you can add database singleton to your java classes by using `import static`:
```java
    import static com.pwwiur.util.database.Jedoo.database;
```
## Documentation
Using Jedoo is very easy. A test file has been added to source code to know how is using Jedoo possible.It has plenty of functions that helps developerts to query fast. Here is some of those functions:

### Select
`ResultSetHandler` is a `AutoClosable` class that should be closed at the end of process, try-finally syntax of java is used in this example to close it after execution to avoid memory leaking problems.

In this example a user from database is selected:
```java 
    try(ResultSetHandler rsh = database.select("users", "*", "username = ?", "pwwiur")) {
        String name = rsh.first().resultset.getString("name");
        System.out.println(name);
    }
```
In this example all users are selected:
```java 
    try(ResultSetHandler rsh = database.all("users")) {
        while(rsh.next()) {
            System.out.println("User ID:" + rsh.getLong("id"));
            System.out.println("User Name:" + rsh.getLong("name"));
        }
    }
```
### Insert
There is plenty of functions in jedoo to insert data to database, but the most common one is:
```java
    long id = database.insert("users", new Object[][]{
        {"name", "Amir"},
        {"username", "pwwuir"}
    });
```
### Update
There is also some functions for updates exection in Jedoo, One of them is:
```java
    database.update("users",  new Object[][]{
        {"name", "Amir Forsati"},
        {"active", 0}
    }, userId);
```
Also you can use setter functions to update one column of \[a row of\] a table. For example:
```java
    database.setString("users", "email", "pwwiur@yahoo.com", userId);
```
### Others

There so many usefull methods to accelerate development process of project. Their list are mentiod below:

 - **query**: To execute custom queries.
 - **select**: Selects data from database.
 - **all**: To get all records in a table.
 - **get**: To get a record by its ID.
 - **getString**: To get a string typed column of a record by its ID.
 - **getLong**: To get a long typed column of a record by its ID.
 - **getInt**: To get a integer typed column of a record by its ID.
 - **getBytes**: To get a byte array typed column of a record by its ID.
 - **count**: To count records of a table \[by some conditions\].
 - **max**: To calculate maximum number of a column of records of a table \[by some conditions\].
 - **min**: To calculate minimum number of a column of records of a table \[by some conditions\].
 - **avg**: To calculate avarage number of a column of records of a table \[by some conditions\].
 - **sum**: To calculate sum of a column of records of a table \[by some conditions\].
 - **has**: To check if record exist in database table by some conditions. This function also can be used to check if a table exists in database.
 - **modify**: To execute custom updates to database.
 - **insert**: To insert new data to database tables.
 - **delete**: To delete some data from a database table.
 - **update**: To update record\[s\] in a table.
 - **setString**: To set value of a string typed column of a record by its ID.
 - **setLong**: To set value of a long typed column of a record by its ID.
 - **setInt**: To set value of a int typed column of a record by its ID.
 - **setBytes**: To set value of a byte array typed column of a record by its ID.
