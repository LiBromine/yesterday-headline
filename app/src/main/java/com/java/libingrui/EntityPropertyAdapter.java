package com.java.libingrui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class EntityPropertyAdapter extends RecyclerView.Adapter<EntityPropertyAdapter.MyHolder> {

    Context mContext;
    private Map<String, String> mData;

    public EntityPropertyAdapter(Context context) {
        super();
        mContext = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entity_property, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        if (mData != null) {
            int i = 0;
            for (String key : mData.keySet()) {
                if (i == position) {
                    holder.keyView.setText(key);
                    holder.valView.setText(mData.getOrDefault(key, "null"));
                    break;
                }
                i++;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        } else {
            return 0;
        }
    }

    public void setList(Map<String, String> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        CardView root;
        TextView keyView;
        TextView valView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            root = (CardView) itemView;
            keyView = itemView.findViewById(R.id.entity_property_key);
            valView = itemView.findViewById(R.id.entity_property_val);
        }
    }
}
