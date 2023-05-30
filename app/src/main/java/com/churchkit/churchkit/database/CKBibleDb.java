package com.churchkit.churchkit.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.churchkit.churchkit.CKPreferences;
import com.churchkit.churchkit.database.dao.bible.BibleChapterDao;
import com.churchkit.churchkit.database.dao.bible.BibleChapterFavoriteDao;
import com.churchkit.churchkit.database.dao.bible.BibleChapterHistoryDao;
import com.churchkit.churchkit.database.dao.bible.BibleDaoGeneral4Insert;
import com.churchkit.churchkit.database.dao.bible.BibleVerseDao;
import com.churchkit.churchkit.database.dao.bible.BookMarkBibleDao;
import com.churchkit.churchkit.database.dao.song.BookMarkSongDao;
import com.churchkit.churchkit.database.dao.song.SongBookDao;
import com.churchkit.churchkit.database.dao.song.SongDao;
import com.churchkit.churchkit.database.dao.song.SongDaoGeneral4Insert;
import com.churchkit.churchkit.database.dao.song.SongFavoriteDao;
import com.churchkit.churchkit.database.dao.song.SongHistoryDao;
import com.churchkit.churchkit.database.dao.song.VerseDao;
import com.churchkit.churchkit.database.entity.base.FavHis;
import com.churchkit.churchkit.database.entity.bible.BibleBook;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.bible.BibleChapterFavorite;
import com.churchkit.churchkit.database.entity.bible.BibleChapterHistory;

import com.churchkit.churchkit.database.entity.bible.BibleVerse;
import com.churchkit.churchkit.database.entity.bible.BookMarkChapter;
import com.churchkit.churchkit.database.dao.bible.BibleBookDao;
import com.churchkit.churchkit.database.entity.bible.BibleVerseFts;
import com.churchkit.churchkit.database.entity.song.BookMarkSong;
import com.churchkit.churchkit.database.entity.bible.BibleChapterFts;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongBook;
import com.churchkit.churchkit.database.entity.song.SongFavorite;
import com.churchkit.churchkit.database.entity.song.SongFts;
import com.churchkit.churchkit.database.entity.song.SongHistory;
import com.churchkit.churchkit.database.entity.song.SongVerseFts;
import com.churchkit.churchkit.database.entity.song.Verse;


@Database(entities = { BibleBook.class, BibleChapter.class, BibleVerse.class,
        BibleChapterFavorite.class, BibleChapterHistory.class,  BibleChapterFts.class,
         BookMarkChapter.class, BibleVerseFts.class}, version = 1, exportSchema = false)
public abstract class CKBibleDb extends RoomDatabase {

    public abstract BibleBookDao bibleBookDao();

    public abstract BibleChapterDao bibleChapterDao();

    public abstract BibleVerseDao bibleVerseDao();

    public abstract BibleChapterFavoriteDao bibleChapterFavoriteDao();

    public abstract BibleChapterHistoryDao bibleChapterHistoryDao();

    public abstract BibleDaoGeneral4Insert bibleDaoGeneral4Insert();

    public abstract BookMarkBibleDao bookMarkBibleDao();




    public static volatile CKBibleDb instance;
    public static CKBibleDb instanceDownload;


    private static CKPreferences preferences;


    public static synchronized CKBibleDb getInstance(Context context) {
        preferences = new CKPreferences(context);
        String defBible = preferences.getBibleName();

        /*if (defBible.equals("null"))
            throw new RuntimeException("Bible name cant not be null");*/





        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            CKBibleDb.class, defBible + ".db")
                    .build();
        }
        return instance;
    }


    public static synchronized CKBibleDb getInstance4Insert(Context context, @NonNull String BIBLE) {


        instanceDownload = Room.databaseBuilder(context.getApplicationContext(),
                        CKBibleDb.class, BIBLE + ".db").addCallback(new Callback() {
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
