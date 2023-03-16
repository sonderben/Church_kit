package com.churchkit.churchkit.adapter.song;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
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

import com.churchkit.churchkit.CKPreferences;
import com.churchkit.churchkit.R;
import com.churchkit.churchkit.Util;
import com.churchkit.churchkit.database.entity.song.SongBook;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class SongHopeAdapter extends RecyclerView.Adapter<SongHopeAdapter.ListPartViewHolder> {


    int typeView;
    FragmentManager fm;
    List<SongBook> songBooks;
    CKPreferences ckPreferences;


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
        ckPreferences = new CKPreferences(fm.getPrimaryNavigationFragment().getContext());
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
    public void onBindViewHolder(@NonNull ListPartViewHolder holder,  int position) {
        holder.title.setText(songBooks.get(position).getName());
        holder.tileAcronym.setText(songBooks.get(position).getAbbreviation().toUpperCase());

        if (ckPreferences.getabbrColor()) {
            final int color = com.churchkit.churchkit.ui.util.Util.getColorByPosition(holder.getAbsoluteAdapterPosition() + 1);
            holder.tileAcronym.setTextColor(color);
           /* if (typeView == 2)
                holder.tileAcronym.setTextColor(color);
            else
                holder.tileAcronym.setTextColor(Color.WHITE);*/
        }


        holder.number.setText(songBooks.get(position).getSongAmount()+" chants");
        if (holder.img != null)
            holder.img.setImageResource( abbrToMipmapRessource( songBooks.get(holder.getAbsoluteAdapterPosition()).getAbbreviation() ) );

       holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SongBook songBook = songBooks.get(holder.getAbsoluteAdapterPosition());



                Bundle bundle = new Bundle();
                bundle.putString("ID", songBook.getSongBookId());
                bundle.putString("songBookName", songBook.getName());
                bundle.putString("FROM", Util.FROM_SONG_HOPE);
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

    private int abbrToMipmapRessource(String abbr){
        switch (abbr.toUpperCase()){
            case "CE": return R.mipmap.ce;
            case "MJ": return R.mipmap.mj;
            case "RN": return R.mipmap.rn;
            case "VR": return R.mipmap.ot;
            //case "EE": return R.mipmap.img_bg_creole;
            case "OR": return R.mipmap.img_bg_french;
            //case "HC": return R.drawable.img3;
            default:return R.mipmap.nt;
        }
    }

}
