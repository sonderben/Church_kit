package com.churchkit.churchkit.ui.more;

import static com.churchkit.churchkit.Util.setAppLanguage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;
import androidx.preference.SwitchPreferenceCompat;

import com.churchkit.churchkit.CKPreferences;
import com.churchkit.churchkit.DataActivity;
import com.churchkit.churchkit.EditNoteActivity;
import com.churchkit.churchkit.R;
import com.churchkit.churchkit.database.entity.bible.BibleInfo;
import com.churchkit.churchkit.database.entity.song.SongInfo;


public class MoreFragment extends PreferenceFragmentCompat/* Fragment implements View.OnClickListener*/{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        ckPreferences = new CKPreferences(requireActivity().getApplicationContext());
        langListPreference = findPreference("LANGUAGE");
        seekBarPreference = findPreference("LETTER_SIZE");
        darKModeListPreference = findPreference("DARK_MODE");
        switchPreferenceCompat  = findPreference("SONG_ABBR_COLOR");
        fontListPreference = findPreference("FONT");
        isFontBold = findPreference("BOLD");
        biblePreference = findPreference("BIBLE");
        songPreference = findPreference("SONG");

        biblePreference.setSummary( BibleInfo.getBibleInfoNameById( ckPreferences.getBibleName() ));
        songPreference.setSummary(SongInfo.getBibleInfoNameById( ckPreferences.getSongName() ));

        ActivityResultLauncher<Intent> dataActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK){

                    Bundle bundle = result.getData().getExtras();
                   String defaultBibleId = bundle.getString("DEFAULT_BIBLE_ID");

                    if (defaultBibleId != null){
                        //getActivity().finishAffinity();
                        //System.exit(0);
                    }
                    //biblePreference.setSummary(defaultBibleId);
                }
            }
        });

        seekBarPreference.setSummary(ckPreferences.getLetterSize()+" px");


        isBoldVisibility(Integer.parseInt(fontListPreference.getValue()));


        seekBarPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            final int value = (int) newValue;
            seekBarPreference.setSummary(value+" px");
            return true;
        });

        langListPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            String lang = (String) newValue;
            setAppLanguage(getContext(),lang);
            getActivity().recreate();
            return true;
        });
        fontListPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            final int index =  Integer.parseInt((String) newValue);
            isBoldVisibility(index);
            return true;
        });

        darKModeListPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            String lang = (String) newValue;
            setAppLanguage(getContext(),lang);
            AppCompatDelegate.setDefaultNightMode(Integer.parseInt(lang));
            getActivity().recreate();
            return true;
        });

        switchPreferenceCompat.setOnPreferenceChangeListener((preference, newValue) -> true);

        biblePreference.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(getActivity(), DataActivity.class);
            intent.putExtra("FROM","BIBLE");
            dataActivity.launch(intent);
            return true;
        });
        songPreference.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(getActivity(), DataActivity.class);
            intent.putExtra("FROM","SONG");
            dataActivity.launch(intent);
            return true;
        });


    }



    private static final int MY_PERMISSIONS_REQUEST_WRITE_SETTINGS = 12432;

    SeekBarPreference seekBarPreference;

    ListPreference langListPreference;
    ListPreference darKModeListPreference,fontListPreference;
    CKPreferences ckPreferences;
    SwitchPreferenceCompat switchPreferenceCompat,isFontBold;
    Preference biblePreference,songPreference;

    private void isBoldVisibility(int index){
        switch (index){
            case 1:
            case 2:
            case 3:
            case 7:
                isFontBold.setEnabled(true);
                break;
            default:
                isFontBold.setEnabled(false);
        }
    }

    @Override
    public void onResume() {
        TextView tv= getActivity().findViewById(R.id.bible);
        if (!ckPreferences.isCurrentAndNextBibleEqual()  && !ckPreferences.isCurrentAndNextSongEqual() ){
            tv.setText(getString(R.string.restart_2_use_new_)+" "+getString(R.string.bible)+" "+getString(R.string.and)+" "+getString(R.string.song) );
           tv.setVisibility(View.VISIBLE);
        }else if (!ckPreferences.isCurrentAndNextBibleEqual()){
            tv.setText(getString(R.string.restart_2_use_new_)+" "+getString(R.string.bible) );
            tv.setVisibility(View.VISIBLE);
        }
        else if (!ckPreferences.isCurrentAndNextSongEqual()){
            tv.setText(getString(R.string.restart_2_use_new_)+" "+getString(R.string.song) );
            tv.setText( tv.getText().toString().replace("la nouvelle","le nouveau") );
            tv.setVisibility(View.VISIBLE);
        }else {
            tv.setVisibility(View.GONE);
        }
        super.onResume();
    }
}

