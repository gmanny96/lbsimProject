package com.example.kartikedutta.lbsimproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

public class fragmentSearchTags extends Fragment {

    View view;
    String text;
    RecyclerView list;
    AppCompatTextView hint;
    ProgressBar progress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        list = view.findViewById(R.id.list);
        hint = view.findViewById(R.id.hint);
        progress = view.findViewById(R.id.progress);

        if (commonData.getInstance().checkConnection())
            hint.setVisibility(View.GONE);
        else {
            hint.setText("No Connection");
            hint.setVisibility(View.VISIBLE);
        }
    }
}
