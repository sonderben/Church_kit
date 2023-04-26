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
    SharedPreferences preference;




    public CKPreferences(Context context){
        this.context = context;
        preference = context.getSharedPreferences(CK_PREFERENCES,Context.MODE_PRIVATE);
        //SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);

        settingPreferences = PreferenceManager.getDefaultSharedPreferences(context);

    }

    public static final int CHAPTER_BIBLE_TYPE_SEARCH = 1;
    public static final int VERSE_BIBLE_TYPE_SEARCH = 2;

    public static final int CHAPTER_SONG_TYPE_SEARCH = 1;
    public static final int VERSE_SONG_TYPE_SEARCH = 2;
    public int getBibleTypeSearch(){
        return preference.getInt("BIBLE_TYPE_SEARCH",CHAPTER_BIBLE_TYPE_SEARCH);
    }
    public void setBibleTypeSearch(int bibleType){
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt("BIBLE_TYPE_SEARCH", bibleType);
        editor.apply(); //commit the changes asynchronously

    }

    public int getSongTypeSearch(){
        return preference.getInt("SONG_TYPE_SEARCH",CHAPTER_SONG_TYPE_SEARCH);
    }
    public void setSongTypeSearch(int songType){
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt("SONG_TYPE_SEARCH", songType);
        editor.apply(); //commit the changes asynchronously

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

    private boolean isTextBold(){
        return settingPreferences.getBoolean("BOLD",false);
    }
    public int getTypeFace(){
        int font = Integer.parseInt(settingPreferences.getString("FONT","0"));
        switch (font){
            case 1:if (isTextBold()){
                return R.font.roboto_bold;
            }else {
                return R.font.robotolight;
            }
            case 2:if (isTextBold()){
                return R.font.roboto_bold;
            }else {
                return R.font.roboto_thin;
            }
            case 3:if (isTextBold()){
                return R.font.tangerine_bold;
            }else {
                return R.font.tangerine_regular;
            }
            case 4: return R.font.fasthand;
            case 5: return R.font.great_vibes;
            case 6: return R.font.indie_flower;
            case 7:if (isTextBold()){
                return R.font.sono_bold;
            }else {
                return R.font.sono_regular;
            }
            default: return 0;
        }



    }


    public String getBibleName(){
        return preference.getString("DEFAULT_BIBLE","null");
    }

    public boolean setBibleName(String bibleName){
        SharedPreferences.Editor editor = preference.edit();
        editor.putString("DEFAULT_BIBLE", bibleName);
        return editor.commit(); //commit the changes asynchronously
    }
    public String getNextBibleName(){
        return preference.getString("NEXT_DEFAULT_BIBLE","null");
    }

    public boolean setNextBibleName(String bibleName){
        SharedPreferences.Editor editor = preference.edit();
        editor.putString("NEXT_DEFAULT_BIBLE", bibleName);
       return editor.commit(); //commit the changes synchronously
    }

    public boolean isCurrentAndNextBibleEqual(){
        return getNextBibleName().equalsIgnoreCase(getBibleName());
    }
    ///////
    public String getSongName(){
        return preference.getString("DEFAULT_SONG","nil");
    }

    public boolean setSongName(String bibleName){
        SharedPreferences.Editor editor = preference.edit();
        editor.putString("DEFAULT_SONG", bibleName);
        return editor.commit(); //commit the changes asynchronously
    }
    public String getNextSongName(){
        return preference.getString("NEXT_DEFAULT_SONG","nil");
    }

    public boolean setNextSongName(String bibleName){
        SharedPreferences.Editor editor = preference.edit();
        editor.putString("NEXT_DEFAULT_SONG", bibleName);
        return editor.commit(); //commit the changes synchronously
    }

    public boolean isCurrentAndNextSongEqual(){
        return getNextSongName().equalsIgnoreCase(getSongName());
    }


}










