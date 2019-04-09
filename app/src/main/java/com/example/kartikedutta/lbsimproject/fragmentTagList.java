package com.example.kartikedutta.lbsimproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class fragmentTagList extends Fragment implements interfaceTagPressed {

    View view;
    AppCompatButton skip;
    RecyclerView list;
    FloatingActionButton done;
    adapterTagList adapter;
    ProgressBar progress;
    Toolbar bar;
    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tags_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        preferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);

        list = view.findViewById(R.id.list);
        skip = view.findViewById(R.id.skip);
        done = view.findViewById(R.id.done);
        progress = view.findViewById(R.id.progress);

        done.hide();

        if(preferences.getString("accountStatus", "none").equals("home")){
            bar = view.findViewById(R.id.bar);
            bar.setNavigationIcon(R.drawable.arrow_left);
            bar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().onBackPressed();
                }
            });
        }

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("tags"));

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences.edit().putString("tags", new JSONArray().put("Any").toString()).apply();
                goToHome();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(commonData.getInstance().checkConnection()) {
                    list.setClickable(false);
                    done.hide();
                    progress.setVisibility(View.VISIBLE);

                    Intent i = new Intent(getActivity(), serviceSend.class);
                    i.putExtra("msg", adapter.getSelectedTags());
                    getActivity().startService(i);
                }
                else Toast.makeText(getActivity(), "No Connection", Toast.LENGTH_SHORT).show();

                    //preferences.edit().putString("tags", tags.toString()).apply();
                //goToHome();
            }
        });

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (commonData.getInstance().checkConnection()) {
                    JSONArray arr = new JSONArray();
                    arr.put("getTags");

                    Intent i = new Intent(getActivity(), serviceSend.class);
                    i.putExtra("msg", arr.toString());
                    getActivity().startService(i);
                } else {
                    Toast.makeText(getActivity(), "No Connection", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.INVISIBLE);
                }
            }
        }, 2000);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                JSONArray arr = new JSONArray(intent.getStringExtra("msg"));
                if (arr.getString(0).equals("tagList")) {
                    List<dataTag> tags = new ArrayList<dataTag>();
                    for (int i = 1; i < arr.length(); i++) {
                        dataTag tag = new dataTag(arr.getString(i));
                        tags.add(tag);
                    }
                    setList(tags);
                }
                if (arr.getString(0).equals("addedTags")) {
                    SharedPreferences preferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                    JSONArray tags = new JSONArray(adapter.getSelectedTags());
                    tags.remove(0);
                    preferences.edit().putString("tags", tags.toString()).apply();
                    goToHome();
                }
                if (arr.getString(0).equals("someErrorOccuredWhileAddingTags")) {
                    Toast.makeText(getActivity(), "Some problem occured try again later.", Toast.LENGTH_SHORT).show();
                    done.show();
                }
                list.setClickable(true);
                progress.setVisibility(View.INVISIBLE);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void goToHome() {
        preferences.edit().putString("accountStatus", "home").apply();

        getActivity().startActivity(new Intent(getActivity(), activityHome.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private void setList(List<dataTag> tags) {
        adapter = new adapterTagList(getActivity(), tags, this);
        list.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        list.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void pressed() {
        if (adapter.getSelectedTags().equals("empty"))
            done.hide();
        else done.show();
    }
}
