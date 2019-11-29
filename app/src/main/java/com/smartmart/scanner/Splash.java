package com.smartmart.scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /************** DELAY TIMER **************/

        TimerTask timer = new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(Splash.this, Home.class);
                startActivity(i);
            }
        };

        Timer splashScreen = new Timer();
        splashScreen.schedule(timer, 3000);

    }
}