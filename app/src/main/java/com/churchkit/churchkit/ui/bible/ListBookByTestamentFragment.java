package com.churchkit.churchkit.ui.bible;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.churchkit.churchkit.CKPreferences;
import com.churchkit.churchkit.R;
import com.churchkit.churchkit.database.entity.bible.BibleBook;
import com.churchkit.churchkit.ui.adapter.bible.BibleAdapter;
import com.churchkit.churchkit.database.CKBibleDb;
import com.churchkit.churchkit.ui.adapter.bible.BookAdapter;
import com.churchkit.churchkit.ui.util.GridSpacingIDeco;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
        db= CKBibleDb.getInstance( requireContext() );
        ckPreferences = new CKPreferences(getContext());

        download = root.findViewById(R.id.info_download_bible);




        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final int testament = getArguments().getInt("TESTAMENT");

        mRecyclerView.setAdapter( adapter );
        if (testament<0){
            download.setVisibility(View.GONE);
            db.bibleBookDao().getAllOldTestamentBibleBook( ckPreferences.getBibleName() ).observe(requireActivity(), bibleBooks -> {

                adapter.setBibleBook( bibleBooks );

            });
        }
        else {
            download.setVisibility(View.GONE);
            db.bibleBookDao().getAllNewTestamentBibleBook( ckPreferences.getBibleName() ).observe(requireActivity(), bibleBooks -> {

                adapter.setBibleBook( bibleBooks );

            });
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager( getContext() ));
        mRecyclerView.addItemDecoration(new GridSpacingIDeco(32));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new BookAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
    }

    RecyclerView mRecyclerView;
    BookAdapter adapter;
    MaterialAutoCompleteTextView autoCompleteTextView;
    CKBibleDb db;
    private CKPreferences ckPreferences;
    TextView download;


}