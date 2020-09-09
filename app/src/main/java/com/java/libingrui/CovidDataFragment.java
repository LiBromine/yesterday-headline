package com.java.libingrui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class CovidDataFragment extends Fragment {
    private NewsViewModel mViewModel;

    private Spinner mSpinnerCountry;
    private Spinner mSpinnerProvince;
    private Spinner mSpinnerCounty;
    private Spinner mSpinnerStartYear;
    private Spinner mSpinnerStartMonth;
    private Spinner mSpinnerStartDay;

    private List<String> countryList;
    private List<String> provinceList;
    private List<String> countyList;
    private List<String> testList;

    public static CovidDataFragment newInstance() {
        CovidDataFragment f = new CovidDataFragment();
        // send some data by bundle
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_covid_data, container, false);
        mSpinnerCountry = view.findViewById(R.id.spinner_country);

        mViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        mViewModel.getCountryList().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                countryList = strings;
                ArrayAdapter<String> newAdapter = new ArrayAdapter<String>(MainActivity.context, R.layout.support_simple_spinner_dropdown_item, countryList);
                mSpinnerCountry.setAdapter(newAdapter);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        testList = new ArrayList<>();
        testList.add("China");
        testList.add("Chin");
        testList.add("Topology");
        ArrayAdapter<String> test = new ArrayAdapter<String>(MainActivity.context, R.layout.support_simple_spinner_dropdown_item, countryList);

    }
}
