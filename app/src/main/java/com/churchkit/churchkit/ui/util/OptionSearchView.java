package com.churchkit.churchkit.ui.util;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;

import androidx.annotation.NonNull;

import com.churchkit.churchkit.CKPreferences;
import com.churchkit.churchkit.R;
import com.churchkit.churchkit.ui.bible.BibleFragment;
import com.churchkit.churchkit.ui.song.SongHopeFragment;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class OptionSearchView implements View.OnClickListener  {
    Context context;
    MaterialAutoCompleteTextView autoCompleteTextView;
    AlertDialog.Builder dialog;
    CKPreferences ckPreferences;
    Class klass;
    AlertDialog alertDialog;

    public OptionSearchView(@NonNull MaterialAutoCompleteTextView autoCompleteTextView, Class T) {
        this.klass = T;
        this.autoCompleteTextView = autoCompleteTextView;
        this.context = autoCompleteTextView.getContext();
        this.ckPreferences = new CKPreferences(context);
         dialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.option_search_layout,null);

        dialog.setView( view );
        RadioButton fullTextSearchRb = view.findViewById(R.id.full_text_search);
        RadioButton normalRb = view.findViewById(R.id.normal_text_search);

        fullTextSearchRb.setOnClickListener(this);
        normalRb.setOnClickListener(this);



        if (klass == SongHopeFragment.class){

            fullTextSearchRb.setChecked( ckPreferences.isSongTypeSearchIsFullTextSearch() );
        }else if (klass == BibleFragment.class){
            fullTextSearchRb.setChecked( ckPreferences.isBibleTypeSearchIsFullTextSearch() );
        }


    }
    public void showDialog(){
         alertDialog = dialog.create();
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.TOP|Gravity.END);

        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
         if (klass == SongHopeFragment.class){
            switch (v.getId()){
                case R.id.normal_text_search:
                    ckPreferences.setSongTypeSearch(CKPreferences.SONG_NORMAL_SEARCH_TYPE);
                    autoCompleteTextView.setHint(context.getString(R.string.normal_search));
                    break;
                case R.id.full_text_search:ckPreferences.setSongTypeSearch(CKPreferences.SONG_FULL_TEXT_SEARCH_TYPE);
                    autoCompleteTextView.setHint(context.getString(R.string.full_text_search));
                    break;
            }
        }else if (klass == BibleFragment.class){
            switch (v.getId()){
                case R.id.normal_text_search: ckPreferences.setBibleTypeSearch( CKPreferences.BIBLE_NORMAL_SEARCH_TYPE);
                    autoCompleteTextView.setHint(context.getString(R.string.normal_search));
                break;
                case R.id.full_text_search:ckPreferences.setBibleTypeSearch( CKPreferences.BIBLE_FULL_TEXT_SEARCH_TYPE);
                    autoCompleteTextView.setHint(context.getString(R.string.full_text_search));
            }
        }
        alertDialog.dismiss();
    }
}





























