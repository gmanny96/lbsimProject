package com.example.kartikedutta.lbsimproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class fragmentNewJob extends Fragment {

    View view;
    FloatingActionButton add;
    AppCompatEditText name, desc;
    Toolbar bar;
    ProgressBar progress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_job, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        name = view.findViewById(R.id.name);
        desc = view.findViewById(R.id.desc);
        bar = view.findViewById(R.id.bar);
        add = view.findViewById(R.id.add);
        progress = view.findViewById(R.id.progress);

        progress.setVisibility(View.INVISIBLE);

        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkData();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkData();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(commonData.getInstance().checkConnection()) {
                    LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                            new IntentFilter("addJob"));

                    JSONArray arr = new JSONArray();
                    arr.put("addJob");
                    arr.put(name.getText().toString());
                    arr.put(desc.getText().toString());
                    arr.put(getArguments().getString("jobTag"));
                    Intent i = new Intent(getActivity(), serviceSend.class);
                    i.putExtra("msg", arr.toString());
                    getActivity().startService(i);

                    progress.setVisibility(View.VISIBLE);
                    add.hide();
                }
                else Toast.makeText(getActivity(), "No Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                JSONArray arr = new JSONArray(intent.getStringExtra("msg"));
                if(arr.getString(0).equals("addedJob")){
                    Toast.makeText(getActivity(), "Job Added", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
                if(arr.getString(0).equals("someProblemOccuredWhileAddingJob")){
                    Toast.makeText(getActivity(), "Some Problem Occured While Adding Job", Toast.LENGTH_SHORT).show();
                }
                progress.setVisibility(View.INVISIBLE);
                add.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
    }

    private void checkData(){
        if(!name.getText().toString().trim().isEmpty() && !desc.getText().toString().trim().isEmpty())
            add.show();
        else add.hide();
    }
}
