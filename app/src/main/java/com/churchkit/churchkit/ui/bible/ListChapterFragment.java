package com.churchkit.churchkit.ui.bible;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.churchkit.churchkit.R;


public class ListChapterFragment extends Fragment {


    public ListChapterFragment() {
        // Required empty public constructor
    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_list_chapter2, container, false);
        mRecyclerView = root.findViewById(R.id.recyclerview);
        adapter = new Adapter(getChildFragmentManager());
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        mRecyclerView.setAdapter( adapter );

        return root;
    }

    RecyclerView mRecyclerView;
    Adapter adapter;

    static class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>{
FragmentManager fm;
        public Adapter(FragmentManager fm){
            this.fm = fm;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_chapter,parent,false);

            return new MyViewHolder(view);
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
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChapterDialogFragment dialogFragment = ChapterDialogFragment.newInstance(1,"Jan 2","Chapter 3 to 8");
                        dialogFragment.show(fm, "ChapterDialogFragment");
                    }
                });
            }
        }
    }
}