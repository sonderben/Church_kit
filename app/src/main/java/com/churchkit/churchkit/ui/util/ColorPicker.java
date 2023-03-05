package com.churchkit.churchkit.ui.util;



import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ColorPicker extends HorizontalScrollView {
    OnClicked onClicked;
    public ColorPicker(Context context,OnClicked onClicked) {
        super(context);
this.onClicked = onClicked;
        init(context);
        addButtonColor();

        vLayout.addView(hLayoutTop);


        this.addView(vLayout);
    }

    public interface OnClicked{
        void clicked(View v,String stringColor);
    }




    public void init(Context context){
        stringColor=new ArrayList<>();
        vLayout=new LinearLayout(context);
        vLayout.setOrientation(LinearLayout.VERTICAL);

        hLayoutTop=new LinearLayout(context);
        hLayoutTop.setOrientation(LinearLayout.HORIZONTAL);




        imageViewArrayList=new ArrayList<>();

        stringColor.add(red);
        stringColor.add(green);
        stringColor.add(blue);
        stringColor.add(yellow);
        stringColor.add(cyan);
        stringColor.add(purple);
        stringColor.add(ligthBlue);
        stringColor.add(orange);
        stringColor.add(brown);
        buttonColor=new FloatingActionButton[stringColor.size()];

        for(int i=0;i<stringColor.size();i++){

            buttonColor[i]=new FloatingActionButton(context);

            ViewGroup.LayoutParams params=
                    new LayoutParams(100,
                            100);
            ((LayoutParams) params).setMargins(0,0,10,10);
            buttonColor[i].setLayoutParams(params);
            buttonColor[i].setBackgroundTintList( ColorStateList.valueOf( Color.parseColor(stringColor.get(i)) ) );
            int finalI = i;
            buttonColor[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    view = v;
                    color = stringColor.get(finalI);
                    //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        ColorPicker.this.onClicked.clicked(v, stringColor.get(finalI) );
                    //}

                }
            });


        }
        for (int i=0;i<buttonColor.length;i++){
            imageViewArrayList.add(buttonColor[i]);
        }
    }


    private void addButtonColor(){
        for(int a=0;a<imageViewArrayList.size();a++){
                hLayoutTop.addView(imageViewArrayList.get(a));
        }

    }

    View view;
    String color;
    LinearLayout vLayout;

    LinearLayout hLayoutTop;

    FloatingActionButton[] buttonColor;

    ArrayList<FloatingActionButton>imageViewArrayList;

    ArrayList<String>stringColor;

    final String red=         "#ff0000";
    final String green=       "#00ff00";
    final String blue=        "#0000ff";
    final String yellow=      "#fff000";
    //final String navy=        "#000080";
    final String cyan=        "#00ffff";
    //final String mayenta=     "#ff00ff";
    //final String olive=       "#808000";
    final String purple=      "#800080";
    //final String gold=        "#ffd700";
    final String ligthBlue=   "#add8e6";
    final String orange=      "#ffa500";
    //final String orangeRed=   "#808000";
    //final String aqua=        "#00ffff";
    //final String dodgerBlue=  "#1e90ff";
    //final String deepPink=    "#ff1493";
    //final String chartReuse=  "#7fff00";
    final String brown=       "#8b4513";
    //final String stateGray=   "#070809";
    //final String indigo=      "#4b0082";

}