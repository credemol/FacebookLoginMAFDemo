package com.archnal.cxshop.facebook.application;

import java.sql.Connection;
import java.sql.SQLException;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;


public class DBConnectionFactory {
    public static final String DB_FILE = 
        AdfmfJavaUtilities.getDirectoryPathRoot(AdfmfJavaUtilities.ApplicationDirectory) + "/FB_MAF.db";
    
    public static final String JDBC_URL = "jdbc:sqlite:" + DB_FILE; 
    
    public DBConnectionFactory() {
        super();
    }
    
    protected static Connection conn = null;

    public static Connection getConnection() throws SQLException {
        if (conn == null) {
            try {
                // create a database connection
                //String Dir = AdfmfJavaUtilities.getDirectoryPathRoot(AdfmfJavaUtilities.ApplicationDirectory);
                //String connStr = "jdbc:sqlite:" + Dir + "/BGF.db";
                String connStr = DB_FILE;
                conn = new SQLite.JDBCDataSource(JDBC_URL).getConnection();
            } catch (SQLException e) {
                // if the error message is "out of memory",
                // it probably means no database file is found
                System.err.println(e.getMessage());
            }
        }
        return conn;
    }
    
    public static void shutdown() throws Exception {
        if(conn != null) {
            conn.close();
        }
    }
    
}