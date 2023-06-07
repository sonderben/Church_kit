package com.churchkit.churchkit.ui.song;


import static com.churchkit.churchkit.Util.BOOK_MARK;
import static com.churchkit.churchkit.Util.IMAGE;
import static com.churchkit.churchkit.Util.formatNumberToString;
import static com.churchkit.churchkit.ui.aboutapp.Payment.startPayment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.churchkit.churchkit.CKPreferences;
import com.churchkit.churchkit.PhoneInfo;
import com.churchkit.churchkit.R;
import com.churchkit.churchkit.database.entity.song.BookMarkSong;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongFavorite;
import com.churchkit.churchkit.database.entity.song.SongHistory;
import com.churchkit.churchkit.database.entity.song.Verse;
import com.churchkit.churchkit.modelview.song.SongBookMarkViewModel;
import com.churchkit.churchkit.modelview.song.SongFavoriteViewModel;
import com.churchkit.churchkit.modelview.song.SongHistoryViewModel;
import com.churchkit.churchkit.modelview.song.SongVerseViewModel;
import com.churchkit.churchkit.ui.EditorBottomSheet;
import com.churchkit.churchkit.ui.util.Util;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.razorpay.Checkout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


public class SongDialogFragment extends DialogFragment implements View.OnClickListener{
    public static final int SONG_BOOKMARK = 1;

