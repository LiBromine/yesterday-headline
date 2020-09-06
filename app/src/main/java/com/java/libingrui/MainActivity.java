package com.java.libingrui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import android.view.View;

import java.util.List;

import android.widget.TextView;

import android.os.AsyncTask;

import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private NewsViewModel mNewsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNewsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);

    //    mNewsViewModel.flushNews();

        mNewsViewModel.getNewsList().observe(this, new Observer<NewsList>() {
            @Override
            public void onChanged(NewsList newsList) {
                Log.v("debug", "onChanged");
                if(newsList != null && newsList.list != null) {
                    for (News item : newsList.list) {
                        Log.v("debug", item._id);
                    }
                }
            }
        });
    }

    public void flushNews(View view) {
        mNewsViewModel.flushNews();
    }

}