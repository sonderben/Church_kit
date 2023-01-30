package com.churchkit.churchkit.adapter.song;

import android.annotation.SuppressLint;
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

import com.churchkit.churchkit.R;
import com.churchkit.churchkit.database.entity.song.SongBook;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class SongHopeAdapter extends RecyclerView.Adapter<SongHopeAdapter.ListPartViewHolder> {


    int typeView;
    FragmentManager fm;
    List<SongBook> songBooks;


    public void setTypeView(int newTypeViewHolder,int oldTypeViewHolder) {
        if (newTypeViewHolder == oldTypeViewHolder) // LIST==LIST || GRID==GRID || GROUP==GROUP => Just For security
            return;
        else {
            this.typeView = newTypeViewHolder;
            notifyItemRangeChanged(0, getItemCount());
        }
    }

    public SongHopeAdapter(int typeView,List<SongBook> songBooks, FragmentManager fm) {
        this.fm = fm;
        this.songBooks = songBooks;
        this.typeView = typeView;
    }

    @NonNull
    @Override
    public ListPartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 2) {
           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_part2,parent,false);
           return new ListPartViewHolder(view);
       }
       else {
           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_part,parent,false);
           return new ListPartViewHolder(view);
       }
    }


    @Override
    public void onBindViewHolder(@NonNull ListPartViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(songBooks.get(position).getName());
        holder.tileAcronym.setText(songBooks.get(position).getAbbreviation());
        holder.number.setText(songBooks.get(position).getSongAmount()+" chants");

       holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SongBook songBook = songBooks.get(position);
                /*passing value by using bundle, so I utilised the navigation and it takes bundle as parameter
                I used the position as the ID, well you can change to whatever that you want to*/
                Bundle bundle = new Bundle();
                bundle.putInt("ID", songBook.getSongBookId());
                bundle.putString("songBookName", songBook.getName());
                NavController navController = Navigation.findNavController(view);
                navController.getGraph().findNode(R.id.listSongsFragment).setLabel(songBook.getName()+" ");
                navController.navigate(R.id.action_homeFragment_to_listSongsFragment2,bundle);

            }
        });
    }



    @Override
    public int getItemCount() {
        return songBooks.size();
    }

    @Override
    public int getItemViewType(int position) {
        return typeView;
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


}
