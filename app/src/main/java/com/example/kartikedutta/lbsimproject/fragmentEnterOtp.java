package com.example.kartikedutta.lbsimproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;

public class fragmentEnterOtp extends Fragment {

    View view;
    AppCompatButton verify, change, resend;
    AppCompatTextView hint;
    AppCompatEditText otp;
    ProgressBar progress;
    SharedPreferences preferences;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.enter_otp, container, false);
        return view;
    }

    @Override

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        preferences = getActivity().getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);

        verify = view.findViewById(R.id.verify);
        otp = view.findViewById(R.id.otp);
        hint = view.findViewById(R.id.hint);
        progress = view.findViewById(R.id.progress);
        change = view.findViewById(R.id.change);
        resend = view.findViewById(R.id.resend);

        hint.setText("A 4 digit OTP has been send to " + preferences.getString("mail", "none") + ".");

        verify.setVisibility(View.INVISIBLE);
        resend.setVisibility(View.INVISIBLE);

        otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (otp.getText().toString().trim().length() == 4)
                    verify.setVisibility(View.VISIBLE);
                else verify.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                        new IntentFilter("otp"));

                JSONArray arr = new JSONArray();
                arr.put("checkOtp");
                arr.put(otp.getText().toString().trim());

                Intent i = new Intent(getActivity(), serviceSend.class);
                i.putExtra("msg", arr.toString());
                getActivity().startService(i);

                verify.setVisibility(View.INVISIBLE);
                change.setVisibility(View.INVISIBLE);

                progress.setVisibility(View.VISIBLE);
            }
        });


        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray arr = new JSONArray();
                arr.put("verifyMail");
                arr.put(preferences.getString("mail", null));
                Intent i = new Intent(getActivity(), serviceSend.class);
                i.putExtra("msg", arr.toString());
                getActivity().startService(i);

                //handler to handle if done //
                //write code to handle here
            }
        });

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                resend.setVisibility(View.VISIBLE);
            }
        }, 30000);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                JSONArray arr = new JSONArray(intent.getStringExtra("msg"));
                if (arr.getString(0).equals("correctOtp")) {
                    Log.e("correctOtp", "recieved");
                    if (arr.getString(1).equals("new")) {
                        preferences.edit().putString("accountStatus", "newAccount").apply();
                        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, new fragmentChooseProfile()).commit();
                    } else {
                        preferences.edit().putString("accountStatus", "home").apply();

                        preferences.edit().putString("accountType", arr.getString(1)).apply();
                        JSONArray arr2 = new JSONArray(arr.getString(2));
                        preferences.edit().putString("name", arr2.getString(0)).apply();


                        switch (arr.getString(1)) {
                            case"user":
                                preferences.edit().putString("tags", arr2.getString(1)).apply();
                                break;
                            case "ngo":
                            case "company":

                                preferences.edit().putString("address", arr2.getString(1)).apply();
                                preferences.edit().putString("tags", arr2.getString(2)).apply();
                                break;
                        }

                        getActivity().startActivity(new Intent(getActivity(), activityMain.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));

                    }
                }
                if (arr.getString(0).equals("WrongOtp"))
                    Toast.makeText(getActivity(), "Enter Correct OTP!!", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            verify.setVisibility(View.VISIBLE);
            change.setVisibility(View.VISIBLE);

            progress.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
    }
}
