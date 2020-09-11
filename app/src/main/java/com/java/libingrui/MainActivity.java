package com.java.libingrui;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;

import java.util.List;

import android.widget.SearchView;
import android.widget.TextView;

import android.os.AsyncTask;

import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String ID = "id";
    public static final String QUERY = "query";
    public static Application app;
    public static Context context;

    private NewsViewModel mNewsViewModel;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        app = getApplication();
        if (savedInstanceState != null) {
            return;
        }

        initListCollectionFragment();
        initToolbar();
        initSearchView();

        // update covid data in Main Activity
        mNewsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        try {
            mNewsViewModel.updateEpidemicData();
        }
        catch(MyException e) {
            e.printStackTrace();
        }
    }

    public void initListCollectionFragment() {
        ListCollectionFragment fragment = new ListCollectionFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
    }

    public void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_top_option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onMainMenuItemClick(MenuItem item) {
        if (item.getTitle().toString().equals(getString(R.string.history))) {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        } else if (item.getTitle().toString().equals(getString(R.string.clear_cache))) {
            Toast.makeText(this, "缓存已清除", Toast.LENGTH_SHORT).show();
        }
    }


    public void initSearchView() {
        searchView = findViewById(R.id.my_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.setIconified(true);
                Intent i = new Intent(MainActivity.this, SearchActivity.class);
                i.putExtra(QUERY, s);
                startActivity(i);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }
}

