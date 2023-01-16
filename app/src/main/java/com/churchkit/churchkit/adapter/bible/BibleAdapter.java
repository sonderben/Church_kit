package com.churchkit.churchkit.adapter.bible;

import android.app.Activity;
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

public class BibleAdapter extends RecyclerView.Adapter {


    int typeView;
    FragmentManager fm;

    /*private final int GROUP_BY_TESTAMENT = 0;
    private final int ALL_BOOK = 1;*/


    public void setTypeView(int typeView) {
        this.typeView = typeView;
        notifyDataSetChanged();
    }

    public BibleAdapter(int typeView,FragmentManager fm) {
        this.fm = fm;
        this.typeView = typeView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if(viewType == 0){
           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_part_group_by_lang,parent,false);
           return new GroupByTestamentViewHolder(view);
       }
       else {
           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_part,parent,false);
           return new AllBookViewHolder(view);
       }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (typeView ==0){
            if(position == 0)
            ( (GroupByTestamentViewHolder) holder ).textView.setText("Old Testament");
            else
                ( (GroupByTestamentViewHolder) holder ).textView.setText("New Testament");
        }else {
            if(position == 0) {
                ((AllBookViewHolder) holder).title.setText("Jenèz");
                ((AllBookViewHolder) holder).tileAcronym.setText("JZ");
                ((AllBookViewHolder) holder).number.setText("50 chapter");
            }else {
                ((AllBookViewHolder) holder).title.setText("Egzod");
                ((AllBookViewHolder) holder).tileAcronym.setText("ED");
                ((AllBookViewHolder) holder).number.setText("40 chapter");
            }
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

    class GroupByTestamentViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public GroupByTestamentViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {/*
                    NavController navController = Navigation.findNavController(view);
                    navController.getGraph().findNode(R.id.listOptionFragment).setLabel("Français");
                    navController.navigate(R.id.action_homeFragment_to_listOptionFragment);*/
                }
            });
        }
    }

    class AllBookViewHolder extends RecyclerView.ViewHolder{
        TextView title,tileAcronym,number;
        MaterialCardView cardView;
        ImageView img;

        public AllBookViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            img = itemView.findViewById(R.id.img);
            tileAcronym = itemView.findViewById(R.id.tile_abr);
            number = itemView.findViewById(R.id.number_song);
            cardView = itemView.findViewById(R.id.cardview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {/*
                    NavController navController = Navigation.findNavController(view);
                    navController.getGraph().findNode(R.id.listSongsFragment).setLabel("Reveillons nous");
                    navController.navigate(R.id.action_homeFragment_to_listSongsFragment2);


*/
                    ListChapter listChapter = ListChapter.newInstance();
                    listChapter.show(fm,"kk");


                }
            });
        }
    }
}
