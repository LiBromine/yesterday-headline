package com.java.libingrui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// Instances of this class are fragments representing a single
// object in our collection.
public class ListItemFragment extends Fragment {
    public static final String ARG_OBJECT = "object";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter rAdapter;
    private RecyclerView.LayoutManager rLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
//        TODO, get the displayed data
        Integer tmp = args.getInt(ARG_OBJECT);
        String[] data = new String[10];
        for (int i = 0; i < 10; i++) {
            data[i] = Integer.toString(i * tmp);
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.news_brief_list_view);
        recyclerView.setHasFixedSize(true);

        rLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(rLayoutManager);
        rAdapter = new ListItemViewAdapter(data);
//        recyclerView.setOnClickListener();
//        TODO, set listener
        recyclerView.setAdapter(rAdapter);
    }
}
