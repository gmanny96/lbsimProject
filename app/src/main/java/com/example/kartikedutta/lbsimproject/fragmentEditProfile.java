package com.example.kartikedutta.lbsimproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

public class fragmentEditProfile extends Fragment {

    View view;
    Toolbar bar;
    AppCompatEditText name, address;
    AppCompatButton deleteAccount;
    FloatingActionButton done;
    SharedPreferences preferences;
    String type;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.edit_profile,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        preferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);

        bar = view.findViewById(R.id.bar);
        name = view.findViewById(R.id.name);
        address = view.findViewById(R.id.address);

        deleteAccount = view.findViewById(R.id.deleteAccount);
        done = view.findViewById(R.id.done);

        done.hide();

        name.setText(preferences.getString("name", "null"));
        type = preferences.getString("accountType", "company");

        if(type.equals("company")) address.setText(preferences.getString("address", "none"));
        else address.setVisibility(View.INVISIBLE);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInfo();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInfo();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                        new IntentFilter("deletedAccount"));

                JSONArray arr = new JSONArray();
                arr.put("deleteAccount");

                Intent i = new Intent(getActivity(), serviceSend.class);
                i.putExtra("msg", arr.toString());
                getActivity().startService(i);
            }
        });

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("updateInfo"));

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray arr = new JSONArray();
                arr.put("updateInfo");
                arr.put(name.getText().toString());
                if(type.equals("company"))
                    arr.put(address.getText().toString());

                name.setClickable(false);
                address.setClickable(false);
                Intent i = new Intent(getActivity(), serviceSend.class);
                i.putExtra("msg", arr.toString());
                getActivity().startService(i);
            }
        });
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                JSONArray arr = new JSONArray(intent.getStringExtra("msg"));
                if (arr.getString(0).equals("deletedAccount")) {
                    SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);
                    preferences.edit().putString("accountStatus", "none").apply();
                    getActivity().startActivity(new Intent(getActivity(), activityMain.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));

                }
                if (arr.getString(0).equals("someProblemOccuredWhileDeletingAccount"))
                    Toast.makeText(getActivity(), "Some problem Occured Try again later!", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    private BroadcastReceiver mMessageReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                JSONArray arr = new JSONArray(intent.getStringExtra("msg"));
                if (arr.getString(0).equals("updatedInfo")) {
                    preferences.edit().putString("name", name.getText().toString()).apply();
                    if(type.equals("company"))
                        preferences.edit().putString("address", address.getText().toString()).apply();
                }
                if (arr.getString(0).equals("someProblemOccuredWhileUpdatingInfo"))
                    Toast.makeText(getActivity(), "Some problem Occured Try again later!", Toast.LENGTH_SHORT).show();

                name.setClickable(true);
                address.setClickable(true);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    private void checkInfo(){
        String nameT = name.getText().toString().trim();
        if(!nameT.isEmpty()){
            if(!nameT.equals(preferences.getString("name", "none"))){
                if(!type.equals("company")){
                    String addT = address.getText().toString().trim();
                    if(!addT.isEmpty()){
                        if(!addT.equals(preferences.getString("name", "none")))
                            done.show();
                    }
                }
                done.show();
            }
        }
        else done.hide();
    }

}
