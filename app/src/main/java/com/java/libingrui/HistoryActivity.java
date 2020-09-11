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

public class HistoryActivity extends AppCompatActivity {
    private NewsViewModel mNewsViewModel;
    private ListItemViewAdapter mAdapter;
    private RecyclerView mRecycler;
    private NewsList mNewsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initToolbar();
        initRecycler();
        initViewModel();
        mNewsViewModel.getWatchedNews();
    }

    public void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        TextView tv = findViewById(R.id.my_profile);
        String profile = getString(R.string.history);
        tv.setText(profile);
    }

    private void initViewModel() {
        mNewsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        mNewsViewModel.getListByType("watched").observe(this, new Observer<NewsList>() {

            @Override
            public void onChanged(NewsList newsList) {
                if (newsList != null) {
                    Log.v("debug", "history list size ="+newsList.list.size());
                    mNewsList = newsList;
                    mAdapter.setNewsList(mNewsList);
                }
            }
        });

    }

    private void initRecycler() {
        mRecycler = findViewById(R.id.list_history_news);
        mAdapter = new ListItemViewAdapter(this);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setOnItemClickListener(new ListItemViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView idReg = view.findViewById(R.id.news_brief_id);
                String id = idReg.getText().toString();
                mNewsViewModel.getNewsById(id);

                Intent intent = new Intent(HistoryActivity.this, NewsActivity.class);
                intent.putExtra(MainActivity.ID, id);
                startActivity(intent);
            }
        });
        mRecycler.setAdapter(mAdapter);
    }

}