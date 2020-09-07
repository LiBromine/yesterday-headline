package com.java.libingrui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class TypeOutGridFragment extends Fragment {
    private MyGridLayout grid;
    private OnItemClickListener onItemClickListener;
    private List<String> data;

    public static TypeOutGridFragment newInstance(ArrayList<String> list) {
        TypeOutGridFragment f = new TypeOutGridFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(TypeActivity.TYPE_OUT, list);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container == null) {
            Log.v("debug", "TypeOutFragment container null");
            return null;
        }
        return inflater.inflate(R.layout.fragment_type_out, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        Bundle arg = getArguments();
        assert arg != null;
        data = arg.getStringArrayList(TypeActivity.TYPE_OUT);

        grid = view.findViewById(R.id.grid2);
        grid.setItems(data);
        grid.setOnItemClickListener(new MyGridLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View tv) {
                if (onItemClickListener != null) {
                    grid.removeView(tv);
                    onItemClickListener.onItemClick(tv);
                }
            }
        });
    }

    interface OnItemClickListener {
        void onItemClick(View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }
}
