package com.archnal.cxshop.facebook.application.util;

import com.archnal.cxshop.facebook.application.model.FacebookProfile;
import com.archnal.cxshop.facebook.application.rest.MeResponse;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;

public class FacebookProfileCopier {
    public FacebookProfileCopier() {
        super();
    }
/*
    public static void copyProfile(MeResponse me, FacebookProfile profile) {
        profile.setFirstName(me.getFirst_name());
        profile.setEmail(me.getEmail());
        profile.setLastName(me.getLast_name());
        profile.setMiddleName(me.getMiddle_name());
        profile.setUsername(me.getUsername());
        profile.setFacebookId(me.getId());
    }
*/   
    
    /*
     *     private String id;
    private String bio;
    private String birthday;
    private String email;
    private String first_name;
    private String gender;
    private String last_name;
    private String link;
    private String locale;
    private String middle_name;
    private String name;
    private String quotes;
    private int timezone;
    private String username;
     */
    public static void copyProfile(MeResponse me) {
        TraceLog.info(FacebookProfileCopier.class, "copyProfile", "START: " + me);
        AdfmfJavaUtilities.setELValue("#{applicationScope.facebook_id}", me.getId());
        AdfmfJavaUtilities.setELValue("#{applicationScope.facebook_firstName}", me.getFirst_name());
        AdfmfJavaUtilities.setELValue("#{applicationScope.facebook_email}", me.getEmail());
        AdfmfJavaUtilities.setELValue("#{applicationScope.facebook_lastName}", me.getLast_name());
        AdfmfJavaUtilities.setELValue("#{applicationScope.facebook_middleName}", me.getMiddle_name());
        AdfmfJavaUtilities.setELValue("#{applicationScope.facebook_username}", me.getUsername());
        AdfmfJavaUtilities.setELValue("#{applicationScope.facebook_birthday}", me.getBirthday());
        AdfmfJavaUtilities.setELValue("#{applicationScope.facebook_gender}", me.getGender());
        AdfmfJavaUtilities.setELValue("#{applicationScope.facebook_link}", me.getLink());
        AdfmfJavaUtilities.setELValue("#{applicationScope.facebook_quotes}", me.getQuotes());
        

        AdfmfJavaUtilities.setELValue("#{applicationScope.facebook_timezone}", String.valueOf(me.getTimezone()));
        TraceLog.info(FacebookProfileCopier.class, "copyProfile", "END");

/*
        AdfmfJavaUtilities.setELValue("#{applicationScope.FacebookProfileBean.firstName}", me.getFirst_name());
        AdfmfJavaUtilities.setELValue("#{applicationScope.FacebookProfileBean.email}", me.getEmail());
        AdfmfJavaUtilities.setELValue("#{applicationScope.FacebookProfileBean.lastName}", me.getLast_name());
        AdfmfJavaUtilities.setELValue("#{applicationScope.FacebookProfileBean.middleName}", me.getMiddle_name());
        AdfmfJavaUtilities.setELValue("#{applicationScope.FacebookProfileBean.username}", me.getUsername());
        AdfmfJavaUtilities.setELValue("#{applicationScope.FacebookProfileBean.id}", me.getId());
*/
    }
    
    public static void clearProfile() {
        TraceLog.info(FacebookProfileCopier.class, "clearProfile", "START: ");
        AdfmfJavaUtilities.setELValue("#{applicationScope.facebook_id}", "");
        AdfmfJavaUtilities.setELValue("#{applicationScope.facebook_firstName}", "");
        AdfmfJavaUtilities.setELValue("#{applicationScope.facebook_email}", "");
        AdfmfJavaUtilities.setELValue("#{applicationScope.facebook_lastName}", "");
        AdfmfJavaUtilities.setELValue("#{applicationScope.facebook_middleName}", "");
        AdfmfJavaUtilities.setELValue("#{applicationScope.facebook_username}", "");
        AdfmfJavaUtilities.setELValue("#{applicationScope.facebook_id}", "");
        AdfmfJavaUtilities.setELValue("#{applicationScope.facebook_birthday}", "");
        AdfmfJavaUtilities.setELValue("#{applicationScope.facebook_gender}", "");
        AdfmfJavaUtilities.setELValue("#{applicationScope.facebook_link}", "");
        AdfmfJavaUtilities.setELValue("#{applicationScope.facebook_quotes}", "");
        AdfmfJavaUtilities.setELValue("#{applicationScope.facebook_timezone}", "");
        
        TraceLog.info(FacebookProfileCopier.class, "clearProfile", "END");
        
    }
    
}
