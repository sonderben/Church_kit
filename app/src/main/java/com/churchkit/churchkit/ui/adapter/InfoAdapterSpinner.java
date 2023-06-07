package com.churchkit.churchkit.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.churchkit.churchkit.CKPreferences;
import com.churchkit.churchkit.R;
import com.churchkit.churchkit.database.entity.base.BaseInfo;
import com.churchkit.churchkit.database.entity.bible.BibleInfo;

import java.util.Collections;
import java.util.List;

public class InfoAdapterSpinner extends ArrayAdapter<BaseInfo> {
    List<BaseInfo>baseInfoList;

    public List<BaseInfo> getBaseInfoList() {
        return baseInfoList;
    }

    Context context;

    public InfoAdapterSpinner(@NonNull Context context, List<BaseInfo>baseInfoList) {
        super(context, 0);
        this.context = context;
        this.baseInfoList = baseInfoList;
        if (baseInfoList.size() > 1){
            CKPreferences ckPreferences = new CKPreferences(context);
            int pos = 0;


            String bibleName = baseInfoList.get(0) instanceof BibleInfo? ckPreferences.getBibleName():
                    ckPreferences.getSongName();
            for (int i = 0; i < baseInfoList.size(); i++) {
                if (baseInfoList.get(i).getId().equalsIgnoreCase(bibleName)){
                    pos = i;
                    break;
                }
            }

            Collections.swap(baseInfoList,0,pos); // put default info at first
        }
    }





    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,Color.WHITE,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,0,parent);
    }

    @Override
    public int getCount() {
        return baseInfoList.size();
    }

    private View initView(int position, int color, @NonNull ViewGroup parent){
       View view = LayoutInflater.from(context).inflate(R.layout.item_info_spinner,parent,false);
        TextView textView = view.findViewById(R.id.info);
        if (color != 0)
            textView.setTextColor(color);
        textView.setText( baseInfoList.get(position).getAbbreviation().substring(0,6) );
        return view;
    }
}
