package com.placement.prepare.e2buddy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.placement.prepare.e2buddy.R;

public class SplashActivity extends AppCompatActivity {

    private TextView textView;
    private ImageView imageView;
    private Animation zoomout;

    private static int SPLASH_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        textView = findViewById(R.id.splashtex);
        imageView = findViewById(R.id.imageView);

        zoomout = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.zoomin);
        imageView.setAnimation(zoomout);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        textView.startAnimation(animation);


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
                if(!prefs.getBoolean("firstTime", false)) {
                    // run your one time code
                    Intent i = new Intent(SplashActivity.this, InstructionActivity.class);
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("firstTime", true);
                    editor.commit();
                }
                else {
                    Intent i = new Intent(SplashActivity.this, Home1Activity.class);
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }

        }, SPLASH_TIME);
    }
}