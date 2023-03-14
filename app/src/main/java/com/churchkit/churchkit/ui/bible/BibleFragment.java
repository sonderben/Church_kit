package com.churchkit.churchkit.ui.bible;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.churchkit.churchkit.R;
import com.churchkit.churchkit.adapter.AutoCompleteTextViewAdapter;
import com.churchkit.churchkit.adapter.bible.BibleAdapter;
import com.churchkit.churchkit.database.ChurchKitDb;
import com.churchkit.churchkit.database.entity.bible.BibleBook;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.databinding.FragmentBibleBinding;
import com.churchkit.churchkit.ui.song.SongDialogFragment;
import com.churchkit.churchkit.ui.song.SongHopeFragment;
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

        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        db=ChurchKitDb.getInstance(getContext());

        db.bibleBookDao().getAllBibleBook().observe(requireActivity(), new Observer<List<BibleBook>>() {
            @Override
            public void onChanged(List<BibleBook> bibleBooks) {
                mAdapter = new BibleAdapter(sharedPreferences.getBoolean(IS_GROUP_BY_TESTAMENT,true)? 0:1,getActivity().getSupportFragmentManager(),bibleBooks);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();


            }
        });



        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        mRecyclerView.addItemDecoration(new GridSpacingIDeco(32));



        AutoCompleteTextViewAdapter autoCompleteAdapter = new AutoCompleteTextViewAdapter(getContext());
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BibleChapter bibleChapter = (BibleChapter) autoCompleteAdapter.getItem(position);

                ChapterDialogFragment dialogFragment =
                        ChapterDialogFragment.newInstance(bibleChapter.getBibleChapterId()
                                , bibleChapter.getBibleBookAbbr()+" chapter "+bibleChapter.getPosition());
                dialogFragment.show(getChildFragmentManager(),"");
            }
        });
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                ChurchKitDb.getInstance(BibleFragment.this.getContext()).bibleChapterDao().bibleChapterFullTextSearch(s.toString()).observe(getViewLifecycleOwner(), bibleChapters -> {
                    autoCompleteAdapter.setSongs(bibleChapters);
                    autoCompleteTextView.setAdapter( autoCompleteAdapter );
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        onCreateMenu();


        View root = bookmarkBinding.getRoot();
        return root;

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


                switchMenuItem.setChecked( sharedPreferences.getBoolean(IS_GROUP_BY_TESTAMENT,true) );


                switchMenuItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isGroupedByLang) {
                        mAdapter.setTypeView( isGroupedByLang ? 0 : 1 );
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(IS_GROUP_BY_TESTAMENT,isGroupedByLang);
                        editor.apply();
                    }
                });




            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {


                return false;
            }
        },getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }
    RecyclerView mRecyclerView;
    BibleAdapter mAdapter;
    MaterialAutoCompleteTextView autoCompleteTextView;
    private final String IS_GROUP_BY_TESTAMENT="IS_GROUP_BY_TESTAMENT";
    SharedPreferences sharedPreferences;
    ChurchKitDb db;
}