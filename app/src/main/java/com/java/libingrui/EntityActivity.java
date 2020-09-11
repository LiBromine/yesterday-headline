package com.java.libingrui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class EntityActivity extends AppCompatActivity {
    private NewsViewModel mNewsViewModel;
    private RecyclerView mRecyclerRelationView;
    private RecyclerView mRecyclerPropertyView;
    private EntityRelationAdapter mAdapterRelation;
    private EntityPropertyAdapter mAdapterProperty;
    private TextView labelView;
    private TextView wikiView;
    private TextView urlView;
    private ImageView imageView;
    private EntityData entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity);
        labelView = findViewById(R.id.entity_label);
        wikiView = findViewById(R.id.entity_wiki);
        urlView = findViewById(R.id.entity_url);
        imageView = findViewById(R.id.entity_image);

        initToolbar();
        initRelationRecycler();
        initPropertyRecycler();
        initViewModel();

        Intent i = getIntent();
        String url = i.getStringExtra(EntityListActivity.URL);
        String img = i.getStringExtra(EntityListActivity.IMAGE);


        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap b = getImageBitmap(img);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(b);
                    }
                });
            }
        });



        urlView.setText(url); // ?
        mNewsViewModel.getEntityDataByUrl(url);

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    private void initViewModel() {
        mNewsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        mNewsViewModel.getSelectedEntityData().observe(this, new Observer<EntityData>() {
            @Override
            public void onChanged(EntityData entityData) {
                if (entityData != null) {
                    entity = entityData;

                    // emit the img
//                    try {
//                        mNewsViewModel.getBitmapDataByUrl(entity.entityDetails.img);
//                    } catch (MyException e) {
//                        e.printStackTrace();
//                    }

                    // show the basic info
                    labelView.setText(entity.entityDetails.label);
                    EntityAbstractInfo abInfo = entity.entityDetails.abstractInfo;
                    String wiki = abInfo.enwiki + abInfo.baidu + abInfo.zhwiki;
                    wikiView.setText(wiki);

                    // show the propery and relation;
                    mAdapterRelation.setList(abInfo.COVID.relations);
                    mAdapterProperty.setList(abInfo.COVID.properties);
                }
            }
        });
        mNewsViewModel.getSelectedBitmapData().observe(this, new Observer<BitmapData>() {
            @Override
            public void onChanged(BitmapData bitmapData) {
                Log.v("debug",  "bitmap data onChanged");
                if (bitmapData != null) {
                    Log.v("debug", "bitmap data not null");
                    Log.v("debug", "length="+bitmapData.bitmap.length+" bitmap="+bitmapData.bitmap.toString());
                    imageView.setImageBitmap(BitmapByteArrayConverter.ByteArrayToBitmap(bitmapData));
                }
            }
        });
    }

    private void initRelationRecycler() {
        mRecyclerRelationView = findViewById(R.id.list_entity_relation);
        mAdapterRelation = new EntityRelationAdapter(this);
        mRecyclerRelationView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerRelationView.setAdapter(mAdapterRelation);
    }

    private void initPropertyRecycler() {
        mRecyclerPropertyView = findViewById(R.id.list_entity_property);
        mAdapterProperty = new EntityPropertyAdapter(this);
        mRecyclerPropertyView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerPropertyView.setAdapter(mAdapterProperty);
    }

    public Bitmap getImageBitmap(String url) {
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imgUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}