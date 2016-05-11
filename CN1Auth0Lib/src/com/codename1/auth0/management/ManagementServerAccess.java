/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.auth0.management;

import com.codename1.auth0.UserProfile;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.Log;
import com.codename1.io.NetworkManager;
import com.codename1.io.gzip.GZConnectionRequest;
import com.codename1.processing.Result;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Chen
 */
public class ManagementServerAccess {

    private static final int REQUEST_GET = 0;

    private static final int REQUEST_POST = 1;

    private static final int REQUEST_PUT = 2;

    private static final int REQUEST_DELETE = 3;
    
    private static ManagementServerAccess instance = new ManagementServerAccess();
    
    private String URL = "https://codename1.eu.auth0.com/api/v2";
    
    private String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIzUUtLcW9QZ1hYN3NjeWxHZ1JnbzdaVUUxZDA4OVYyZSIsInNjb3BlcyI6eyJ1c2VycyI6eyJhY3Rpb25zIjpbInJlYWQiLCJ1cGRhdGUiLCJjcmVhdGUiXX19LCJpYXQiOjE0NjI5NzQ0OTAsImp0aSI6IjIwZTllZjhlMDMwZWZjOTNlNzAwYTY5ODNlYWNlNGM4In0.tUL2yQ4aBoybYqH6lWjW8toIMQLeG-7RQK9Hnr2kMoo";
    
    private ManagementServerAccess(){    
    }
    
    public static ManagementServerAccess getInstance() {
        return instance;
    }

    public List getUsers()throws IOException {
        Map response = invokeREST(REQUEST_GET, "/users", null);
        List <Map>users = (List) response.get("root");
        ArrayList res = new ArrayList();
        for (Map user : users) {
            res.add(new UserProfile(user));
        }
        return res;
    }
    
    private Map invokeREST(int request, String appendToURL, Map params) throws IOException {
        //if there are params to the request build the json to send
        final String[] sendData = new String[1];
        if (params != null) {
            Result res = Result.fromContent(params);
            sendData[0] = res.toString();
        }
        ConnectionRequest req = new GZConnectionRequest() {

            @Override
            protected void buildRequestBody(OutputStream os) throws IOException {
                if (sendData[0] == null) {
                    super.buildRequestBody(os);
                } else {
                    Writer writer = null;
                    writer = new OutputStreamWriter(os, "UTF-8");
                    writer.write(sendData[0]);
                    writer.flush();
                    writer.close();
                }
            }

            protected void handleErrorResponseCode(int code, String message) {
                //if the error code is a 4XX error code, it's an applciation error
                //we should handle it in the app
                if (code >= 500 || code < 400) {
                    super.handleErrorResponseCode(code, message);
                }
            }

        };
        req.setReadResponseForErrors(true);
        req.setUrl(URL + appendToURL);
        if (request != REQUEST_DELETE) {
            req.setContentType("application/json");
        }
        req.addRequestHeader("Accept", "application/json");
        req.addRequestHeader("Authorization", token);
        
        req.setPost(request == REQUEST_POST || request == REQUEST_PUT);
        if (request == REQUEST_PUT) {
            req.setHttpMethod("PUT");
        }
        if (request == REQUEST_DELETE) {
            req.setHttpMethod("DELETE");
        }
        req.setWriteRequest(params != null);
        req.setTimeout(15000);

        NetworkManager.getInstance().addToQueueAndWait(req);
        byte[] data = req.getResponseData();
        if (data == null) {
            throw new IOException("Network Error, try again later.");
        }

        JSONParser parser = new JSONParser();
        Map response = parser.parseJSON(new InputStreamReader(new ByteArrayInputStream(data), "UTF-8"));
        if(response.get("error") != null){
            throw new IOException((String)response.get("message"));
        }
        return response;
    }
    
    
}
