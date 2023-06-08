package com.churchkit.churchkit.ui.bible;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.churchkit.churchkit.CKPreferences;
import com.churchkit.churchkit.R;
import com.churchkit.churchkit.Util;
import com.churchkit.churchkit.database.CKBibleDb;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.bible.BibleChapterFavoriteWrapper;
import com.churchkit.churchkit.database.entity.bible.BibleChapterHistoryWrapper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DateFormat;
import java.util.Calendar;
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
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        ckPreferences = new CKPreferences(getContext());



        db= CKBibleDb.getInstance( getContext() );





        switch (getArguments().getString("FROM")){
            case Util.FROM_BIBLE_FAVORITE:
                db.bibleChapterFavoriteDao().loadFavoritesChapter(ckPreferences.getBibleName() ).observe(getViewLifecycleOwner(), bibleChapterFavoriteBibleChapterMap -> {
                    adapter = new Adapter(getChildFragmentManager(), null);
                    adapter.setChapterFavoriteWrapperList( BibleChapterFavoriteWrapper.fromMap(bibleChapterFavoriteBibleChapterMap ) );
                    mRecyclerView.setAdapter( adapter );
                });
                break;
            case Util.FROM_BIBLE_HISTORY:
                db.bibleChapterHistoryDao().loadHistoriesChapter(ckPreferences.getBibleName()).observe(getViewLifecycleOwner(), bibleChapterFavoriteBibleChapterMap -> {
                    adapter = new Adapter(getChildFragmentManager(), null);
                    adapter.setChapterHistoryWrapperList( BibleChapterHistoryWrapper.fromMap(bibleChapterFavoriteBibleChapterMap ) );
                    mRecyclerView.setAdapter( adapter );
                });
                break;
            default:
                adapter = new Adapter(getChildFragmentManager(), getArguments().getString("BOOK_NAME_ABBREVIATION"));
                bibleBookId = getArguments().getString("ID");

                db.bibleChapterDao().getAllChapterByBookId(bibleBookId).observe(requireActivity(), bibleChapters -> {
                    adapter.setBibleChapters(bibleChapters);
                    mRecyclerView.setAdapter( adapter );

                });
        }



        return root;
    }

    RecyclerView mRecyclerView;
    Adapter adapter;
    CKBibleDb db/*= CKBibleDb.getInstance( getContext() )*/;
    static String bibleBookId;
    CKPreferences ckPreferences;

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
    }

    static class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>{
        FragmentManager fm;
        List<BibleChapter> bibleChapters;
        List<BibleChapterFavoriteWrapper> chapterFavoriteWrapperList;
        List<BibleChapterHistoryWrapper> chapterHistoryWrapperList;
        String bookNameAbbreviation;

        public void setBibleChapters(List<BibleChapter> bibleChapters) {
            this.bibleChapters = bibleChapters;
        }

        public void  setChapterFavoriteWrapperList(List<BibleChapterFavoriteWrapper>bcfw){
            this.chapterFavoriteWrapperList = bcfw;
        }

        public void setChapterHistoryWrapperList(List<BibleChapterHistoryWrapper> bibleChapterHistoryWrapperList){
            this.chapterHistoryWrapperList= bibleChapterHistoryWrapperList;
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

            BibleChapter bibleChapter;
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
            Calendar calendar = Calendar.getInstance();

            if (bibleChapters != null){
                bibleChapter = bibleChapters.get(holder.getAbsoluteAdapterPosition());

                holder.date.setVisibility(View.GONE);

                holder.chapter.setText( Util.formatNumberToString( bibleChapter.getPosition() ) );
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //String reference = bookNameAbbreviation+" chapter "+bibleChapter.getPosition();
                        ChapterDialogFragment dialogFragment = ChapterDialogFragment.newInstance(
                                bibleChapter);
                        dialogFragment.show(fm, "ChapterDialogFragment");
                    }
                });
            }else if (chapterFavoriteWrapperList != null){
                bibleChapter = chapterFavoriteWrapperList.get(holder.getAbsoluteAdapterPosition()).getBibleChapter();
                //holder.chapter.setText(bibleChapter.getPosition()+"");
                holder.chapter.setText( Util.formatNumberToString( bibleChapter.getPosition() ) );
                calendar.setTimeInMillis( chapterFavoriteWrapperList.get(holder.getAbsoluteAdapterPosition()).getDate() );

                holder.date.setText(dateFormat.format(calendar.getTime()));

                holder.itemView.setOnClickListener(v -> {
                    //String reference = chapterFavoriteWrapperList.get(holder.getAbsoluteAdapterPosition()).getAbbreviation();
                    ChapterDialogFragment dialogFragment = ChapterDialogFragment.newInstance(
                            bibleChapter);
                    dialogFragment.show(fm, "ChapterDialogFragment");
                });
            }else if (chapterHistoryWrapperList != null){
                bibleChapter = chapterHistoryWrapperList.get(holder.getAbsoluteAdapterPosition()).getBibleChapter();
                //holder.chapter.setText(bibleChapter.getPosition()+"");
                holder.chapter.setText( Util.formatNumberToString( bibleChapter.getPosition() ) );


                calendar.setTimeInMillis( chapterHistoryWrapperList.get(holder.getAbsoluteAdapterPosition()).getDate() );

                holder.date.setText(dateFormat.format(calendar.getTime()));

                holder.itemView.setOnClickListener(v -> {
                    //String reference = chapterHistoryWrapperList.get(holder.getAbsoluteAdapterPosition()).getAbbreviation();
                    ChapterDialogFragment dialogFragment = ChapterDialogFragment.newInstance(
                            bibleChapter);
                    dialogFragment.show(fm, "ChapterDialogFragment");
                });
            }
        }




        @Override
        public int getItemCount() {
            if (bibleChapters != null){
                return bibleChapters.size();
            }
            if (chapterFavoriteWrapperList != null){
                return  chapterFavoriteWrapperList.size();
            }
            if (chapterHistoryWrapperList != null){
                return chapterHistoryWrapperList.size();
            }
            return 0;
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView chapter,date;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                chapter = itemView.findViewById(R.id.chapter);
                date = itemView.findViewById(R.id.date);

            }
        }
    }
}