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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class fragmentJobList extends Fragment implements interfaceJobs {

    View view;
    RecyclerView list;
    adapterJobList adapter;
    AppCompatTextView hint;
    ProgressBar progress;
    SwipeRefreshLayout refresh;
    FloatingActionButton add;
    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.job_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        preferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);

        list = view.findViewById(R.id.list);
        hint = view.findViewById(R.id.hint);
        progress = view.findViewById(R.id.progress);
        refresh = view.findViewById(R.id.refresh);
        add = view.findViewById(R.id.add);

        if(preferences.getString("accountType", "none").equals("company")){
            if(commonData.getInstance().checkConnection())
                add.show();
        }
        else add.hide();

        loadList();

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadList();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(commonData.getInstance().checkConnection())
                    startActivity(new Intent(getActivity(), activityOther.class).putExtra("page", "addJob"));
                else Toast.makeText(getActivity(), "No Connection", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void loadList(){
        if(commonData.getInstance().checkConnection()) {
            progress.setVisibility(View.VISIBLE);
            hint.setVisibility(View.INVISIBLE);
            refresh.setRefreshing(false);
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                    new IntentFilter("jobs"));

            JSONArray arr = new JSONArray();
            arr.put("getJobs");
            arr.put(preferences.getString("tags", "none"));
            //arr.put(new JSONArray().put("Any").toString());
            Intent i = new Intent(getActivity(), serviceSend.class);
            i.putExtra("msg", arr.toString());
            getActivity().startService(i);
        }
        else{
            hint.setText("No Connection");
            hint.setVisibility(View.VISIBLE);

            if(refresh.isRefreshing())
                refresh.setRefreshing(false);
            progress.setVisibility(View.INVISIBLE);
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                JSONArray arr = new JSONArray(intent.getStringExtra("msg"));
                if(arr.getString(0).equals("jobsList")){
                    List<dataJob> jobs = new ArrayList<dataJob>();

                    for(int i = 1;i<arr.length();i++){
                        jobs.add(new dataJob(arr.getInt(i), arr.getString(++i),arr.getString(++i),arr.getString(++i), arr.getString(++i), arr.getString(++i),arr.getBoolean(++i),commonData.getInstance().getDateTime(getActivity(), Long.valueOf(arr.getString(++i)))));
                    }

                    putList(jobs);

                    hint.setVisibility(View.INVISIBLE);

                    if(refresh.isRefreshing())
                        refresh.setRefreshing(false);
                }
                if(arr.getString(0).equals("noJobsFound")){
                    hint.setText("No Jobs Found");
                    hint.setVisibility(View.VISIBLE);
                }

                if(arr.getString(0).equals("someProblemOccuredWhileGettingJobs")){
                    hint.setText("Some Problem Occured While Loading Jobs");
                    hint.setVisibility(View.VISIBLE);
                }
                progress.setVisibility(View.INVISIBLE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void putList(List<dataJob> jobs){
        adapter = new adapterJobList(getActivity(), jobs, this);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver1);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
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
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver1,
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
        loadList();
    }
}
