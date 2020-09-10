package com.java.libingrui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EntityListActivity extends AppCompatActivity {
    public static String URL = "url";

    private RecyclerView mRecycler;
    private EntityViewAdapter mAdapter;
    private NewsViewModel mNewsViewModel;
    private String query;
    private EntityDataList list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_list);
        Intent i = getIntent();
        query = i.getStringExtra(MainActivity.QUERY);

        initToolbar(query);
        initRecycler();
        initViewModel();
        mNewsViewModel.searchEntityDataByKeyword(query);
    }

    private void initToolbar(String query) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        TextView tv = findViewById(R.id.my_profile);
        String profile = "搜索\"" + query + "\"得到的实体...";
        tv.setText(profile);
    }

    private void initRecycler() {
        mRecycler = findViewById(R.id.list_entity);
        mAdapter = new EntityViewAdapter(this);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setOnItemClickListener(new EntityViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView urlReg = view.findViewById(R.id.entity_url);
                String url = urlReg.getText().toString();
                mNewsViewModel.getEntityDataByUrl(url);

                Intent intent = new Intent(EntityListActivity.this, EntityActivity.class);
                intent.putExtra(EntityListActivity.URL, url);
                startActivity(intent);
            }
        });
        mRecycler.setAdapter(mAdapter);
    }

    private void initViewModel() {
        mNewsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        mNewsViewModel.getSearchEntityDataList().observe(this, new Observer<EntityDataList>() {
            @Override
            public void onChanged(EntityDataList entityDataList) {
                if (entityDataList != null) {
                    list = entityDataList;
                    mAdapter.setList(list);
                }
            }
        });
    }
}