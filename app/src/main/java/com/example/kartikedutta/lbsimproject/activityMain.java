package com.example.kartikedutta.lbsimproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class activityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, serviceConnect.class));

        SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);
        switch(preferences.getString("accountStatus", "none")){
            case "newAccount":
                getSupportFragmentManager().beginTransaction().add(R.id.container,new fragmentChooseProfile()).commit();
                break;
            case "home":
                startActivity(new Intent(this, activityHome.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case "chooseTags":
                getSupportFragmentManager().beginTransaction().add(R.id.container,new fragmentTagList()).commit();
                break;
            default:
                getSupportFragmentManager().beginTransaction().add(R.id.container,new fragmentSplash()).commit();
                break;
        }
    }
}
