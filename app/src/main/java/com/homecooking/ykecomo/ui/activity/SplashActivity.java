package com.homecooking.ykecomo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.ui.activity.chefZone.ChefMenuPrincipalActivity;


public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private boolean isUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent;
                if (App.isReady()) {
                    App.setMemberFromPrefs();
                    if(App.getUserEnvironmentFromPref() == 1) mainIntent = new Intent(SplashActivity.this, MenuPrincipalActivity.class);
                    else mainIntent = new Intent(SplashActivity.this, ChefMenuPrincipalActivity.class);
                } else {
                    mainIntent = new Intent(SplashActivity.this, InitHelpActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("from", "splash");
                    mainIntent.putExtras(extras);
                }
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
