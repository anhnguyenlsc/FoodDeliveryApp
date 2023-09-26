package edu.csc.fooddelivery_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splashscreen_intro extends AppCompatActivity {

    //Wait for 3000ms
    int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen_intro);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Splashscreen_intro.this, Input_phonenumber.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}