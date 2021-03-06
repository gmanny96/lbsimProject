package com.example.kartikedutta.lbsimproject;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.json.JSONArray;

import java.util.List;

class adapterStarredList extends RecyclerView.Adapter {

    LayoutInflater inflater;
    private static List<dataJob> jobs;
    Context context;
    private static interfaceJobs jInterface;

    adapterStarredList(Context context, List<dataJob> j, interfaceJobs ji){
        this.context = context;
        inflater = LayoutInflater.from(context);
        jobs = j;
        jInterface = ji;
    }

    private static class jobHolder extends RecyclerView.ViewHolder {
        AppCompatTextView name, company, time;
        AppCompatImageButton star;
        RelativeLayout layout;

        jobHolder(View v) {
            super(v);
            layout = v.findViewById(R.id.layout);
            name = v.findViewById(R.id.name);
            company = v.findViewById(R.id.company);
            time = v.findViewById(R.id.time);
            star = v.findViewById(R.id.star);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JSONArray arr = new JSONArray();
                    arr.put(jobs.get(getAdapterPosition()).jid);
                    arr.put(jobs.get(getAdapterPosition()).name);
                    arr.put(jobs.get(getAdapterPosition()).company);
                    arr.put(jobs.get(getAdapterPosition()).desc);
                    arr.put(jobs.get(getAdapterPosition()).starred);
                    arr.put(jobs.get(getAdapterPosition()).tag);
                    arr.put(jobs.get(getAdapterPosition()).time);
                    jInterface.openJobPage(arr.toString());
                }
            });

            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    jInterface.unStarredJob(jobs.get(getAdapterPosition()).jid, getAdapterPosition());
                    jobs.remove(getLayoutPosition());
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new jobHolder(inflater.inflate(R.layout.job_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        jobHolder job = (jobHolder) holder;

        job.name.setText(jobs.get(position).name);
        job.company.setText(jobs.get(position).company);
        job.time.setText(jobs.get(position).time);
        job.star.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
    }

    @Override
    public int getItemCount() {
        if(jobs!=null && jobs.size()>0) return jobs.size();
        return 0;
    }

    void removeRow(int pos){
        notifyItemRemoved(pos);
    }
}
