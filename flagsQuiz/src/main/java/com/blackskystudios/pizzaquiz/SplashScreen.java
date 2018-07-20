package com.blackskystudios.pizzaquiz;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SplashScreen extends AppCompatActivity {

    ImageView splash_title;
    RelativeLayout splash_dark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splash_title = (ImageView)findViewById(R.id.splash_title);
        splash_dark = (RelativeLayout)findViewById(R.id.splash_dark);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                splash_title.animate().translationY(20f).setDuration(500);
                splash_title.animate().scaleX(1f).setDuration(500);
                splash_title.animate().scaleY(1f).setDuration(500);
                splash_dark.animate().alpha(1f).setDuration(500);


                transition();
            }
        }, 2500);
    }


    public void transition(){
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashScreen.this,MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(mainIntent);
                finish() ;
                overridePendingTransition(0,0);
            }
        }, 500);
    }
}
