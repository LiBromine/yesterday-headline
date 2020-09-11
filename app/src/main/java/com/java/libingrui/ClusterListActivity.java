package com.java.libingrui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ClusterListActivity extends AppCompatActivity {
    public static String TYPE = "type";
    public static String NAME = "name";

    private NewsViewModel mNewsViewModel;
    private ListItemViewAdapter mAdapter;
    private RecyclerView mRecycler;
    private int index;
    private String name;
    private NewsList mNewsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cluster_list);
        Intent i = getIntent();
        index = i.getIntExtra(TYPE, 0);
        name = i.getStringExtra(NAME);

        initToolbar(name);
        initRecycler();
        initViewModel();
        mNewsViewModel.getEventListByType(index);
    }

    public void initToolbar(String query) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        TextView tv = findViewById(R.id.my_profile);
        String profile = "类别\"" + query + "\"中的新闻...";
        tv.setText(profile);
    }

    private void initViewModel() {
        mNewsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        mNewsViewModel.getListByType("event").observe(this, new Observer<NewsList>() {
            @Override
            public void onChanged(NewsList newsList) {
                if (newsList != null) {
                    Log.v("debug", "search list size ="+newsList.list.size());
                    mNewsList = newsList;
                    mAdapter.setNewsList(mNewsList, false);
                }
            }
        });

    }

    private void initRecycler() {
        mRecycler = findViewById(R.id.list_cluster_news);
        mAdapter = new ListItemViewAdapter(this);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setOnItemClickListener(new ListItemViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //        TODO, set listener
//                TextView idReg = view.findViewById(R.id.news_brief_id);
//                String id = idReg.getText().toString();
//                mNewsViewModel.getNewsById(id);

//                Intent intent = new Intent(ClusterListActivity.this, NewsActivity.class);
//                intent.putExtra(MainActivity.ID, id);
//                startActivity(intent);
            }
        });
        mRecycler.setAdapter(mAdapter);
    }

}