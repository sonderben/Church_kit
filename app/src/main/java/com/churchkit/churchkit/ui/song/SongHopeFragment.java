package com.churchkit.churchkit.ui.song;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

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
import com.churchkit.churchkit.ui.adapter.AutoCompleteTextViewAdapter;
import com.churchkit.churchkit.ui.adapter.song.SongHopeAdapter;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.Verse;
import com.churchkit.churchkit.databinding.FragmentSongHopeBinding;
import com.churchkit.churchkit.modelview.song.SongBookViewModel;
import com.churchkit.churchkit.modelview.song.SongVerseViewModel;
import com.churchkit.churchkit.modelview.song.SongViewModel;
import com.churchkit.churchkit.ui.util.GridSpacingItemDecoration;
import com.churchkit.churchkit.ui.util.OptionSearchView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.List;

public class SongHopeFragment extends Fragment {


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentSongHopeBinding songBinding = FragmentSongHopeBinding.inflate(getLayoutInflater());
        View root = songBinding.getRoot();

        layInfo = songBinding.layInfo;

        songBookViewModel = ViewModelProvider.AndroidViewModelFactory.
                getInstance(getActivity().getApplication()).create(SongBookViewModel.class);

        songViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(SongViewModel.class);

        songVerseViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(SongVerseViewModel.class);

        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        autoCompleteTextView = songBinding.search;
        mRecyclerView = songBinding.recyclerview;
        gridLayoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerView.addItemDecoration(listGridItemDeco);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        /*DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);*/


         ckPreferences = new CKPreferences(getContext());






        if (sharedPreferences.getInt(LIST_GRID, LIST) == GRID) {
            gridLayoutManager.setSpanCount(GRID);
        } else {
            gridLayoutManager.setSpanCount(sharedPreferences.getInt(LIST_GRID, LIST));
        }


        AutoCompleteTextViewAdapter autoCompleteAdapter = new AutoCompleteTextViewAdapter(getContext(), SongHopeFragment.class);


        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (ckPreferences.getSongTypeSearch() == CKPreferences.CHAPTER_SONG_TYPE_SEARCH) {
                    songViewModel.songFullTextSearch("*" + s.toString() + "*").observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
                        @Override
                        public void onChanged(List<Song> songs) {
                            if (s.length() > 1) {
                                autoCompleteAdapter.setSongs(songs);
                                autoCompleteTextView.setAdapter(autoCompleteAdapter);
                            }
                        }
                    });
                } else {
                    songVerseViewModel.search("*" + s.toString() + "*").observe(getViewLifecycleOwner(), new Observer<List<Verse>>() {
                        @Override
                        public void onChanged(List<Verse> verses) {
                            if (s.length() > 1) {
                                autoCompleteAdapter.setSongs(verses);
                                autoCompleteTextView.setAdapter(autoCompleteAdapter);
                            }
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        autoCompleteTextView.setOnTouchListener((v, event) -> {

            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (event.getRawX() >= (autoCompleteTextView.getRight() - autoCompleteTextView.getCompoundDrawables()[2].getBounds().width())) {

                    final OptionSearchView osv = new OptionSearchView(autoCompleteTextView, SongHopeFragment.class);
                    osv.showDialog();
                    return true;

                }
            }
            return false;
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (ckPreferences.getSongTypeSearch() == CKPreferences.CHAPTER_SONG_TYPE_SEARCH) {
                    Song song = (Song) autoCompleteAdapter.getItem(position);
                    SongDialogFragment songDialogFragment = SongDialogFragment.newInstance(
                            song);
                    songDialogFragment.show(getChildFragmentManager(), "");
                } else {
                    Verse verse = (Verse) autoCompleteAdapter.getItem(position);
                    songViewModel.getSongById(verse.getSongId()).observe(SongHopeFragment.this.getViewLifecycleOwner(), song -> {
                        SongDialogFragment songDialogFragment = SongDialogFragment.newInstance(
                                song);
                        songDialogFragment.show(getChildFragmentManager(), "");
                    });
                }
            }
        });


        songBookViewModel.getAllSongBook().observe(requireActivity(), songBooks -> {
            if (songBooks != null) {
                homeAdapter = new SongHopeAdapter(
                        getTypeView(), songBooks, getActivity().getSupportFragmentManager()
                );

                mRecyclerView.setAdapter(homeAdapter);
            }
        });


        onCreateMenu();


        return root;
    }

    private int getTypeView() {
        return sharedPreferences.getInt(LIST_GRID, LIST);
    }


    private void onCreateMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
/*
                menuInflater.inflate(R.menu.menu_song_hope, menu);
                setIconListOrGrid(menu.findItem(R.id.recyclerview_style));
                menu.findItem(R.id.app_bar_switch).getActionView().findViewById(R.id.switch1).setVisibility(View.GONE);*/
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (menuItem.getItemId() == R.id.recyclerview_style) {
                    if (sharedPreferences.getInt(LIST_GRID, LIST) == LIST) {
                        gridLayoutManager.setSpanCount(GRID);

                        editor.putInt(LIST_GRID, GRID);
                        editor.apply();
                        homeAdapter.setTypeView(GRID, LIST);
                    } else {
                        gridLayoutManager.setSpanCount(LIST);
                        editor.putInt(LIST_GRID, LIST);
                        homeAdapter.setTypeView(LIST, GRID);
                        editor.apply();
                    }
                    setIconListOrGrid(menuItem);
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void setIconListOrGrid(MenuItem menuItem) {

        if (sharedPreferences.getInt(LIST_GRID, LIST) == 1)
            menuItem.setIcon(R.drawable.grid_view_24);
        else menuItem.setIcon(R.drawable.list_view_24);
    }

    @Override
    public void onResume() {
        autoCompleteTextView.setHint( ckPreferences.isSongTypeSearchIsVerse()?
                getString(R.string.search_by_verse):getString(R.string.search_by_ref) );
        if (ckPreferences.isZeroSongDownloaded()  ){
            layInfo.setVisibility(View.VISIBLE);
        }else {
            layInfo.setVisibility(View.GONE);
        }
        super.onResume();
    }

    private final int LIST = 1;
    private final int GRID = 2;
    private final String LIST_GRID = "LIST_GRID";
    CKPreferences ckPreferences;
    LinearLayout layInfo;
    MaterialAutoCompleteTextView autoCompleteTextView;
    RecyclerView mRecyclerView;
    SongHopeAdapter homeAdapter;
    SharedPreferences sharedPreferences;
    GridLayoutManager gridLayoutManager;
    private SongViewModel songViewModel;
    private SongVerseViewModel songVerseViewModel;
    private SongBookViewModel songBookViewModel;

    GridSpacingItemDecoration listGridItemDeco = new GridSpacingItemDecoration(2, 32, false);


}