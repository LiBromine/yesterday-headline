package com.java.libingrui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListItemViewAdapter extends RecyclerView.Adapter<ListItemViewAdapter.NewsViewHolder> {
    private NewsList mList;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    public static int TITLE_TRUNCATE = 30;
    public static int CONTENT_TRUNCATE = 30;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        public CardView root;
        public TextView titleView;
        public TextView textView;
        public TextView srcView;
        public TextView idView;
        public NewsViewHolder(View view) {
            super(view);
            root = (CardView) view;
            titleView = view.findViewById(R.id.news_brief_title);
            textView = view.findViewById(R.id.news_brief_text);
            srcView = view.findViewById(R.id.news_brief_source);
            idView = view.findViewById(R.id.news_brief_id);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ListItemViewAdapter(Context c) {
        mContext = c;
        //
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_brief_view, parent, false);
        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if (mList != null && mList.list != null) {
            News item = mList.list.get(position);
            holder.titleView.setText(compressTitle(item.title));
            holder.textView.setText(compressContent(item.content));
            String source = item.source + "  " + item.date;
            holder.srcView.setText(source);
            holder.idView.setText(item._id);

            if (item.is_watched == 1) {
                holder.titleView.setTextColor(MainActivity.context.getColor(R.color.myGray));
                holder.textView.setTextColor(MainActivity.context.getColor(R.color.myGray));
                holder.srcView.setTextColor(MainActivity.context.getColor(R.color.myGray));
            } else {
                holder.titleView.setTextColor(MainActivity.context.getColor(R.color.myBlack));
                holder.textView.setTextColor(MainActivity.context.getColor(R.color.myGray));
                holder.srcView.setTextColor(MainActivity.context.getColor(R.color.myBlack));
            }

        } else {
            holder.titleView.setText("No Title");
            holder.textView.setText("No text");
            holder.srcView.setText("No source");
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

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mList != null && mList.list != null) {
            return mList.list.size();
        } else {
            return 0;
        }
    }

    void setNewsList(NewsList list) {
        mList = list;
        notifyDataSetChanged();
    }

    private String compressTitle(String title) {
        if (title.length() > TITLE_TRUNCATE) {
            return title.substring(0, TITLE_TRUNCATE) + "...";
        } else {
            return title;
        }
    }

    private String compressContent(String content) {
        if (content.length() > CONTENT_TRUNCATE) {
            return content.substring(0, CONTENT_TRUNCATE) + "...";
        } else {
            return content;
        }
    }

}
