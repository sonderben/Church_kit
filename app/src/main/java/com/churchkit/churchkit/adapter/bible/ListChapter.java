package com.churchkit.churchkit.adapter.bible;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.churchkit.churchkit.R;

public class ListChapter extends DialogFragment {
    public ListChapter(){}
    public static ListChapter newInstance(){
        return new ListChapter();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_list_chapter,container,false);
        mRecyclerView = root.findViewById(R.id.recyclerview);
        adapter = new Adapter();
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        mRecyclerView.setAdapter( adapter );
        closeButton = root.findViewById(R.id.close);
        closeButton.setOnClickListener(x->this.dismissNow());
        return root;
    }

    RecyclerView mRecyclerView;
    Adapter adapter;
    ImageView closeButton;
    class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>{

        public Adapter(){

        }
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_chapter,parent,false);

            return new MyViewHolder( view );
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.chapter.setText(position+1+"");
        }

        @Override
        public int getItemCount() {
            return 50;
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView chapter;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                chapter = itemView.findViewById(R.id.chapter);
            }
        }
    }
}
