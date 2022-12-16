package com.churchkit.churchkit.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.churchkit.churchkit.R;
import com.google.android.material.card.MaterialCardView;

public class HomeFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_home, container, false);



        cardViewCreole =root.findViewById(R.id.cardview1);
        cardViewFrench =root.findViewById(R.id.cardview2);

        cardViewCreole.setOnClickListener(x->{
            Navigation.findNavController(root).navigate(R.id.action_homeFragment_to_listOptionFragment);
         });
        cardViewFrench.setOnClickListener(x->{

        });
        return root;
    }
    MaterialCardView cardViewCreole;
    MaterialCardView cardViewFrench;



}