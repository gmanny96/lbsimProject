package com.example.kartikedutta.lbsimproject;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.json.JSONArray;

import java.util.List;

public class adapterAddedJobs extends RecyclerView.Adapter {

    LayoutInflater inflater;
    private static List<dataJob> jobs = null;
    private static interfaceAddedJobs jInterface;
    Context context;

    adapterAddedJobs(Context context, List<dataJob> j, interfaceAddedJobs ji){
        this.context = context;
        inflater = LayoutInflater.from(context);
        jobs = j;
        jInterface = ji;
    }

    private static class jobHolder extends RecyclerView.ViewHolder {
        AppCompatTextView name, id, time;
        AppCompatImageButton delete;
        RelativeLayout layout;

        jobHolder(final View v) {
            super(v);
            layout = v.findViewById(R.id.layout);
            name = v.findViewById(R.id.name);
            id = v.findViewById(R.id.jid);
            time = v.findViewById(R.id.time);

            delete = v.findViewById(R.id.delete);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("job", "clicked");
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

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    jInterface.deleteJob(jobs.get(getAdapterPosition()).jid, getLayoutPosition());
                    jobs.remove(getLayoutPosition());
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new jobHolder(inflater.inflate(R.layout.delete_job_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        jobHolder job = (jobHolder) holder;
        job.name.setText(jobs.get(position).name);
        job.id.setText("ID: "+jobs.get(position).jid);
        job.time.setText(jobs.get(position).time);
    }

    void removeRow(int pos){
        notifyItemRemoved(pos);
    }

    @Override
    public int getItemCount() {
        if(jobs!=null && jobs.size()>0) return jobs.size();
        return 0;
    }
}
