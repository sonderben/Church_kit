package com.churchkit.churchkit.ui.song;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.churchkit.churchkit.R;
import com.churchkit.churchkit.adapter.song.SongHopeAdapter;
import com.churchkit.churchkit.databinding.FragmentSongHopeBinding;
import com.churchkit.churchkit.ui.util.GridSpacingIDeco;
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
        mRecyclerView.setLayoutManager(gridLayoutManager);

        if ( sharedPreferences.getBoolean(IS_GROUP,true) ){
            mRecyclerView.addItemDecoration(groupItemDeco);
        }else {
            gridLayoutManager.setSpanCount(sharedPreferences.getInt(LIST_GRID,LIST));
            mRecyclerView.addItemDecoration(listGridItemDeco);
        }

        List<String> strings = new ArrayList<>();
        strings.add("101");
        strings.add("102");
        strings.add("001");
        strings.add("003");
        strings.add("121");
        strings.add("132");
        strings.add("041");
        strings.add("003");
        SearchSongAutoCompleteAdapter autoCompleteAdapter = new SearchSongAutoCompleteAdapter(getContext(),strings);
        autoCompleteTextView.setAdapter(autoCompleteAdapter);

        homeAdapter = new SongHopeAdapter( sharedPreferences.getBoolean(IS_GROUP,true) ? 0 :
                sharedPreferences.getInt(LIST_GRID,LIST) ,getActivity().getSupportFragmentManager() );
        mRecyclerView.setAdapter(homeAdapter);



        onCreateMenu();

        return root;
    }

    private void onCreateMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                //SharedPreferences.Editor editor = sharedPreferences.edit();

                menu.clear();
                menuInflater.inflate(R.menu.menu_song_hope, menu);
                setIconListOrGrid(menu.findItem(R.id.recyclerview_style));
               /* Switch switchMenuItem = */
                menu.findItem(R.id.app_bar_switch).getActionView().findViewById(R.id.switch1).setVisibility(View.GONE);
                MenuItem listOrGridItem = menu.findItem(R.id.recyclerview_style);

                //switchMenuItem.setChecked( sharedPreferences.getBoolean(IS_GROUP,true) );
                listOrGridItem.setVisible( !sharedPreferences.getBoolean(IS_GROUP,true) );


                /*switchMenuItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isGroupedByLang) {
                        //if is grouped by language
                        if (isGroupedByLang){
                            homeAdapter.setTypeView(0,sharedPreferences.getInt(LIST_GRID,LIST) );
                            if (gridLayoutManager.getSpanCount() != 1) gridLayoutManager.setSpanCount(1);

                            if (mRecyclerView.getItemDecorationAt(0) instanceof GridSpacingItemDecoration){
                                mRecyclerView.removeItemDecoration(listGridItemDeco);
                                mRecyclerView.addItemDecoration(groupItemDeco);
                            }

                            editor.putBoolean(IS_GROUP,true);
                            //editor.putInt(LIST_GRID,0);
                            editor.apply();
                        }else {
                            // dwe rele nouvo setType la
                            int newTypeViewHolder =sharedPreferences.getInt(LIST_GRID,LIST);// 1 or 2
                            if (gridLayoutManager.getSpanCount() != newTypeViewHolder)gridLayoutManager.setSpanCount(newTypeViewHolder);

                            homeAdapter.setTypeView(newTypeViewHolder,0 );
                            mRecyclerView.removeItemDecoration(groupItemDeco);
                            mRecyclerView.addItemDecoration(listGridItemDeco);
                            editor.putBoolean(IS_GROUP,false);
                            editor.apply();
                        }
                        listOrGridItem.setVisible( !isGroupedByLang );
                    }
                });*/




            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

                if(menuItem.getItemId() == R.id.recyclerview_style){
                    if ( sharedPreferences.getInt(LIST_GRID,LIST) == LIST ) { // if the current view holder is LIST
                        gridLayoutManager.setSpanCount(GRID);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt(LIST_GRID,GRID);
                        editor.apply();
                        homeAdapter.setTypeView(GRID,LIST);
                    }else {//the current view holder is GRID
                        gridLayoutManager.setSpanCount(LIST);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
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

        if (sharedPreferences.getInt(LIST_GRID,LIST) == 1) menuItem.setIcon(R.drawable.column_24);
        else menuItem.setIcon(R.drawable.list_24);
    }
    private static class SearchSongAutoCompleteAdapter extends ArrayAdapter<String > {
        public SearchSongAutoCompleteAdapter(@NonNull Context context,  @NonNull List<String> objects) {
            super(context, 0, objects);
        }



        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_auto_complete,parent,false);

            TextView number = convertView.findViewById(R.id.number);

            number.setText(getItem(position));


            return convertView;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //set width autoCompleteTextView to 80 % to the width of screen
        autoCompleteTextView.getLayoutParams().width = (int) ( Util.getScreenDisplayMetrics(
                getContext()
        ).widthPixels * 0.80f );
    }

    private final int LIST = 1;
    private final int GRID = 2;
    //private final int GROUP = 0;
    private final String LIST_GRID = "LIST_GRID";
    private final String IS_GROUP = "IS_GROUP";
    MaterialAutoCompleteTextView autoCompleteTextView;
    RecyclerView mRecyclerView;
    SongHopeAdapter homeAdapter;
    SharedPreferences sharedPreferences;
    GridSpacingIDeco groupItemDeco = new GridSpacingIDeco(154);
    GridLayoutManager gridLayoutManager;

    GridSpacingItemDecoration listGridItemDeco = new GridSpacingItemDecoration(2,32,false);






}