    public static SongDialogFragment newInstance(Song song){
        mSong = song;
        mSongId = song.getId();
        mSongTitle = song.getTitle();
        mReference = formatNumberToString(song.getPosition()) +song.getBookAbbreviation();

        return new SongDialogFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
          root = (ViewGroup) inflater.inflate(R.layout.fragment_list_chapter,container,false);

        ckp = new CKPreferences(getContext());


        songHistoryViewModel = ViewModelProvider.AndroidViewModelFactory.
                getInstance(getActivity().getApplication()).create(SongHistoryViewModel.class);
        songFavoriteViewModel = ViewModelProvider.AndroidViewModelFactory.
                getInstance(getActivity().getApplication()).create(SongFavoriteViewModel.class);
        songBookMarkViewModel = ViewModelProvider.AndroidViewModelFactory.
                getInstance(getActivity().getApplication()).create(SongBookMarkViewModel.class);
        songVerseViewModel = ViewModelProvider.AndroidViewModelFactory.
                getInstance(getActivity().getApplication()).create(SongVerseViewModel.class);
          init();
        setHistory();

          favorite.setOnClickListener(this::onClick);
          more.setOnClickListener(this::onClick);
          donate.setOnClickListener(this::onClick);

         liveDataBookMarkSong= songBookMarkViewModel.getAllBookMark(mSongId);

        songFavoriteLiveData=songFavoriteViewModel.getSongFavoriteWithSongId(ckp.getSongName(), mSongId);
        songFavoriteLiveData.observe(getViewLifecycleOwner(), songFavorite -> {

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


         /* churchKitDd.bookMarkSongDao().getAllBookMark(mSongId).observe(getViewLifecycleOwner(), bookMarkSongs -> {

              tv.setText(allVerse);
             if (bookMarkSongs.size() != 0){
                 Spannable spannable = (Spannable) tv.getText();


                 for (int i = 0; i < bookMarkSongs.size(); i++) {
                     int start = bookMarkSongs.get(i).getStart();
                     int end = bookMarkSongs.get(i).getEnd();



                     int color = Color.parseColor(bookMarkSongs.get(i).getColor().replace("#", "#6b"));


                     ClickableSpan clickableSpan = new ClickableSpan() {
                         @Override
                         public void onClick(@NonNull View widget) {
                            int index = bookMarkSongs.indexOf( new BookMarkSong("1","1","1",start,end,mSongId) );
                           BookMarkSong bookMarkSong = bookMarkSongs.get(index);

                           EditorBottomSheet editorBottomSheet = EditorBottomSheet.getInstance(bookMarkSong,tv,SONG_BOOKMARK,2,mSongId);
                           editorBottomSheet.show(getChildFragmentManager(),"");
                         }
                         @Override
                         public void updateDrawState(TextPaint ds) {
                             super.updateDrawState(ds);
                             ds.setUnderlineText(false); // remove underline
                             ds.setColor(mTextViewColor);

                         }
                     };
                     try {
                         spannable.setSpan(new BackgroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                         spannable.setSpan(clickableSpan,start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                     }catch (IndexOutOfBoundsException io){
                         
                     }

                 }
                 tv.setText(spannable);
                 tv.setMovementMethod( LinkMovementMethod.getInstance() );
             }

          });
          */



        scrollToChorusTextView.setOnClickListener(v -> scrollToChorus());




        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


        bookTitle.setText(mReference);
        songTitle.setText(mSongTitle);



        /*churchKitDd.verseDao()*/songVerseViewModel.getAllVerseByIdSong(mSongId).observe(requireActivity(), verses -> {
            System.out.println("msongid: "+mSongId+" "+verses);
           // System.out.println("verse: "+verses);
            allVerse = listVerseToString(verses);
            tv.setText( allVerse );

            liveDataBookMarkSong.observe(getViewLifecycleOwner(), new Observer<List<BookMarkSong>>() {
                @Override
                public void onChanged(List<BookMarkSong> bookMarkSongs) {
                    tv.setText(allVerse);
                    if (bookMarkSongs.size() != 0){
                        Spannable spannable = (Spannable) tv.getText();


                        for (int i = 0; i < bookMarkSongs.size(); i++) {
                            int start = bookMarkSongs.get(i).getStart();
                            int end = bookMarkSongs.get(i).getEnd();



                            int color = Color.parseColor(bookMarkSongs.get(i).getColor().replace("#", "#6b"));


                            ClickableSpan clickableSpan = new ClickableSpan() {
                                @Override
                                public void onClick(@NonNull View widget) {
                                    int index = bookMarkSongs.indexOf( new BookMarkSong("1","1","1",start,end,mSongId) );
                                    BookMarkSong bookMarkSong = bookMarkSongs.get(index);

                                    EditorBottomSheet editorBottomSheet = EditorBottomSheet.getInstance(bookMarkSong,tv,SONG_BOOKMARK,2,mSongId,getReferenceFromTextSelected(tv.getSelectionStart(),tv.getSelectionEnd()));
                                    editorBottomSheet.show(getChildFragmentManager(),"");
                                }
                                @Override
                                public void updateDrawState(TextPaint ds) {
                                    super.updateDrawState(ds);
                                    ds.setUnderlineText(false); // remove underline
                                    ds.setColor(mTextViewColor);

                                }
                            };
                            try {
                                spannable.setSpan(new BackgroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                spannable.setSpan(clickableSpan,start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }catch (IndexOutOfBoundsException io){

                            }

                        }
                        tv.setText(spannable);
                        tv.setMovementMethod( LinkMovementMethod.getInstance() );
                    }
                }
            });
        });

        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (ckp.getButtonChorus())
                setChorusButtonVisibility();
        });


        tv.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            EditorBottomSheet editorBottomSheet;
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {

        menu.clear();
        mode.getMenuInflater().inflate(R.menu.selection_action_menu, menu);

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {


        menu.clear();
        mode.getMenuInflater().inflate(R.menu.selection_action_menu, menu);


        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()){
            case R.id.g_image:
                fab.setVisibility(View.GONE);
                editorBottomSheet = EditorBottomSheet.getInstanceWithActionMode(mode,tv,SONG_BOOKMARK,IMAGE,mSongId,getReferenceFromTextSelected(tv.getSelectionStart(),tv.getSelectionEnd()));
                editorBottomSheet.show(SongDialogFragment.this.getChildFragmentManager(),"");


                break;
            case R.id.book_mark:
                editorBottomSheet = EditorBottomSheet.getInstanceWithActionMode(mode,tv,SONG_BOOKMARK,BOOK_MARK,mSongId,null);
                editorBottomSheet.show(SongDialogFragment.this.getChildFragmentManager(),"");
                break;
            case R.id.copy:
                com.churchkit.churchkit.Util.copyText(getContext(), com.churchkit.churchkit.Util.getSelectedText(tv),"verse");
                break;
        }
        return false;
    }





    @Override
    public void onDestroyActionMode(ActionMode mode) {
        fab.setVisibility(View.VISIBLE);
        editorBottomSheet = null;
    }

});

        return root;
    }




