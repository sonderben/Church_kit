package com.churchkit.churchkit.adapter.song;

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
import com.google.android.material.card.MaterialCardView;

public class SongHopeAdapter extends RecyclerView.Adapter {


    int typeView;
    FragmentManager fm;


    public void setTypeView(int newTypeViewHolder,int oldTypeViewHolder) {
        if (newTypeViewHolder == oldTypeViewHolder) // LIST==LIST || GRID==GRID || GROUP==GROUP => Just For security
            return;
        else {
            this.typeView = newTypeViewHolder;
            notifyItemRangeChanged(0, getItemCount());
        }
    }

    public SongHopeAdapter(int typeView, FragmentManager fm) {
        this.fm = fm;
        this.typeView = typeView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       /*if(viewType == 0){
           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_part_group_by_lang,parent,false);
           return new GroupByLanguageViewHolder(view);
       }else*/ if(viewType == 2) {
           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_part2,parent,false);
           return new ListPartViewHolder(view);
       }
       else {
           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_part,parent,false);
           return new ListPartViewHolder(view);
       }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return typeView;
    }



    class ListPartViewHolder extends RecyclerView.ViewHolder{
        TextView title,tileAcronym;
        MaterialCardView cardView;
        ImageView img;

        public ListPartViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            img = itemView.findViewById(R.id.img);
            tileAcronym = itemView.findViewById(R.id.tile_abr);
            cardView = itemView.findViewById(R.id.cardview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavController navController = Navigation.findNavController(view);
                    navController.getGraph().findNode(R.id.listSongsFragment).setLabel("Reveillons nous");
                    navController.navigate(R.id.action_homeFragment_to_listSongsFragment2);
                }
            });
        }
    }


}
