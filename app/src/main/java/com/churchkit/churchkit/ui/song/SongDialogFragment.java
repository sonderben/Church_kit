package com.churchkit.churchkit.ui.song;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;

import com.churchkit.churchkit.R;
import com.churchkit.churchkit.database.ChurchKitDb;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongFavorite;
import com.churchkit.churchkit.database.entity.song.SongHistory;
import com.churchkit.churchkit.database.entity.song.Verse;
import com.churchkit.churchkit.ui.EditorBottomSheet;
import com.churchkit.churchkit.ui.util.Util;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class SongDialogFragment extends DialogFragment {
    public static SongDialogFragment newInstance(String idSong,String reference,String title){
        mSongId = idSong;
        mSongTitle = title;
        mReference = reference;
        System.out.println("SongDialogFragment: "+mReference);
        return new SongDialogFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
          root = (ViewGroup) inflater.inflate(R.layout.fragment_list_chapter,container,false);




          init();
        setHistory();

          favorite.setOnClickListener(v -> {
              SongFavorite songFavorite= churchKitDd.songFavoriteDao().isExisted(mSongId);
              favorite.setEnabled(false);

              if (songFavorite != null)
                  churchKitDd.songFavoriteDao().delete(songFavorite);
              else{
                  System.out.println("mreference: "+mReference);
                  churchKitDd.songFavoriteDao().insert(
                          new SongFavorite(mSongId,Calendar.getInstance().getTimeInMillis(), mReference)
                  );
              }

          });

          churchKitDd.songFavoriteDao().existed(mSongId).observe(getViewLifecycleOwner(), songFavorite -> {

              favorite.setEnabled(true);
              Drawable drawable = favorite.getDrawable();
              if (songFavorite != null){

                  if (drawable != null) {
                      drawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                      favorite.setImageDrawable(drawable);
                  }
              }
              else{
                  int color = getResources().getColor(R.color.white);
                  if (drawable != null) {
                      drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                      favorite.setImageDrawable(drawable);
                  }
              }

          });




        chorus.setOnClickListener(v -> scrollToChorus());



        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


        bookTitle.setText(mReference);
        songTitle.setText(mSongTitle);



        fab.setOnClickListener(x->this.dismiss());

        churchKitDd.verseDao().getAllVerseByIdSong(mSongId).observe(requireActivity(), new Observer<List<Verse>>() {
            @Override
            public void onChanged(List<Verse> verses) {
                tv.setText( listVerseToString(verses) );
            }
        });

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                setChorusButtonVisibility();
            }
        });


        tv.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            EditorBottomSheet editorBottomSheet;
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {

        menu.clear();
        mode.getMenuInflater().inflate(R.menu.selection_action_menu, menu);
        System.out.println("onCreateActionMode xiomi");

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {


        menu.clear();
        mode.getMenuInflater().inflate(R.menu.selection_action_menu, menu);
        System.out.println("onPrepareActionMode xiomi");


        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()){
            case R.id.g_image:
                viewEditor.setVisibility(View.VISIBLE);
                fab.setVisibility(View.GONE);
                editorBottomSheet = EditorBottomSheet.getInstance(mode,tv,IMAGE);
                editorBottomSheet.show(SongDialogFragment.this.getChildFragmentManager(),"");
                break;
            case R.id.book_mark:
                editorBottomSheet = EditorBottomSheet.getInstance(mode,tv,BOOK_MARK);
                editorBottomSheet.show(SongDialogFragment.this.getChildFragmentManager(),"");
                break;
        }
        return true;
    }

    int IMAGE =1;
    int BOOK_MARK =2;



    @Override
    public void onDestroyActionMode(ActionMode mode) {
        viewEditor.setVisibility(View.GONE);
        fab.setVisibility(View.VISIBLE);
        editorBottomSheet = null;
    }
});



        return root;
    }




    public void init(){
    churchKitDd = ChurchKitDb.getInstance(requireContext());
    chorus = root.findViewById(R.id.chorus);
    scrollView = root.findViewById(R.id.scrollView);
    viewEditor = root.findViewById(R.id.view_editor);
    tv  = root.findViewById(R.id.text);
    bookTitle = root.findViewById(R.id.book_name);
    songTitle = root.findViewById(R.id.chap_);
     fab = root.findViewById(R.id.fab_clos);
     favorite = root.findViewById(R.id.favorite);
}


    private  void setChorusButtonVisibility(){
        if (isPhraseVisible("-Chorus-")){
            chorus.setVisibility(View.GONE);
        }
        else
            chorus.setVisibility(View.VISIBLE);
    }


    ViewGroup root;
    TextView tv,bookTitle,songTitle,chorus;
    static String mSongId;
    static String mSongTitle;
    static String mReference;
    ConstraintLayout viewEditor;
    FloatingActionButton fab;
    ChurchKitDb churchKitDd;
    ScrollView scrollView;
    private ImageView favorite;
    private void setHistory(){

        churchKitDd.songHistoryDao().insert(
                new SongHistory(mSongId,Calendar.getInstance().getTimeInMillis(), mReference)
            );

    }

    private String listVerseToString(List<Verse> verses){
        StringBuilder verseString = new StringBuilder();
        Verse verse=null;

        if (verses != null){

            if (verses.size()!=0 && verses.get(0).getPosition()<0)
                Collections.swap(verses,0,1);

            for (int i=0;i< verses.size();i++){

                verse = verses.get(i);
                verseString.append( verse.getPosition()<0?"-Chorus-":verse.getPosition() );
                verseString.append("\n");
                verseString.append(verse.getVerse());
                verseString.append("\n");
            }


        }else {
            verseString.append("Error");
        }

        return verseString.toString();
    }

    private void scrollToChorus(){
        int index = tv.getText().toString().indexOf("-Chorus-");
        Layout layout = tv.getLayout();
        int line = layout.getLineForOffset(index);
        int y = layout.getLineTop(line);
        scrollView.smoothScrollTo(0,y);

    }
    public void scrollToSpecificPhrase(final ScrollView scrollView, final TextView textView, final String phrase) {
        textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int index = textView.getText().toString().indexOf(phrase);
                if (index != -1) {
                    Layout layout = textView.getLayout();
                    int line = layout.getLineForOffset(index);
                    int y = layout.getLineTop(line);
                    int y2 = layout.getLineBottom(line);
                    Rect rect = new Rect(0, y, textView.getWidth(), y2);
                    Rect visibleRect = new Rect();
                    textView.getLocalVisibleRect(visibleRect);
                    if (!Rect.intersects(rect, visibleRect)) {
                        scrollView.scrollTo(0, y);
                    }
                    textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }


    public boolean isPhraseVisible(@NonNull final String phrase) {
        int index = tv.getText().toString().indexOf(phrase);
        if (index == -1) {
            return false;
        }
        Layout layout = tv.getLayout();
        int line = layout.getLineForOffset(index);
        int y = layout.getLineTop(line);
        int y2 = layout.getLineBottom(line);
        Rect rect = new Rect(0, y, tv.getWidth(), y2);
        Rect visibleRect = new Rect();
        tv.getLocalVisibleRect(visibleRect);
        return Rect.intersects(rect, visibleRect);
    }



    @Override
    public void onResume() {
        super.onResume();
        setChorusButtonVisibility();
        ////set width and height of ListPartDialogFragment to 100 % to the width of screen
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width =(int) (Util.getScreenDisplayMetrics(getContext()).widthPixels * 1f);
        params.height = (int) (Util.getScreenDisplayMetrics(getContext()).heightPixels * 1f);
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    @Override
    public void onDestroy() {
        System.out.println("on destroy");
        mReference = null;
        super.onDestroy();
    }
}