    private void init(){
    //churchKitDd = CKBibleDb.getInstance(requireContext());
    scrollToChorusTextView = root.findViewById(R.id.chorus);
    scrollView = root.findViewById(R.id.scrollView);
    tv  = root.findViewById(R.id.text);
    bookTitle = root.findViewById(R.id.book_name);
    songTitle = root.findViewById(R.id.chap_);
     fab = root.findViewById(R.id.fab_clos);
     favorite = root.findViewById(R.id.favorite);
     donate = root.findViewById(R.id.donate);
     more = root.findViewById(R.id.more);

        mTextViewColor = tv.getCurrentTextColor();

     fab.setOnClickListener(this::onClick);

     bookTitle.setSelected(true);

     if (PhoneInfo.manufacturer.equalsIgnoreCase("Xiaomi"))
         more.setVisibility(View.VISIBLE);

}



    private  void setChorusButtonVisibility(){
        if (isPhraseVisible(getContext().getString(R.string.chorus))){
            scrollToChorusTextView.setVisibility(View.GONE);
        }
        else if (tv.getText().toString().contains( getContext().getString(R.string.chorus) ))
            scrollToChorusTextView.setVisibility(View.VISIBLE);
        else {
            scrollToChorusTextView.setVisibility(View.GONE);
        }
    }



    private void setHistory(){


        songHistoryViewModel.insert(
                new SongHistory(mSongId, ckp.getSongName(), Calendar.getInstance().getTimeInMillis(), mReference)
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

                int lengthSpecial = 3;
                if (verse.getPosition()<0)
                    lengthSpecial =2+getString(R.string.chorus).length();

                versePositionList.add(
                        new Util.VersePosition( verse.getVerseId(), verse.getPosition(), verseString.length(),verseString.length()+verse.getVerse().length()+lengthSpecial )
                );



                verseString.append( verse.getPosition()<0? getContext().getString(R.string.chorus) :verse.getPosition() );
                verseString.append("\n");
                verseString.append(verse.getVerse());
                verseString.append("\n");
            }
            System.out.println( versePositionList );


        }else {
            verseString.append("Error");
        }

