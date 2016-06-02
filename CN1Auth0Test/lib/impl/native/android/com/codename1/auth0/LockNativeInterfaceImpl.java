package com.codename1.auth0;

import com.codename1.impl.android.*;
import com.auth0.lock.Lock;
import com.auth0.lock.LockContext;
import com.auth0.lock.LockActivity;
import com.auth0.core.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.codename1.processing.Result;
import android.support.v4.content.LocalBroadcastManager;
import static com.auth0.lock.Lock.AUTHENTICATION_ACTION;


public class LockNativeInterfaceImpl {

    private LocalBroadcastManager broadcastManager;

    private BroadcastReceiver authenticationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            com.auth0.core.UserProfile p = intent.getParcelableExtra(Lock.AUTHENTICATION_ACTION_PROFILE_PARAMETER);
            Token token = intent.getParcelableExtra(Lock.AUTHENTICATION_ACTION_TOKEN_PARAMETER);
            java.util.Map m = p.getExtraInfo();
            Result res = Result.fromContent(m);
            String extra = res.toString();
            LockManager.loggedIn(token.getAccessToken(), p.getId(), p.getName(), p.getNickname(), p.getEmail(), p.getPictureURL(), extra);
        }
    };

    private boolean initialized = false;

    public void showLockScreen() {
        if (!initialized) {
            initialized = true;
            init();
        }
        
    }

    public boolean isSupported() {
        return true;
    }

    private void init() {
        LockContext.configureLock(
                new Lock.Builder()
                .loadFromApplication(AndroidNativeUtil.getActivity().getApplication())
                .closable(true)
        );
        broadcastManager = LocalBroadcastManager.getInstance(AndroidNativeUtil.getActivity());
        broadcastManager.registerReceiver(authenticationReceiver, new IntentFilter(AUTHENTICATION_ACTION));
        Intent intent = new Intent(AndroidNativeUtil.getActivity(), LockActivity.class);
        AndroidNativeUtil.startActivityForResult(intent, new IntentResultListener(){
            public void onActivityResult (int requestCode, int resultCode, Intent data){

            }
        });
    }

}
