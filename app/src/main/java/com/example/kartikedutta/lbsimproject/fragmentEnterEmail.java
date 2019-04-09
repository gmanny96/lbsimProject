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
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

public class fragmentEnterEmail extends Fragment {

    View view;
    AppCompatButton cont;
    AppCompatEditText mail;
    ProgressBar progress;
    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.enter_mail, container, false);
        return view;
    }

    @Override

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        preferences = view.getContext().getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);

        cont = view.findViewById(R.id.cont);
        mail = view.findViewById(R.id.email);
        progress = view.findViewById(R.id.progress);

        if (!preferences.getString("mail", "none").equals("none")) {
            mail.setText(preferences.getString("mail", null));
            if (isValidEmail(mail.getText().toString()))
                cont.setVisibility(View.VISIBLE);
        } else cont.setVisibility(View.INVISIBLE);

        mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isValidEmail(mail.getText().toString().trim())) {
                    cont.setVisibility(View.VISIBLE);
                } else {
                    cont.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(commonData.getInstance().checkConnection()) {
                    LocalBroadcastManager.getInstance(view.getContext()).registerReceiver(mMessageReceiver,
                            new IntentFilter("mail"));

                    JSONArray arr = new JSONArray();
                    arr.put("verifyMail");
                    arr.put(mail.getText().toString());

                    Intent i = new Intent(getActivity(), serviceSend.class);
                    i.putExtra("msg", arr.toString());
                    view.getContext().startService(i);

                    mail.setClickable(false);
                    cont.setVisibility(View.INVISIBLE);

                    progress.setVisibility(View.VISIBLE);
                }
                else Toast.makeText(getActivity(), "No Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                JSONArray arr = new JSONArray(intent.getStringExtra("msg"));
                if (arr.getString(0).equals("sendedOtp")) {
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, new fragmentEnterOtp()).addToBackStack(null).commit();
                    preferences.edit().putString("mail", mail.getText().toString()).apply();
                }
                if (arr.getString(0).equals("someErrorOccuredWhileSendingOtp"))
                    Toast.makeText(getActivity(), "Some problem occured try again later.", Toast.LENGTH_SHORT).show();

                mail.setClickable(false);
                cont.setVisibility(View.VISIBLE);
                progress.setVisibility(View.INVISIBLE);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        LocalBroadcastManager.getInstance(view.getContext()).unregisterReceiver(mMessageReceiver);
    }
}
