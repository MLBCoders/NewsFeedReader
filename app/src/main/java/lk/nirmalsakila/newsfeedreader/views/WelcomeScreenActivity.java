package lk.nirmalsakila.newsfeedreader.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import lk.nirmalsakila.newsfeedreader.R;
import lk.nirmalsakila.newsfeedreader.adapters.WelcomePreferenceManager;

public class WelcomeScreenActivity extends AppCompatActivity {
    private static  int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent;
                if(new WelcomePreferenceManager(WelcomeScreenActivity.this.getApplicationContext()).checkPreference()){
                    homeIntent = new Intent(WelcomeScreenActivity.this, MainActivity.class);
                }else{
                    homeIntent = new Intent(WelcomeScreenActivity.this, WelcomeActivity.class);
                }
                startActivity(homeIntent);
                finish();
            }
        },SPLASH_TIME_OUT);

    }
}
