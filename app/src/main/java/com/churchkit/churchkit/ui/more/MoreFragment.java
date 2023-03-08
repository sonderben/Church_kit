package com.churchkit.churchkit.ui.more;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.churchkit.churchkit.CKPreferences;
import com.churchkit.churchkit.R;

import java.util.Arrays;
import java.util.List;


public class MoreFragment extends Fragment implements View.OnClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         root =  inflater.inflate(R.layout.more_history,container,false);
         init();
         increaseFontSize.setOnClickListener(this::onClick);
         decreaseFontSize.setOnClickListener(this::onClick);

        return root;
    }
    private void init(){
        preferences = new CKPreferences(requireContext());
        increaseFontSize = root.findViewById(R.id.increase);
        decreaseFontSize = root.findViewById(R.id.decrease);
        switchChorus = root.findViewById(R.id.switch_chorus);
        switchSongAbbr = root.findViewById(R.id.switch_song_abbr);
        fontSize = root.findViewById(R.id.font_size);
        spinnerTypeFace = root.findViewById(R.id.spinner_font);
        descriptionLetterSize = root.findViewById(R.id.description_letter_size);
        descriptionLetterFont = root.findViewById(R.id.description_letter_font);
        fontSizeInteger = preferences.getLetterSize();
        fontSize.setText( String.valueOf( preferences.getLetterSize() ) );
        //descriptionLetterSize.setTextSize(fontSizeInteger);



        SpinnerAdapter adapter = new SpinnerAdapter();
        spinnerTypeFace.setAdapter(adapter);

        spinnerTypeFace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerAdapter spinAdap = (SpinnerAdapter) parent.getAdapter();

                int typeFaceInt= (int) spinAdap.getItemId(position);
                Typeface typeface = typeFaceInt == 0 ? Typeface.DEFAULT : ResourcesCompat.getFont(getContext(), typeFaceInt);
                //descriptionLetterFont.setTypeface(typeface);
                preferences.updateTypeFace(typeFaceInt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        switchChorus.setChecked(preferences.getButtonChorus());
        switchSongAbbr.setChecked(preferences.getabbrColor());
        switchChorus.setOnCheckedChangeListener((buttonView, isChecked) -> preferences.updateButtonChorus(isChecked));
        switchSongAbbr.setOnCheckedChangeListener((buttonView, isChecked) -> preferences.updateAbbrColor(isChecked));

    }
    private View root;
    private ImageButton increaseFontSize,decreaseFontSize;
    private TextView fontSize,descriptionLetterSize,descriptionLetterFont;
    private int fontSizeInteger;
    private CKPreferences preferences;
    private Spinner spinnerTypeFace;
    private Switch switchChorus,switchSongAbbr;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.increase:
                fontSizeInteger += 1;
                fontSize.setText( String.valueOf(fontSizeInteger) );
                //descriptionLetterSize.setTextSize(fontSizeInteger);
                preferences.updateLetterSize(fontSizeInteger);
                break;
            case R.id.decrease:
                if (fontSizeInteger > 12){
                    fontSizeInteger -= 1;
                    fontSize.setText( String.valueOf(fontSizeInteger) );
                    //descriptionLetterSize.setTextSize(fontSizeInteger);
                    preferences.updateLetterSize(fontSizeInteger);
                }
                break;
        }
    }

    private final class SpinnerAdapter extends BaseAdapter{

        class MyFont{
            String name;
            int id;
            public MyFont(String name,int id){
                this.id = id;
                this.name = name;
            }
            public MyFont(){}
            public List<MyFont> ListMyFont(){
                return Arrays.asList(
                        new MyFont("Sans Serif",0),
                        new MyFont("Roboto Light",R.font.robotolight),
                        new MyFont("Roboto Thin",R.font.robororhin),
                        new MyFont("Tangarine Regular",R.font.tangerine_regular)
                );
            }
        }

        List<MyFont> myFontList;
        public SpinnerAdapter(){
            myFontList = new MyFont().ListMyFont();

        }


        @Override
        public int getCount() {
            return myFontList.size();
        }

        @Override
        public Object getItem(int position) {
            return myFontList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return myFontList.get(position).id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView typeFace = new TextView( getContext() );
            typeFace.setTextSize(28);

            typeFace.setText( myFontList.get(position).name );
            int typeFaceInt= myFontList.get(position).id;
            Typeface typeface = typeFaceInt == 0 ? Typeface.DEFAULT : ResourcesCompat.getFont(getContext(), typeFaceInt);

            typeFace.setTypeface( typeface );
            return typeFace;
        }
    }

}