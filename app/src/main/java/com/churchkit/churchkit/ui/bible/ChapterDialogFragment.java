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

import com.churchkit.churchkit.CKPreferences;
import com.churchkit.churchkit.PhoneInfo;
import com.churchkit.churchkit.R;
import com.churchkit.churchkit.database.ChurchKitDb;
import com.churchkit.churchkit.database.entity.bible.BibleChapterFavorite;
import com.churchkit.churchkit.database.entity.bible.BibleChapterHistory;
import com.churchkit.churchkit.database.entity.bible.BibleVerse;
import com.churchkit.churchkit.database.entity.song.SongFavorite;
import com.churchkit.churchkit.ui.EditorBottomSheet;
import com.churchkit.churchkit.ui.song.SongDialogFragment;
import com.churchkit.churchkit.ui.util.Util;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.razorpay.Checkout;

import java.util.Calendar;
import java.util.List;

public class ChapterDialogFragment extends DialogFragment implements View.OnClickListener{
    public ChapterDialogFragment(){

    }
    public static ChapterDialogFragment newInstance(String id,String reference){
        mId = id;
        mReference = reference;
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
                    editorBottomSheet = EditorBottomSheet.getInstance(mode,versets,IMAGE,mId);
                    editorBottomSheet.show(ChapterDialogFragment.this.getChildFragmentManager(),"");
                    break;
                case R.id.book_mark:
                    editorBottomSheet = EditorBottomSheet.getInstance(mode,versets,BOOK_MARK,mId);
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
        }

    });

        db.bibleVerseDao().getAllVerse(mId).observe(requireActivity(), bibleVerseList -> {
            versets.setText( listVerseToString(bibleVerseList) );
            setVerseTitle( bibleVerseList);
        });

        endingFavoriteImageView.setOnClickListener(this::onClick);


        db.bibleChapterFavoriteDao().existed(mId).observe(getViewLifecycleOwner(), songFavorite -> {

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




        return root;
    }

    private void setChapterHistory() {
        db.bibleChapterHistoryDao().insert(
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

    private void setVerseTitle(List<BibleVerse> bibleVerseList){

            chapTitle.setText("Verse: 1 to "+bibleVerseList.size());


    }

    private String listVerseToString(List<BibleVerse> bibleVerseList){
        StringBuilder verseString = new StringBuilder();

        if (bibleVerseList != null){
            for (int i=0;i< bibleVerseList.size();i++){

                verseString.append(positionToSupIndex(bibleVerseList.get(i).getPosition()));
                verseString.append(bibleVerseList.get(i).getVerseText()+" ");

            }
        }else {
            verseString.append("Error");
        }
        return verseString.toString();
    }


    ImageView donate, endingFavoriteImageView,more;
    TextView versets,bookReference,chapTitle;
    static String mId;
    static String mReference, mChapterhapter;
    static int mVerseAmount;
    FloatingActionButton fab;
    ConstraintLayout headerLayout;

    public  Point getLocationOnScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        Toast.makeText(getContext(),"endingFavoriteImageView=> x: "+location[0]+" y: "+location[1],Toast.LENGTH_LONG).show();
        return new Point(location[0], location[1]);
    }

    ChurchKitDb db= ChurchKitDb.getInstance(getContext());
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
                BibleChapterFavorite songFavorite= db.bibleChapterFavoriteDao().isExisted(mId);
                endingFavoriteImageView.setEnabled(false);

                if (songFavorite != null)
                    db.bibleChapterFavoriteDao().delete(songFavorite);
                else{

                    db.bibleChapterFavoriteDao().insert(
                            new BibleChapterFavorite(mId, Calendar.getInstance().getTimeInMillis(), mReference)
                    );
                    Toast toast=Toast.makeText(getContext(),"Add to favorite with success",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP,0,0);
                    toast.show();
                }
                break;
            case R.id.more:
                PopupMenu popupMenu = new PopupMenu(getContext(),v);
                popupMenu.getMenuInflater().inflate(R.menu.selection_action_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        EditorBottomSheet editorBottomSheet;
                        switch (item.getItemId()){
                            case R.id.g_image:
                                fab.setVisibility(View.GONE);
                                editorBottomSheet = EditorBottomSheet.getInstance(null,versets,IMAGE,mId);
                                editorBottomSheet.show(ChapterDialogFragment.this.getChildFragmentManager(),"");
                                break;
                            case R.id.book_mark:
                                editorBottomSheet = EditorBottomSheet.getInstance(null,versets,BOOK_MARK,mId);
                                editorBottomSheet.show(ChapterDialogFragment.this.getChildFragmentManager(),"");
                                break;
                            case R.id.copy:
                                com.churchkit.churchkit.Util.copyText(getContext(), com.churchkit.churchkit.Util.getSelectedText(versets),"verse");
                                break;
                        }
                        return false;
                    }
                });
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
