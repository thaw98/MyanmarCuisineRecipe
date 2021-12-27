package com.example.myanmarcuisinerecipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {


    private ImageView logo;
    private TextView logoText;
    private Animation logoAnim, logoTextAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //initlization
        logo = findViewById(R.id.logo);
        logoText = findViewById(R.id.logoText);
        logoAnim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.logo_anim);
        logoTextAnim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.logo_text_anim);


        //Assign Animation
        logo.setAnimation(logoAnim);
        logoText.setAnimation(logoTextAnim);

        //Redirect to next Activity
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        };
        new Handler().postDelayed(runnable, 6000);
    }
}