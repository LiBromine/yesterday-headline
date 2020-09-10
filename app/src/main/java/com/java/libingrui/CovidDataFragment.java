package com.java.libingrui;

import android.graphics.Color;
import android.graphics.Typeface;
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
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.iterators.ObjectArrayIterator;

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

        countryList = new ArrayList<>();
        provinceList = new ArrayList<>();
        countyList = new ArrayList<>();

        initListener();
        initViewModel(view);
        initLineChart(view);

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
        mSpinnerCountry.setAdapter(test);


        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            entries.add(new Entry(i, i * i));
        }
        LineDataSet lineDataSet = new LineDataSet(entries, "Label");
        lineDataSet.setColor(R.color.colorAccent);
        lineDataSet.setValueTextColor(R.color.colorPrimary);
        LineData lineData = new LineData(lineDataSet);
        mLineChart.setData(lineData);
//        setData(20, 20);
        mLineChart.invalidate();

    }

    private void initListener() {
        listenerCountry = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String country = mSpinnerCountry.getSelectedItem().toString();
                Log.w("debug", "country listener get " + country);
                mViewModel.getProvincesOfCountry(country);
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
                    mViewModel.getCountiesOfProvince(country.toString(), province.toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        };
    }

    private void initViewModel(View view) {
        Log.v("debug", "initViewModel");
        mViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        mViewModel.getStringListByType("countries").observe(this, new Observer<StringList>() {
            @Override
            public void onChanged(StringList stringList) {
                boolean flag = true;
                if(countryList == null || stringList.nameList.size() != countryList.size()) {
                    flag = false;
                }
                else {
                    for(int i = 0; i < countryList.size(); ++i)
                        if(!countryList.get(i).equals(stringList.nameList.get(i))) {
                            flag  = false;
                            break;
                        }
                }
                if(flag) {
                    return;
                }
                countryList = new ArrayList<>();
                CollectionUtils.addAll(countryList, new String[stringList.nameList.size()]);
                Collections.copy(countryList,stringList.nameList);


                // countryList = stringList.nameList;
                // Don't need to sort, because zms sort in the backend
                // Collections.sort(countryList);
                ArrayAdapter<String> newAdapter = new ArrayAdapter<>(MainActivity.context, R.layout.support_simple_spinner_dropdown_item, countryList);
                mSpinnerCountry.setAdapter(newAdapter);
            }
        });

        mViewModel.getStringListByType("provinces").observe(this, new Observer<StringList>() {
            @Override
            public void onChanged(StringList stringList) {
                boolean flag = true;
                if(provinceList == null || stringList == null || provinceList.size() != stringList.nameList.size()) {
                    flag = false;
                }
                else {
                    for(int i = 0; i < provinceList.size(); ++i)
                        if(!provinceList.get(i).equals(stringList.nameList.get(i))) {
                            flag  = false;
                            break;
                        }
                }
                if(flag) {
                    return;
                }
                if(stringList != null ) {
                    provinceList = stringList.nameList;
                }
                else {
                    provinceList = new ArrayList<>();
                }
                // Don't need to sort, because zms sort in the backend
                // Collections.sort(provinceList);
                ArrayAdapter<String> newAdapter = new ArrayAdapter<>(MainActivity.context, R.layout.support_simple_spinner_dropdown_item, provinceList);
                mSpinnerProvince.setAdapter(newAdapter);
            }
        });

        mViewModel.getStringListByType("counties").observe(this, new Observer<StringList>() {
            @Override
            public void onChanged(StringList stringList) {
                boolean flag = true;
                if(countyList == null || stringList == null || countyList.size() != stringList.nameList.size()) {
                    flag = false;
                }
                else {
                    for(int i = 0; i < countyList.size(); ++i)
                        if(!countyList.get(i).equals(stringList.nameList.get(i))) {
                            flag  = false;
                            break;
                        }
                }
                if(flag) {
                    ArrayAdapter<String> newAdapter = new ArrayAdapter<>(MainActivity.context, R.layout.support_simple_spinner_dropdown_item, countyList);
                    mSpinnerCounty.setAdapter(newAdapter);
                    return;
                }
                if(stringList != null) {
                    countyList = stringList.nameList;
                }else {
                    countyList = new ArrayList<>();
                }
                //Don't need to sort, because zms sort in the backend
                //Collections.sort(countyList);
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

    private void initLineChart(View view) {
        mLineChart = view.findViewById(R.id.line_chart);

        // enable touch gestures
        mLineChart.setTouchEnabled(true);

        mLineChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(true);
        mLineChart.setHighlightPerDragEnabled(true);

    }
}
