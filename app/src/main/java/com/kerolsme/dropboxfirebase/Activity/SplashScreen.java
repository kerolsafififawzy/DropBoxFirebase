package com.kerolsme.dropboxfirebase.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.kerolsme.dropboxfirebase.R;

public class SplashScreen extends AppCompatActivity {


    private FirebaseAuth mFirebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
         mFirebase = FirebaseAuth.getInstance();
         startActivity();
    }


    public void startActivity () {

        new CountDownTimer(2000, 1000) {
            public void onTick(long millisUntilFinished) {
                Log.e("TAG", "onTick: Start " + millisUntilFinished);
            }

            public void onFinish() {
                if (mFirebase.getCurrentUser() != null) {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(SplashScreen.this,Login.class);
                    startActivity(intent);
                    finish();
                }

            }
        }.start();

    }
}