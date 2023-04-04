package com.churchkit.churchkit.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.churchkit.churchkit.database.dao.bible.BibleBookDao;
import com.churchkit.churchkit.database.dao.bible.BibleChapterDao;
import com.churchkit.churchkit.database.dao.bible.BibleChapterFavoriteDao;
import com.churchkit.churchkit.database.dao.bible.BibleChapterHistoryDao;
import com.churchkit.churchkit.database.dao.bible.BibleVerseDao;
import com.churchkit.churchkit.database.dao.bible.BookMarkBibleDao;
import com.churchkit.churchkit.database.dao.song.BookMarkSongDao;
import com.churchkit.churchkit.database.dao.song.SongBookDao;
import com.churchkit.churchkit.database.dao.song.SongDao;
import com.churchkit.churchkit.database.dao.song.SongFavoriteDao;
import com.churchkit.churchkit.database.dao.song.SongHistoryDao;
import com.churchkit.churchkit.database.dao.song.VerseDao;
import com.churchkit.churchkit.database.entity.bible.BookMarkChapter;
import com.churchkit.churchkit.database.entity.song.BookMarkSong;
import com.churchkit.churchkit.database.entity.bible.BibleBook;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.bible.BibleChapterFavorite;
import com.churchkit.churchkit.database.entity.bible.BibleChapterFts;
import com.churchkit.churchkit.database.entity.bible.BibleChapterHistory;
import com.churchkit.churchkit.database.entity.bible.BibleVerse;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongBook;
import com.churchkit.churchkit.database.entity.song.SongFavorite;
import com.churchkit.churchkit.database.entity.song.SongFts;
import com.churchkit.churchkit.database.entity.song.SongHistory;
import com.churchkit.churchkit.database.entity.song.Verse;

//@TypeConverters({ChurchKitConverter.class})
@Database(entities = {SongBook.class, Song.class, Verse.class, BibleBook.class, BibleChapter.class,
        BibleVerse.class, SongFavorite.class,BibleChapterFavorite.class,
        BibleChapterHistory.class,SongHistory.class, SongFts.class, BibleChapterFts.class,
        BookMarkSong.class, BookMarkChapter.class},version = 1,exportSchema = false)
public abstract class ChurchKitDb extends RoomDatabase {
    public abstract SongBookDao songBookDao();
    public abstract SongDao songDao();
    public abstract VerseDao verseDao();
    public abstract BibleBookDao bibleBookDao();
    public abstract BibleChapterDao bibleChapterDao();
    public abstract BibleVerseDao bibleVerseDao();
    public abstract SongFavoriteDao songFavoriteDao();
    public abstract SongHistoryDao songHistoryDao();
    public abstract BibleChapterFavoriteDao bibleChapterFavoriteDao();
    public abstract BibleChapterHistoryDao bibleChapterHistoryDao();

    public abstract BookMarkBibleDao bookMarkBibleDao();
    public abstract BookMarkSongDao bookMarkSongDao();


    public static volatile ChurchKitDb instance;
    //private static Context mContext;



    public static synchronized ChurchKitDb getInstance(Context context){
       // mContext = context;
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ChurchKitDb.class,"church.db").addCallback(new Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);

                }

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                }
            })
                    //.addTypeConverter(new ChurchKitConverter(mContext))
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

}
