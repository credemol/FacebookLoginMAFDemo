package com.archnal.cxshop.facebook.application.dao;

import com.archnal.cxshop.facebook.application.DBConnectionFactory;
import com.archnal.cxshop.facebook.application.model.FacebookProfile;

import com.archnal.cxshop.facebook.application.util.TraceLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class FacebookProfileRepository {
    public FacebookProfileRepository() {
        super();
    }
    
    public FacebookProfile getFacebookProfile() {
        TraceLog.info(getClass(), "getFacebookProfile", "START");
        FacebookProfile profile = new FacebookProfile();
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        final String sql = "SELECT ID, ACCESS_TOKEN, EXPIRATION_DATE FROM FACEBOOK_LOGIN";
        
        try {
            conn = DBConnectionFactory.getConnection();
            
            stmt = conn.prepareStatement(sql);
            
            rs = stmt.executeQuery();
            
            if(rs.next()) {
                profile.setId(rs.getInt("ID"));
                profile.setAccessToken(rs.getString("ACCESS_TOKEN"));
                profile.setExpirationDate(rs.getTimestamp("EXPIRATION_DATE"));
                return profile;
            } else {
                return null;
            }

        } catch(SQLException e) {
            TraceLog.severe(getClass(), "getFacebookProfile", "ERROR: " + e.getMessage());
            return null;
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch(Exception e) {}
            }
            if(stmt != null) {
                try {
                    stmt.close();
                } catch(Exception e) {}
            }
            TraceLog.info(getClass(), "getFacebookProfile", "END");

        }
        
    }
    
    public boolean createProfile(String accessToken, Timestamp expirationDate) {
        TraceLog.info(getClass(), "createProfile", "START");
        FacebookProfile profile = new FacebookProfile();
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        final String sql = "INSERT INTO FACEBOOK_LOGIN (ACCESS_TOKEN, EXPIRATION_DATE) VALUES (?, ?)";
        
        TraceLog.info(getClass(), "createProfile", "sql: " + sql);
        try {
            conn = DBConnectionFactory.getConnection();
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, accessToken);
            stmt.setTimestamp(2, expirationDate);
            
            int inserted = stmt.executeUpdate();
            return (inserted == 1);
        } catch(SQLException e) {
            TraceLog.severe(getClass(), "createProfile", "ERROR: " + e.getMessage());
            return false;
        } finally {
            if(stmt != null) {
                try {
                    stmt.close();
                } catch(Exception e) {}
            }
            TraceLog.info(getClass(), "createProfile", "END");
        }
        
    }
    
    public boolean clearProfile() {
        TraceLog.info(getClass(), "clearProfile", "START");
        FacebookProfile profile = new FacebookProfile();
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        final String sql = "DELETE FROM FACEBOOK_LOGIN";
        
        try {
            conn = DBConnectionFactory.getConnection();
            
            stmt = conn.prepareStatement(sql);
            
            int deleted = stmt.executeUpdate();
            return (deleted == 1);
        } catch(SQLException e) {
            TraceLog.severe(getClass(), "clearProfile", "ERROR: " + e.getMessage());
            return false;
        } finally {
            if(stmt != null) {
                try {
                    stmt.close();
                } catch(Exception e) {}
            }
            TraceLog.info(getClass(), "clearProfile", "END");
        }
        
    }
}
