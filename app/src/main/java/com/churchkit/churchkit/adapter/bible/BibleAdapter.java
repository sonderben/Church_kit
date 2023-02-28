package com.churchkit.churchkit.adapter.bible;

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
import com.churchkit.churchkit.database.entity.bible.BibleBook;
import com.churchkit.churchkit.ui.bible.BibleFragment;
import com.churchkit.churchkit.ui.bible.ChapterDialogFragment;
import com.churchkit.churchkit.ui.bible.ListChapterFragment;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class BibleAdapter extends RecyclerView.Adapter {


    int typeView;
    boolean fromBookByTestamentFragment = false;
    FragmentManager fm;
    List<BibleBook> bibleBooks;

    public void setTypeView(int typeView) {
        this.typeView = typeView;
        notifyDataSetChanged();
    }

    public BibleAdapter(int typeView,FragmentManager fm,List<BibleBook> bibleBooks) {
        this.fm = fm;;
        this.typeView = typeView;
        this.bibleBooks=bibleBooks;
    }
    public BibleAdapter(FragmentManager fm,List<BibleBook> bibleBooks) {
        this.fm = fm;
        this.typeView = 1;
        fromBookByTestamentFragment= true;
        this.bibleBooks=bibleBooks;
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (typeView ==0){
            Bundle bundle = new Bundle();
            if(position == 0) {
                ((GroupByTestamentViewHolder) holder).textView.setText("Old Testament");
                bundle.putInt("TESTAMENT",-2);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NavController navController = Navigation.findNavController(v);
                        navController.getGraph().findNode(R.id.listChapterByTestamentFragment).setLabel("Old Testament");
                        navController.navigate(R.id.action_bookmarkFragment_to_listBookByTestamentFragment,bundle);
                    }
                });

            }
            if (position == 1) {
                bundle.putInt("TESTAMENT",2);
                ((GroupByTestamentViewHolder) holder).textView.setText("New Testament");
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NavController navController = Navigation.findNavController(v);
                        navController.getGraph().findNode(R.id.listChapterByTestamentFragment).setLabel("New Testament");
                        navController.navigate(R.id.action_bookmarkFragment_to_listBookByTestamentFragment,bundle);
                    }
                });
            }
        }else {
                ((AllBookViewHolder) holder).title.setText(bibleBooks.get(position).getTitle());
                ((AllBookViewHolder) holder).tileAcronym.setText(bibleBooks.get(position).getAbbreviation());
                ((AllBookViewHolder) holder).number.setText( bibleBooks.get(position).getAmountChapter()+" Chapters" );

            ((AllBookViewHolder) holder).itemView.setOnClickListener(view -> {
                BibleBook bibleBook = bibleBooks.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("ID", bibleBook.getBibleBookId());
                bundle.putString("BOOK_NAME_ABBREVIATION", bibleBook.getAbbreviation());
                NavController navController = Navigation.findNavController(view);
                navController.getGraph().findNode(R.id.listChapterFragment).setLabel(bibleBook.getTitle()+" ");

                if (fromBookByTestamentFragment){
                    navController.navigate(R.id.action_listChapterByTestamentFragment_to_listChapterFragment);
                }else {
                    navController.navigate(R.id.action_bookmarkFragment_to_listChapterFragment,bundle);
                }




            });
        }
    }

    @Override
    public int getItemCount() {
        return bibleBooks.size();
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


        }
    }
}
