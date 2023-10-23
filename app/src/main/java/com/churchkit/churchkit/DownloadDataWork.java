package com.churchkit.churchkit;

import static com.churchkit.churchkit.util.Constant.BIBLE;
import static com.churchkit.churchkit.util.Constant.NOTIFICATION_CHANNEL_ID;
import static com.churchkit.churchkit.util.Constant.SONG;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.churchkit.churchkit.database.CKBibleDb;
import com.churchkit.churchkit.database.CKSongDb;
import com.churchkit.churchkit.database.dao.bible.BibleDaoGeneral4Insert;
import com.churchkit.churchkit.database.dao.bible.BibleInfoDao;
import com.churchkit.churchkit.database.dao.song.SongDaoGeneral4Insert;
import com.churchkit.churchkit.database.dao.song.SongInfoDao;
import com.churchkit.churchkit.database.entity.base.BaseInfo;
import com.churchkit.churchkit.database.entity.bible.BibleBook;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.bible.BibleInfo;
import com.churchkit.churchkit.database.entity.bible.BibleVerse;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongBook;
import com.churchkit.churchkit.database.entity.song.SongInfo;
import com.churchkit.churchkit.database.entity.song.Verse;
import com.churchkit.churchkit.modelview.bible.BibleInfoViewModel;
import com.churchkit.churchkit.modelview.song.SongInfoViewModel;
import com.churchkit.churchkit.ui.data.DataFragmentDelete;
import com.churchkit.churchkit.ui.data.DataViewModel;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Subscription;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DownloadDataWork extends Worker {
    Context context;
    /*public final static String BIBLE = "BIBLE";
    public final static String SONG = "SONG";*/
    public final static String BASE_INFO_TYPE = "BASE_INFO_TYPE";
    private CountDownLatch countDownLatchDownload = new CountDownLatch(2);



    public final static String BASE_INFO_URL = "BASE_INFO_URL";
    public final static String BASE_INFO_ID = "BASE_INFO_ID";
    public final static String BASE_INFO_NAME = "BASE_INFO_NAME";
    public static final String PROGRESS = "PROGRESS";
    public static final String INFO_PREPOPULATE = "INFO_PREPOPULATE";

    private DataViewModel dataViewModel;
    private CKPreferences ckPreferences;

    public DownloadDataWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        ckPreferences = new CKPreferences( context );
        setProgressAsync(new Data.Builder().putInt(PROGRESS, 0).build());
        setProgressAsync(new Data.Builder().putString(INFO_PREPOPULATE, null).build());

    }

    @NonNull
    @Override
    public Result doWork() {
        Data data = getInputData();
        String baseInfoUrl = data.getString("BASE_INFO_URL");
        String baseInfoId = data.getString("BASE_INFO_ID");
        String name = data.getString(BASE_INFO_NAME);
        String TYPE_BASE_INFO = data.getString(BASE_INFO_TYPE); // BIBLE or SONG


        if (TYPE_BASE_INFO.equals("BIBLE") || TYPE_BASE_INFO.equals("SONG")) {
            downloadDataFromFireBaseStorage(baseInfoUrl, baseInfoId,name, TYPE_BASE_INFO);
            return Result.success();
        }


        return Result.failure();
    }

    public void prepopulateBibleFromJSonFile(String jsonStr, String bibleInfoId) {

        CKBibleDb ckBibleDb = CKBibleDb.getInstance( context.getApplicationContext() );
        BibleDaoGeneral4Insert bibleDaoGeneral4Insert = ckBibleDb.bibleDaoGeneral4Insert();
        BibleInfoDao bibleInfoDao =  ckBibleDb.bibleInfoDao();

        Flowable.fromAction(() -> {
                    try {

                        JSONObject jsonObject = new JSONObject(jsonStr);
                        JSONArray data = jsonObject.getJSONArray("data");

                        for (int a = 0; a < data.length(); a++) {
                            JSONObject songBookJson = data.getJSONObject(a);

                            int testament = songBookJson.getString("testament").equalsIgnoreCase("OT") ? -1 : 1;
                            String color;
                            String image;
                            try {
                                color = songBookJson.getString("color");
                                image = songBookJson.getString("image");
                            } catch (JSONException e) {
                                color = null;
                                image = null;
                            }
                            String bibleBookId = bibleInfoId + "_" + songBookJson.getString("id");
                            BibleBook bibleBook = new BibleBook(bibleInfoId,
                                    bibleBookId,
                                    songBookJson.getString("abbr"),
                                    songBookJson.getString("name"),
                                    songBookJson.getInt("position"),
                                    testament,
                                    songBookJson.getInt("amountChapter"),
                                    color,
                                    image);


                            try {
                                bibleDaoGeneral4Insert.insertBibleBook(bibleBook);
                            } catch (Throwable t) {
                                System.err.println(t.getMessage());
                            }

                            JSONArray bibleChapterList = songBookJson.getJSONArray("bibleChapterList");


                            for (int jj = 0; jj < bibleChapterList.length(); jj++) {
                                JSONObject chapterJson = bibleChapterList.getJSONObject(jj);

                                String bibleChapterId = bibleBookId + chapterJson.getInt("position");

                                BibleChapter bibleChapter = new BibleChapter(bibleInfoId,
                                        bibleChapterId + "",
                                        songBookJson.getString("name"),
                                        chapterJson.getInt("position"),
                                        bibleBookId + "",
                                        songBookJson.getString("abbr")
                                );

                                setProgressAsync(new Data.Builder().putString(INFO_PREPOPULATE, bibleBook.getTitle()+" / "+bibleChapter.getPosition() ).build());


                                JSONArray bibleVerseArray = chapterJson.getJSONArray("bibleVerses");


                                try {
                                    bibleDaoGeneral4Insert.insertBibleChapter(bibleChapter);
                                } catch (Throwable t) {
                                    System.err.println(t.getMessage());
                                }
                                List<BibleVerse> bibleVerses = new ArrayList<>();
                                for (int i = 0; i < bibleVerseArray.length(); i++) {
                                    JSONObject verseJson = bibleVerseArray.getJSONObject(i);

                                    String reference = songBookJson.getString("abbr") + " " + chapterJson.getInt("position") + ":" + verseJson.getInt("position");

                                    String bibleVerseId = bibleInfoId + " " + reference;


                                    BibleVerse bibleVerse = new BibleVerse(
                                            bibleInfoId
                                            , bibleVerseId
                                            , verseJson.getInt("position")
                                            , reference
                                            , verseJson.getString("verseText")
                                            , bibleChapterId);
                                    bibleVerses.add(bibleVerse);


                                }
                                try {
                                    bibleDaoGeneral4Insert.insertBibleVerses(bibleVerses);
                                } catch (Throwable t) {
                                    System.err.println(t.getMessage());
                                }


                            }


                        }

                        bibleInfoDao.updateIsDownloaded(true, bibleInfoId );
                        ckPreferences.setWorkRequestId( bibleInfoId,null );

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        countDownLatchDownload.countDown();
                    }
                })
                .subscribeOn(Schedulers.computation())
                //.observeOn( AndroidSchedulers.mainThread())
                .subscribe();
        /*try {
            countDownLatchDownload.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/
    }

    public void prepopulateSongFromJSonFile(String jsonStr, String songInfoId) {
        CKSongDb ckBibleDb = CKSongDb.getInstance( context.getApplicationContext() );
        SongDaoGeneral4Insert bibleDaoGeneral4Insert = ckBibleDb.songDaoGeneral4Insert();
        SongInfoDao songInfoDao =  ckBibleDb.songInfoDao();

        Flowable.fromAction(() -> {
                    try {
                        Gson gson = new Gson();

                        JSONObject jsonObject = new JSONObject(jsonStr);
                        JSONArray dataArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject songBookJson = dataArray.getJSONObject(i);

                            SongBook songBook = gson.fromJson(songBookJson.toString(), SongBook.class);
                            songBook.setSongInfoId(songInfoId);
                            songBook.setId(songInfoId + "_" + songBookJson.getString("id"));
                            songBook.setChildAmount(songBookJson.getInt("songAmount"));
                            songBook.setTitle(songBookJson.getString("name"));


                            try {
                                bibleDaoGeneral4Insert.insertSongBook(songBook);
                            } catch (Throwable t) {
                                // Handle the Throwable
                                System.err.println("Caught Throwable: " + t.getMessage());
                            }


                            JSONArray songArrayJson = songBookJson.getJSONArray("songList");


                            for (int j = 0; j < songArrayJson.length(); j++) {


                                JSONObject songObj = songArrayJson.getJSONObject(j);
                                Song song = gson.fromJson(songObj.toString(), Song.class);
                                String songId = songObj.getString("id") + UUID.randomUUID();
                                song.setId(songId);
                                song.setBookId(songInfoId + "_" + songBookJson.getString("id"));
                                song.setInfoId(songInfoId);


                                song.setBookTitle(songBook.getTitle());
                                song.setBookAbbreviation(songBook.getAbbreviation());


                                try {
                                    bibleDaoGeneral4Insert.insertSong(song);
                                    setProgressAsync(new Data.Builder().putString(INFO_PREPOPULATE, songBook.getTitle()+" / "+song.getTitle() ).build());
                                } catch (Throwable t) {
                                    // Handle the Throwable
                                    System.err.println("Caught Throwable: " + t.getMessage());
                                }

                                setProgressAsync(new Data.Builder().putString(INFO_PREPOPULATE, songBook.getTitle() + "/" + song.getTitle()).build());


                                JSONArray verseArray = songObj.getJSONArray("verseList");
                                List<Verse> verseList = new ArrayList<>();

                                for (int k = 0; k < verseArray.length(); k++) {
                                    JSONObject verseObj = verseArray.getJSONObject(k);
                                    Verse verse = gson.fromJson(verseObj.toString(), Verse.class);
                                    verse.setVerseId(songId + verseObj.getInt("position"));
                                    verse.setSongId(songId);
                                    verse.setInfoId(songInfoId);
                                    verse.setReference(floatToString(song.getPosition()) + " " + songBook.getAbbreviation() + " " + verseObj.getInt("position"));
                                    verse.setSongTitle(song.getTitle());
                                    verseList.add(verse);

                                }
                                System.out.println("afiche: " + song.getTitle() + " pos: " + song.getPosition());


                                try {
                                    bibleDaoGeneral4Insert.insertSongVerses(verseList);
                                } catch (Throwable t) {
                                    // Handle the Throwable
                                    System.err.println("Caught Throwable: " + t.getMessage());
                                }


                            }


                        }
                        songInfoDao.updateIsDownloaded(true, songInfoId );
                        ckPreferences.setWorkRequestId( songInfoId,null );


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }finally {
                        countDownLatchDownload.countDown();
                    }
                })
                .subscribeOn(Schedulers.computation())
                .subscribe();


    }

    private String floatToString(float position) {
        try {
            return String.valueOf((int) position);
        } catch (Exception e) {
            return String.valueOf(position);
        }
    }

    private void downloadDataFromFireBaseStorage(String bibleInfoUrl, String baseInfoId,String name, String TYPE_BASE_INFO) {


        FirebaseStorage fs = FirebaseStorage.getInstance();
        StorageReference storageRef = fs.getReference().child(bibleInfoUrl);


        String path = bibleInfoUrl.replace("bible/", "");
        path = path.replace("song/", "");

        System.out.println("path: " + path);
        File localFile = new File(context.getCacheDir(), path);


        FileDownloadTask downloadTask = storageRef.getFile(localFile);


        String finalPath = path;
        downloadTask.addOnProgressListener(snapshot -> {
                    double percent = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    Log.i("Downloadpercent", "percent: " + percent);
                    setProgressAsync(new Data.Builder().putInt(PROGRESS, (int) percent).build());


                    if (percent == 100) {

                        readFromCache(finalPath,name, baseInfoId, TYPE_BASE_INFO);
                        //setProgressAsync(new Data.Builder().putInt(PROGRESS, (int) percent).build());

                    }
                })
                .addOnFailureListener(e -> {
                    countDownLatchDownload.countDown();
                    downloadTask.cancel();
                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        countDownLatchDownload.countDown();
                    }
                }).addOnCompleteListener(task -> {
                    countDownLatchDownload.countDown();
                });

        try {
            countDownLatchDownload.await();
        } catch (InterruptedException e) {
        }


    }


    private void readFromCache(String path,String name, String bibleInfoId, String TYPE_BASE_INFO) {

        /*String path = bibleInfo.getUrl().replace("bible/","");
        path = path.replace("song/","");*/
        File localFile = new File(context.getCacheDir(), path);
        StringBuilder stringBuilder = new StringBuilder();

        try {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(localFile)));


            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            if (TYPE_BASE_INFO.equals( BIBLE )) {
                prepopulateBibleFromJSonFile(stringBuilder.toString(), bibleInfoId);

            }
            if ( TYPE_BASE_INFO.equals( SONG ) ) {
                prepopulateSongFromJSonFile(stringBuilder.toString(), bibleInfoId);
            }
            createNotification(name,context);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public  void createNotification(String name, Context context){
        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.church_kit_logo)
                .setContentTitle("Download complete")
                .setContentText(name)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent( pendingIntent )
                .setAutoCancel( true );


        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

        notificationManager.notify(new Random().nextInt(180693),builder.build());

    }

}
