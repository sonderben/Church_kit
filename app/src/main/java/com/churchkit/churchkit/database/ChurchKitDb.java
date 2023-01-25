package com.churchkit.churchkit.database;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.churchkit.churchkit.database.dao.SongBookDao;
import com.churchkit.churchkit.database.dao.SongDao;
import com.churchkit.churchkit.database.dao.VerseDao;
import com.churchkit.churchkit.database.entity.Song;
import com.churchkit.churchkit.database.entity.SongBook;
import com.churchkit.churchkit.database.entity.Verse;

//@TypeConverters({ChurchKitConverter.class})
@Database(entities = {SongBook.class, Song.class, Verse.class},version = 1,exportSchema = false)
public abstract class ChurchKitDb extends RoomDatabase {
    public abstract SongBookDao songBookDao();
    public abstract SongDao songDao();
    public abstract VerseDao verseDao();

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
