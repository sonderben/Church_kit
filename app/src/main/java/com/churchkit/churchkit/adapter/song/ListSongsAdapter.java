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
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongFavoriteWrapper;
import com.churchkit.churchkit.database.entity.song.SongHistoryWrapper;
import com.churchkit.churchkit.ui.song.SongDialogFragment;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

public class ListSongsAdapter extends RecyclerView.Adapter<ListSongsAdapter.ListSongsViewHolder> {

    List<Song>songList;
    List<SongFavoriteWrapper>songFavoriteWrapperList;
    List<SongHistoryWrapper> songHistoryWrapperList;
    FragmentManager fm;
    String songBookName;

    public ListSongsAdapter( FragmentManager fm,String songBookName){
        this.songBookName = songBookName;
        this.fm = fm;
    }

    public  void setSongList(List<Song>songList){
       this.songList = songList;
    }
    public void setSongFavoriteWrapperList(List<SongFavoriteWrapper>sfw){
        songFavoriteWrapperList = sfw;
    }
    public void setSongHistoryWrapperList(List<SongHistoryWrapper>shw){
        songHistoryWrapperList = shw;
    }

    @NonNull
    @Override
    public ListSongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_song_item_view_holder,parent,false);
        return new ListSongsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListSongsViewHolder holder, int position) {

        if (songList != null){
            final Song tempSong = songList.get(position);
            holder.title.setText(tempSong.getTitle());
            holder.number.setText( formatNumberToString( tempSong.getPosition() ) );
            holder.date.setVisibility(View.GONE);

            holder.itemView.setOnClickListener(view -> {
                SongDialogFragment listChapter = SongDialogFragment.newInstance(tempSong.getSongID(),formatNumberToString(tempSong.getPosition()) +songBookName, tempSong.getTitle());
                listChapter.show(fm,"kek");
            } );
        }else if (songFavoriteWrapperList != null){
            final Song tempSong = songFavoriteWrapperList.get(position).getSong();//songList.get(position);
            holder.title.setText(tempSong.getTitle());
            holder.number.setText( formatNumberToString( tempSong.getPosition() ) );

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis( songFavoriteWrapperList.get(position).getDate() );
            DateFormat dateFormat=DateFormat.getDateInstance(DateFormat.MEDIUM);

            holder.date.setText( dateFormat.format(calendar.getTime()) );

            holder.itemView.setOnClickListener(view -> {
                songBookName = songFavoriteWrapperList.get(holder.getAbsoluteAdapterPosition()).getSongBookName();
                SongDialogFragment listChapter = SongDialogFragment.newInstance(tempSong.getSongID(),/*formatNumberToString(tempSong.getPosition()) +*/songBookName, tempSong.getTitle());
                listChapter.show(fm,"kk");
            } );
        }else if (songHistoryWrapperList!=null){
            final Song tempSong = songHistoryWrapperList.get(position).getSong();//songList.get(position);
            holder.title.setText(tempSong.getTitle());
            holder.number.setText( formatNumberToString( tempSong.getPosition() ) );

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis( songHistoryWrapperList.get(position).getDate() );
            DateFormat dateFormat=DateFormat.getDateInstance(DateFormat.MEDIUM);

            holder.date.setText( dateFormat.format(calendar.getTime()) );

            holder.itemView.setOnClickListener(view -> {
                songBookName = songHistoryWrapperList.get(holder.getAbsoluteAdapterPosition()).getSongBookName();
                SongDialogFragment listChapter = SongDialogFragment.newInstance(tempSong.getSongID(),/*formatNumberToString(tempSong.getPosition()) +*/songBookName, tempSong.getTitle());
                listChapter.show(fm,"kk");
            } );
        }

    }

    @Override
    public int getItemCount() {
        if (songList != null){
            return songList.size();
        }
        if (songHistoryWrapperList != null){
            return songHistoryWrapperList.size();
        }
        if (songFavoriteWrapperList != null){
            return songFavoriteWrapperList.size();
        }
        return 0;
    }

    class ListSongsViewHolder extends RecyclerView.ViewHolder{

        TextView number,title,date;
        public ListSongsViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
        }
    }

}
