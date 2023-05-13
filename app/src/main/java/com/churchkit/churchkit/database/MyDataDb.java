package com.churchkit.churchkit.database;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.churchkit.churchkit.CKPreferences;
import com.churchkit.churchkit.database.dao.note.NoteDao;
import com.churchkit.churchkit.database.dao.note.NoteDirectoryDao;
import com.churchkit.churchkit.database.entity.note.NoteDirectoryEntity;
import com.churchkit.churchkit.database.entity.note.NoteEntity;
import com.churchkit.churchkit.util.Converter;

import java.util.Calendar;

@TypeConverters(Converter.class)
@Database(entities = {NoteDirectoryEntity.class, NoteEntity.class},version = 1)
public abstract class MyDataDb extends RoomDatabase {
    public abstract NoteDao noteDao();
    public abstract NoteDirectoryDao noteDirectoryDao();

    private static MyDataDb instance;

    public static synchronized MyDataDb getInstance(Context context) {

//.fallbackToDestructiveMigration()

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            MyDataDb.class,  "my_data.db")
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            ContentValues noteDirectory=new ContentValues();
                            //noteDirectory.put("id",1);
                            noteDirectory.put("title","Default directory");
                            noteDirectory.put("note_amount",0);
                            noteDirectory.put("color","#00f");
                            noteDirectory.put("date", Calendar.getInstance().getTimeInMillis());
                            //noteDirectory.put("password","1234");
                           long a= db.insert("note_directory", OnConflictStrategy.REPLACE,noteDirectory);


                            ContentValues note=new ContentValues();
                            note.put("id",1);
                            note.put("title","Default note");
                            //note.put("note_amount",0);
                            note.put("noteText","genial");
                            note.put("date", Calendar.getInstance().getTimeInMillis());
                            note.put("note_directory_id",(int)a);
                            //noteDirectory.put("password","1234");
                            db.insert("note", OnConflictStrategy.IGNORE,note);
                        }
                    })
                    .build();
        }
        return instance;
    }
}
