package com.java.libingrui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TypeActivity extends AppCompatActivity {
    private MyGridLayout gd1;
    private MyGridLayout gd2;
    private TextView editView;
    private List<String> typeIn;
    private List<String> typeOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);

        initEditButton();
        initType();
        initGrid();

    }

    public void initEditButton() {
        editView = findViewById(R.id.text_type_edit);
        editView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tmp = (TextView) view;
                if (tmp.getText() == getString(R.string.edit)) {
                    tmp.setText(R.string.done);
                } else {
                    tmp.setText(R.string.edit);
                }
            }
        });
    }

    public void initType() {
        // TODO
        typeIn = new ArrayList<>();
        typeIn.add("news");
        typeIn.add("paper");
        typeOut = new ArrayList<>();
        typeOut.add("epi");
        typeOut.add("pro");
        typeOut.add("graph");
    }

    public void initGrid() {
        gd1 = findViewById(R.id.grid1);
        gd2 = findViewById(R.id.grid2);
        gd1.setItems(typeIn);
        gd2.setItems(typeOut);
        gd1.setOnItemClickListener(new MyGridLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View tv) {
                if (editView.getText() != getString(R.string.edit)) {
                    Button tmp = tv.findViewById(R.id.button_type);
                    String type = tmp.getText().toString();
                    typeIn.remove(type);
                    typeOut.add(type);
                    gd1.removeView(tv);
                    gd2.addItem(type);
                } else {
                    // TODO
                }
            }
        });
        gd2.setOnItemClickListener(new MyGridLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View tv) {
                Button tmp = tv.findViewById(R.id.button_type);
                String type = tmp.getText().toString();
                typeOut.remove(type);
                typeIn.add(type);
                gd2.removeView(tv);
                gd1.addItem(type);
            }
        });
    }
}