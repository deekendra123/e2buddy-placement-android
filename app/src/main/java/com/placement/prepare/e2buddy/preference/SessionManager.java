package com.placement.prepare.e2buddy.preference;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.placement.prepare.e2buddy.object.User;

public class SessionManager {

    private static final String SHARED_PREF_NAME = "thewada";
    private static final String KEY_USERNAME = "keyUserName";
    private static final String KEY_EMAIL = "keyEmail";
    private static final String KEY_PROFILE_PIC = "keyProfileImage";
    private static final String KEY_ID = "keyId";



    private static SessionManager mInstance;
    private static Context mCtx;

    private SessionManager(Context context) {
        mCtx = context;
    }

    public static synchronized SessionManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SessionManager(context);
        }
        return mInstance;
    }

    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getName());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_PROFILE_PIC, user.getProfileImage());
        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_ID, 0) != 0;
    }

    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_PROFILE_PIC, null)
        );
    }

}