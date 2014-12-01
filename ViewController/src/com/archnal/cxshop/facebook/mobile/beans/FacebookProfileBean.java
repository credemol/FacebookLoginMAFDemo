package com.archnal.cxshop.facebook.mobile.beans;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;

public class FacebookProfileBean {
    private String id;      // facebook id
    private String email;
    private String firstName; // first_name
    private String lastName; // last_name
    private String gender;
    private String link;
    private String locale;
    private String name;
    private String middleName; // middle_name;
    private String timezone;
    private String username;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public FacebookProfileBean() {
        super();
    }


    public void setId(String id) {
        String oldId = this.id;
        this.id = id;
        propertyChangeSupport.firePropertyChange("id", oldId, id);
    }

    public String getId() {
        return id;
    }

    public void setEmail(String email) {
        String oldEmail = this.email;
        this.email = email;
        propertyChangeSupport.firePropertyChange("email", oldEmail, email);
    }

    public String getEmail() {
        return email;
    }

    public void setFirstName(String firstName) {
        String oldFirstName = this.firstName;
        this.firstName = firstName;
        propertyChangeSupport.firePropertyChange("firstName", oldFirstName, firstName);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        String oldLastName = this.lastName;
        this.lastName = lastName;
        propertyChangeSupport.firePropertyChange("lastName", oldLastName, lastName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setGender(String gender) {
        String oldGender = this.gender;
        this.gender = gender;
        propertyChangeSupport.firePropertyChange("gender", oldGender, gender);
    }

    public String getGender() {
        return gender;
    }

    public void setLink(String link) {
        String oldLink = this.link;
        this.link = link;
        propertyChangeSupport.firePropertyChange("link", oldLink, link);
    }

    public String getLink() {
        return link;
    }

    public void setLocale(String locale) {
        String oldLocale = this.locale;
        this.locale = locale;
        propertyChangeSupport.firePropertyChange("locale", oldLocale, locale);
    }

    public String getLocale() {
        return locale;
    }

    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        propertyChangeSupport.firePropertyChange("name", oldName, name);
    }

    public String getName() {
        return name;
    }

    public void setMiddleName(String middleName) {
        String oldMiddleName = this.middleName;
        this.middleName = middleName;
        propertyChangeSupport.firePropertyChange("middleName", oldMiddleName, middleName);
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setTimezone(String timezone) {
        String oldTimezone = this.timezone;
        this.timezone = timezone;
        propertyChangeSupport.firePropertyChange("timezone", oldTimezone, timezone);
    }

    public String getTimezone() {
        return timezone;
    }

    public void setUsername(String username) {
        String oldUsername = this.username;
        this.username = username;
        propertyChangeSupport.firePropertyChange("username", oldUsername, username);
    }

    public String getUsername() {
        return username;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
}
