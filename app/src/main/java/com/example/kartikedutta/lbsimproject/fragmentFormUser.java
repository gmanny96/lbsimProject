package com.example.kartikedutta.lbsimproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

public class fragmentFormUser extends Fragment{

    View view;
    AppCompatButton cont;
    AppCompatEditText name;
    AppCompatCheckBox age;
    ProgressBar progress;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.user_form_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cont=view.findViewById(R.id.cont);
        name = view.findViewById(R.id.name);
        age = view.findViewById(R.id.age);
        progress = view.findViewById(R.id.bar);

        checkClickable();

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkClickable();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        age.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkClickable();
            }
        });

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                        new IntentFilter("account"));

                JSONArray arr = new JSONArray();
                arr.put("newAccount");
                arr.put("user");
                arr.put(name.getText().toString());
                Intent i = new Intent(getActivity(), serviceSend.class);
                i.putExtra("msg", arr.toString());
                getActivity().startService(i);

                cont.setVisibility(View.INVISIBLE);
                progress.setVisibility(View.VISIBLE);
            }
        });
    }

    private void checkClickable(){
        if(!name.getText().toString().trim().isEmpty() && age.isChecked())
            cont.setVisibility(View.VISIBLE);
        else cont.setVisibility(View.INVISIBLE);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                JSONArray arr = new JSONArray(intent.getStringExtra("msg"));
                if(arr.getString(0).equals("accountCreated")) {
                    SharedPreferences preferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                    preferences.edit().putString("accountType", "user").apply();
                    preferences.edit().putString("name", name.getText().toString()).apply();
                    preferences.edit().putString("accountStatus", "chooseTags").apply();

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new fragmentTagList()).commit();
                }
                if(arr.getString(0).equals("someProblemOccuredWhileCreatingAccount"))
                    Toast.makeText(getActivity(), "Some Problem Occured. Try Again Later!!", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            cont.setVisibility(View.VISIBLE);

            progress.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
    }
}
