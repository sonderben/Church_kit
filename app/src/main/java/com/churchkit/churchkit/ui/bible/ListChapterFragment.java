package com.churchkit.churchkit.ui.bible;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.churchkit.churchkit.R;
import com.churchkit.churchkit.database.ChurchKitDb;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;

import java.util.List;


public class ListChapterFragment extends Fragment {


    public ListChapterFragment() {
        // Required empty public constructor
    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_list_chapter2, container, false);
        mRecyclerView = root.findViewById(R.id.recyclerview);
        adapter = new Adapter(getChildFragmentManager(), getArguments().getString("BOOK_NAME_ABBREVIATION"));
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));


        bibleBookId = getArguments().getString("ID");
       // String bookNameAbbreviation = getArguments().getString("BOOK_NAME_ABBREVIATION");

        db.bibleChapterDao().getAllChapterByBookId(bibleBookId).observe(requireActivity(), new Observer<List<BibleChapter>>() {
            @Override
            public void onChanged(List<BibleChapter> bibleChapters) {

                adapter.setBibleChapters(bibleChapters);
                mRecyclerView.setAdapter( adapter );

            }
        });

        return root;
    }

    RecyclerView mRecyclerView;
    Adapter adapter;
    ChurchKitDb db= ChurchKitDb.getInstance(getContext());
    static String bibleBookId;

    static class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>{
        FragmentManager fm;
        List<BibleChapter> bibleChapters;
        String bookNameAbbreviation;

        public void setBibleChapters(List<BibleChapter> bibleChapters) {
            this.bibleChapters = bibleChapters;
        }

        public Adapter(FragmentManager fm,String bookNameAbbreviation){
            this.bookNameAbbreviation = bookNameAbbreviation;
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
            holder.chapter.setText(bibleChapters.get(position).getPosition()+"");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String reference = bookNameAbbreviation+" chapter "+bibleChapters.get(holder.getAdapterPosition()).getPosition();
                    ChapterDialogFragment dialogFragment = ChapterDialogFragment.newInstance(
                            bibleChapters.get(holder.getAdapterPosition()).getBibleChapterId(),
                            reference);
                    dialogFragment.show(fm, "ChapterDialogFragment");
                }
            });
        }




        @Override
        public int getItemCount() {
            return bibleChapters.size();
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