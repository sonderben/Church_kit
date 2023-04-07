package com.churchkit.churchkit.ui.song;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.churchkit.churchkit.Util;
import com.churchkit.churchkit.adapter.song.ListSongsAdapter;
import com.churchkit.churchkit.database.ChurchKitDb;
import com.churchkit.churchkit.database.entity.song.SongFavoriteWrapper;
import com.churchkit.churchkit.database.entity.song.SongHistoryWrapper;
import com.churchkit.churchkit.databinding.FragmentListSongsBinding;
import com.churchkit.churchkit.modelview.song.SongFavoriteViewModel;
import com.churchkit.churchkit.modelview.song.SongHistoryViewModel;


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

        String from = getArguments().getString("FROM");
        String songBookName = getArguments().getString("songBookName");
        listSongsAdapter = new ListSongsAdapter(getChildFragmentManager(),songBookName);
        switch (from){
            case Util.FROM_SONG_FAVORITE:
                songFavoriteViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(SongFavoriteViewModel.class);

                songFavoriteViewModel.getSongFavorite().observe(getViewLifecycleOwner(), songFavoriteSongMap -> {
                    listSongsAdapter.setSongFavoriteWrapperList( SongFavoriteWrapper.fromMap(songFavoriteSongMap) );
                    mRecyclerView.setAdapter( listSongsAdapter );
                });
                break;
            case Util.FROM_SONG_HISTORY:
                SongHistoryViewModel songHistoryViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(SongHistoryViewModel.class);
                songHistoryViewModel.getSongWithHistory().observe(getViewLifecycleOwner(), songHistorySongMap -> {
                    listSongsAdapter.setSongHistoryWrapperList( SongHistoryWrapper.fromMap(songHistorySongMap) );
                    mRecyclerView.setAdapter( listSongsAdapter );
                });
                break;
            default:
                churchKitDb.songDao().getAllSongWithVerseById(getArguments().getString("ID")).observe(requireActivity(), songs -> {
                    listSongsAdapter.setSongList(songs);
                    mRecyclerView.setAdapter(listSongsAdapter);
                });
        }


        return  listSongsBinding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        churchKitDb=null;
        mRecyclerView=null;
        listSongsAdapter = null;
    }

    RecyclerView mRecyclerView;
    ChurchKitDb churchKitDb;
    ListSongsAdapter listSongsAdapter;
    private SongFavoriteViewModel songFavoriteViewModel;


}