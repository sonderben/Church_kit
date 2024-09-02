package com.churchkit.churchkit.ui.more;

import static com.churchkit.churchkit.Util.setAppLanguage;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;
import androidx.preference.SwitchPreferenceCompat;

import com.churchkit.churchkit.CKPreferences;
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
        developperPreference = findPreference("DEVELOPPER");
        switchPreferenceCompat  = findPreference("SONG_ABBR_COLOR");
        fontListPreference = findPreference("FONT");
        isFontBold = findPreference("BOLD");
        biblePreference = findPreference("BIBLE");
        songPreference = findPreference("SONG");

        biblePreference.setSummary( BibleInfo.getBibleInfoNameById( ckPreferences.getBibleName() ));
        songPreference.setSummary(SongInfo.getBibleInfoNameById( ckPreferences.getSongName() ));



     /*   ActivityResultLauncher<Intent> dataActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
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
        });*/

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

        developperPreference.setOnPreferenceClickListener( preference -> {
            NavController navController = Navigation.findNavController( requireView() );
            //navController.navigate(R.id.action_developerFragment_to_dataFragment);
            navController.navigate(R.id.action_historyFragment_to_developerFragment);
            return true;
        } );



        switchPreferenceCompat.setOnPreferenceChangeListener((preference, newValue) -> true);

        biblePreference.setOnPreferenceClickListener(preference -> {
            NavController navController = Navigation.findNavController(requireView());
            navController.getGraph().findNode(R.id.dataFragment).setLabel(getString(R.string.download_bible));
            Bundle bundle = new Bundle();
            bundle.putString("FROM","BIBLE");
            navController.navigate(R.id.action_historyFragment_to_dataFragment,bundle);


            return true;
        });
        songPreference.setOnPreferenceClickListener(preference -> {
            Bundle bundle = new Bundle();
            bundle.putString("FROM","SONG");
            NavController navController = Navigation.findNavController(requireView());
            navController.getGraph().findNode(R.id.dataFragment).setLabel(getString(R.string.download_a_song_book));
            navController.navigate(R.id.action_historyFragment_to_dataFragment,bundle);
            return true;
        });


    }



    private static final int MY_PERMISSIONS_REQUEST_WRITE_SETTINGS = 12432;

    SeekBarPreference seekBarPreference;

    ListPreference langListPreference;
    ListPreference fontListPreference;
    CKPreferences ckPreferences;
    SwitchPreferenceCompat switchPreferenceCompat,isFontBold;
    Preference biblePreference,songPreference, developperPreference;

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


        super.onResume();
    }
}

