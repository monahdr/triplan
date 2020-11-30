package com.example.triplanproject.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.triplanproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Bottom Navigation View

        bottomNavigationView = findViewById(R.id.bottomNav);
        //bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.home:
                       selectedFragment = new HomeFragment();
                        break;

                    case R.id.settings:
                        selectedFragment = new SettingsFragment();
                        break;

                    case R.id.userProfile:
                        //startActivity(new Intent(getApplicationContext(),UserProfile.class));
                       // overridePendingTransition(0,0);
                        selectedFragment = new UserProfileFragment();
                        break;
                }
               getSupportFragmentManager().beginTransaction().replace(R.id.container, selectedFragment).commit();
                return true;
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();

    }



}