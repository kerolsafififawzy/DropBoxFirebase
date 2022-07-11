package com.kerolsme.dropboxfirebase.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.kerolsme.dropboxfirebase.R;
import com.kerolsme.dropboxfirebase.Utils.CloudDatabase;

public class Premium extends AppCompatActivity {

    private MaterialButton materialButton;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);
        setSupportActionBar(findViewById(R.id.toolbar_premium));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        materialButton = findViewById(R.id.plus);
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CloudDatabase cloudDatabase = new CloudDatabase(getApplication());
                cloudDatabase.AccountPremium(new CloudDatabase.CloudResult() {
                    @Override
                    public void onError(Throwable throwable) {

                        Toast.makeText(Premium.this, "Error, Try Again", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSucceed() {
                        finish();
                    }
                });
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}