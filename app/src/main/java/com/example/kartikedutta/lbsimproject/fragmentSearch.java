package com.example.kartikedutta.lbsimproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

public class fragmentSearch extends Fragment {

    View view;
    AppCompatEditText text;
    AppCompatImageButton search;
    ViewPager pager;
    TabLayout tabs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_page, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        text = view.findViewById(R.id.text);
        search = view.findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text.getText().toString();
            }
        });

        pager = view.findViewById(R.id.pager);
        tabs = view.findViewById(R.id.tabs);

        pager.setAdapter(new adapterChooseSearchPager(getActivity().getSupportFragmentManager()));

        tabs.setupWithViewPager(pager);

    }
}
