package com.churchkit.churchkit;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.Preference;

import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

public class CKPreferences {
    private static final String USE_DIF_COLOR = "USE_DIF_COLOR";
    private static final String BUTTON_CHORUS = "BUTTON_CHORUS";
    Context context;
    SharedPreferences preference;
    private final String CK_PREFERENCES= "CK_PREFERENCES";
    private final String FONT_SIZE= "FONT_SIZE";
    private final String TypeFace= "TypeFace";

    public CKPreferences(Context context){
        this.context = context;
        preference = context.getSharedPreferences(CK_PREFERENCES,Context.MODE_PRIVATE);

    }

    public void updateLetterSize(int fontSize){
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(FONT_SIZE,fontSize);
        editor.apply();
    }
    public int getLetterSize(){
        return  preference.getInt(FONT_SIZE,12);
    }

    public void updateTypeFace(int typeFace){
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(TypeFace,typeFace);
        editor.apply();
    }
    public int getTypeFace(){
        return  preference.getInt(TypeFace, 0);
    }

    public List<Integer> listAllTYpeFace(){
        return Arrays.asList(R.font.robotolight,R.font.robororhin,R.font.tangerine_regular);
    }

    public void updateAbbrColor(boolean useDifColor){
        SharedPreferences.Editor editor = preference.edit();
        editor.putBoolean(USE_DIF_COLOR,useDifColor);
        editor.apply();
    }
    public boolean getabbrColor(){
        return  preference.getBoolean(USE_DIF_COLOR, false);
    }

    public void updateButtonChorus(boolean useDifColor){
        SharedPreferences.Editor editor = preference.edit();
        editor.putBoolean(BUTTON_CHORUS,useDifColor);
        editor.apply();
    }
    public boolean getButtonChorus(){
        return  preference.getBoolean(BUTTON_CHORUS, false);
    }

}
