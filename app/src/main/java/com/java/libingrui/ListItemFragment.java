package com.java.libingrui;


import android.app.AppComponentFactory;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Instances of this class are fragments representing a single
// object in our collection.
public class ListItemFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener {
    public static final String POSITION = "position";
    public static final String CATEGORY = "category";

    private RecyclerView recyclerView;
    private SwipeToLoadLayout swipeToLoadLayout;
    private ListItemViewAdapter rAdapter;
    private RecyclerView.LayoutManager rLayoutManager;
    private NewsViewModel mViewModel;
    private NewsList data;
    private String category;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initSwipeView(view);
        initRecycler(view);
        initViewModel(view);
    }

    public void initSwipeView(View view) {
        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout)
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        autoRefresh();
    }

    public void initRecycler(View view) {
        Bundle args = getArguments();
//        TODO, get the displayed data
        Integer tmp = args.getInt(POSITION);
        category = args.getString(CATEGORY);

        recyclerView = (RecyclerView) view.findViewById(R.id.news_brief_list_view);
        recyclerView.setHasFixedSize(true);

        rLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(rLayoutManager);

        rAdapter = new ListItemViewAdapter(getContext());
        rAdapter.setOnItemClickListener(new ListItemViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //        TODO, set listener
                Toast.makeText(getContext(), "Click " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), NewsActivity.class);
                TextView idReg = view.findViewById(R.id.news_brief_id);
                String id = idReg.getText().toString();
                intent.putExtra(MainActivity.TEST, id);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(rAdapter);
    }

    public void initViewModel(View view) {
        // TODO, unsure
        mViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
//        mViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(MainActivity.app)).get(NewsViewModel.class);
        mViewModel.getNewsList(category).observe(this, new Observer<NewsList>() {
            @Override
            public void onChanged(@Nullable final NewsList newsList) {
                // Update the cached copy of the words in the adapter.
                data = newsList;
                rAdapter.setNewsList(data);
            }
        });

    }

    @Override
    public void onLoadMore() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setLoadingMore(false);
            }
        }, 2000);
    }

    @Override
    public void onRefresh() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(false);
            }
        }, 2000);
    }

    private void autoRefresh() {
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
    }
}
