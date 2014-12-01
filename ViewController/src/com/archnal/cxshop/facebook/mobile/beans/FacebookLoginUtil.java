package com.archnal.cxshop.facebook.mobile.beans;

import com.archnal.cxshop.facebook.application.dao.FacebookProfileRepository;
import com.archnal.cxshop.facebook.application.model.FacebookProfile;
import com.archnal.cxshop.facebook.application.rest.FacebookRestClient;
import com.archnal.cxshop.facebook.application.rest.MeResponse;
import com.archnal.cxshop.facebook.application.util.FacebookProfileCopier;
import com.archnal.cxshop.facebook.application.util.TraceLog;

import java.sql.Timestamp;

import java.util.StringTokenizer;

import oracle.adfmf.framework.api.AdfmfContainerUtilities;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;

public class FacebookLoginUtil {
    private static FacebookProfileRepository facebookProfileRepository = new FacebookProfileRepository();
    private static FacebookRestClient facebookRestClient = new FacebookRestClient();
    
    public FacebookLoginUtil() {
        super();
    }
    
    public static void handleAccessToken(String url) {
        TraceLog.info(FacebookLoginUtil.class, "handleAccessToken","START - url: " + url);
        
        String queryString = url.substring(url.indexOf('#') + 1);
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
            
            TraceLog.info(FacebookLoginUtil.class, "handleAccessToken", "key: " + key + ", value: " + value) ;
            //ValueExpression val =
            //    AdfmfJavaUtilities.getValueExpression("#{applicationScope." + key + "}", Object.class);
            //val.setValue(AdfmfJavaUtilities.getAdfELContext(), value);                    
            AdfmfJavaUtilities.setELValue("#{applicationScope." + key + "}", value);
        }
        TraceLog.info(FacebookLoginUtil.class, "handleAccessToken", "Complete to parse URL");
        
        
        String accessToken = (String) AdfmfJavaUtilities.getELValue("#{applicationScope.access_token}");
        TraceLog.info(FacebookLoginUtil.class, "handleAccessToken", "accessToken: " + accessToken);

        String expiresIn = (String) AdfmfJavaUtilities.getELValue("#{applicationScope.expires_in}");
        TraceLog.info(FacebookLoginUtil.class, "handleAccessToken", "expiresIn: " + expiresIn);
        
        FacebookProfile profile = new FacebookProfile();
        profile.setAccessToken( accessToken);
        
        
        if(expiresIn != null && expiresIn.trim().length() > 0) {
            int expiresInValue = Integer.parseInt(expiresIn);
            Timestamp expirationDate = new Timestamp(System.currentTimeMillis() + (1000 * (expiresInValue - 60))) ; // subtract 1 minute.
            TraceLog.info(FacebookLoginUtil.class, "handleAccessToken", "expirationDate: " + expirationDate);
            profile.setExpirationDate(expirationDate);
        }
        
        TraceLog.info(FacebookLoginUtil.class, "handleAccessToken", "profile: " + profile) ;
        facebookProfileRepository.createProfile(profile.getAccessToken(), profile.getExpirationDate());
        
        MeResponse me = facebookRestClient.executeFacebookMe(profile.getAccessToken());
        TraceLog.info(FacebookLoginUtil.class, "handleAccessToken", "me: " + me);
        
        FacebookProfileCopier.copyProfile(me);     
        
        AdfmfContainerUtilities.invokeContainerJavaScriptFunction(
            AdfmfJavaUtilities.getFeatureName(),
                "adf.mf.api.amx.doNavigation",
                new Object[] { "goWelcome" });
        
        TraceLog.info(FacebookLoginUtil.class, "handleAccessToken","END");
    }
}
