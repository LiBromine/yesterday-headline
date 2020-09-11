package com.java.libingrui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ScholarDetailActivity extends AppCompatActivity {
    private NewsViewModel mNewsViewModel;
    private ImageView imgV;
    private ImageView candleV;
    private TextView nameV;
    private TextView affiliationV;
    private TextView positionV;
    private TextView idV;
    private TextView bioV;
    private TextView eduV;

    private Person p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholar_detail);

        nameV = findViewById(R.id.scholar_name);
        affiliationV = findViewById(R.id.scholar_affiliation);
        positionV = findViewById(R.id.scholar_position);
        idV = findViewById(R.id.scholar_id);
        imgV = findViewById(R.id.scholar_img);
        candleV = findViewById(R.id.candle);
        bioV = findViewById(R.id.scholar_bio);
        eduV = findViewById(R.id.scholar_edu);

        initToolbar();
        initViewModel();

        Intent intent = getIntent();
        String id = intent.getStringExtra(MainActivity.ID);
        mNewsViewModel.getPersonById(id);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    private void initViewModel() {
        mNewsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        mNewsViewModel.getSelectedPerson().observe(this, new Observer<Person>() {
            @Override
            public void onChanged(Person person) {
                if (person != null) {
                    p = person;
                    setText();
                }
            }
        });
        mNewsViewModel.getSelectedBitmapData().observe(this, new Observer<BitmapData>() {
            @Override
            public void onChanged(BitmapData bitmapData) {
                if (bitmapData != null) {
                    imgV.setImageBitmap(BitmapByteArrayConverter.ByteArrayToBitmap(bitmapData));
                }
            }
        });
    }

    private void setText() {
        nameV.setText(p.name + "/" +  p.name_zh);
        affiliationV.setText(p.profile.affiliation);
        positionV.setText(p.profile.position);
        idV.setText(p.id);
        if (p.is_passedaway) {
            candleV.setImageDrawable(getDrawable(R.drawable.candle));
        }
        bioV.setText(p.profile.bio);
        eduV.setText(p.profile.edu);
    }

}