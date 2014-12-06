package com.archnal.cxshop.facebook.mobile.beans;

import com.archnal.cxshop.facebook.application.dao.FacebookProfileRepository;
import com.archnal.cxshop.facebook.application.rest.FacebookRestClient;
import com.archnal.cxshop.facebook.application.util.FacebookProfileCopier;
import com.archnal.cxshop.facebook.application.util.TraceLog;

import oracle.adfmf.amx.event.ActionEvent;
import oracle.adfmf.framework.api.AdfmfContainerUtilities;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;

public class FacebookLoginBean {
    FacebookProfileRepository facebookProfileRepository = new FacebookProfileRepository();
    FacebookRestClient facebookRestClient = new FacebookRestClient();
    
    public FacebookLoginBean() {
    }

    public void facebookLoginClicked(ActionEvent actionEvent) {
        // Add event code here...
        TraceLog.info(getClass(), "facebookLoginClicked", "START");
        
        AdfmfContainerUtilities.invokeContainerJavaScriptFunction("com.archnal.cxshop.facebook.FacebookLogin", "login", new Object[] {});
        
        TraceLog.info(getClass(), "facebookLoginClicked", "END");
    }

    public void facebookLogoutClicked(ActionEvent actionEvent) {
        // Add event code here...
        TraceLog.info(getClass(), "facebookLogoutClicked", "START");
        
        String accessToken = (String) AdfmfJavaUtilities.getELValue("#{applicationScope.access_token}");
        String facebookId = (String) AdfmfJavaUtilities.getELValue("#{applicationScope.facebook_id}");
        TraceLog.info(getClass(), "facebookLogoutClicked", "facebookId: " + facebookId);
        boolean revoked = facebookRestClient.revokeLogin(facebookId, accessToken);
        //boolean revoked = true;
        
        TraceLog.info(getClass(), "facebookLogoutClicked", "revoked: " + revoked);
        
        
        if(revoked) {
            facebookProfileRepository.clearProfile();
            FacebookProfileCopier.clearProfile();
            AdfmfJavaUtilities.setELValue("#{applicationScope.access_token}", "");
            AdfmfJavaUtilities.setELValue("#{applicationScope.expires_in}", "");
            AdfmfJavaUtilities.setELValue("#{applicationScope.autoLoginSuccess}", Boolean.FALSE);

            AdfmfContainerUtilities.resetFeature("com.archnal.cxshop.facebook.FacebookLogin", true);
        }        
        
        //TraceLog.info(getClass(), "facebookLogoutClicked", "accessToken: " + accessToken);
        //AdfmfContainerUtilities.invokeContainerJavaScriptFunction("com.archnal.cxshop.facebook.FacebookLogin", "logout", new Object[] { accessToken });
  
              
        TraceLog.info(getClass(), "facebookLogoutClicked", "END");        
    }
}
