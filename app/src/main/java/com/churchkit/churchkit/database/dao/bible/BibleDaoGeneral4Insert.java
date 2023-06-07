package com.churchkit.churchkit.database.dao.bible;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.churchkit.churchkit.database.entity.bible.BibleBook;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.bible.BibleVerse;

import java.util.List;

@Dao
public interface BibleDaoGeneral4Insert {
    @Insert//(onConflict = OnConflictStrategy.REPLACE)
    long insertBibleBook(BibleBook bibleBook);
    @Insert//(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertBibleChapters(List<BibleChapter> bibleChapters);

    @Insert//(onConflict = OnConflictStrategy.REPLACE)
    long insertBibleChapter(BibleChapter bibleChapters);

    @Insert//(onConflict = OnConflictStrategy.REPLACE)
    long insertBibleVerse(BibleVerse bibleVerse);

    @Insert//(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertBibleVerses(List<BibleVerse> bibleVerse);
}
