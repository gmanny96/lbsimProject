package com.example.kartikedutta.lbsimproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class activityOther extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switch (getIntent().getStringExtra("page")){
            case "addJob":
                getSupportFragmentManager().beginTransaction().add(R.id.container, new fragmentChooseNewJobTags()).commit();
                break;
            case "editProfile":
                getSupportFragmentManager().beginTransaction().add(R.id.container, new fragmentEditProfile()).commit();
                break;
            case "editTags":
                getSupportFragmentManager().beginTransaction().add(R.id.container, new fragmentTagList()).commit();
                break;
            case "aboutUs":
                getSupportFragmentManager().beginTransaction().add(R.id.container, new fragmentAboutUs()).commit();
                break;
            case "addedJobs":
                getSupportFragmentManager().beginTransaction().add(R.id.container, new fragmentAddedJobs()).commit();
                break;
            case "jobPage":
                Bundle b = new Bundle();
                b.putString("job", getIntent().getStringExtra("job"));
                fragmentJobPage jPage = new fragmentJobPage();
                jPage.setArguments(b);
                getSupportFragmentManager().beginTransaction().add(R.id.container, jPage).commit();
                break;
        }
    }
}
