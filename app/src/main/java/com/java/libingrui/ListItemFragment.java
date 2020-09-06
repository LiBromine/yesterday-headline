package com.java.libingrui;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import android.util.Log;

// Instances of this class are fragments representing a single
// object in our collection.
public class ListItemFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener {
    public static final String POSITION = "position";
    public static final String CATEGORY = "category";

    RecyclerView recyclerView;
    SwipeToLoadLayout swipeToLoadLayout;
    private ListItemViewAdapter rAdapter;
    private RecyclerView.LayoutManager rLayoutManager;
    private NewsViewModel mViewModel;
    private NewsList data;
    private String category;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initRecycler(view);
        initViewModel(view);
        initSwipeView(view);
    }

    public void initSwipeView(View view) {
        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        autoRefresh();
    }

    public void initRecycler(View view) {
        Bundle args = getArguments();
        category = args.getString(CATEGORY);

        recyclerView = (RecyclerView) view.findViewById(R.id.swipe_target);
//        recyclerView.setHasFixedSize(true);
        rLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(rLayoutManager);
        Log.v("recyclerView", recyclerView.getWidth() + " " + recyclerView.getHeight());

        rAdapter = new ListItemViewAdapter(getContext());
        rAdapter.setOnItemClickListener(new ListItemViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //        TODO, set listener
                Toast.makeText(getContext(), "Click " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), NewsActivity.class);
                TextView idReg = view.findViewById(R.id.news_brief_id);
                String id = idReg.getText().toString();
                intent.putExtra(MainActivity.ID, id);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(rAdapter);
    }

    public void initViewModel(View view) {
        mViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
//        mViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(MainActivity.app)).get(NewsViewModel.class);
        mViewModel.Var_getNewsList(category).observe(this, new Observer<NewsList>() {
            @Override
            public void onChanged(@Nullable final NewsList newsList) {
                // Update the cached copy of the words in the adapter.
                if (newsList != null)  {
                    Log.v("debug", "onChanged starts");
                    data = newsList;
                    if(newsList.list != null ) Log.v("debug", "newsList length =" + newsList.list.size());
                    rAdapter.setNewsList(data);
                }
            }
        });

    }

    @Override
    public void onLoadMore() {
        mViewModel.getMoreNews();
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setLoadingMore(false);
            }
        }, 2000);
    }

    @Override
    public void onRefresh() {
        mViewModel.flushNews();
        Log.v("onRefresh", "Refreshing");
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
