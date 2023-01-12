package com.churchkit.churchkit.ui.song;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.churchkit.churchkit.R;
import com.churchkit.churchkit.databinding.FragmentHomeBinding;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class SongHope extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentHomeBinding songBinding = FragmentHomeBinding.inflate(getLayoutInflater());
        View root= songBinding.getRoot();


        autoCompleteTextView = songBinding.search;


        songBinding.cardview1.setOnClickListener(x->{
            NavController navController = Navigation.findNavController(root);
            navController.getGraph().findNode(R.id.listOptionFragment).setLabel("Français");
            navController.navigate(R.id.action_homeFragment_to_listOptionFragment);

         });
        songBinding.cardview2.setOnClickListener(x->{

        });


        return root;
    }
    MaterialAutoCompleteTextView autoCompleteTextView;



}