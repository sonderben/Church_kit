package com.churchkit.churchkit.ui.adapter;
/*
public class AutoCompleteTextViewAdapter {
}*/

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.churchkit.churchkit.CKPreferences;
import com.churchkit.churchkit.R;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.bible.BibleVerse;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.Verse;
import com.churchkit.churchkit.ui.bible.BibleFragment;

import java.util.List;

public class AutoCompleteTextViewAdapter extends ArrayAdapter<Object> implements Filterable {
    List objects;
    CharSequence mConstraint;
    CKPreferences ckPreferences;
    Class aClass;

    public void setSongs(List objects) {
        this.objects = objects;
    }

    public AutoCompleteTextViewAdapter(@NonNull Context context,Class c) {
        super(context, 0);
        aClass=c;
        ckPreferences = new CKPreferences(getContext());
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Object object = getItem(position);

        if ( aClass == BibleFragment.class){

            /*if (object instanceof BibleChapter){
                if(convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_auto_complete_song, parent, false);
                }

                TextView songPosition = convertView.findViewById(R.id.number);
                TextView bookName = convertView.findViewById(R.id.book);
                TextView title = convertView.findViewById(R.id.title);


                BibleChapter bibleChapter = (BibleChapter) object;
                songPosition.setText( "" );
                title.setText( "");
                bookName.setText( bibleChapter.getBookAbbreviation()+" "+bibleChapter.getPosition() );
            }else {*/
                if(convertView == null)
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_auto_complete_verse,parent,false);
                TextView bibleVerseTextView = convertView.findViewById(R.id.full_text_search);
                BibleVerse bibleVerse = (BibleVerse) object;
                String verseTextTemp = bibleVerse.getVerseText();
                String verseText = strong(mConstraint.toString(),verseTextTemp);
                Spanned temp = Html.fromHtml("<b> <b> "+bibleVerse.getReference()+"</b> </b>"+" "+verseText);

                bibleVerseTextView.setText(temp);
            //}

        } else {
            /*if (object instanceof Song){
                if(convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_auto_complete_song, parent, false);
                }

                TextView songPosition = convertView.findViewById(R.id.number);
                TextView bookName = convertView.findViewById(R.id.book);
                TextView title = convertView.findViewById(R.id.title);


                Song bibleChapter = (Song) object;
                songPosition.setText( "" );
                title.setText( "");
                bookName.setText( bibleChapter.getBookAbbreviation()+" "+bibleChapter.getPosition() );
            }else {*/
                if(convertView == null)
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_auto_complete_verse,parent,false);
                TextView bibleVerseTextView = convertView.findViewById(R.id.full_text_search);
                Verse bibleVerse = (Verse) object;
                String verseTextTemp = bibleVerse.getVerse();
                String verseText = strong(mConstraint.toString(),verseTextTemp);
                Spanned temp = Html.fromHtml("<b> <b> "+bibleVerse.getReference()+"</b> </b>"+" "+verseText);

                bibleVerseTextView.setText(temp);
            //}
        }


        return convertView;
    }
    private String strong(String searchText,String verseText){
        String verseTextText = verseText;
        if ( verseText!=null && verseText.length() > 2 ){

            String []searchTextArray = searchText.split(" ");
            for (int i = 0; i < searchTextArray.length; i++) {
                int index = verseText.toUpperCase().indexOf( searchTextArray[i].toUpperCase() );
                System.out.println("condition: "+searchText+" :"+ index );

                if (index>-1){
                    String oldText = verseText.substring(index).split(" ")[0];
                    verseTextText = verseText.replace(oldText,"<font color='#121C64'>"+oldText/*searchTextArray[i]*/+"</font>");

                }
            }
        }
        return verseTextText;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            public CharSequence convertResultToString(Object resultValue) {
                /*if ( resultValue instanceof Song)
                    return ((Song )resultValue).getTitle();
                else if ( resultValue instanceof BibleChapter )
                    return  ((BibleChapter )resultValue).getBibleBookAbbr()+ " " + ((BibleChapter )resultValue).getPosition();
                else*/
                    return mConstraint;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                mConstraint = constraint;
                FilterResults filterResults = new FilterResults();
                filterResults.count=objects.size();
                filterResults.values=objects;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
        return filter;
    }
}
