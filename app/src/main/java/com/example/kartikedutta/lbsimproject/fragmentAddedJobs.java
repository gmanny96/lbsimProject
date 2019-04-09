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

public class fragmentAddedJobs extends Fragment implements interfaceAddedJobs{

    View view;
    Toolbar bar;
    RecyclerView list;
    adapterAddedJobs adapter;
    AppCompatTextView hint;
    ProgressBar progress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.delete_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bar = view.findViewById(R.id.bar);
        progress = view.findViewById(R.id.progress);
        hint = view.findViewById(R.id.hint);
        list = view.findViewById(R.id.list);

        if(commonData.getInstance().checkConnection()) {
            LocalBroadcastManager.getInstance(view.getContext()).registerReceiver(mMessageReceiver,
                    new IntentFilter("addedJobs"));

            LocalBroadcastManager.getInstance(view.getContext()).registerReceiver(mMessageReceiver1,
                    new IntentFilter("deleteJob"));
            JSONArray arr = new JSONArray();
            arr.put("getAddedJobs");

            Intent i = new Intent(getActivity(), serviceSend.class);
            i.putExtra("msg", arr.toString());
            getActivity().startService(i);

            hint.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.VISIBLE);

        }else{
            hint.setVisibility(View.VISIBLE);
            hint.setText("No Connection");
        }
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

    }

    @Override
    public void openJobPage(String job) {
        Intent i = new Intent(getActivity(), activityOther.class);
        i.putExtra("page", "jobPage");
        i.putExtra("job", job);
        getActivity().startActivity(i);
    }

    @Override
    public void deleteJob(int id, int pos) {
        JSONArray arr = new JSONArray();
        arr.put("deleteJob");
        arr.put(id);

        Intent i = new Intent(getActivity(), serviceSend.class);
        i.putExtra("msg", arr.toString());
        getActivity().startService(i);

        adapter.removeRow(pos);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                JSONArray arr = new JSONArray(intent.getStringExtra("msg"));
                if(arr.getString(0).equals("addedJobsList")){
                    List<dataJob> jobs = new ArrayList<dataJob>();

                    for(int i = 1;i<arr.length();i++){
                        jobs.add(new dataJob(arr.getInt(i), arr.getString(++i),arr.getString(++i),arr.getString(++i), arr.getString(++i), arr.getString(++i),arr.getBoolean(++i), commonData.getInstance().getDateTime(getActivity(), Long.valueOf(arr.getString(++i)))));
                    }

                    putList(jobs);
                    hint.setVisibility(View.INVISIBLE);
                }
                if(arr.getString(0).equals("noAddedJobsFound")){
                    hint.setText("Your Added Jobs Will Appear Here");
                    hint.setVisibility(View.VISIBLE);
                }

                if(arr.getString(0).equals("someProblemOccuredWhileGettingAddedJobs")){
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
        adapter = new adapterAddedJobs(getActivity(), jobs, this);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(adapter);
    }

    private BroadcastReceiver mMessageReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                JSONArray arr = new JSONArray(intent.getStringExtra("msg"));
                if(arr.getString(0).equals("deletedJob")){
                    Toast.makeText(getActivity(), "Job Deleted", Toast.LENGTH_SHORT).show();
                }
                if(arr.getString(0).equals("someProblemOccuredWhileDeletingJob")){
                    Toast.makeText(getActivity(), "Some Problem Occured While Deleting Job", Toast.LENGTH_SHORT).show();
                }

                if(adapter.getItemCount()==0) {
                    hint.setText("Your Added Jobs Will Appear Here");
                    hint.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
