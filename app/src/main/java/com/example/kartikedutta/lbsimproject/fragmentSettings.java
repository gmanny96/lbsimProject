package com.example.kartikedutta.lbsimproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class fragmentSettings extends Fragment {

    View view;
    AppCompatButton editProfile, aboutUs, signOut, editTags, addedJobs;
    SharedPreferences preferences;
    String type;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.settings,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        preferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);

        editProfile = view.findViewById(R.id.editProfile);
        editTags = view.findViewById(R.id.editTags);
        aboutUs = view.findViewById(R.id.aboutUs);
        signOut = view.findViewById(R.id.signOut);
        addedJobs = view.findViewById(R.id.addedJobs);

        type = preferences.getString("accountType", "company");

        if(type.equals("company"))
        {
            addedJobs.setVisibility(View.VISIBLE);
            addedJobs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), activityOther.class).putExtra("page", "addedJobs"));
                }
            });
        }
        else addedJobs.setVisibility(View.INVISIBLE);


        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), activityOther.class).putExtra("page", "editProfile"));
            }
        });

        editTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), activityOther.class).putExtra("page", "editTags"));
            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), activityOther.class).putExtra("page", "aboutUs"));
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);
                preferences.edit().putString("accountStatus", "none").apply();

                getActivity().startActivity(new Intent(getActivity(), activityMain.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }
}
