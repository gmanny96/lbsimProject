package com.example.kartikedutta.lbsimproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class adapterNewJobTagList extends RecyclerView.Adapter {

    LayoutInflater inflater;
    private static List<dataTag> tags;
    private static int tagNum = -1;
    Context context;
    private static interfaceTag tInterface;

    adapterNewJobTagList(Context c, List<dataTag> t, interfaceTag ti) {
        context = c;
        inflater = LayoutInflater.from(c);
        tags = t;
        tInterface = ti;
    }

    private static class tagHolder extends RecyclerView.ViewHolder {
        AppCompatButton tag;

        tagHolder(final View v) {
            super(v);
            tag = v.findViewById(R.id.tag);
            tag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tags.get(getAdapterPosition()).Selected) {
                        tag.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.lightGrey));
                        tags.get(getAdapterPosition()).Selected = false;
                        tagNum = -1;
                    } else {
                        tag.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimary));
                        tags.get(getAdapterPosition()).Selected = true;
                        if (tagNum != -1) tInterface.updateTag(tagNum);
                        tagNum = getAdapterPosition();
                    }
                    tInterface.pressed();
                    /*if(getAdapterPosition()==tagNum){
                        tag.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.lightGrey));
                        tags.get(getAdapterPosition()).Selected = false;
                        tagNum = -1;
                    }
                    else {
                        if(tagNum != -1)
                            tInterface.updateTag(tagNum);

                        tag.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimary));
                        tags.get(getAdapterPosition()).Selected = true;
                        tagNum = getAdapterPosition();
                    }*/
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new tagHolder(inflater.inflate(R.layout.tag_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((tagHolder) holder).tag.setText(tags.get(position).tag);
        if (tags.get(position).Selected)
            ((tagHolder) holder).tag.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        else
            ((tagHolder) holder).tag.setBackgroundColor(ContextCompat.getColor(context, R.color.lightGrey));

    }

    @Override
    public int getItemCount() {
        if (tags != null && tags.size() > 0) return tags.size();
        return 0;
    }

    String getSelectedTag() {
        if (tagNum != -1)
            return tags.get(tagNum).tag;
        return "empty";
    }

    void updateRow(int pos) {
        tags.get(pos).Selected = false;
        notifyItemChanged(pos);
    }
}
