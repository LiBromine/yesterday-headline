package com.java.libingrui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.List;

public class MyGridLayout extends GridLayout {

    private OnItemClickListener onItemClickListener;
    private List<String> data;
    private static int COLUMN = 4;
    private int margin = 30;

    public MyGridLayout(Context context) {
        super(context);
    }

    public MyGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    interface OnItemClickListener {
        void onItemClick(View tv);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public void addItem(String type) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.type_item, this, false);
        Button bt = v.findViewById(R.id.button_type);
        bt.setText(type);
        v.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view);
                }
            }
        });

        int cnt = getChildCount();
        GridLayout.Spec row = GridLayout.spec(cnt / COLUMN);
        GridLayout.Spec col = GridLayout.spec(cnt % COLUMN);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(row, col);
        params.width = getResources().getDisplayMetrics().widthPixels / 4 - margin * 2;
        params.setMargins(margin, margin, margin, margin);
        v.setLayoutParams(params);
        addView(v, params);
    }

    public void setItems(List<String> list) {
        data = list;
        for (final String s : list) {
            addItem(s);
        }
    }
}
