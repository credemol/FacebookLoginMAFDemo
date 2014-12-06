package com.archnal.cxshop.facebook.application;

import com.archnal.cxshop.facebook.application.dao.FacebookProfileRepository;
import com.archnal.cxshop.facebook.application.model.FacebookProfile;
import com.archnal.cxshop.facebook.application.rest.FacebookRestClient;
import com.archnal.cxshop.facebook.application.rest.MeResponse;
import com.archnal.cxshop.facebook.application.util.FacebookProfileCopier;

import com.archnal.cxshop.facebook.application.util.TraceLog;

import com.sun.util.logging.Level;

import java.sql.Timestamp;

import java.util.Map;
import java.util.StringTokenizer;

import javax.el.ValueExpression;

import oracle.adfmf.framework.api.AdfmfContainerUtilities;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.framework.event.Event;
import oracle.adfmf.framework.event.EventListener;
import oracle.adfmf.framework.exception.AdfException;
import oracle.adfmf.util.Utility;
import oracle.adfmf.util.logging.Trace;

public class OpenURLEventListener implements EventListener {
    private FacebookProfileRepository facebookProfileRepository = new FacebookProfileRepository();
    private FacebookRestClient facebookRestClient = new FacebookRestClient();
    
    public OpenURLEventListener() {
        super();
    }
    
    public void onMessage(Event event) {
        // The URL must be of the format:   
        // access_token, expires_in..
        String url = event.getPayload();

        TraceLog.info(getClass(), "onMessage", "URL Scheme:" + url);

        
        int index = -1;
        if((index = url.indexOf("fb_callback")) > -1) {
            String fb_url = url.substring(index + "fb_callback/".length());
       
            processOauthCallback(fb_url);
            //AdfmfJavaUtilities.setELValue("#{applicationScope.facebookProfile}", profile);
        } else if((index = url.indexOf("fb_logout")) > -1) {
            String fb_url = url.substring(index + "fb_logout/".length());
            
            processLogout(fb_url);
        }
        
        //if index = url.lastIndexOf("?fb_callback=");
    }
    
    private void processOauthCallback(String fb_url) {
        //String queryString = fb_url.substring(fb_url.indexOf('#') + 1);
        String queryString = (fb_url.charAt(0) == '#' || fb_url.charAt(0) == '?') ? fb_url.substring(1) : fb_url;
        StringTokenizer tokenizer = new StringTokenizer(queryString, "&");
        
        
        while(tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            
            int i = token.indexOf('=');
            
            String key = "";
            String value = "";
            
            if(i > 0) {
                key = token.substring(0, i);
                value = token.substring(i + 1);
            } else {
                key = token;
            }
            
            TraceLog.info(getClass(), "processOauthCallback", "key: " + key + ", value: " + value) ;
            //ValueExpression val =
            //    AdfmfJavaUtilities.getValueExpression("#{applicationScope." + key + "}", Object.class);
            //val.setValue(AdfmfJavaUtilities.getAdfELContext(), value);                    
            AdfmfJavaUtilities.setELValue("#{applicationScope." + key + "}", value);
        }
        TraceLog.info(getClass(), "processOauthCallback", "Complete to parse URL");
        

        
        String accessToken = (String) AdfmfJavaUtilities.getELValue("#{applicationScope.access_token}");
        TraceLog.info(getClass(), "processOauthCallback", "accessToken: " + accessToken);

        if(accessToken == null || accessToken.trim().length() == 0) {
            AdfmfJavaUtilities.setELValue("#{applicationScope.autoLoginSuccess}", Boolean.FALSE);
            
            AdfmfContainerUtilities.resetFeature("com.archnal.cxshop.facebook.FacebookLogin", true);
            
            return;   
        }

        String expiresIn = (String) AdfmfJavaUtilities.getELValue("#{applicationScope.expires_in}");
        TraceLog.info(getClass(), "processOauthCallback", "expiresIn: " + expiresIn);
        
        FacebookProfile profile = new FacebookProfile();
        profile.setAccessToken( accessToken);
        
        
        if(expiresIn != null && expiresIn.trim().length() > 0) {
            int expiresInValue = Integer.parseInt(expiresIn);
            Timestamp expirationDate = new Timestamp(System.currentTimeMillis() + (1000 * (expiresInValue - 60))) ; // subtract 1 minute.
            TraceLog.info(getClass(), "processOauthCallback", "expirationDate: " + expirationDate);
            profile.setExpirationDate(expirationDate);
        }
        
        TraceLog.info(getClass(), "processOauthCallback", "profile: " + profile) ;
        facebookProfileRepository.createProfile(profile.getAccessToken(), profile.getExpirationDate());
        
        MeResponse me = facebookRestClient.executeFacebookMe(profile.getAccessToken());
        TraceLog.info(getClass(), "processOauthCallback", "me: " + me);
        
        FacebookProfileCopier.copyProfile(me);     
        
        //AdfmfJavaUtilities.setELValue("#{securityContext.userName}", me.getId());
        //AdfmfJavaUtilities.setELValue("#{securityContext.authenticated}", Boolean.TRUE);
        
        AdfmfJavaUtilities.setELValue("#{applicationScope.autoLoginSuccess}", Boolean.TRUE);
        
        AdfmfContainerUtilities.resetFeature("com.archnal.cxshop.facebook.FacebookLogin", true);
        
        
    }

    private void processLogout(String fb_url) {
        TraceLog.info(getClass(), "processLogout", "START - fb_url: " + fb_url);
        
        facebookProfileRepository.clearProfile();
        FacebookProfileCopier.clearProfile();
        AdfmfJavaUtilities.setELValue("#{applicationScope.access_token}", "");
        AdfmfJavaUtilities.setELValue("#{applicationScope.expires_in}", "");
        
        
        TraceLog.info(getClass(), "processLogout", "END ");
    }
    
    
    public void onError(AdfException adfException) {
        Trace.log(Utility.ApplicationLogger, Level.SEVERE, LifeCycleListenerImpl.class, "start:onError",
                  adfException.getMessage());
    }

    public void onOpen(String string) {
    }    
}
