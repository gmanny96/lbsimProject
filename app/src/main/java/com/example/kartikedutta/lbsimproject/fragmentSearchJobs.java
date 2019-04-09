package com.example.kartikedutta.lbsimproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class fragmentSearchJobs extends Fragment implements interfaceJobs {

    View view;
    AppCompatEditText text;
    AppCompatImageButton search;
    RecyclerView list;
    AppCompatTextView hint;
    ProgressBar progress;
    adapterJobList adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        text = view.findViewById(R.id.text);
        search = view.findViewById(R.id.search);
        list = view.findViewById(R.id.list);
        hint = view.findViewById(R.id.hint);
        progress = view.findViewById(R.id.progress);

        if (commonData.getInstance().checkConnection()) {
            hint.setText("Searched Jobs will display here");
            hint.setVisibility(View.VISIBLE);
        }else {
            hint.setText("No Connection");
            hint.setVisibility(View.VISIBLE);
        }

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(commonData.getInstance().checkConnection()) {
                    if (text.getText().toString().trim().isEmpty())
                        Toast.makeText(getActivity(), "Enter a valid keyword", Toast.LENGTH_SHORT).show();
                    else {

                        LocalBroadcastManager.getInstance(view.getContext()).registerReceiver(mMessageReceiver,
                                new IntentFilter("search"));

                        loadList();

                    }
                }
            }
        });
    }

    private void loadList(){
        JSONArray arr = new JSONArray();
        arr.put("searchJobs");
        arr.put(text.getText().toString());

        Intent i = new Intent(getActivity(), serviceSend.class);
        i.putExtra("msg", arr.toString());
        getActivity().startService(i);

        text.setClickable(false);
        search.setClickable(false);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                JSONArray arr = new JSONArray(intent.getStringExtra("msg"));
                if(arr.getString(0).equals("searchJobsList")){
                    List<dataJob> jobs = new ArrayList<dataJob>();

                    for(int i = 1;i<arr.length();i++){
                        jobs.add(new dataJob(arr.getInt(i), arr.getString(++i),arr.getString(++i),arr.getString(++i),  arr.getString(++i), arr.getString(++i),arr.getBoolean(++i), commonData.getInstance().getDateTime(getActivity(), Long.valueOf(arr.getString(++i)))));
                    }

                    putList(jobs);
                    hint.setVisibility(View.INVISIBLE);
                }
                if(arr.getString(0).equals("noSearchJobsFound")){
                    hint.setText("No Jobs Found matching this keyword");
                    hint.setVisibility(View.VISIBLE);
                }
                if(arr.getString(0).equals("someProblemOccuredWhileSearchingJobs")){
                    hint.setText("Some Problem Occured!!");
                    hint.setVisibility(View.VISIBLE);
                }

                search.setClickable(true);
                text.setClickable(true);

                progress.setVisibility(View.INVISIBLE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver1);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
    }

    private void putList(List<dataJob> jobs){
        adapter = new adapterJobList(getActivity(), jobs, this);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(adapter);
    }

    @Override
    public void openJobPage(String job) {
        Intent i = new Intent(getActivity(), activityOther.class);
        i.putExtra("page", "jobPage");
        i.putExtra("job", job);
        getActivity().startActivity(i);
    }

    @Override
    public void starredJob(int jid) {
        JSONArray arr = new JSONArray();
        arr.put("starJob");
        arr.put(jid);
        Intent i = new Intent(getActivity(), serviceSend.class);
        i.putExtra("msg", arr.toString());
        getActivity().startService(i);

        setJobListener();
    }

    @Override
    public void unStarredJob(int jid, int pos) {
        JSONArray arr = new JSONArray();
        arr.put("unStarJob");
        arr.put(jid);
        Intent i = new Intent(getActivity(), serviceSend.class);
        i.putExtra("msg", arr.toString());
        getActivity().startService(i);

        setJobListener();
    }

    private void setJobListener(){
        LocalBroadcastManager.getInstance(view.getContext()).registerReceiver(mMessageReceiver1,
                new IntentFilter("star"));
    }

    private BroadcastReceiver mMessageReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                JSONArray arr = new JSONArray(intent.getStringExtra("msg"));
                if(arr.getString(0).equals("starredJob")){
                    Toast.makeText(getActivity(), "Job Added to Starred List", Toast.LENGTH_SHORT).show();
                }
                if(arr.getString(0).equals("someProblemOccuredWhileStarredJobs")){
                    Toast.makeText(getActivity(), "Some Problem Occured While Adding to Starred List", Toast.LENGTH_SHORT).show();
                }
                if(arr.getString(0).equals("unStarredJob")){
                    Toast.makeText(getActivity(), "Job Removed from Starred List", Toast.LENGTH_SHORT).show();
                }
                if(arr.getString(0).equals("someProblemOccuredWhileUnStarredJobs")){
                    Toast.makeText(getActivity(), "Some Problem Occured While Removing from Starred List", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if(!text.getText().toString().trim().isEmpty())
            loadList();
    }
}
