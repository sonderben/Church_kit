package com.churchkit.churchkit.ui.song;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.churchkit.churchkit.Model.Song;
import com.churchkit.churchkit.adapter.song.ListSongsAdapter;
import com.churchkit.churchkit.databinding.FragmentListSongsBinding;

import java.util.ArrayList;
import java.util.List;


public class ListSongsFragment extends Fragment {

    public ListSongsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentListSongsBinding listSongsBinding = FragmentListSongsBinding.inflate(getLayoutInflater());

        mRecyclerView = listSongsBinding.recyclerview;
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(  new ListSongsAdapter( getSongsList() ) );

        return  listSongsBinding.getRoot();
    }

    private List<Song> getSongsList() {
        List<Song>songList = new ArrayList<>();
        songList.add(new Song(1,"Il est un nom",false));
        songList.add(new Song(2,"De la poussière de la terre",true));
        songList.add(new Song(30,"Un enfant est né",false));
        songList.add(new Song(114,"Grâce infinie",true));
        songList.add(new Song(5,"Je me rappelle un jour, où dans le ciel je vis",true));
        return songList;
    }
    RecyclerView mRecyclerView;


}