package com.churchkit.churchkit.ui.song;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.churchkit.churchkit.R;
import com.churchkit.churchkit.database.CKBibleDb;
import com.churchkit.churchkit.database.CKSongDb;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongFavorite;

import java.util.Map;


public class SongFavoriteFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public SongFavoriteFragment() {
        // Required empty public constructor
    }


    public static SongFavoriteFragment newInstance(String param1, String param2) {
        SongFavoriteFragment fragment = new SongFavoriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        db = CKSongDb.getInstance( getContext() );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_song_favorite, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);

        db.songFavoriteDao().getAllSongFavoriteWithSong().observe(getViewLifecycleOwner(), new Observer<Map<SongFavorite, Song>>() {
            @Override
            public void onChanged(Map<SongFavorite, Song> songFavoriteSongMap) {
                System.out.println("yow poze ui: "+songFavoriteSongMap);
            }
        });

        return  view;
    }
    CKSongDb db;
    RecyclerView recyclerView;
}