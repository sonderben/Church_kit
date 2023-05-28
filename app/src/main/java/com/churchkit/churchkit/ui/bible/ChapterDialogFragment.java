package com.churchkit.churchkit.ui.bible;

import static com.churchkit.churchkit.Util.BOOK_MARK;
import static com.churchkit.churchkit.Util.IMAGE;
import static com.churchkit.churchkit.ui.aboutapp.Payment.startPayment;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.churchkit.churchkit.CKPreferences;
import com.churchkit.churchkit.PhoneInfo;
import com.churchkit.churchkit.R;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.bible.BibleChapterFavorite;
import com.churchkit.churchkit.database.entity.bible.BibleChapterHistory;
import com.churchkit.churchkit.database.entity.bible.BibleVerse;
import com.churchkit.churchkit.database.entity.bible.BookMarkChapter;
import com.churchkit.churchkit.modelview.bible.BibleBookMarkViewModel;
import com.churchkit.churchkit.modelview.bible.BibleFavoriteViewModel;
import com.churchkit.churchkit.modelview.bible.BibleHistoryViewModel;
import com.churchkit.churchkit.modelview.bible.BibleVerseViewModel;
import com.churchkit.churchkit.ui.EditorBottomSheet;
import com.churchkit.churchkit.ui.util.Util;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.razorpay.Checkout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChapterDialogFragment extends DialogFragment implements View.OnClickListener{
    public static final int BIBLE_BOOKMARK = 2;
    private String allVersets="";
    private int mTextViewColor;
    LiveData< List<BibleVerse> > bibleVerseLiveData;

    public ChapterDialogFragment(){

    }
    public static ChapterDialogFragment newInstance(BibleChapter bibleChapter){
        mId = bibleChapter.getId();
        mReference = bibleChapter.getBookAbbreviation()+" "+bibleChapter.getPosition();
        return new ChapterDialogFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ConstraintLayout root = (ConstraintLayout) inflater.inflate(R.layout.fragment_list_chapter,container,false);



        versets = root.findViewById(R.id.text);
        bookReference = root.findViewById(R.id.book_name);
        chapTitle = root.findViewById(R.id.chap_);
        root.findViewById(R.id.chorus).setVisibility(View.GONE);
        bookReference.setText(mReference);
        headerLayout = root.findViewById(R.id.header);
        chapTitle.setText(mChapterhapter);
        fab = root.findViewById(R.id.fab_clos);
        donate = root.findViewById(R.id.donate);
        fab.setOnClickListener(x->this.dismiss());
        endingFavoriteImageView = root.findViewById(R.id.favorite);
        more = root.findViewById(R.id.more);
        more.setOnClickListener(this::onClick);
        donate.setOnClickListener(this::onClick);

        bookReference.setSelected(true);

        mTextViewColor = versets.getCurrentTextColor();
        bibleChapterViewModel = ViewModelProvider.AndroidViewModelFactory.
                getInstance(getActivity().getApplication()).create(BibleHistoryViewModel.class);
        bibleVerseViewModel = ViewModelProvider.AndroidViewModelFactory.
                getInstance(getActivity().getApplication()).create(BibleVerseViewModel.class);

        bibleFavoriteViewModel = ViewModelProvider.AndroidViewModelFactory.
                getInstance(getActivity().getApplication()).create(BibleFavoriteViewModel.class);
        bibleHistoryViewModel = ViewModelProvider.AndroidViewModelFactory.
                getInstance(getActivity().getApplication()).create(BibleHistoryViewModel.class);
        bibleBookMarkViewModel = ViewModelProvider.AndroidViewModelFactory.
                getInstance(getActivity().getApplication()).create(BibleBookMarkViewModel.class);
        setChapterHistory();
        if (PhoneInfo.manufacturer.equalsIgnoreCase("Xiaomi"))
            more.setVisibility(View.VISIBLE);

        versets.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
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
                    editorBottomSheet = EditorBottomSheet.getInstanceWithActionMode(mode,versets,BIBLE_BOOKMARK,IMAGE,mId,getReferenceFromTextSelected(versets.getSelectionStart(),versets.getSelectionEnd()) );
                    editorBottomSheet.show(ChapterDialogFragment.this.getChildFragmentManager(),"");
                    break;
                case R.id.book_mark:
                    editorBottomSheet = EditorBottomSheet.getInstanceWithActionMode(mode,versets,BIBLE_BOOKMARK,BOOK_MARK,mId,"");
                    editorBottomSheet.show(ChapterDialogFragment.this.getChildFragmentManager(),"");
                    break;
                case R.id.copy:
                    com.churchkit.churchkit.Util.copyText(getContext(), com.churchkit.churchkit.Util.getSelectedText(versets),"verse");
                    break;
            }
            return false;
        }





        @Override
        public void onDestroyActionMode(ActionMode mode) {
            fab.setVisibility(View.VISIBLE);
            editorBottomSheet = null;

            if(bibleFavoriteLiveData != null && bibleFavoriteLiveData.hasActiveObservers()){
                bibleFavoriteLiveData.removeObservers(getViewLifecycleOwner());
            }
        }

    });
         bibleVerseLiveData = bibleVerseViewModel.getAllVerse(mId);
         bibleVerseLiveData.observe(requireActivity(), bibleVerseList -> {
            allVersets = listVerseToString(bibleVerseList);
            versets.setText( allVersets );
            setVerseTitle( bibleVerseList);

            liveDataBookMark.observe(getViewLifecycleOwner(), new Observer<List<BookMarkChapter>>() {
                @Override
                public void onChanged(List<BookMarkChapter> bookMarkChapters) {
                    versets.setText(allVersets);

                    if (bookMarkChapters.size() != 0){

                        Spannable spannable = (Spannable) versets.getText();


                        for (int i = 0; i < bookMarkChapters.size(); i++) {
                            int start = bookMarkChapters.get(i).getStart();
                            int end = bookMarkChapters.get(i).getEnd();
                            // just for text


                            int color = Color.parseColor(bookMarkChapters.get(i).getColor().replace("#", "#6b"));



                            ClickableSpan clickableSpan = new ClickableSpan() {
                                @Override
                                public void onClick(@NonNull View widget) {
                                    int index = bookMarkChapters.indexOf( new BookMarkChapter("1","1","1",start,end,mId) );
                                    BookMarkChapter bookMarkSong = bookMarkChapters.get(index);

                                    EditorBottomSheet editorBottomSheet = EditorBottomSheet.getInstance(bookMarkSong,versets,BIBLE_BOOKMARK,2,mId,null);
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
                        versets.setText(spannable);
                        versets.setMovementMethod( LinkMovementMethod.getInstance() );
                    }
                }
            });
        });

        endingFavoriteImageView.setOnClickListener(this::onClick);


         bibleFavoriteLiveData = bibleFavoriteViewModel.existed(mId);
        bibleFavoriteLiveData.observe(getViewLifecycleOwner(), songFavorite -> {

            endingFavoriteImageView.setEnabled(true);
            Drawable drawable = endingFavoriteImageView.getDrawable();
            if (songFavorite != null){

                if (drawable != null) {
                    drawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                    endingFavoriteImageView.setImageDrawable(drawable);
                }
            }
            else{
                int color = getResources().getColor(R.color.white);
                if (drawable != null) {
                    drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                    endingFavoriteImageView.setImageDrawable(drawable);
                }
            }

        });



         liveDataBookMark = bibleBookMarkViewModel.getAllBookMark(mId);


        return root;
    }

    private void setChapterHistory() {
        bibleChapterViewModel.insert(
                new BibleChapterHistory(mId, Calendar.getInstance().getTimeInMillis(), mReference)
        );

    }

    @Override
    public void onResume() {
        super.onResume();
        CKPreferences ckp = new CKPreferences(getContext());
        int typeFaceInt = ckp.getTypeFace();

        Typeface typeface = typeFaceInt == 0 ? Typeface.DEFAULT : ResourcesCompat.getFont(getContext(), typeFaceInt);

        versets.setTypeface(typeface);
        versets.setTextSize( ckp.getLetterSize() );

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width =(int) (Util.getScreenDisplayMetrics(getContext()).widthPixels * 1f);
        params.height = (int) (Util.getScreenDisplayMetrics(getContext()).heightPixels * 1f);
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    @Override
    public void onStart() {
        liveDataBookMark.removeObservers(getViewLifecycleOwner());
        super.onStart();
    }

    private void setVerseTitle(List<BibleVerse> bibleVerseList){

            chapTitle.setText("Verse: 1 to "+bibleVerseList.size());


    }

    private String listVerseToString(List<BibleVerse> bibleVerseList){
        StringBuilder verseString = new StringBuilder();

        if (bibleVerseList != null){
            for (int i=0;i< bibleVerseList.size();i++){

                BibleVerse verse = bibleVerseList.get(i);
                final int lengthSpecial = 2;




                versePositionList.add(
                        new Util.VersePosition( verse.getBibleVerseId(), verse.getPosition(), verseString.length(),verseString.length()+verse.getVerseText().length()+lengthSpecial )
                );

                verseString.append(positionToSupIndex(bibleVerseList.get(i).getPosition()));
                verseString.append(bibleVerseList.get(i).getVerseText()+" ");

            }
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

    private LiveData< List<BookMarkChapter> > liveDataBookMark;
    private ImageView donate, endingFavoriteImageView,more;
    private TextView versets,bookReference,chapTitle;
    private static String mId;
    private static String mReference, mChapterhapter;
    private static int mVerseAmount;
    private List<Util.VersePosition> versePositionList = new ArrayList<>();
    private FloatingActionButton fab;
    private ConstraintLayout headerLayout;
    private LiveData<BibleChapterFavorite> bibleFavoriteLiveData;
    private BibleHistoryViewModel bibleChapterViewModel;
    private BibleVerseViewModel bibleVerseViewModel;
    private BibleFavoriteViewModel bibleFavoriteViewModel;
    private BibleHistoryViewModel bibleHistoryViewModel;
    private BibleBookMarkViewModel bibleBookMarkViewModel;

    public  Point getLocationOnScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        Toast.makeText(getContext(),"endingFavoriteImageView=> x: "+location[0]+" y: "+location[1],Toast.LENGTH_LONG).show();
        return new Point(location[0], location[1]);
    }

    //CKBibleDb db= CKBibleDb.getInstance(getContext());
    private String toUnicode( int position){
        final String zero = "\u2070";
        final String one = "\u00B9";
        final String two = "\u00B2";
        final String twee = "\u00B3";
        final String four = "\u2074";
        final String five = "\u2075";
        final String six = "\u2076";
        final String seven = "\u2077";
        final String eight = "\u2078";
        final String nine = "\u2079";
        switch (position){
            case 0: return zero;
            case 1: return one;
            case 2: return two;
            case 3: return twee;
            case 4: return four;
            case 5: return five;
            case 6: return six;
            case 7: return seven;
            case 8: return eight;
            case 9: return nine;
            default:
                return "";
        }
    }
    private String positionToSupIndex (int position){
        int pos = position;
        StringBuilder stri = new StringBuilder();
        do{
            stri.insert(    0,toUnicode(pos%10));

            pos = pos/10;

        }while(pos>0);

        return stri.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.favorite:
                BibleChapterFavorite songFavorite= bibleFavoriteLiveData.getValue();
                endingFavoriteImageView.setEnabled(false);

                if (songFavorite != null)
                    bibleFavoriteViewModel.delete(songFavorite);
                else{

                    bibleFavoriteViewModel.insert(
                            new BibleChapterFavorite(mId, Calendar.getInstance().getTimeInMillis(), mReference)
                    );
                    Toast toast=Toast.makeText(getContext(),"Add to favorite with success",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP,0,0);
                    toast.show();
                }
                break;
            case R.id.more:
                final PopupMenu popupMenu = new PopupMenu(getContext(),v);
                popupMenu.getMenuInflater().inflate(R.menu.selection_action_menu, popupMenu.getMenu());
                popupMenu.show();
                if (versets.getSelectionEnd()>0){
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            EditorBottomSheet editorBottomSheet;
                            switch (item.getItemId()){
                                case R.id.g_image:
                                    fab.setVisibility(View.GONE);
                                    editorBottomSheet = EditorBottomSheet.getInstance(null,versets,BIBLE_BOOKMARK,IMAGE,mId,getReferenceFromTextSelected(versets.getSelectionStart(),versets.getSelectionEnd()));
                                    editorBottomSheet.show(ChapterDialogFragment.this.getChildFragmentManager(),"");
                                    break;
                                case R.id.book_mark:
                                    editorBottomSheet = EditorBottomSheet.getInstance(null,versets,BIBLE_BOOKMARK,BOOK_MARK,mId,null);
                                    editorBottomSheet.show(ChapterDialogFragment.this.getChildFragmentManager(),"");
                                    break;
                                case R.id.copy:
                                    com.churchkit.churchkit.Util.copyText(getContext(), com.churchkit.churchkit.Util.getSelectedText(versets),"verse");
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
                startPayment(ChapterDialogFragment.this.getActivity());
                break;
            case R.id.fab_clos:
                ChapterDialogFragment.this.dismiss();
                break;
        }
    }
}
