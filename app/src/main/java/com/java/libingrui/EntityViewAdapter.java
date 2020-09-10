package com.java.libingrui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.zip.Inflater;

public class EntityViewAdapter extends RecyclerView.Adapter<EntityViewAdapter.EntityHolder> {
    private EntityDataList mList;
    private Context mContext;
    private EntityViewAdapter.OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public EntityViewAdapter(Context context) {
        super();
        mContext = context;
    }
    @NonNull
    @Override
    public EntityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entity_brief_view, parent, false);
        return new EntityHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EntityHolder holder, int position) {
        if (mList != null && mList.list != null) {
            EntityData data = mList.list.get(position);
            EntityDetails details = data.entityDetails;
            holder.labelView.setText(details.label);
            holder.hotView.setText(details.hot);
            holder.urlView.setText(details.url);
        }

        if (mOnItemClickListener != null) {
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(view, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null && mList.list != null) {
            return mList.list.size();
        } else {

            return 0;
        }
    }

    public void setList(EntityDataList list) {
        mList = list;
        notifyDataSetChanged();
    }

    public static class EntityHolder extends RecyclerView.ViewHolder {
        public CardView root;
        public TextView labelView;
        public TextView hotView;
        public TextView urlView;

        public EntityHolder(@NonNull View itemView) {
            super(itemView);
            root = (CardView) itemView;
            labelView = itemView.findViewById(R.id.entity_label);
            hotView = itemView.findViewById(R.id.entity_hot);
            urlView = itemView.findViewById(R.id.entity_url);
        }
    }
}
