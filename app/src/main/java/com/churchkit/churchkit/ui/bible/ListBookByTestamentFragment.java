package com.churchkit.churchkit.ui.bible;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.churchkit.churchkit.R;
import com.churchkit.churchkit.adapter.bible.BibleAdapter;
import com.churchkit.churchkit.ui.util.GridSpacingIDeco;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;


public class ListBookByTestamentFragment extends Fragment {


    public ListBookByTestamentFragment() {
        // Required empty public constructor
    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_bible, container, false);

        autoCompleteTextView = root.findViewById(R.id.search);
        autoCompleteTextView.setVisibility(View.GONE);
        mRecyclerView = root.findViewById(R.id.recyclerview);

        adapter = new BibleAdapter(1,getChildFragmentManager());
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        mRecyclerView.addItemDecoration(new GridSpacingIDeco(32));
        mRecyclerView.setAdapter(adapter);

        return root;
    }

    RecyclerView mRecyclerView;
    BibleAdapter adapter;
    MaterialAutoCompleteTextView autoCompleteTextView;


}