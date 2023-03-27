package com.churchkit.churchkit.ui.more;

import static com.churchkit.churchkit.Util.setAppLanguage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;
import androidx.preference.SwitchPreferenceCompat;

import com.churchkit.churchkit.CKPreferences;
import com.churchkit.churchkit.R;


public class MoreFragment extends PreferenceFragmentCompat/* Fragment implements View.OnClickListener*/{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        ckPreferences = new CKPreferences(getContext());
        langListPreference =(ListPreference) findPreference("LANGUAGE");
        seekBarPreference = (SeekBarPreference) findPreference("LETTER_SIZE");
        darKModeListPreference = findPreference("DARK_MODE");
        switchPreferenceCompat  = (SwitchPreferenceCompat) findPreference("SONG_ABBR_COLOR");

        seekBarPreference.setSummary(ckPreferences.getLetterSize()+" px");


        seekBarPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            int value = (int) newValue;
            seekBarPreference.setSummary(value+" px");
            return true;
        });

        langListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                String lang = (String) newValue;
                setAppLanguage(getContext(),lang);
                getActivity().recreate();
                return true;
            }
        });

        darKModeListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                String lang = (String) newValue;
                setAppLanguage(getContext(),lang);
                AppCompatDelegate.setDefaultNightMode(Integer.parseInt(lang));
                getActivity().recreate();
                return true;
            }
        });

        switchPreferenceCompat.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {

                return true;
            }
        });


    }



    private static final int MY_PERMISSIONS_REQUEST_WRITE_SETTINGS = 12432;

    SeekBarPreference seekBarPreference;

    ListPreference langListPreference;
    ListPreference darKModeListPreference;
    CKPreferences ckPreferences;
    SwitchPreferenceCompat switchPreferenceCompat;





}

