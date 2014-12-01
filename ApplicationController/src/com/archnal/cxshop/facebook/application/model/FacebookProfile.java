package com.archnal.cxshop.facebook.application.model;

import java.sql.Timestamp;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;

public class FacebookProfile {
    private int id;
    private String accessToken;
    private Timestamp expirationDate;

    public FacebookProfile() {
        super();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Timestamp getExpirationDate() {
        return expirationDate;
    }
    
    public String toString() {
        return "id: " + id + ", accessToken: " + accessToken + ", expirationDate: " + expirationDate;
    }
}
