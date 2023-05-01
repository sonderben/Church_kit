package com.churchkit.churchkit.ui.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
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
        RadioButton verse = view.findViewById(R.id.verse);
        RadioButton ref = view.findViewById(R.id.ref);

        verse.setOnClickListener(this);
        ref.setOnClickListener(this);



        if (klass == SongHopeFragment.class){

            verse.setChecked( ckPreferences.isSongTypeSearchIsVerse() );
        }else if (klass == BibleFragment.class){
            verse.setChecked( ckPreferences.isBibleTypeSearchIsVerse() );
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
        /*if (v.getId()==R.id.close)
            alertDialog.dismiss();*/
         if (klass == SongHopeFragment.class){
            switch (v.getId()){
                case R.id.ref:
                    ckPreferences.setSongTypeSearch(CKPreferences.CHAPTER_SONG_TYPE_SEARCH);
                    autoCompleteTextView.setHint(context.getString(R.string.search_by_ref));
                    break;
                case R.id.verse:ckPreferences.setSongTypeSearch(CKPreferences.VERSE_SONG_TYPE_SEARCH);
                    autoCompleteTextView.setHint(context.getString(R.string.search_by_verse));
                    break;
            }
        }else if (klass == BibleFragment.class){
            switch (v.getId()){
                case R.id.ref: ckPreferences.setBibleTypeSearch( CKPreferences.CHAPTER_BIBLE_TYPE_SEARCH );
                    autoCompleteTextView.setHint(context.getString(R.string.search_by_ref));
                break;
                case R.id.verse:ckPreferences.setBibleTypeSearch( CKPreferences.VERSE_BIBLE_TYPE_SEARCH );
                    autoCompleteTextView.setHint(context.getString(R.string.search_by_verse));
            }
        }
        alertDialog.dismiss();
    }
}





























