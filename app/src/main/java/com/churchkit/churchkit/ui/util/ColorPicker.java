package com.churchkit.churchkit.ui.util;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.churchkit.churchkit.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ColorPicker extends HorizontalScrollView {
    OnClicked onClicked;
    Context mContext;
    String mColorSelected;

    public ColorPicker(Context context,String colorSelected, OnClicked onClicked) {
        super(context);
        this.onClicked = onClicked;
        mColorSelected = colorSelected;
        mContext = context;
        init(context);
    }

    public interface OnClicked {
        void clicked(View v, String stringColor);
        void deleteBookMark();
    }


    public void init(Context context) {
        stringColor = new ArrayList<>();
        hLayoutTop = new LinearLayout(context);



        if (mColorSelected != null) {
            FloatingActionButton remove = new FloatingActionButton(context);
            remove.setImageResource(R.drawable.ic_clear_24);
            remove.setSize(FloatingActionButton.SIZE_MINI);
            remove.setBackgroundTintList( ColorStateList.valueOf( Color.TRANSPARENT ) );
            //remove.setElevation(0.01f);
            remove.setCompatElevation(0.01f);
            hLayoutTop.addView(remove);
            remove.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    view = v;
                    //ColorPicker.this.onClicked.clicked(v, null);
                    ColorPicker.this.onClicked.deleteBookMark();


                }
            });
        }




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

            if (mColorSelected!=null && stringColor.get(i).equalsIgnoreCase(mColorSelected)) {
                Drawable myFabSrc = getResources().getDrawable(R.drawable.ic_check_24);
                Drawable willBeWhite = myFabSrc.getConstantState().newDrawable();

                willBeWhite.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                floatingActionButton.setImageDrawable(willBeWhite);

                //floatingActionButton.setImageResource(R.drawable.bookmark_selected_24);
            }
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