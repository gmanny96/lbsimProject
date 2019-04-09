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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class fragmentStarredList extends Fragment implements interfaceJobs {

    View view;
    RecyclerView list;
    AppCompatTextView hint;
    Toolbar bar;
    adapterStarredList adapter;
    SwipeRefreshLayout refresh;
    ProgressBar progress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.starred_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bar = view.findViewById(R.id.bar);
        hint = view.findViewById(R.id.hint);
        list = view.findViewById(R.id.list);
        refresh = view.findViewById(R.id.refresh);
        progress = view.findViewById(R.id.progress);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadList();
            }
        });

        loadList();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("starList"));

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver1,
                new IntentFilter("star"));
    }

    private void loadList(){
        if(commonData.getInstance().checkConnection())
        {
            hint.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.VISIBLE);

            JSONArray arr = new JSONArray();
            arr.put("getStarredJobs");

            Intent i = new Intent(getActivity(), serviceSend.class);
            i.putExtra("msg", arr.toString());
            getActivity().startService(i);
        }
        else{

            hint.setText("No Connection");

            hint.setVisibility(View.VISIBLE);
            progress.setVisibility(View.INVISIBLE);
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                JSONArray arr = new JSONArray(intent.getStringExtra("msg"));
                if(arr.getString(0).equals("starredJobsList")){
                    List<dataJob> jobs = new ArrayList<dataJob>();

                    for(int i = 1;i<arr.length();i++){
                        jobs.add(new dataJob(arr.getInt(i), arr.getString(++i),arr.getString(++i),arr.getString(++i),arr.getString(++i), arr.getString(++i),true, commonData.getInstance().getDateTime(getActivity(), Long.valueOf(arr.getString(++i)))));
                    }

                    putList(jobs);

                    hint.setVisibility(View.INVISIBLE);
                }
                if(arr.getString(0).equals("noStarredJobsFound")){
                    hint.setText("Your Starred Jobs Will Appear Here");
                    hint.setVisibility(View.VISIBLE);
                }
                if(arr.getString(0).equals("someProblemOccuredWhileSearchingStarredJobs")){
                    hint.setText("Some Problem Occured, try again later");
                    hint.setVisibility(View.VISIBLE);
                }

                progress.setVisibility(View.INVISIBLE);
                if(refresh.isRefreshing())
                    refresh.setRefreshing(false);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void putList(List<dataJob> jobs){
        adapter = new adapterStarredList(getActivity(), jobs, this);
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
    }

    @Override
    public void starredJob(int jid) {}

    @Override
    public void unStarredJob(int jid, int pos) {
        JSONArray arr = new JSONArray();
        arr.put("unStarJob");
        arr.put(jid);
        Intent i = new Intent(getActivity(), serviceSend.class);
        i.putExtra("msg", arr.toString());
        getActivity().startService(i);

        adapter.removeRow(pos);
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

                if(adapter.getItemCount()==0)
                    hint.setVisibility(View.VISIBLE);

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