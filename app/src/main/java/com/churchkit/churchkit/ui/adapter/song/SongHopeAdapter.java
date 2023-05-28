package com.churchkit.churchkit.ui.adapter.song;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.churchkit.churchkit.CKPreferences;
import com.churchkit.churchkit.R;
import com.churchkit.churchkit.Util;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongBook;
import com.churchkit.churchkit.ui.song.SongDialogFragment;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class SongHopeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int SONG_BOOK_TYPE = 1;
    private static final int SONG_TYPE = 2;
    FragmentManager fm;
    List<SongBook> songBooks;
    List<Song> songs;
    CKPreferences ckPreferences;




    public SongHopeAdapter( Context context,FragmentManager fm) {
        ckPreferences = new CKPreferences(context);
        this.fm = fm;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
        songBooks = null;
        notifyDataSetChanged();
    }

    public void setSongBooks(List<SongBook> songBooks) {
        this.songBooks = songBooks;
        songs = null;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SONG_TYPE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song_one_part,parent,false);
            return new ListSongViewHolder(view);
        }
           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_part,parent,false);
           return new ListPartViewHolder(view);

    }




    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,  int position) {

        if ( songBooks != null){
            ListPartViewHolder tempVh = (ListPartViewHolder) holder;

            tempVh.title.setText(songBooks.get(position).getTitle());
            tempVh.tileAcronym.setText(songBooks.get(position).getAbbreviation().toUpperCase());
            if (ckPreferences.getabbrColor()) {
                tempVh.tileAcronym.setTextColor(Color.parseColor("#"+ songBooks.get(holder.getAbsoluteAdapterPosition()).getColor() ) );
            }

            tempVh.number.setText(songBooks.get(position).getChildAmount()+" chants");

            tempVh.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SongBook songBook = songBooks.get(holder.getAbsoluteAdapterPosition());
                    Bundle bundle = new Bundle();
                    bundle.putString("ID", songBook.getId());
                    bundle.putString("songBookName", songBook.getTitle());
                    bundle.putString("FROM", Util.FROM_SONG_HOPE);
                    NavController navController = Navigation.findNavController(view);
                    navController.getGraph().findNode(R.id.listSongsFragment).setLabel(songBook.getTitle()+" ");
                    navController.navigate(R.id.action_homeFragment_to_listSongsFragment2,bundle);
                }
            });
        }
        else {
            ListSongViewHolder tempvh = (ListSongViewHolder) holder;
            float pos =  songs.get( tempvh.getAbsoluteAdapterPosition() ).getPosition();
            String title = Util.formatNumberToString(pos) +songs.get( tempvh.getAbsoluteAdapterPosition() ).getTitle();
            tempvh.title.setText(title);

            tempvh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SongDialogFragment songDialogFragment = SongDialogFragment.newInstance(
                            songs.get( tempvh.getAbsoluteAdapterPosition() ));

                    songDialogFragment.show(fm, "");
                }
            });
        }



    }



    @Override
    public int getItemCount() {

        if (songs != null)
            return songs.size();
        else if (songBooks != null)
            return songBooks.size();

        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (songs != null)
            return SONG_TYPE;
        else {
            return SONG_BOOK_TYPE;
        }
    }



    class ListPartViewHolder extends RecyclerView.ViewHolder{
        public TextView title,tileAcronym,number;
        MaterialCardView cardView;
        ImageView img;

        public ListPartViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
             number= itemView.findViewById(R.id.number_song);
            img = itemView.findViewById(R.id.img);
            tileAcronym = itemView.findViewById(R.id.tile_abr);
            cardView = itemView.findViewById(R.id.cardview);


        }
    }
    class ListSongViewHolder extends RecyclerView.ViewHolder{
        public TextView title;

        public ListSongViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);



        }
    }



}
