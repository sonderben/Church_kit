package com.churchkit.churchkit.ui.util;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.churchkit.churchkit.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ColorPicker extends HorizontalScrollView {
    OnClicked onClicked;
    //Context mContext;

    public ColorPicker(Context context, OnClicked onClicked) {
        super(context);
        this.onClicked = onClicked;
       // mContext = context;
        init(context);
    }

    public interface OnClicked {
        void clicked(View v, String stringColor);
    }


    public void init(Context context) {
        stringColor = new ArrayList<>();







        hLayoutTop = new LinearLayout(context);

        FloatingActionButton remove = new FloatingActionButton(context);
        remove.setImageResource(R.drawable.bookmark);
        remove.setSize(FloatingActionButton.SIZE_MINI);
        hLayoutTop.addView(remove);

        remove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                view = v;
                ColorPicker.this.onClicked.clicked(v, null);


            }
        });


        hLayoutTop.setOrientation(LinearLayout.HORIZONTAL);

        super.addView(hLayoutTop);


        stringColor.add(red);
        stringColor.add(green);
        stringColor.add(blue);
        stringColor.add(yellow);
        stringColor.add(cyan);
        stringColor.add(purple);
        stringColor.add(ligthBlue);
        stringColor.add(orange);
        stringColor.add(brown);


        for (int i = 0; i < stringColor.size(); i++) {

            FloatingActionButton floatingActionButton = new FloatingActionButton(context);
            floatingActionButton.setSize(FloatingActionButton.SIZE_MINI);
            hLayoutTop.addView(floatingActionButton);

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(100, 100);

            params.setMargins(0, 0, 10, 10);

            floatingActionButton.setLayoutParams(params);
            floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(stringColor.get(i))));
            int finalI = i;
            floatingActionButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    view = v;
                    color = stringColor.get(finalI);
                    ColorPicker.this.onClicked.clicked(v, stringColor.get(finalI));


                }
            });


        }

    }


    View view;
    String color;


    LinearLayout hLayoutTop;


    ArrayList<String> stringColor;

    final String red = "#ff0000";
    final String green = "#00ff00";
    final String blue = "#0000ff";
    final String yellow = "#fff000";
    final String cyan = "#00ffff";
    final String purple = "#800080";
    final String ligthBlue = "#add8e6";
    final String orange = "#ffa500";
    final String brown = "#8b4513";


}