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
import com.churchkit.churchkit.adapter.bible.BibleAdapter;
import com.google.android.material.card.MaterialCardView;

public class HomeAdapter extends RecyclerView.Adapter {


    int typeView;


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

    public HomeAdapter(int typeView) {
        this.typeView = typeView;
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
        if (typeView ==0){
            if(position == 0)
                ( (GroupByLanguageViewHolder) holder ).textView.setText("Français");
            else
                ( (GroupByLanguageViewHolder) holder ).textView.setText("Kreyòl");
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return typeView;
    }

    class GroupByLanguageViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        public GroupByLanguageViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.name);
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
