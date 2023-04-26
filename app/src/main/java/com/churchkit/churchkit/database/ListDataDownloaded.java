package com.churchkit.churchkit.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.churchkit.churchkit.database.dao.bible.BibleInfoDao;
import com.churchkit.churchkit.database.dao.song.SongInfoDao;
import com.churchkit.churchkit.database.entity.bible.BibleInfo;
import com.churchkit.churchkit.database.entity.song.SongInfo;


@Database(entities = {BibleInfo.class, SongInfo.class},version = 1,exportSchema = false)
public abstract class ListDataDownloaded extends RoomDatabase {
    public abstract BibleInfoDao bibleInfoDao();
    public abstract SongInfoDao songInfoDao();


    public static volatile ListDataDownloaded instance;


    public static synchronized ListDataDownloaded getInstance(Context context){
        System.out.println("genial db");
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ListDataDownloaded.class,"listdata.db").addCallback(new Callback() {
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
        }
        return instance;
    }




}
