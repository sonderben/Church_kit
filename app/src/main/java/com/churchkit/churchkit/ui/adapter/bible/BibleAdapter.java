package com.churchkit.churchkit.ui.adapter.bible;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.churchkit.churchkit.R;
import com.churchkit.churchkit.database.entity.bible.BibleBook;
import com.churchkit.churchkit.ui.util.Util;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class BibleAdapter extends RecyclerView.Adapter {


    int typeView;
    boolean fromBookByTestamentFragment = false;
    FragmentManager fm;
    Context context;
    Resources resource;
    List<BibleBook> bibleBooks;

    int amountOldTestament = 0;
    int amountNewTestament = 0;

    public void setAmountOldTestament(int testament){
        this.amountOldTestament = testament;
    }
    public void setAmountNewTestament(int testament){
        this.amountNewTestament = testament;
    }

    public void setTypeView(int typeView) {
        this.typeView = typeView;
        notifyDataSetChanged();
    }



    public BibleAdapter(FragmentManager fm){
        this.fm = fm;
    }
    public void config(int typeView,List<BibleBook> bibleBooks){
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
        if (context == null) {
            context = holder.itemView.getContext();
            resource = context.getResources();
        }
        if (typeView ==0){ // group by testament
            Bundle bundle = new Bundle();
            if(position == 0) {
                ((GroupByTestamentViewHolder) holder).textView.setText(R.string.old_testament);
                //( (GroupByTestamentViewHolder) holder ).img.setImageResource(R.mipmap.ot);

                Glide.with(( (GroupByTestamentViewHolder) holder ).img)
                        .load("https://images.pexels.com/photos/5986493/pexels-photo-5986493.jpeg?cs=srgb&dl=pexels-cottonbro-studio-5986493.jpg&fm=jpg&_gl=1*1x83rz0*_ga*MTA1MjM4NDYwNy4xNjgzMDk0MzY1*_ga_8JE65Q40S6*MTY4NDE2MzU0Ny43LjEuMTY4NDE2NDExMi4wLjAuMA..")
                        .placeholder(R.mipmap.img_bg_creole)
                        .into( ( (GroupByTestamentViewHolder) holder ).img );

                ( (GroupByTestamentViewHolder) holder ).bookNumber.setText(amountOldTestament+" "+resource.getString(R.string.books));
                bundle.putInt("TESTAMENT",-2);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NavController navController = Navigation.findNavController(v);
                        navController.getGraph().findNode(R.id.listChapterByTestamentFragment).setLabel(holder.itemView.getContext().getString( R.string.old_testament ));
                        navController.navigate(R.id.action_bookmarkFragment_to_listBookByTestamentFragment,bundle);
                    }
                });

            }
            if (position == 1) {
                bundle.putInt("TESTAMENT",2);
                ((GroupByTestamentViewHolder) holder).textView.setText(R.string.new_testament);
                ( (GroupByTestamentViewHolder) holder ).img.setImageResource(R.mipmap.nt);
                ( (GroupByTestamentViewHolder) holder ).bookNumber.setText(amountNewTestament+" "+resource.getString(R.string.books));

                Glide.with(( (GroupByTestamentViewHolder) holder ).img)
                        .asBitmap()
                        .load("https://images.pexels.com/photos/8814959/pexels-photo-8814959.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")
                        .error(R.mipmap.nt)
                        .into( ( (GroupByTestamentViewHolder) holder ).img );





                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NavController navController = Navigation.findNavController(v);
                        navController.getGraph().findNode(R.id.listChapterByTestamentFragment).setLabel(holder.itemView.getContext().getString( R.string.new_testament ));
                        navController.navigate(R.id.action_bookmarkFragment_to_listBookByTestamentFragment,bundle);
                    }
                });
            }
        }else {
            int tempPosition = holder.getAbsoluteAdapterPosition() + 1;
            int posColor = tempPosition<11?tempPosition:tempPosition%10;
            final int color = Util.getColorByPosition( posColor );

                ((AllBookViewHolder) holder).title.setText(bibleBooks.get(position).getTitle());
                ((AllBookViewHolder) holder).tileAcronym.setText(bibleBooks.get(position).getAbbreviation());
                ((AllBookViewHolder) holder).number.setText( bibleBooks.get(position).getChildAmount()+" Chapters" );
            ((AllBookViewHolder) holder).tileAcronym.setTextColor(color);

            ((AllBookViewHolder) holder).itemView.setOnClickListener(view -> {
                BibleBook bibleBook = bibleBooks.get(position);
                Bundle bundle = new Bundle();

                bundle.putString("ID", bibleBook.getId());
                bundle.putString("BOOK_NAME_ABBREVIATION", bibleBook.getAbbreviation());
                NavController navController = Navigation.findNavController(view);
                navController.getGraph().findNode(R.id.listChapterFragment).setLabel(bibleBook.getTitle()+" ");

                if (fromBookByTestamentFragment){
                    navController.navigate(R.id.action_listChapterByTestamentFragment_to_listChapterFragment,bundle);
                }else {
                    navController.navigate(R.id.action_bookmarkFragment_to_listChapterFragment,bundle);
                }




            });
        }
    }

    @Override
    public int getItemCount() {
        //0=2
        return typeView == 0?2: bibleBooks.size();
    }

    @Override
    public int getItemViewType(int position) {
        return typeView;
    }

    class GroupByTestamentViewHolder extends RecyclerView.ViewHolder{
        TextView textView,bookNumber;
        ImageView img;
        public GroupByTestamentViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.name);
            img = itemView.findViewById(R.id.img1);
            bookNumber = itemView.findViewById(R.id.book_number);

            //view.getLayoutParams().height = (int) (Util.getScreenDisplayMetrics(parent.getContext()).heightPixels * 0.5f);
            //img.getLayoutParams().height = 100;
        }
    }


    class AllBookViewHolder extends RecyclerView.ViewHolder{
        TextView title,tileAcronym,number;
        MaterialCardView cardView;
        ImageView img;
        View view;

        public AllBookViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            img = itemView.findViewById(R.id.img);
            tileAcronym = itemView.findViewById(R.id.tile_abr);
            number = itemView.findViewById(R.id.number_song);
            cardView = itemView.findViewById(R.id.cardview);

            itemView.findViewById(R.id.lang).setVisibility(View.GONE);
            view = itemView.findViewById(R.id.point);
            view.setVisibility(View.GONE);
            //view.


        }
    }
}
