package com.archnal.cxshop.facebook.application;

import com.archnal.cxshop.facebook.application.dao.FacebookProfileRepository;
import com.archnal.cxshop.facebook.application.model.FacebookProfile;
import com.archnal.cxshop.facebook.application.rest.FacebookRestClient;
import com.archnal.cxshop.facebook.application.rest.MeResponse;
import com.archnal.cxshop.facebook.application.util.FacebookProfileCopier;
import com.archnal.cxshop.facebook.application.util.TraceLog;

import com.sun.util.logging.Level;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.Date;
import java.util.StringTokenizer;

import javax.el.MethodExpression;
import javax.el.ValueExpression;

import oracle.adfmf.application.LifeCycleListener;
import oracle.adfmf.framework.api.AdfmfContainerUtilities;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.framework.event.Event;
import oracle.adfmf.framework.event.EventListener;
import oracle.adfmf.framework.event.EventSource;
import oracle.adfmf.framework.event.EventSourceFactory;
import oracle.adfmf.framework.exception.AdfException;
import oracle.adfmf.framework.model.AdfELContext;
import oracle.adfmf.util.Utility;
import oracle.adfmf.util.logging.Trace;

/**
 * The application life cycle listener provides the basic structure for developers needing
 * to include their own functionality during the different stages of the application life
 * cycle.  Please note that there is <b>no user</b> associated with any of the following
 * methods.
 *
 * Common examples of functionality that might be added:
 *
 * start:
 *   1. determine if the application has updates
 *   2. determine if there already exists a local application database image
 *   3. setup any one time state for the application
 *
 * activate:
 *   1. read any application cache stores and re-populate state (if needed)
 *   2. establish/re-establish any database connections and cursors
 *   3. process any pending web-service requests
 *   4. obtain any required resources
 *
 * deactivate:
 *   1. write any restorable state to an application cache store
 *   2. close any database cursors and connections
 *   3. defer any pending web-service requests
 *   4. release held resources
 *
 * stop:
 *   1. logoff any remote services
 *
 * NOTE:
 * 1. In order for the system to recognize an application life cycle listener
 *    it must be registered in the maf-application.xml file.
 * 2. Application assemblers must implement this interface if they would like to
 *    receive notification of application start, hibernation, and application resume.
 *    If a secure web service is needed, you will need to do this from your 'default'
 *    feature where you will have an associated user.
 *
 * @see oracle.adfmf.application.LifeCycleListener
 */
public class LifeCycleListenerImpl implements LifeCycleListener
{
    private FacebookProfileRepository facebookProfileRepository = new FacebookProfileRepository();
    private FacebookRestClient facebookRestClient = new FacebookRestClient();
  public LifeCycleListenerImpl()
  {
  }

  /**
   * The start method will be called at the start of the application.
   * 
   * NOTE:
   * 1. This is a <b>blocking</b> call and will freeze the user interface
   *    while the method is being executed.  If you have any longer running
   *    items you should create a background thread and do the work there.
   * 2. Only the application controller's classes will be available in this
   *    method.
   * 3. At this stage, only an anonymous user is associated with the application
   *    so do not attempt to call any secure web services in this method.
   */
  public void start()
  {
    // Add code here...
      
    Trace.log(Utility.ApplicationLogger, Level.SEVERE, LifeCycleListenerImpl.class, "Inside app life cycle start method",
              "!!!!!!!!!!Application lifecycle start method invoked !!!!!!!!!!");         
    EventSource openURLEventSource =
        EventSourceFactory.getEventSource(EventSourceFactory.OPEN_URL_EVENT_SOURCE_NAME);
    openURLEventSource.addListener(new OpenURLEventListener());
    
      File dbFile = new File(DBConnectionFactory.DB_FILE);
      Trace.log(Utility.ApplicationLogger, Level.INFO, LifeCycleListenerImpl.class, "start",
                "DB FILE: " + dbFile.getAbsolutePath());
      
      if(dbFile.exists() == false) {
          copyDB();
      }
      
      
      FacebookProfile profile = facebookProfileRepository.getFacebookProfile();
      TraceLog.info(getClass(), "start", "profile: " + profile) ;
      if(profile != null) {
        if(profile.getExpirationDate().after(new Date())) {
            TraceLog.info(getClass(), "start", "access token is valid.");
            AdfmfJavaUtilities.setELValue("#{applicationScope.access_token}", profile.getAccessToken());
            AdfmfJavaUtilities.setELValue("#{applicationScope.autoLoginSuccess}", Boolean.TRUE);
            
            MeResponse me = facebookRestClient.executeFacebookMe(profile.getAccessToken());
            FacebookProfileCopier.copyProfile(me);
            
            //AdfmfJavaUtilities.setELValue("#{applicationScope.refreshFacebookProfile}", "true");
            
        } else {
            TraceLog.info(getClass(), "start", "access token is invalid.");
            facebookProfileRepository.clearProfile();
            
            AdfmfJavaUtilities.setELValue("#{applicationScope.autoLoginSuccess}", Boolean.FALSE);
        }
      } else {
          AdfmfJavaUtilities.setELValue("#{applicationScope.autoLoginSuccess}", Boolean.FALSE);
      }
      
  }
  


