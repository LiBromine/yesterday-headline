package com.java.libingrui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CovidDataFragment extends Fragment {
    private NewsViewModel mViewModel;

    private Spinner mSpinnerCountry;
    private Spinner mSpinnerProvince;
    private Spinner mSpinnerCounty;
    private Button mFindButton;
    private LineChart mLineChart;

    ArrayAdapter<String> nullAdapter = new ArrayAdapter<>(MainActivity.context, R.layout.support_simple_spinner_dropdown_item, new ArrayList<String>());

    private AdapterView.OnItemSelectedListener listenerCountry;
    private AdapterView.OnItemSelectedListener listenerProvince;

    private List<String> countryList;
    private List<String> provinceList;
    private List<String> countyList;

    private EpidemicInfo data;

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
        mSpinnerProvince = view.findViewById(R.id.spinner_province);
        mSpinnerCounty = view.findViewById(R.id.spinner_county);
        mFindButton = view.findViewById(R.id.button_covid_region_data);
        mLineChart = view.findViewById(R.id.line_chart);

        countryList = new ArrayList<>();
        provinceList = new ArrayList<>();
        countyList = new ArrayList<>();

        initListener();
        initViewModel(view);

        mSpinnerCountry.setOnItemSelectedListener(listenerCountry);
        mSpinnerProvince.setOnItemSelectedListener(listenerProvince);
        mFindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Object country = mSpinnerCountry.getSelectedItem();
                Object province = mSpinnerProvince.getSelectedItem();
                Object county = mSpinnerCounty.getSelectedItem();
                Log.w("debug", "country: " + country + "  province: " + province + "  county: " + county);
                if (country != null && province != null && county != null) {
                    mViewModel.getEpidemicInfoByRegionName(country.toString(), province.toString(), county.toString());
                } else {
                    Toast.makeText(getContext(), "三个选项均不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        List<String> testList = new ArrayList<>();
        testList.add("China");
        testList.add("Chin");
        testList.add("Topology");
        ArrayAdapter<String> test = new ArrayAdapter<>(MainActivity.context, R.layout.support_simple_spinner_dropdown_item, testList);
//        mSpinnerCountry.setAdapter(test);


        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            entries.add(new Entry(i, i * i));
        }
        LineDataSet lineDataSet = new LineDataSet(entries, "Label");
        lineDataSet.setColor(R.color.colorAccent);
        lineDataSet.setValueTextColor(R.color.colorPrimary);
        LineData lineData = new LineData(lineDataSet);
        mLineChart.setData(lineData);
        mLineChart.setScaleEnabled(true);
        mLineChart.setDragEnabled(true);
        mLineChart.invalidate();

    }

    private void initListener() {
        listenerCountry = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String country = mSpinnerCountry.getSelectedItem().toString();
                Log.w("debug", "country listener get " + country);
                mViewModel.getProvinceByCountry(country);
                mSpinnerCounty.setAdapter(nullAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        };
        listenerProvince = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object country = mSpinnerCountry.getSelectedItem();
                Object province = mSpinnerProvince.getSelectedItem();
                if (country != null && province != null) {
                    mViewModel.getCountyByProvince(country.toString(), province.toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        };
    }

    private void initViewModel(View view) {
        mViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        mViewModel.getCountryList().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                countryList = strings;
                Collections.sort(countryList);
                ArrayAdapter<String> newAdapter = new ArrayAdapter<>(MainActivity.context, R.layout.support_simple_spinner_dropdown_item, countryList);
                mSpinnerCountry.setAdapter(newAdapter);
            }
        });

        mViewModel.getProvinceList().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                provinceList = strings;
                Collections.sort(provinceList);
                ArrayAdapter<String> newAdapter = new ArrayAdapter<>(MainActivity.context, R.layout.support_simple_spinner_dropdown_item, provinceList);
                mSpinnerProvince.setAdapter(newAdapter);
            }
        });

        mViewModel.getCountyList().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                countyList = strings;
                Collections.sort(countyList);
                ArrayAdapter<String> newAdapter = new ArrayAdapter<>(MainActivity.context, R.layout.support_simple_spinner_dropdown_item, countyList);
                mSpinnerCounty.setAdapter(newAdapter);
            }
        });

        mViewModel.getSelectedEpidemicInfo().observe(this, new Observer<List<EpidemicInfo>>() {
            @Override
            public void onChanged(List<EpidemicInfo> epidemicInfos) {
//                data = epidemicInfos.get()
//                 TODO, chart
            }
        });
    }
}
