package com.archnal.cxshop.facebook.application.util;

import java.io.IOException;

import java.net.URLEncoder;

public class URLEncoderMain {
    public URLEncoderMain() {
        super();
    }
    
    public String encode(String url) throws IOException {
        return URLEncoder.encode(url, "UTF-8");    
    }

    public static void main(String[] args) throws IOException {
        URLEncoderMain urlEncoderMain = new URLEncoderMain();
        
        System.out.println(urlEncoderMain.encode("http://macbook.nicholas.com/openfb/logoutcallback.html"));
    }
}
