package com.java.libingrui;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

public class ClusterFragment extends Fragment {
    private NewsViewModel mNewsViewModel;
    private List<String> mData;
    private LinearLayout mLinearLayout;
    private TagAdapter<String> mAdapter;
    private int init = 0;

    public ClusterFragment() {
    }

    public static ClusterFragment newInstance() {
        ClusterFragment f = new ClusterFragment();
        // add some args
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cluster, container, false);

        mLinearLayout = view.findViewById(R.id.linear_layout);
        mData = new ArrayList<>();

        mNewsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        mNewsViewModel.flushEventNews();
        mNewsViewModel.getStringListByType("keywords").observe(this, new Observer<StringList>() {
            @Override
            public void onChanged(StringList stringList) {
                if (stringList != null && stringList.nameList != null && init == 0) {
                    Log.v("debug", "cluster init");
                    mLinearLayout.removeAllViews();
                    mData = stringList.nameList;
                    for (int i = 0; i < mData.size(); i++) {
                        CardView root = (CardView) inflater.inflate(R.layout.cluster_item, null, false);
                        TextView cn = root.findViewById(R.id.cluster_name);
                        TextView ci = root.findViewById(R.id.cluster_index);
                        cn.setText(mData.get(i));
                        ci.setText(String.valueOf(i));
                        root.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                TextView cn = root.findViewById(R.id.cluster_name);
                                TextView ci = view.findViewById(R.id.cluster_index);
                                String name = cn.getText().toString();
                                int idx = Integer.parseInt(ci.getText().toString());
                                Intent intent = new Intent(getContext(), ClusterListActivity.class);
                                intent.putExtra(ClusterListActivity.TYPE, idx);
                                intent.putExtra(ClusterListActivity.NAME, name);

                                mNewsViewModel.getEventListByType(idx);
                                startActivity(intent);
                            }
                        });
                        mLinearLayout.addView(root);

                    }
                }
            }
        });

        return view;
    }
}
