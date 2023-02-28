package com.churchkit.churchkit.ui.song;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.churchkit.churchkit.adapter.song.ListSongsAdapter;
import com.churchkit.churchkit.database.ChurchKitDb;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.databinding.FragmentListSongsBinding;

import java.util.List;


public class ListSongsFragment extends Fragment {

    public ListSongsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentListSongsBinding listSongsBinding = FragmentListSongsBinding.inflate(getLayoutInflater());

        churchKitDb = ChurchKitDb.getInstance(requireContext());

        mRecyclerView = listSongsBinding.recyclerview;
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));


        churchKitDb.songDao().getAllSongWithVerseById(getArguments().getString("ID")).observe(requireActivity(), new Observer<List<com.churchkit.churchkit.database.entity.song.Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                mRecyclerView.setAdapter(new ListSongsAdapter(songs,getChildFragmentManager(),getArguments().getString("songBookName")));
            }
        });
        return  listSongsBinding.getRoot();
    }

 
    RecyclerView mRecyclerView;
    ChurchKitDb churchKitDb;


}