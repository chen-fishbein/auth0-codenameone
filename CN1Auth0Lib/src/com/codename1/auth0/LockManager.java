/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.auth0;

import com.codename1.system.NativeLookup;
import com.codename1.util.Callback;

/**
 *
 * @author Chen
 */
public class LockManager {
    
    private LoginCallback callback;
    
    private LockNativeInterface lockNative;
    
    private static LockManager instance;

    private LockManager() {
        try {
            lockNative = (LockNativeInterface) NativeLookup.create(LockNativeInterface.class);            
        } catch (Exception e) {
        }
    }
    
    public static LockManager getInstance(){
        if(instance == null){
            instance = new LockManager();            
        }
        return instance;
    }
    
    public static void loggedIn(String token, String userId, String name, 
            String nickname, String email, String pictureURL, String extraInfo){
        getInstance().callback.loggedIn(token, new UserProfile(userId, name, nickname, email, pictureURL, extraInfo));
    }
    
    public boolean isSupported(){
        return lockNative != null && lockNative.isSupported();
    }
    
    public void showLockScreen(LoginCallback callback){
        if(isSupported()){
            this.callback = callback;
            lockNative.showLockScreen();
        }        
    }
}
