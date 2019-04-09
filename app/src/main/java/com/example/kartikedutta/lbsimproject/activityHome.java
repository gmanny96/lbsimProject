package com.example.kartikedutta.lbsimproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

public class activityHome extends AppCompatActivity {

    BottomNavigationView tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        tabs = findViewById(R.id.bottom_bar);

        tabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        tabs.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.jobs:
                        Log.e("lala", "lala");
                        getSupportFragmentManager().beginTransaction().add(R.id.layout, new fragmentJobList()).commit();
                        break;
                    case R.id.search:
                        Log.e("lala", "lala1");
                        getSupportFragmentManager().beginTransaction().add(R.id.layout, new fragmentSearchJobs()).commit();
                        break;
                    case R.id.starred:
                        Log.e("lala", "lala2");
                        getSupportFragmentManager().beginTransaction().add(R.id.layout, new fragmentStarredList()).commit();
                        break;
                    case R.id.settings:
                        Log.e("lala", "lala3");
                        getSupportFragmentManager().beginTransaction().add(R.id.layout, new fragmentSettings()).commit();
                        break;

                    default:break;
                }
                return true;
            }
        });

        tabs.setSelectedItemId(R.id.jobs);

    }
}


