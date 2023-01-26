package com.churchkit.churchkit.adapter.song;

import static com.churchkit.churchkit.Util.formatNumberToString;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.churchkit.churchkit.Model.Song;
import com.churchkit.churchkit.R;
import com.churchkit.churchkit.database.entity.Song;
import com.churchkit.churchkit.ui.song.SongDialogFragment;

import java.util.List;

public class ListSongsAdapter extends RecyclerView.Adapter<ListSongsAdapter.ListSongsViewHolder> {

    List<Song>songList;
    FragmentManager fm;
    String songBookName;

    public ListSongsAdapter(List<Song>songList, FragmentManager fm,String songBookName){
        this.songBookName = songBookName;
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

        final Song tempSong = songList.get(position);
        holder.title.setText(tempSong.getTitle());
        holder.number.setText( formatNumberToString( tempSong.getNum() ) );
            holder.imgBookMark.setVisibility(View.INVISIBLE);

        holder.itemView.setOnClickListener(view -> {
            SongDialogFragment listChapter = SongDialogFragment.newInstance(tempSong.getSongID(),formatNumberToString(tempSong.getNum()) +songBookName, tempSong.getTitle());
            listChapter.show(fm,"kk");
        } );
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


        }
    }

}
