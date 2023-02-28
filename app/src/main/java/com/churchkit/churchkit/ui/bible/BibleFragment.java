package com.churchkit.churchkit.ui.bible;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.churchkit.churchkit.R;
import com.churchkit.churchkit.adapter.bible.BibleAdapter;
import com.churchkit.churchkit.database.ChurchKitDb;
import com.churchkit.churchkit.database.entity.bible.BibleBook;
import com.churchkit.churchkit.databinding.FragmentBibleBinding;
import com.churchkit.churchkit.ui.util.GridSpacingIDeco;

import java.util.List;

public class BibleFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentBibleBinding bookmarkBinding = FragmentBibleBinding.inflate(getLayoutInflater());
        mRecyclerView = bookmarkBinding.recyclerview;

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
    private final String IS_GROUP_BY_TESTAMENT="IS_GROUP_BY_TESTAMENT";
    SharedPreferences sharedPreferences;
    ChurchKitDb db;
}