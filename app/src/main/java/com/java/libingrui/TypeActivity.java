package com.java.libingrui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TypeActivity extends AppCompatActivity {
    public static String TYPE_IN = "type_in";
    public static String TYPE_OUT = "type_out";

    private MyGridLayout gd1;
    private MyGridLayout gd2;
    private TextView editView;
    private ArrayList<String> typeIn;
    private ArrayList<String> typeOut;
    ChangeBounds changeBounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);

        initChangeBounds();
//        initEditButton();
        initType();
        initFragment();

        setupWindowAnimations();
    }

    public void initChangeBounds() {
        changeBounds = new ChangeBounds();
        changeBounds.setDuration(1000);
    }

    public void initEditButton() {
        editView = findViewById(R.id.text_type_edit);
        editView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tmp = (TextView) view;
                if (tmp.getText().toString().equals(getString(R.string.edit))) {
                    tmp.setText(R.string.done);
                } else {
                    tmp.setText(R.string.edit);
                }
            }
        });
    }

    public void initType() {
        // TODO, by intent
        Intent i = getIntent();
        typeIn = i.getStringArrayListExtra(TYPE_IN);
        typeOut = i.getStringArrayListExtra(TYPE_OUT);

//        typeIn = new ArrayList<>();
//        typeIn.add("news");
//        typeIn.add("paper");
//        typeOut = new ArrayList<>();
//        typeOut.add("epi");
//        typeOut.add("pro");
//        typeOut.add("graph");
    }

    public void initFragment() {
        FragmentManager fm = getSupportFragmentManager();

        TypeInGridFragment ti = TypeInGridFragment.newInstance(typeIn);
        TypeOutGridFragment to = TypeOutGridFragment.newInstance(typeOut);

        ti.setOnItemClickListener(iListener);
        ti.setSharedElementEnterTransition(changeBounds);
        to.setOnItemClickListener(oListener);
        to.setSharedElementEnterTransition(changeBounds);

        fm.beginTransaction().add(R.id.container_type_in, ti).commit();
        fm.beginTransaction().add(R.id.container_type_out, to).commit();
//        gd1 = findViewById(R.id.grid1);
//        gd2 = findViewById(R.id.grid2);
//        gd1.setItems(typeIn);
//        gd2.setItems(typeOut);
//        gd1.setOnItemClickListener(new MyGridLayout.OnItemClickListener() {
//            @Override
//            public void onItemClick(View tv) {
//                if (editView.getText() != getString(R.string.edit)) {
//                    String type = ((TextView)tv).getText().toString();
//                    typeIn.remove(type);
//                    typeOut.add(type);
//                    gd1.removeView(tv);
//                    gd2.addItem(type);
//                } else {
//                     TODO
//                }
//            }
//        });
//        gd2.setOnItemClickListener(new MyGridLayout.OnItemClickListener() {
//            @Override
//            public void onItemClick(View tv) {
//                String type = ((TextView)tv).getText().toString();
//                typeOut.remove(type);
//                typeIn.add(type);
//                gd2.removeView(tv);
//                gd1.addItem(type);
//            }
//        });
    }

    private TypeInGridFragment.OnItemClickListener iListener = new TypeInGridFragment.OnItemClickListener() {

        @Override
        public void onItemClick(View v) {
            TextView editView = findViewById(R.id.text_type_edit);
            if (!editView.getText().toString().equals(getString(R.string.edit))) {
                String type = ((TextView)v).getText().toString();
                typeIn.remove(type);
                typeOut.add(type);

                TypeOutGridFragment f = TypeOutGridFragment.newInstance(typeOut);
                f.setSharedElementEnterTransition(changeBounds);
                f.setOnItemClickListener(oListener);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_type_out, f)
                        .addSharedElement(findViewById(R.id.text_type_raw), type)
                        .commit();


            } else {
//                     TODO
            }
        }
    };

    private TypeOutGridFragment.OnItemClickListener oListener = new TypeOutGridFragment.OnItemClickListener() {
        @Override
        public void onItemClick(View v) {
            String type = ((TextView)v).getText().toString();
            typeOut.remove(type);
            typeIn.add(type);

            TypeInGridFragment f = TypeInGridFragment.newInstance(typeIn);
            f.setSharedElementEnterTransition(changeBounds);
            f.setOnItemClickListener(iListener);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_type_in, f)
                    .addSharedElement(v, type)
                    .commit();
        }
    };

    private void setupWindowAnimations() {
        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setEnterTransition(slide);
//        getWindow().setReturnTransition(slide);
    }

    @Override
    public void onBackPressed() {
        if(typeIn.size() == 0){
            Toast.makeText(getApplicationContext(), "必须至少选择一个频道", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = getIntent();
        intent.putExtra(TYPE_IN, typeIn);
        intent.putExtra(TYPE_OUT, typeOut);
        setResult(-1, intent);
        finish();
    }
}