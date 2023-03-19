package com.churchkit.churchkit.ui.more;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.churchkit.churchkit.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}