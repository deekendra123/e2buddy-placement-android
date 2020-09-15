package com.placement.prepare.e2buddy.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.placement.prepare.e2buddy.R;
import com.placement.prepare.e2buddy.fragment.InternShipFragment;
import com.placement.prepare.e2buddy.fragment.QuizFragment;
import com.placement.prepare.e2buddy.utils.Utils;

public class Home1Activity extends AppCompatActivity {

    final QuizFragment quizFragment = new QuizFragment();
    final InternShipFragment internShipFragment = new InternShipFragment();

    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment active = quizFragment;
    private static final String TAG = Home1Activity.class.getSimpleName();
    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home1);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager.beginTransaction().add(R.id.frame_container, internShipFragment, "2").hide(internShipFragment).commit();
        fragmentManager.beginTransaction().add(R.id.frame_container,quizFragment, "1").commit();


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_quiz:
                    fragmentManager.beginTransaction().hide(active).show(quizFragment).commit();
                    active = quizFragment;
                    return true;

                case R.id.navigation_placement:
                    fragmentManager.beginTransaction().hide(active).show(internShipFragment).commit();
                    active = internShipFragment;
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Utils.showToast("Please click BACK again to exit");
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}