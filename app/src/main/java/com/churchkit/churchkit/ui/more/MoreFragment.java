package com.churchkit.churchkit.ui.more;

import static com.churchkit.churchkit.Util.setAppLanguage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
        switchPreferenceCompat  = (SwitchPreferenceCompat) findPreference("ADAPTIVE_BRIGHTNESS");

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
                adapbri();
                if (Settings.System.canWrite(getContext())) {
                    // Tu aplicación tiene permiso para escribir en la configuración del sistema
                    // Aquí puedes modificar el brillo manualmente
                } else {
                    // Solicita el permiso WRITE_SETTINGS al usuario
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                    startActivityForResult(intent, MY_PERMISSIONS_REQUEST_WRITE_SETTINGS);
                }

                return true;
            }
        });


    }


    public void adapbri(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_SETTINGS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_SETTINGS},
                    MY_PERMISSIONS_REQUEST_WRITE_SETTINGS);
        }else {
            Settings.System.putInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            Settings.System.putInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 128);
        }
    }
    private static final int MY_PERMISSIONS_REQUEST_WRITE_SETTINGS = 12432;

    SeekBarPreference seekBarPreference;
    //ListPreference
    ListPreference langListPreference;
    ListPreference darKModeListPreference;
    CKPreferences ckPreferences;
    SwitchPreferenceCompat switchPreferenceCompat;

}

/*
if (Settings.System.canWrite(this)) {
    // Tu aplicación tiene permiso para escribir en la configuración del sistema
    // Aquí puedes modificar el brillo manualmente
} else {
    // Solicita el permiso WRITE_SETTINGS al usuario
    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
    intent.setData(Uri.parse("package:" + getPackageName()));
    startActivityForResult(intent, MY_PERMISSIONS_REQUEST_WRITE_SETTINGS);
}

* */