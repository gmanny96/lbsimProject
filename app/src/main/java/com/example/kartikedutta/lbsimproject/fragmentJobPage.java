package com.example.kartikedutta.lbsimproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

public class fragmentJobPage extends Fragment {

    View view;
    AppCompatImageButton back, star;
    AppCompatTextView job, company, desc, time;
    AppCompatButton tag;
    int jid;
    Boolean starred;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.job_page,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        back = view.findViewById(R.id.back);
        star = view.findViewById(R.id.star);
        job = view.findViewById(R.id.job);
        company = view.findViewById(R.id.company);
        tag = view.findViewById(R.id.tag);
        desc = view.findViewById(R.id.desc);
        time = view.findViewById(R.id.time);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(commonData.getInstance().checkConnection()) {
                    LocalBroadcastManager.getInstance(view.getContext()).registerReceiver(mMessageReceiver,
                            new IntentFilter("star"));
                    if(starred)
                        unStarredJob();
                    else starredJob();
                }
                else Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

        try {
            JSONArray arr = new JSONArray(getArguments().getString("job"));
            jid = arr.getInt(0);
            job.setText(arr.getString(1));
            company.setText(arr.getString(2));
            desc.setText(arr.getString(3));
            tag.setText(arr.getString(5));
            starred = arr.getBoolean(4);
            time.setText("Date Added : "+arr.getString(6));
            setStarColor();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                JSONArray arr = new JSONArray(intent.getStringExtra("msg"));
                if(arr.getString(0).equals("starredJob")){
                    Toast.makeText(view.getContext(), "Job Added to Starred List", Toast.LENGTH_SHORT).show();
                }
                if(arr.getString(0).equals("someProblemOccuredWhileStarredJobs")){
                    Toast.makeText(view.getContext(), "Some Problem Occured While Adding to Starred List", Toast.LENGTH_SHORT).show();
                }
                if(arr.getString(0).equals("unStarredJob")){
                    Toast.makeText(view.getContext(), "Job Removed from Starred List", Toast.LENGTH_SHORT).show();
                }
                if(arr.getString(0).equals("someProblemOccuredWhileUnStarredJobs")){
                    Toast.makeText(view.getContext(), "Some Problem Occured While Removing from Starred List", Toast.LENGTH_SHORT).show();
                }

                starred = !starred;
                setStarColor();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void setStarColor(){
        if(starred)
            star.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        else
            star.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.lightGrey), PorterDuff.Mode.MULTIPLY);
    }

    private void starredJob() {
        JSONArray arr = new JSONArray();
        arr.put("starJob");
        arr.put(jid);
        Intent i = new Intent(getActivity(), serviceSend.class);
        i.putExtra("msg", arr.toString());
        view.getContext().startService(i);
    }

    private void unStarredJob() {
        JSONArray arr = new JSONArray();
        arr.put("unStarJob");
        arr.put(jid);
        Intent i = new Intent(getActivity(), serviceSend.class);
        i.putExtra("msg", arr.toString());
        view.getContext().startService(i);
    }
}
