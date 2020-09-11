package com.java.libingrui;


import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class CollectionAdapter extends FragmentStateAdapter {
    List<String> categoryList;
//    TODO, get count in db and change constructor

    public CollectionAdapter(Fragment fragment, List<String> list) {
        super(fragment);
        categoryList = list;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return a NEW fragment instance in createFragment(int)
        Fragment fragment = null;
        if (categoryList != null) {
            // TODO, create special fragment
            Log.v("debug", "create a fragment in " + categoryList.get(position) + " type");
            Log.v("debug", "createFragment  Thread.currentThread().getId() = " + Thread.currentThread().getId());
            if ((categoryList.get(position).equals("news") || categoryList.get(position).equals("paper"))) {
                fragment = new ListItemFragment();
            } else if (categoryList.get(position).equals("covid")) {
                fragment = CovidDataFragment.newInstance();
            } else if (categoryList.get(position).equals("graph")) {
                fragment = EntityFragment.newInstance();
            } else if (categoryList.get(position).equals("cluster")) {
                fragment = ClusterFragment.newInstance();
            }
        }

        if (fragment == null) {
            fragment = new Fragment();
        }
        Log.w("debug", "fragment is " + fragment);

        // give args
        Bundle args = new Bundle();
        args.putInt(ListItemFragment.POSITION, position);
        if (categoryList != null && categoryList.size() > position) {
            args.putString(ListItemFragment.CATEGORY, categoryList.get(position));
        } else {
            args.putString(ListItemFragment.CATEGORY, "Error");
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        if (categoryList != null) {
            return categoryList.size();
        } else {
            return 0;
        }
    }

    // TODO
    public void setCategoryList(List<String> list) {
        categoryList = list;
        notifyDataSetChanged();
    }
}

