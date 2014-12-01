package com.archnal.cxshop.facebook.application.util;


import com.sun.util.logging.Level;

import oracle.adfmf.util.Utility;
import oracle.adfmf.util.logging.Trace;

public class TraceLog {
    public TraceLog() {
        super();
    }
    
    public static void info(Class clazz, String method, String message) {
        Trace.log(Utility.ApplicationLogger, Level.INFO, clazz, method, buildMessage(message));
    }
    
    public static void severe(Class clazz, String method, String message) {
        Trace.log(Utility.ApplicationLogger, Level.SEVERE, clazz, method, buildMessage(message));
    }
    
    public static void warning(Class clazz, String method, String message) {
        Trace.log(Utility.ApplicationLogger, Level.WARNING, clazz, method, buildMessage(message));
    }
    
    public static void fine(Class clazz, String method, String message) {
        Trace.log(Utility.ApplicationLogger, Level.INFO, clazz, method, buildMessage(message));
    }

    private static String buildMessage(String message) {
        return ">>>>>>>>>> " + message;
    }


}
