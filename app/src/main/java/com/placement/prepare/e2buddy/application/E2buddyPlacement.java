package com.placement.prepare.e2buddy.application;

import android.app.Application;

public class E2buddyPlacement extends Application {

    public static E2buddyPlacement instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static E2buddyPlacement getInstance(){
        return instance;
    }
}
