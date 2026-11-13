package com.sliit.bankapp.util;

import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnectionManager {
    private final String url, user, pass;

    public DBConnectionManager(ServletContext ctx) {
        url  = ctx.getInitParameter("jdbc.url");
        user = ctx.getInitParameter("jdbc.user");
        pass = ctx.getInitParameter("jdbc.pass");
        try { Class.forName("com.mysql.cj.jdbc.Driver"); }
        catch (ClassNotFoundException ex) { throw new RuntimeException(ex); }
    }
    public Connection getConnection() throws Exception {
        return DriverManager.getConnection(url, user, pass);
    }
}
