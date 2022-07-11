package com.kerolsme.dropboxfirebase.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.kerolsme.dropboxfirebase.BroadCast_Utilts.StartDialog;
import com.kerolsme.dropboxfirebase.R;
import com.kerolsme.dropboxfirebase.Service.Upload;
import com.kerolsme.dropboxfirebase.Utils.DialogLoading;
import com.kerolsme.dropboxfirebase.fragments.FragmentMangerCustom;

public class MainActivity extends AppCompatActivity {


   private ActivityResultLauncher<Intent> someActivityResultLauncher;
   private final String TAG = MainActivity.class.getName();
   private IntentFilter intentFilter ;
   private StartDialog startDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MaterialCardView SearchCard = findViewById(R.id.SearchCard);
        View SpaceLine = findViewById(R.id.spaceLine);
        FloatingActionButton fba = findViewById(R.id.upload);
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav);
        ViewPager2 viewPager2 = findViewById(R.id.viewpager);
        FragmentMangerCustom mangerCustom = new FragmentMangerCustom(this);
        viewPager2.setAdapter(mangerCustom);
        viewPager2.setUserInputEnabled(false);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.Home) {
                    viewPager2.setCurrentItem(0,false);
                    return true;
                } else if (item.getItemId() == R.id.Files) {
                    viewPager2.setCurrentItem(1,false);
                    return true;
                } else {
                    viewPager2.setCurrentItem(2,false);
                    return true;
                }
            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0 || position == 1) {
                    fba.setVisibility(View.VISIBLE);
                    SearchCard.setVisibility(View.VISIBLE);
                    SpaceLine.setVisibility(View.VISIBLE);
                }else {
                    SearchCard.setVisibility(View.GONE);
                    fba.setVisibility(View.GONE);
                    SpaceLine.setVisibility(View.GONE);
                }
            }
        });

        onActivityResult();
        fba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                someActivityResultLauncher.launch(intent);
            }
        });

        SearchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,search.class));
            }
        });

    }

    public void onActivityResult () {
         someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            // There are no request codes
                            Uri data = result.getData().getData();
                            Intent startServiceUpload = new Intent(MainActivity.this, Upload.class);
                            startServiceUpload.setData(data);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                startForegroundService(startServiceUpload);
                            } else {
                                startService(startServiceUpload);
                            }


                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
               fm.popBackStack();
        } else {
              super.onBackPressed();
        }
    }


}
