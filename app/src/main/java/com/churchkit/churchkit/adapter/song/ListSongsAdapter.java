package com.churchkit.churchkit.adapter.song;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.churchkit.churchkit.Model.Song;
import com.churchkit.churchkit.R;
import com.churchkit.churchkit.ui.song.SongDialogFragment;

import java.util.List;

public class ListSongsAdapter extends RecyclerView.Adapter<ListSongsAdapter.ListSongsViewHolder> {

    List<Song>songList;
    FragmentManager fm;

    public ListSongsAdapter(List<Song>songList, FragmentManager fm){
        this.songList=songList;
        this.fm = fm;
    }

    @NonNull
    @Override
    public ListSongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_song_item_view_holder,parent,false);
        return new ListSongsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListSongsViewHolder holder, int position) {

        holder.title.setText(songList.get(position).getTitle());
        holder.number.setText( formatNumberToString( songList.get(position).getNumber() ) );
        if( !songList.get(position).isBookmark() ){
            holder.imgBookMark.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    class ListSongsViewHolder extends RecyclerView.ViewHolder{

        TextView number,title;
        ImageView imgBookMark;
        public ListSongsViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            title = itemView.findViewById(R.id.title);
            imgBookMark = itemView.findViewById(R.id.img_bookmark);

            itemView.setOnClickListener(view -> {

        SongDialogFragment listChapter = SongDialogFragment.newInstance();
        listChapter.show(fm,"kk");
            } );
        }
    }
    private String formatNumberToString(int number){
        if(number >99)
            return number+"";
        if (number>9)
            return "0"+number;

        return "00"+number;

    }
}