        return verseString.toString();
    }

    private String getReferenceFromTextSelected(int startSelected,int endSelected){

        int startPosition =0;
        int endPosition =0;


        for (int i = 0; i < versePositionList.size(); i++) {
            Util.VersePosition vp= versePositionList.get(i);

            if(startSelected>=vp.getStart() && startSelected<=vp.getEnd() ){
                startPosition = vp.getPosition();
            }
            if(endSelected<=vp.getEnd() &&endSelected>=vp.getStart()  ){
                endPosition = vp.getPosition();
            }
        }
        String a = "";
        if (startPosition == endPosition){
            a = startPosition<0?getString(R.string.chorus): String.valueOf(startPosition);
        }else {
            a = startPosition<0?getString(R.string.chorus): startPosition + " à "+
                    (endPosition<0?getString(R.string.chorus): String.valueOf(endPosition));
        }


        return mReference+": " +a;
    }

    private void scrollToChorus(){
        int index = tv.getText().toString().indexOf( getContext().getString(R.string.chorus) );
        Layout layout = tv.getLayout();
        int line = layout.getLineForOffset(index);
        int y = layout.getLineTop(line);
        scrollView.smoothScrollTo(0,y);

    }

    private boolean isPhraseVisible(@NonNull final String phrase) {
        int index = tv.getText().toString().indexOf(phrase);
        if (index == -1) {
            System.out.println("isPhraseVisible: false1");
            return false;
        }
        Layout layout = tv.getLayout();
        int line = layout.getLineForOffset(index);
        int y = layout.getLineTop(line);
        int y2 = layout.getLineBottom(line);
        Rect rect = new Rect(0, y, tv.getWidth(), y2);
        Rect visibleRect = new Rect();
        tv.getLocalVisibleRect(visibleRect);
        return ( Rect.intersects(rect, visibleRect) );
    }

    @Override
    public void onResume() {
        super.onResume();


        if ( ckp.getButtonChorus() && isPhraseVisible( getContext().getString(R.string.chorus) ) ) {
            setChorusButtonVisibility();
            scrollToChorusTextView.setVisibility(View.VISIBLE);
        } else {
            scrollToChorusTextView.setVisibility(View.GONE);
        }




        int typeFaceInt = ckp.getTypeFace();

        Typeface typeface = typeFaceInt == 0 ? Typeface.DEFAULT : ResourcesCompat.getFont(getContext(), typeFaceInt);

        tv.setTypeface(typeface);
        tv.setTextSize( ckp.getLetterSize() );

        ////set width and height of ListPartDialogFragment to 100 % to the width of screen
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width =(int) (Util.getScreenDisplayMetrics(getContext()).widthPixels * 1f);
        params.height = (int) (Util.getScreenDisplayMetrics(getContext()).heightPixels * 1f);
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    @Override
    public void onDestroy() {
        mReference = null;
        super.onDestroy();
    }

    @Override
    public void onStop() {
        liveDataBookMarkSong.removeObservers( getViewLifecycleOwner() );
        super.onStop();
    }

    private ViewGroup root;
    private TextView tv,bookTitle,songTitle, scrollToChorusTextView;
    private static String mSongId;
    private static Song mSong;
    private static String mSongTitle;
    private static String mReference;
    private FloatingActionButton fab;
    //private CKBibleDb churchKitDd;
    private ScrollView scrollView;
    private ImageView favorite,donate,more;
    private CKPreferences ckp ;
    private String allVerse="";
    private SongHistoryViewModel songHistoryViewModel;
    private SongFavoriteViewModel songFavoriteViewModel;
    private SongBookMarkViewModel songBookMarkViewModel;
    private SongVerseViewModel songVerseViewModel;
    int mTextViewColor;
    private LiveData<SongFavorite> songFavoriteLiveData;
    LiveData< List<BookMarkSong> > liveDataBookMarkSong;
    List<Util.VersePosition> versePositionList = new ArrayList<>();


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.favorite:
                SongFavorite songFavorite= songFavoriteLiveData.getValue();
                favorite.setEnabled(false);

                if (songFavorite != null) {
                    songFavoriteViewModel.delete(songFavorite);
                }
                else{
                    songFavoriteViewModel.insert(
                            new SongFavorite(mSongId,ckp.getSongName(),Calendar.getInstance().getTimeInMillis(), mReference)
                    );
                    /*Toast toast=Toast.makeText(getContext(),"Add to favorite with success",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP,0,0);
                    toast.show();*/
                }
                break;
            case R.id.more:
                final PopupMenu popupMenu = new PopupMenu(getContext(),v);
                popupMenu.getMenuInflater().inflate(R.menu.selection_action_menu, popupMenu.getMenu());
                popupMenu.show();
                if (tv.getSelectionEnd()>0){
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            EditorBottomSheet editorBottomSheet;
                            switch (item.getItemId()){
                                case R.id.g_image:
                                    fab.setVisibility(View.GONE);
                                    editorBottomSheet = EditorBottomSheet.getInstance(null,tv,SONG_BOOKMARK,IMAGE,mSongId,getReferenceFromTextSelected(tv.getSelectionStart(),tv.getSelectionEnd()));
                                    editorBottomSheet.show(SongDialogFragment.this.getChildFragmentManager(),"");
                                    break;
                                case R.id.book_mark:
                                    editorBottomSheet = EditorBottomSheet.getInstance(null,tv,SONG_BOOKMARK,BOOK_MARK,mSongId,getReferenceFromTextSelected(tv.getSelectionStart(),tv.getSelectionEnd()));
                                    editorBottomSheet.show(SongDialogFragment.this.getChildFragmentManager(),"");
                                    break;
                                case R.id.copy:
                                    com.churchkit.churchkit.Util.copyText(getContext(), com.churchkit.churchkit.Util.getSelectedText(tv),"verse");
                                    break;
                            }
                            return false;
                        }
                    });
                }else {
                    Toast.makeText(getContext(), "Please select a text first", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.donate:
                Checkout.preload( getContext().getApplicationContext() );
                startPayment(SongDialogFragment.this.getActivity());
                break;
            case R.id.fab_clos:
                SongDialogFragment.this.dismiss();
                break;
        }
    }
}
