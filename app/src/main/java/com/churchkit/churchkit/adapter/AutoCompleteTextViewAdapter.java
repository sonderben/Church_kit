package com.churchkit.churchkit.adapter;
/*
public class AutoCompleteTextViewAdapter {
}*/

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.churchkit.churchkit.R;
import com.churchkit.churchkit.Util;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.song.Song;

import java.util.List;

public class AutoCompleteTextViewAdapter extends ArrayAdapter<Object> implements Filterable {
    List objects;
    CharSequence mConstraint;

    public void setSongs(List objects) {
        this.objects = objects;
    }

    public AutoCompleteTextViewAdapter(@NonNull Context context) {
        super(context, 0);
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
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_auto_complete,parent,false);

        TextView songPosition = convertView.findViewById(R.id.number);
        TextView bookName = convertView.findViewById(R.id.book);
        TextView title = convertView.findViewById(R.id.title);

        Object object = getItem(position);
        if (object instanceof Song){
            Song song = (Song) object;

            songPosition.setText( Util.formatNumberToString( song.getPosition() ) );
            title.setText( song.getTitle() );
            bookName.setText( song.getBookTitle()  );
        }else if (object instanceof BibleChapter){
            BibleChapter bibleChapter = (BibleChapter) object;
            songPosition.setText( "" );
            title.setText( "");
            bookName.setText( bibleChapter.getBookAbbreviation()+" "+bibleChapter.getPosition() );
        }




        return convertView;
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
