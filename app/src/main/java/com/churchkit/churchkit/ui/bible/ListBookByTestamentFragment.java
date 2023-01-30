package com.churchkit.churchkit.ui.bible;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.churchkit.churchkit.R;
import com.churchkit.churchkit.adapter.bible.BibleAdapter;
import com.churchkit.churchkit.database.ChurchKitDb;
import com.churchkit.churchkit.database.entity.bible.BibleBook;
import com.churchkit.churchkit.ui.util.GridSpacingIDeco;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.List;


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
        db=ChurchKitDb.getInstance(requireContext());
        db.bibleBookDao().getAllBibleBook().observe(getViewLifecycleOwner(), new Observer<List<BibleBook>>() {
            @Override
            public void onChanged(List<BibleBook> bibleBooks) {
                adapter = new BibleAdapter(1,getChildFragmentManager(),bibleBooks);
                System.out.println("Boom: "+bibleBooks);
            }
        });

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        mRecyclerView.addItemDecoration(new GridSpacingIDeco(32));
        mRecyclerView.setAdapter(adapter);

        return root;
    }

    RecyclerView mRecyclerView;
    BibleAdapter adapter;
    MaterialAutoCompleteTextView autoCompleteTextView;
    ChurchKitDb db;


}