    private void copyDB() {
        try {
            TraceLog.info(getClass(), "copyDB", "START");
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            InputStream in = cl.getResourceAsStream(".adf/META-INF/FB_MAF.db");
            String outFile = DBConnectionFactory.DB_FILE;
    
            // blank out the DB file if its already there before we copy it.
            // We do this in a scoped section so that the File goes out of scope and releases itself
            {
                File f = new File(outFile);
                if (f != null && f.exists()) {
                    f.createNewFile();
                }
            }

            FileOutputStream out = new FileOutputStream(outFile);

            byte[] buf = new byte[2048];

            for (int len = 0; (len = in.read(buf)) > 0;) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();

            TraceLog.info(getClass(), "copyDB", "END");
        } catch (FileNotFoundException e) {
            TraceLog.severe(getClass(), "copyDB", "ERROR: " + e.getMessage());
        } catch (IOException e) {
            TraceLog.severe(getClass(), "copyDB", "ERROR: " + e.getMessage());        
        }
    }
  /**
   * The stop method will be called at the termination of the application.
   * 
   * NOTE:
   * 1. Depending on how the application is being shutdown, this method may
   *    or may not be called. For example, if a user kills the Application from
   *    the iOS multitask UI then stop will not be called.  Because of this, each
   *    feature should save off their state in the deactivate handler.
   * 2. Only the application controller's classes will be available in this
   *    method.
   * 3. At this stage, only an anonymous user is associated with the application
   *    so do not attempt to call any secure web services in this method.
   */
  public void stop()
  {
    // Add code here...
  }

  /**
   * The activate method will be called when the application is started (post
   * the start method) and when an application is resumed by the operating
   * system.  If the application supports checkpointing, this is a place where
   * the application should read the checkpoint information and resume the process.
   * 
   * NOTE:
   * 1. This is a <b>blocking</b> call and will freeze the user interface
   *    while the method is being executed.  If you have any longer running
   *    items you should create a background thread and do the work there.
   * 2. Only the application controller's classes will be available in this
   *    method.
   * 3. At this stage, only an anonymous user is associated with the application
   *    so do not attempt to call any secure web services in this method.
   * 4. Once an application is activated, the visible feature's activate lifecycle
   *    method will be executed (if configured) post this method being called.
   */
  public void activate()
  {
    // Add code here...
  }

  /**
   * The deactivate method will be called as part of the application shutdown
   * process or when the application is being deactivated/hibernated by the
   * operating system.  This is the place where application developers would
   * write application checkpoint information in either a database or a "device
   * only" file so if the application is terminated while in the background
   * the application can resume the process when the application is reactivated.
   * 
   * NOTE:
   * 1. This is a <b>blocking</b> call and will freeze the user interface
   *    while the method is being executed.  If you have any longer running
   *    items you should create a background thread and do the work there.
   * 2. Only the application controller's classes will be available in this
   *    method.
   * 3. At this stage, only an anonymous user is associated with the application
   *    so do not attempt to call any secure web services in this method.
   * 4. When an application is being deactivated, the visible feature's
   *    deactivate lifecycle method will be executed (if configured) prior to
   *    this method being called.
   */
  public void deactivate()
  {
    // Add code here...
  }
}
