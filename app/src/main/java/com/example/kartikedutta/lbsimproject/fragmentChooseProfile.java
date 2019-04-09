package com.example.kartikedutta.lbsimproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class fragmentChooseProfile extends Fragment {

    View view;
    ViewPager pager;
    TabLayout tabs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.new_profile,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        pager = view.findViewById(R.id.pager);
        tabs = view.findViewById(R.id.tabs);

        pager.setAdapter(new adapterChooseProfilePager(getActivity().getSupportFragmentManager()));

        tabs.setupWithViewPager(pager);
    }
}
