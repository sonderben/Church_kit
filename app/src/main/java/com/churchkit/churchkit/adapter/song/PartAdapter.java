package com.churchkit.churchkit.adapter.song;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.churchkit.churchkit.Model.Part;
import com.churchkit.churchkit.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class PartAdapter extends RecyclerView.Adapter<PartAdapter.MyViewHolder> {

    List<Part> parts;
    GridLayoutManager gridLayoutManager;
    public void setPart(List<Part> parts,GridLayoutManager gridLayoutManager){
        this.parts=parts;
        notifyDataSetChanged();
        this.gridLayoutManager = gridLayoutManager;
    }
    @NonNull
    @Override

    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType != 0){
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_part2, parent, false);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_part, parent, false);
        }

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if(holder.img != null){
            holder.img.setImageResource( parts.get(position).getImg() );
            holder.tileAcronym.setBackgroundColor(parts.get(position).getColor() );
        }else {
            holder.tileAcronym.setBackgroundTintList(ColorStateList.valueOf(parts.get(position).getColor()));
        }

        holder.title.setText( parts.get(position).getTitle() );
        holder.tileAcronym.setText( parts.get(position).getTitleAcronym() );

    }

    @Override
    public int getItemViewType(int position) {
        if (gridLayoutManager.getSpanCount()==1)
            return 0;
        return 1;
    }

    @Override
    public int getItemCount() {
        return parts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title,tileAcronym;
        MaterialCardView cardView;
        ImageView img;
        public MyViewHolder(@NonNull View itemView) {
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
                    navController.navigate(R.id.action_listOptionFragment_to_listSongsFragment);

                    //Navigation.findNavController(view).navigate(R.id.action_listOptionFragment_to_listSongsFragment);

                }
            });
        }
    }
}
