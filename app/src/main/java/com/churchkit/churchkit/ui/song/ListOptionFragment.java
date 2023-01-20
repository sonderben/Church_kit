package com.churchkit.churchkit.ui.song;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.churchkit.churchkit.Model.Part;
import com.churchkit.churchkit.R;
import com.churchkit.churchkit.adapter.song.PartAdapter;
import com.churchkit.churchkit.databinding.FragmentListOptionBinding;
import com.churchkit.churchkit.ui.util.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;


public class ListOptionFragment extends Fragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentListOptionBinding listOptionBinding = FragmentListOptionBinding.inflate(getLayoutInflater());
        View root = listOptionBinding.getRoot();
        recyclerView = listOptionBinding.recyclerview;

        partAdapter = new PartAdapter();


        setParts();

        gridLayoutManager = new GridLayoutManager(getContext(),2,RecyclerView.VERTICAL,false);
        partAdapter.setPart(parts,gridLayoutManager);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,32,false));

        recyclerView.setAdapter(partAdapter);

       onCreateMenu();

        return root;
    }

    private void onCreateMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.menu_column_list, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId()==R.id.recyclerview_style){
                    if(gridLayoutManager.getSpanCount() != 2){
                        gridLayoutManager.setSpanCount(2);
                        menuItem.setIcon(R.drawable.list_24);
                    }
                    else {
                        gridLayoutManager.setSpanCount(1);
                        menuItem.setIcon(R.drawable.column_24);
                    }
                    partAdapter.notifyItemRangeChanged(0,partAdapter.getItemCount());
                }else {
                    getActivity().onBackPressed();
                }



                return true;
            }
        },getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }
/*
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_column_list,menu);
    }*/

    private void setParts() {
        parts =new ArrayList<>();
        parts.add(new Part("Reveillons nous","RN",R.mipmap.reveillonnous,getResources().getColor(R.color.purple_200) ));
        parts.add(new Part("Chant d'esperance","CE",R.mipmap.reveillonsnous2,getResources().getColor(R.color.teal_200)));
        parts.add(new Part("La voix du reveil creole","VC",R.mipmap.img_bg_creole,getResources().getColor(R.color.teal_700)));
        parts.add(new Part("Haïti chante avec radio lumière","HC",R.mipmap.img_bg_french,getResources().getColor(R.color.purple_700)));
        parts.add(new Part("Reveillons nous","RN",R.mipmap.reveillonnous,getResources().getColor(R.color.purple_200) ));


    }

    RecyclerView recyclerView;
    PartAdapter partAdapter;
    List<Part> parts;
    GridLayoutManager gridLayoutManager;
}