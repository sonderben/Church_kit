package com.churchkit.churchkit;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

public class CKPreferences {
    //private static final String USE_DIF_COLOR = "USE_DIF_COLOR";
    //private static final String BUTTON_CHORUS = "BUTTON_CHORUS";
    Context context;
    //SharedPreferences preference;
    private final String CK_PREFERENCES= "CK_PREFERENCES";
    private final String FONT_SIZE= "FONT_SIZE";
    private final String TypeFace= "TypeFace";
    private static  SharedPreferences settingPreferences = null;




    public CKPreferences(Context context){
        this.context = context;
        //preference = context.getSharedPreferences(CK_PREFERENCES,Context.MODE_PRIVATE);
        settingPreferences = PreferenceManager.getDefaultSharedPreferences(context);

    }
    public static SharedPreferences getSettingPref(Context context){
        if (settingPreferences == null){
            settingPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            return settingPreferences;
        }
        return settingPreferences;
    }

    public  String getLanguage(){
        //LANGUAGE
        return settingPreferences.getString("LANGUAGE","fr");
    }

    public  int getLetterSize(){
        return settingPreferences.getInt("LETTER_SIZE", Integer.parseInt(context.getString(R.string.letter_size)));
    }
    public boolean getabbrColor(){
        return  settingPreferences.getBoolean("SONG_ABBR_COLOR", false);
    }
    public boolean getButtonChorus(){
        return  settingPreferences.getBoolean("CHORUS", false);
    }
    public int getDarkMode(){

                //AUto == 2
                //Off =  0
                // on ==  1


        int darkMOde = Integer.parseInt(settingPreferences.getString("DARK_MODE","2"));
        switch (darkMOde){
            case 0: return AppCompatDelegate.MODE_NIGHT_NO;
            case 1: return AppCompatDelegate.MODE_NIGHT_YES;
            default:return AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        }
    }
    public int getTypeFace(){
        int font = Integer.parseInt(settingPreferences.getString("FONT","0"));
        System.out.println("settingPreferences:"+ settingPreferences.getString("FONT","0") );
        switch (font){
            case 1: return R.font.robotolight;
            case 2: return R.font.roboto_thin;
            case 3: return R.font.tangerine_regular;
            case 4: return R.font.fasthand;
            case 5: return R.font.great_vibes;
            case 6: return R.font.indie_flower;
            case 7: return R.font.sono_regular;
            default: return 0;
        }



    }

}










