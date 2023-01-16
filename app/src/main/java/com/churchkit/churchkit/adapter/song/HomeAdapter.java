package com.churchkit.churchkit.adapter.song;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.churchkit.churchkit.R;
import com.google.android.material.card.MaterialCardView;

public class HomeAdapter extends RecyclerView.Adapter {

    /*Switch switchMenuItem;
    GridLayoutManager gridLayoutManager;*/
    int typeView;

    /*public void setTypeView(int typeView) {
        this.typeView = typeView;
        if (typeView==0)
            notifyDataSetChanged();
        else
            notifyItemRangeChanged(0,getItemCount());
    }*/
    public void setTypeView(int newTypeViewHolder,int oldTypeViewHolder) {
        if (newTypeViewHolder == oldTypeViewHolder) // LIST==LIST || GRID==GRID || GROUP==GROUP => Just For security
            return;
        if (newTypeViewHolder != oldTypeViewHolder && (newTypeViewHolder == 0 || oldTypeViewHolder == 0) ) {
            this.typeView = newTypeViewHolder;
            notifyDataSetChanged();
        }else {

            this.typeView = newTypeViewHolder;
            notifyItemRangeChanged(0, getItemCount());
        }
    }

    public HomeAdapter(int typeView/*Switch switchMenuItem,GridLayoutManager gridLayoutManager*/) {
        this.typeView = typeView;
        /*
        this.switchMenuItem = switchMenuItem;
        this.gridLayoutManager = gridLayoutManager;

         */
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if(viewType == 0){
           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_part_group_by_lang,parent,false);
           return new GroupByLanguageViewHolder(view);
       }else if(viewType == 2) {
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
        /*if (switchMenuItem.isChecked()){
            return 0;
        }else {
            if(gridLayoutManager.getSpanCount() == 1)
            return 1;
            else return 2;
        }*/
        return typeView;
    }

    class GroupByLanguageViewHolder extends RecyclerView.ViewHolder{


        public GroupByLanguageViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavController navController = Navigation.findNavController(view);
                    navController.getGraph().findNode(R.id.listOptionFragment).setLabel("Français");
                    navController.navigate(R.id.action_homeFragment_to_listOptionFragment);
                }
            });
        }
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
