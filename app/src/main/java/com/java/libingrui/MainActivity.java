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

public class MainActivity extends AppCompatActivity {
    public static final String ID = "id";
    public static final String QUERY = "query";
    public static Application app;

    private NewsViewModel mNewsViewModel;
    private SearchView searchView;
    ActivityOptionsCompat options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = getApplication();
        options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
        if (savedInstanceState != null) {
            return;
        }

        initListCollectionFragment();
        initToolbar();
        initSearchView();
        setupWindowAnimations();
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

    public void onGroupItemClick(MenuItem item) {
        // One of the group items (using the onClick attribute) was clicked
        // The item parameter passed here indicates which item it is
        // All other menu item clicks are handled by <code><a href="/reference/android/app/Activity.html#onOptionsItemSelected(android.view.MenuItem)">onOptionsItemSelected()</a></code>
    }

    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(1000);
//        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);
    }

    public void initSearchView() {
        searchView = findViewById(R.id.my_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Intent i = new Intent(MainActivity.this, SearchActivity.class);
                i.putExtra(QUERY, s);
                startActivity(i, options.toBundle());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }
}

