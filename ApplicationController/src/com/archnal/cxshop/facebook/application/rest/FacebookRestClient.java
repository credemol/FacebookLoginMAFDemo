package com.archnal.cxshop.facebook.application.rest;

import com.archnal.cxshop.facebook.application.util.TraceLog;

import oracle.adfmf.dc.ws.rest.RestServiceAdapter;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.framework.api.JSONBeanSerializationHelper;
import oracle.adfmf.framework.api.Model;

public class FacebookRestClient {
    public static final String CONNECTION_NAME = "FB_GRAPH";
    
    public FacebookRestClient() {
        super();
    }
    
    public MeResponse executeFacebookMe(String accessToken) {
        TraceLog.info(getClass(), "executeFacebookMe", "START");
        RestServiceAdapter restServiceAdapter = Model.createRestServiceAdapter();

        // Clear any previously set request properties, if any
        restServiceAdapter.clearRequestProperties();

        // Set the connection name
        restServiceAdapter.setConnectionName(CONNECTION_NAME);

        // Specify the type of request
        restServiceAdapter.setRequestType(RestServiceAdapter.REQUEST_TYPE_GET);

        // Specify the number of retries
        restServiceAdapter.setRetryLimit(0);


        // Set the URI which is defined after the endpoint in the connections.xml.
        // The request is the endpoint + the URI being set
        restServiceAdapter.setRequestURI("/me?access_token=" + accessToken);
        
        AdfmfJavaUtilities.setELValue("#{applicationScope.requestURI}", restServiceAdapter.getRequestURI());
        //setJsonResponse("/me?access_token=" + getAccessToken());

        // Execute SEND and RECEIVE operation
        try {
            // For GET request, there is no payload
            String jsonResponse = restServiceAdapter.send("");
            
            TraceLog.info(getClass(), "executeFacebookMe", "jsonResponse: " + jsonResponse);
            
            // Now create a new RESTJSONResponse object and parse the JSON string returned into this class
            MeResponse res = (MeResponse) JSONBeanSerializationHelper.fromJSON(MeResponse.class, jsonResponse);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            TraceLog.severe(getClass(), "executeFacebookMe", "ERROR: " + e.getMessage());
            return null;
        } finally {
            TraceLog.info(getClass(), "executeFacebookMe", "END");
        }
        
    }   
    
    public void logout(String appId, String accessToken, String confirm, String next) {
        if(confirm == null || confirm.trim().length() == 0) {
            confirm = "1";
        }
        String url = "https://www.facebook.com/logout.php?confirm=" + confirm + "&access_token=" + accessToken + "&next="  + next;
        
        RestServiceAdapter restServiceAdapter = Model.createRestServiceAdapter();

        // Clear any previously set request properties, if any
        restServiceAdapter.clearRequestProperties();

        // Set the connection name
        restServiceAdapter.setConnectionName(CONNECTION_NAME);

        // Specify the type of request
        restServiceAdapter.setRequestType(RestServiceAdapter.REQUEST_TYPE_GET);

        //restServiceAdapter.addRequestProperty("Cookie", value);
        // Specify the number of retries
        restServiceAdapter.setRetryLimit(0);
        
        restServiceAdapter.setRequestURI(url);
        
        
    }
}
