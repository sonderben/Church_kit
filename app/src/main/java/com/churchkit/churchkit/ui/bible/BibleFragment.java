package com.churchkit.churchkit.ui.bible;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.churchkit.churchkit.CKPreferences;
import com.churchkit.churchkit.R;
import com.churchkit.churchkit.adapter.AutoCompleteTextViewAdapter;
import com.churchkit.churchkit.adapter.bible.BibleAdapter;
import com.churchkit.churchkit.database.CKBibleDb;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.bible.BibleVerse;
import com.churchkit.churchkit.databinding.FragmentBibleBinding;
import com.churchkit.churchkit.modelview.bible.BibleBookViewModel;
import com.churchkit.churchkit.modelview.bible.BibleChapterViewModel;
import com.churchkit.churchkit.modelview.bible.BibleVerseViewModel;
import com.churchkit.churchkit.ui.util.GridSpacingIDeco;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.List;

public class BibleFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentBibleBinding bookmarkBinding = FragmentBibleBinding.inflate(getLayoutInflater());
        mRecyclerView = bookmarkBinding.recyclerview;
        autoCompleteTextView = bookmarkBinding.search;

        ckPreferences = new CKPreferences(getContext());
        bibleChapterViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().
                getApplication()).create(BibleChapterViewModel.class);
        bibleBookViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity()
                .getApplication()).create(BibleBookViewModel.class);
        bibleVerseViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().
                getApplication()).create(BibleVerseViewModel.class);


        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        mAdapter = new BibleAdapter(getActivity().getSupportFragmentManager());
        bibleBookViewModel.getAllBibleBook().observe(requireActivity(), bibleBooks -> {
            mAdapter.config(sharedPreferences.getBoolean(IS_GROUP_BY_TESTAMENT, true) ? 0 : 1,bibleBooks);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        });


        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mRecyclerView.addItemDecoration(new GridSpacingIDeco(32));


        autoCompleteAdapter = new AutoCompleteTextViewAdapter(getContext(), BibleFragment.class);
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            onItemAutoCompleteClick(position);
        });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (ckPreferences.getBibleTypeSearch() == CKPreferences.CHAPTER_BIBLE_TYPE_SEARCH)
                    bibleChapterViewModel.bibleChapterFullTextSearch("*" + s.toString() + "*").observe(getViewLifecycleOwner(), bibleChapters -> {
                        autoCompleteAdapter.setSongs(bibleChapters);
                        autoCompleteTextView.setAdapter(autoCompleteAdapter);
                    });
                else
                    CKBibleDb.getInstance( BibleFragment.this.getContext() ).bibleVerseDao().search("*" + s.toString() + "*").observe(getViewLifecycleOwner(), new Observer<List<BibleVerse>>() {
                        @Override
                        public void onChanged(List<BibleVerse> bibleVerses) {
                            autoCompleteAdapter.setSongs(bibleVerses);
                            autoCompleteTextView.setAdapter(autoCompleteAdapter);
                        }
                    });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final PopupMenu popupMenu = new PopupMenu(getContext(), autoCompleteTextView);
        popupMenu.setGravity(Gravity.RIGHT);

        popupMenu.getMenuInflater().inflate(R.menu.menu_search, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.reference:
                    ckPreferences.setBibleTypeSearch(CKPreferences.CHAPTER_BIBLE_TYPE_SEARCH);
                    break;
                case R.id.verse:
                    ckPreferences.setBibleTypeSearch(CKPreferences.VERSE_BIBLE_TYPE_SEARCH);
            }
            return true;
        });

        autoCompleteTextView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (autoCompleteTextView.getRight() - autoCompleteTextView.getCompoundDrawables()[2].getBounds().width())) {
                    popupMenu.show();
                    return true;
                }
            }
            return false;
        });
        onCreateMenu();


        View root = bookmarkBinding.getRoot();
        return root;

    }

    private void onItemAutoCompleteClick(int position) {
        if (ckPreferences.getBibleTypeSearch() == CKPreferences.CHAPTER_BIBLE_TYPE_SEARCH) {
            BibleChapter bibleChapter = (BibleChapter) autoCompleteAdapter.getItem(position);

            ChapterDialogFragment dialogFragment =
                    ChapterDialogFragment.newInstance(bibleChapter);
            dialogFragment.show(getChildFragmentManager(), "");
        } else {
            BibleVerse verse = (BibleVerse) autoCompleteAdapter.getItem(position);
            bibleChapterViewModel.getChapterByVerseId(verse.getBibleChapterId()).observe(getViewLifecycleOwner(), bibleChapter -> {
                ChapterDialogFragment dialogFragment = ChapterDialogFragment.newInstance(bibleChapter);
                dialogFragment.show(getChildFragmentManager(), "");
            });
        }
    }

    private void onCreateMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {


                menu.clear();
                menuInflater.inflate(R.menu.menu_song_hope, menu);

                Switch switchMenuItem = menu.findItem(R.id.app_bar_switch).getActionView().findViewById(R.id.switch1);
                //MenuItem listOrGridItem = menu.findItem(R.id.recyclerview_style);
                menu.removeItem(R.id.recyclerview_style);


                switchMenuItem.setChecked(sharedPreferences.getBoolean(IS_GROUP_BY_TESTAMENT, true));


                switchMenuItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isGroupedByLang) {
                        mAdapter.setTypeView(isGroupedByLang ? 0 : 1);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(IS_GROUP_BY_TESTAMENT, isGroupedByLang);
                        editor.apply();
                    }
                });


            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {


                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    RecyclerView mRecyclerView;
    BibleAdapter mAdapter;
    MaterialAutoCompleteTextView autoCompleteTextView;
    private final String IS_GROUP_BY_TESTAMENT = "IS_GROUP_BY_TESTAMENT";
    SharedPreferences sharedPreferences;
    BibleChapterViewModel bibleChapterViewModel;
    private AutoCompleteTextViewAdapter autoCompleteAdapter;
    private CKPreferences ckPreferences;
    private BibleBookViewModel bibleBookViewModel;
    BibleVerseViewModel bibleVerseViewModel;


}