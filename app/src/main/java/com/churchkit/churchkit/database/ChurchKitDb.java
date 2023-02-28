package com.churchkit.churchkit.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.churchkit.churchkit.database.dao.bible.BibleBookDao;
import com.churchkit.churchkit.database.dao.bible.BibleChapterDao;
import com.churchkit.churchkit.database.dao.bible.BibleVerseDao;
import com.churchkit.churchkit.database.dao.song.SongBookDao;
import com.churchkit.churchkit.database.dao.song.SongDao;
import com.churchkit.churchkit.database.dao.song.VerseDao;
import com.churchkit.churchkit.database.entity.bible.BibleBook;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.bible.BibleVerse;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongBook;
import com.churchkit.churchkit.database.entity.song.Verse;

//@TypeConverters({ChurchKitConverter.class})
@Database(entities = {SongBook.class, Song.class, Verse.class, BibleBook.class, BibleChapter.class, BibleVerse.class},version = 1,exportSchema = false)
public abstract class ChurchKitDb extends RoomDatabase {
    public abstract SongBookDao songBookDao();
    public abstract SongDao songDao();
    public abstract VerseDao verseDao();
    public abstract BibleBookDao bibleBookDao();
    public abstract BibleChapterDao bibleChapterDao();
    public abstract BibleVerseDao bibleVerseDao();


    public static volatile ChurchKitDb instance;
    private static Context mContext;



    public static synchronized ChurchKitDb getInstance(Context context){
        mContext = context;
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ChurchKitDb.class,"church.db").addCallback(new Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    //insertData();
                    System.out.println("base de datos creada");


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
