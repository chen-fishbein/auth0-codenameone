/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.auth0;

import com.codename1.io.JSONParser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 *
 * @author Chen
 */
public class UserProfile {
    
    private String userId;
    private String name;
    private String nickname;
    private String email;
    private String pictureURL;
    private Map extraInfo;

    public UserProfile() {
    }

    public UserProfile(String userId, String name, String nickname, String email, String pictureURL, Map extraInfo) {
        this.userId = userId;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.pictureURL = pictureURL;
        this.extraInfo = extraInfo;
    }

    public UserProfile(String userId, String name, String nickname, String email, String pictureURL, String extraInfo) {
        this.userId = userId;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.pictureURL = pictureURL;
        JSONParser p = new JSONParser();
        try {
            this.extraInfo = p.parseJSON(new InputStreamReader(new ByteArrayInputStream(extraInfo.getBytes()), "UTF-8"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public UserProfile(Map userData) {
        this.userId = (String)userData.get("user_id");
        this.name = (String)userData.get("name");
        this.nickname = (String)userData.get("nickname");
        this.email = (String)userData.get("email");
        this.pictureURL = (String)userData.get("picture");        
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public Map getExtraInfo() {
        return extraInfo;
    }
    
    
    
}
