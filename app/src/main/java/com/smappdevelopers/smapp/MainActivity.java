package com.smappdevelopers.smapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);

                if (getIntent().getExtras() != null) {
                    for (String key : getIntent().getExtras().keySet()) {
                        String value = getIntent().getExtras().getString(key);
                        homeIntent.putExtra(key, value);
                    }
                }

                startActivity(homeIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
