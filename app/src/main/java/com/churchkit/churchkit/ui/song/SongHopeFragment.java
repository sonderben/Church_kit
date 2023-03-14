package com.churchkit.churchkit.ui.song;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.churchkit.churchkit.R;
import com.churchkit.churchkit.adapter.AutoCompleteTextViewAdapter;
import com.churchkit.churchkit.adapter.song.SongHopeAdapter;
import com.churchkit.churchkit.database.ChurchKitDb;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongBook;
import com.churchkit.churchkit.databinding.FragmentSongHopeBinding;
import com.churchkit.churchkit.ui.util.GridSpacingItemDecoration;
import com.churchkit.churchkit.ui.util.Util;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

public class SongHopeFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentSongHopeBinding songBinding = FragmentSongHopeBinding.inflate(getLayoutInflater());
        View root= songBinding.getRoot();

        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        autoCompleteTextView = songBinding.search;
        mRecyclerView = songBinding.recyclerview;
        gridLayoutManager = new GridLayoutManager(getContext(),1);
        mRecyclerView.addItemDecoration(listGridItemDeco);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        /*DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);*/




        if(sharedPreferences.getInt(LIST_GRID,LIST)==GRID) {
            gridLayoutManager.setSpanCount(GRID);
        }
        else {
            gridLayoutManager.setSpanCount(sharedPreferences.getInt(LIST_GRID,LIST));
        }




        AutoCompleteTextViewAdapter autoCompleteAdapter = new AutoCompleteTextViewAdapter(getContext());


        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                 ChurchKitDb.getInstance(SongHopeFragment.this.getContext()).songDao().songFullTextSearch("*"+s.toString()+"*").observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
                    @Override
                    public void onChanged(List<Song> songs) {
                       if ( s.length() >1 ){
                           //Toast.makeText(getContext(),"men: "+songs,Toast.LENGTH_LONG).show();
                           autoCompleteAdapter.setSongs(songs);
                           autoCompleteTextView.setAdapter(autoCompleteAdapter);
                       }
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song song = (Song) autoCompleteAdapter.getItem(position);
                SongDialogFragment songDialogFragment = SongDialogFragment.newInstance(
                        song.getSongID()
                        , com.churchkit.churchkit.Util.formatNumberToString( song.getPosition() ) +" "+song.getBookName()
                        ,song.getTitle());
                songDialogFragment.show(getChildFragmentManager(),"");
            }
        });



        ChurchKitDb churchKitDb = ChurchKitDb.getInstance(getContext());
        churchKitDb.songBookDao().getAllSongBook().observe(requireActivity(), songBooks -> {
            if(songBooks!=null){
                homeAdapter = new SongHopeAdapter(
                        getTypeView(),songBooks,getActivity().getSupportFragmentManager()
                );

                mRecyclerView.setAdapter(homeAdapter);
            }
        });



        onCreateMenu();



        return root;
    }

    private int getTypeView(){
        return  sharedPreferences.getInt(LIST_GRID,LIST);
    }


    private void onCreateMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();

                menuInflater.inflate(R.menu.menu_song_hope, menu);
                setIconListOrGrid(menu.findItem(R.id.recyclerview_style));
                menu.findItem(R.id.app_bar_switch).getActionView().findViewById(R.id.switch1).setVisibility(View.GONE);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(menuItem.getItemId() == R.id.recyclerview_style){
                    if ( sharedPreferences.getInt(LIST_GRID,LIST) == LIST ) {
                        gridLayoutManager.setSpanCount(GRID);

                        editor.putInt(LIST_GRID,GRID);
                        editor.apply();
                        homeAdapter.setTypeView(GRID,LIST);
                    }else {
                        gridLayoutManager.setSpanCount(LIST);
                        editor.putInt(LIST_GRID,LIST);
                        homeAdapter.setTypeView(LIST,GRID);
                        editor.apply();
                    }
                    setIconListOrGrid( menuItem);
                    return true;
                }
                return false;
            }
        },getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void setIconListOrGrid(MenuItem menuItem){

        if (sharedPreferences.getInt(LIST_GRID,LIST) == 1) menuItem.setIcon(R.drawable.grid_view_24);
        else menuItem.setIcon(R.drawable.list_view_24);
    }



    @Override
    public void onResume() {
        super.onResume();
        autoCompleteTextView.getLayoutParams().width = (int) ( Util.getScreenDisplayMetrics(
                getContext()
        ).widthPixels * 0.80f );
    }

    private final int LIST = 1;
    private final int GRID = 2;
    private final String LIST_GRID = "LIST_GRID";
    MaterialAutoCompleteTextView autoCompleteTextView;
    RecyclerView mRecyclerView;
    SongHopeAdapter homeAdapter;
    SharedPreferences sharedPreferences;
    GridLayoutManager gridLayoutManager;

    GridSpacingItemDecoration listGridItemDeco = new GridSpacingItemDecoration(2,32,false);







}