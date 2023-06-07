package com.churchkit.churchkit.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.churchkit.churchkit.CKPreferences;
import com.churchkit.churchkit.database.dao.song.BookMarkSongDao;
import com.churchkit.churchkit.database.dao.song.SongBookDao;
import com.churchkit.churchkit.database.dao.song.SongDao;
import com.churchkit.churchkit.database.dao.song.SongDaoGeneral4Insert;
import com.churchkit.churchkit.database.dao.song.SongFavoriteDao;
import com.churchkit.churchkit.database.dao.song.SongHistoryDao;
import com.churchkit.churchkit.database.dao.song.SongInfoDao;
import com.churchkit.churchkit.database.dao.song.VerseDao;
import com.churchkit.churchkit.database.entity.song.BookMarkSong;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongBook;
import com.churchkit.churchkit.database.entity.song.SongFavorite;
import com.churchkit.churchkit.database.entity.song.SongHistory;
import com.churchkit.churchkit.database.entity.song.SongInfo;
import com.churchkit.churchkit.database.entity.song.SongVerseFts;
import com.churchkit.churchkit.database.entity.song.Verse;


@Database(entities = {SongBook.class, SongInfo.class, Song.class, Verse.class, SongFavorite.class,
         SongHistory.class, /*SongFts.class,*/ SongVerseFts.class,BookMarkSong.class},
        version = 1, exportSchema = false)
public abstract class CKSongDb extends RoomDatabase {
    public abstract SongBookDao songBookDao();

    public abstract SongInfoDao songInfoDao();

    public abstract SongDao songDao();

    public abstract VerseDao verseDao();

    public abstract SongFavoriteDao songFavoriteDao();

    public abstract SongHistoryDao songHistoryDao();

    public abstract SongDaoGeneral4Insert songDaoGeneral4Insert();

    public abstract BookMarkSongDao bookMarkSongDao();

    //public abstract BookMarkSongDao bookMarkSongDao();


    public static volatile CKSongDb instance;
    public static CKSongDb instanceDownload;


    private static CKPreferences preferences;


    public static synchronized CKSongDb getInstance(Context context) {
        preferences = new CKPreferences(context);
        //String defBible = preferences.getSongName();


        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            CKSongDb.class,  "song.db")
                    .build();
        }
        return instance;
    }


    public static synchronized CKSongDb getInstance4Insert(Context context, @NonNull String song) {


        instanceDownload = Room.databaseBuilder(context.getApplicationContext(),
                        CKSongDb.class,  "song.db").addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);

                    }

                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                        super.onOpen(db);
                    }
                })
                .build();

        return instanceDownload;
    }

}
