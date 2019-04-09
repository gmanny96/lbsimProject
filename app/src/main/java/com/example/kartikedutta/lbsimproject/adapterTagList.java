package com.example.kartikedutta.lbsimproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;

import java.util.List;

class adapterTagList extends RecyclerView.Adapter {

    LayoutInflater inflater;
    private static List<dataTag> tags;
    static interfaceTagPressed pressed;

    adapterTagList(Context c, List<dataTag> t, interfaceTagPressed p){
        inflater = LayoutInflater.from(c);
        tags = t;
        pressed = p;
    }

    private static class tagHolder extends RecyclerView.ViewHolder {
        AppCompatButton tag;

        tagHolder(final View v) {
            super(v);
            tag = v.findViewById(R.id.tag);
            tag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(tags.get(getAdapterPosition()).Selected)
                    {
                        tag.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.lightGrey));
                        tags.get(getAdapterPosition()).Selected = false;
                    }
                    else
                    {
                        tag.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimary));
                        tags.get(getAdapterPosition()).Selected = true;
                    }
                    pressed.pressed();
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
    }

    @Override
    public int getItemCount() {
        if(tags != null && tags.size()>0)return tags.size();
        return 0;
    }

    String getSelectedTags(){
        JSONArray arr = new JSONArray();
        arr.put("chosenTags");
        for(int i = 0;i<tags.size();i++){
            if(tags.get(i).Selected)
                arr.put(tags.get(i).tag);
        }
        if(arr.length()>1)
            return arr.toString();
        return "empty";
    }
}
