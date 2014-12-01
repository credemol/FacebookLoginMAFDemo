package com.archnal.cxshop.facebook.mobile.listeners;

import com.archnal.cxshop.facebook.application.util.TraceLog;

import com.archnal.cxshop.facebook.mobile.beans.FacebookProfileBean;

import oracle.adfmf.feature.LifeCycleListener;
import oracle.adfmf.framework.api.AdfmfContainerUtilities;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;

public class FacebookLoginFeatureLifeCycleListener implements LifeCycleListener {
    public FacebookLoginFeatureLifeCycleListener() {
        super();
    }

    public void activate() {
        TraceLog.info(getClass(), "activate", "START");
        // TODO Implement this method
        String refreshFacebookProfile = 
            (String) AdfmfJavaUtilities.getELValue("#{applicationScope.refreshFacebookProfile}");

        TraceLog.info(getClass(), "activate", "refreshFacebookProfile: " + refreshFacebookProfile);    
        /*
        if("true".equals(refreshFacebookProfile)) {
            AdfmfContainerUtilities.invokeContainerJavaScriptFunction(
                AdfmfJavaUtilities.getFeatureName(),
                    "adf.mf.api.amx.doNavigation",
                    new Object[] { "goWelcome" });

        }
*/
        /*
        if("true".equals(refreshFacebookProfile)) {
            String facebookId = (String)AdfmfJavaUtilities.getELValue("#{applicationScope.facebook_id}");
            TraceLog.info(getClass(), "activate", "facebookId: " + facebookId);
            AdfmfJavaUtilities.setELValue("#{pageFlowScope.FacebookProfileBean.id}", facebookId);
            
            String username = (String)AdfmfJavaUtilities.getELValue("#{applicationScope.facebook_username}");
            TraceLog.info(getClass(), "activate", "username: " + username);
            AdfmfJavaUtilities.setELValue("#{pageFlowScope.FacebookProfileBean.username}", username);
            
            
            String email = (String)AdfmfJavaUtilities.getELValue("#{applicationScope.facebook_email}");
            TraceLog.info(getClass(), "activate", "email: " + email);
            AdfmfJavaUtilities.setELValue("#{pageFlowScope.FacebookProfileBean.email}", email);
            
            
            AdfmfJavaUtilities.setELValue("#{applicationScope.refreshFacebookProfile}", "false");
        }
*/
        TraceLog.info(getClass(), "activate", "END");
    }

    public void deactivate() {
        // TODO Implement this method
    }
}
