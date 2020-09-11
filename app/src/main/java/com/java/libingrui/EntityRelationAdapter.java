package com.java.libingrui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EntityRelationAdapter extends RecyclerView.Adapter<EntityRelationAdapter.MyHolder> {

    Context mContext;
    private List<EntityRelation> mList;

    public EntityRelationAdapter(Context context) {
        super();
        mContext = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entity_relation, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        if (mList != null) {
            Log.v("debug", mList.get(position).relation + "/" + mList.get(position).forward + "/" + mList.get(position).label);
            holder.relationView.setText(mList.get(position).relation);
            holder.labelView.setText(mList.get(position).label);
            holder.arrowView.setImageResource(R.drawable.abc_vector_test);
            if (mList.get(position).forward) {
                holder.arrowView.setRotation(180f);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        } else {
            return 0;
        }
    }

    public void setList(List<EntityRelation> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        CardView root;
        TextView relationView;
        ImageView arrowView;
        TextView labelView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            root = (CardView) itemView;
            relationView = itemView.findViewById(R.id.entity_relation);
            arrowView = itemView.findViewById(R.id.back_arrow);
            labelView = itemView.findViewById(R.id.entity_label);
        }
    }
}
