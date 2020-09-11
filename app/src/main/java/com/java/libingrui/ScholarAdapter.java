package com.java.libingrui;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class ScholarAdapter extends RecyclerView.Adapter<ScholarAdapter.MyHolder> {

    private Context mContext;
    private OnItemClickListener onItemClickListener;
    private PersonList mData;
    private NewsViewModel mNewsViewModel;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public ScholarAdapter(Context context) {
        super();
        mContext = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scholar_brief_view, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        if (mData != null && mData.list != null) {
            Person p = mData.list.get(position);
            Profile pf = p.profile;
            holder.nameV.setText(p.name + "/" + p.name_zh);
            holder.affiliationV.setText(pf.affiliation);
            holder.positionV.setText(pf.position);
        }
    }

    @Override
    public int getItemCount() {
        if (mData != null && mData.list != null) {
            return mData.list.size();
        } else {
            return 0;
        }
    }

    void setList(PersonList list) {
        if (list != null) {
            mData = list;
            notifyDataSetChanged();
        }
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public CardView root;
        public ImageView imgV;
        public TextView nameV;
        public TextView affiliationV;
        public TextView positionV;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imgV = itemView.findViewById(R.id.scholar_img);
            nameV = itemView.findViewById(R.id.scholar_name);
            affiliationV = itemView.findViewById(R.id.scholar_affiliation);
            positionV = itemView.findViewById(R.id.scholar_position);
        }
    }
}
