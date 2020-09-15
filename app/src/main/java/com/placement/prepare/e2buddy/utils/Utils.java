package com.placement.prepare.e2buddy.utils;

import android.widget.Toast;

import com.placement.prepare.e2buddy.application.E2buddyPlacement;

public class Utils {
    public static void showToast(String message){
        Toast.makeText(E2buddyPlacement.getInstance(), message,Toast.LENGTH_SHORT).show();
    }
}
