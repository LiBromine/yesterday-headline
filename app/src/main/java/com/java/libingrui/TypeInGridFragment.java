package com.java.libingrui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class TypeInGridFragment extends Fragment {
    private static String editViewText = "编辑";
    private MyGridLayout grid;
    private TextView editView;
    private OnItemClickListener onItemClickListener;
    private List<String> data;

    public static TypeInGridFragment newInstance(ArrayList<String> list) {
        TypeInGridFragment f = new TypeInGridFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(TypeActivity.TYPE_IN, list);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container == null) {
            Log.v("debug", "TypeInFragment container null");
            return null;
        }
        return inflater.inflate(R.layout.fragment_type_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initEditButton(view);

        Bundle arg = getArguments();
        assert arg != null;
        data = arg.getStringArrayList(TypeActivity.TYPE_IN);

        grid = view.findViewById(R.id.grid1);
        grid.setItems(data);
        grid.setOnItemClickListener(new MyGridLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View tv) {
                if (onItemClickListener != null) {
                    if (!editView.getText().toString().equals(getString(R.string.edit))) {
                        grid.removeView(tv);
                    }
                    onItemClickListener.onItemClick(tv);
                }
            }
        });
    }

    interface OnItemClickListener {
        void onItemClick(View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public void initEditButton(View view) {
        editView = view.findViewById(R.id.text_type_edit);
        editView.setText(editViewText);
        editView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tmp = (TextView) view;
                if (tmp.getText().toString().equals(getString(R.string.edit))) {
                    tmp.setText(R.string.done);
                    editViewText = getString(R.string.done);
                } else {
                    tmp.setText(R.string.edit);
                    editViewText = getString(R.string.edit);
                }
            }
        });
    }
}
