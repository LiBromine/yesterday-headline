package com.java.libingrui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EntityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntityFragment extends Fragment {

    private SearchView mSearchView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EntityFragment.
     */
    public static EntityFragment newInstance() {
        EntityFragment fragment = new EntityFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_entity, container, false);
        initSearchView(view);

        return view;
    }

    public void initSearchView(View view) {
        mSearchView = view.findViewById(R.id.entity_search);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mSearchView.setIconified(true);
                Intent i = new Intent(getContext(), EntityListActivity.class);
                i.putExtra(MainActivity.QUERY, s);
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