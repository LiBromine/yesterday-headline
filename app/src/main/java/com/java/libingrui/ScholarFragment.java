package com.java.libingrui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class ScholarFragment extends Fragment {
    private RecyclerView mRecycler;
    private ScholarAdapter mAdapter;
    private Button mButtonAll;
    private Button mButtonDead;
    private NewsViewModel mNewsViewModel;
    private PersonList mDataAll;
    private PersonList mDataDead;

    public static ScholarFragment newInstance() {
        ScholarFragment f = new ScholarFragment();
        // add some args
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scholar, container, false);

        initRecycler(view);
        initViewModel(view);
        initButton(view);
        return view;
    }

    private void initButton(View view) {
        mButtonAll = view.findViewById(R.id.button_all_scholar);
        mButtonDead = view.findViewById(R.id.button_dead_scholar);
        mButtonAll.setText("知疫学者");
        mButtonDead.setText("追忆学者");
        mButtonAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.setList(mDataAll);
            }
        });
        mButtonDead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.setList(mDataDead);
            }
        });
    }

    private void initRecycler(View view) {
        mRecycler = view.findViewById(R.id.list_scholar);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ScholarAdapter(getContext());
        mAdapter.setOnItemClickListener(new ScholarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView idReg = view.findViewById(R.id.scholar_id);
                TextView imgReg = view.findViewById(R.id.scholar_img);
                String id = idReg.getText().toString();
                String img = imgReg.getText().toString();
                mNewsViewModel.getPersonById(id);
                try {
                    mNewsViewModel.getBitmapDataByUrl(img);
                } catch (Exception e) { }

                Log.v("debug", "exactly to here in ScholarFragment");
                Intent intent = new Intent(getContext(), ScholarDetailActivity.class);
                intent.putExtra(MainActivity.ID, id);
//                intent.putExtra(IMG, img);
                startActivity(intent);
            }
        });
        mRecycler.setAdapter(mAdapter);
    }

    private void initViewModel(View view) {
        mNewsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        mNewsViewModel.flushPerson();
        mNewsViewModel.flushPassedAwayPerson();
        // observe
        mNewsViewModel.getAllPersonList().observe(this, new Observer<PersonList>() {
            @Override
            public void onChanged(PersonList personList) {
                if (personList != null) {
                    mDataAll = personList;
                }
            }
        });
        mNewsViewModel.getPassedAwayPersonList().observe(this, new Observer<PersonList>() {
            @Override
            public void onChanged(PersonList personList) {
                if (personList != null) {
                    mDataDead = personList;
                }
            }
        });
    }
}
