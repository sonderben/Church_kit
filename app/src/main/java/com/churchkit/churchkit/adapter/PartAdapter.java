package com.churchkit.churchkit.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.churchkit.churchkit.Model.Part;
import com.churchkit.churchkit.R;

import java.util.List;

public class PartAdapter extends RecyclerView.Adapter<PartAdapter.MyViewHolder> {

    List<Part> parts;
    public void setPart(List<Part> parts){
        this.parts=parts;
        notifyDataSetChanged();
    }
    @NonNull
    @Override

    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_part, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(parts.get(position).getTitle());
        holder.tileAcronym.setText(parts.get(position).getTitleAcronym());
        holder.cardView.setCardBackgroundColor(parts.get(position).getColor());
        Log.i("benderson","jnuduf: "+position);
    }

    @Override
    public int getItemCount() {
        return parts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title,tileAcronym;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            tileAcronym = itemView.findViewById(R.id.tile_abr);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }
}
