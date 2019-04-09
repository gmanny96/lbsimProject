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
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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

public class fragmentChooseNewJobTags extends Fragment implements interfaceTag {

    View view;
    Toolbar bar;
    RecyclerView list;
    AppCompatButton skip;
    FloatingActionButton done;
    adapterNewJobTagList adapter;
    ProgressBar progress;
    Bundle b;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.tags_list,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bar = view.findViewById(R.id.bar);
        list = view.findViewById(R.id.list);
        skip = view.findViewById(R.id.skip);
        done = view.findViewById(R.id.done);

        skip.setVisibility(View.GONE);

        bar.setNavigationIcon(R.drawable.arrow_left);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        progress = view.findViewById(R.id.progress);

        done.hide();

        if(commonData.getInstance().checkConnection()) {
            LocalBroadcastManager.getInstance(view.getContext()).registerReceiver(mMessageReceiver,
                    new IntentFilter("tags"));

            JSONArray arr = new JSONArray();
            arr.put("getTags");

            Intent i = new Intent(getActivity(), serviceSend.class);
            i.putExtra("msg", arr.toString());
            view.getContext().startService(i);

        }
        else Toast.makeText(getActivity(), "No Connection", Toast.LENGTH_SHORT).show();



        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b = new Bundle();
                b.putString("jobTag", adapter.getSelectedTag());
                startNewJob();
            }
        });
    }

    private void startNewJob(){
        fragmentNewJob fragment = new fragmentNewJob();
        fragment.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                JSONArray arr = new JSONArray(intent.getStringExtra("msg"));
                if (arr.getString(0).equals("tagList")) {
                    List<dataTag> tags = new ArrayList<dataTag>();
                    for(int i = 1;i<arr.length();i++){
                        dataTag tag = new dataTag(arr.getString(i));
                        tags.add(tag);
                    }
                    putLists(tags);
                }
                progress.setVisibility(View.INVISIBLE);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void putLists(List<dataTag> tags){
        adapter = new adapterNewJobTagList(getActivity(), tags, this);
        list.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        list.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        LocalBroadcastManager.getInstance(view.getContext()).unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void updateTag(int num) {
        adapter.updateRow(num);
    }

    @Override
    public void pressed() {
        if(adapter.getSelectedTag().equals("empty"))
            done.hide();
        else done.show();
    }
}
