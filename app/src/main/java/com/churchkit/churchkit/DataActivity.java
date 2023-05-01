package com.churchkit.churchkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.window.SplashScreen;

import com.churchkit.churchkit.database.CKBibleDb;
import com.churchkit.churchkit.database.CKSongDb;
import com.churchkit.churchkit.database.dao.bible.BibleDaoGeneral4Insert;
import com.churchkit.churchkit.database.dao.song.SongDaoGeneral4Insert;
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
import com.google.android.material.appbar.MaterialToolbar;
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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        origin = getIntent().getStringExtra("FROM");

        init();


        //List<Integer> collect1 = num.stream().map(n -> n * 2).collect(Collectors.toList());

        if (origin.equalsIgnoreCase("BIBLE")) {
            toolbar.setTitle("Select or download a bible");
            info.setVisibility(ckPreferences.isCurrentAndNextBibleEqual() ? View.GONE : View.VISIBLE);
            viewModel.getAllBibleInfo().observe(this, bibleInfoList -> {
                if (myAdapter.isDataEmpty())
                    myAdapter.setBibleInfoList(new ArrayList<>(bibleInfoList));
            });
        } else {
            toolbar.setTitle("Select or download a song book");
            info.setVisibility(ckPreferences.isCurrentAndNextSongEqual() ? View.GONE : View.VISIBLE);
            songInfoViewModel.getAllBibleInfo().observe(this, bibleInfoList -> {
                if (myAdapter.isDataEmpty())
                    myAdapter.setBibleInfoList(new ArrayList<>(bibleInfoList));
            });
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private BibleInfoViewModel viewModel;
    private SongInfoViewModel songInfoViewModel;
    private String origin;
    private CKPreferences ckPreferences;
    private String defaultBibleId;
    private TextView info;
    private MaterialToolbar toolbar;

    private void init() {
        recyclerView = findViewById(R.id.recyclerview);
        info = findViewById(R.id.info);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myAdapter = new MyAdapter();
        ckPreferences = new CKPreferences(getApplicationContext());
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        viewModel = new BibleInfoViewModel(getApplication());
        songInfoViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(SongInfoViewModel.class);
    }


    private final class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        List<BaseInfo> bibleInfoList = new ArrayList<>();


        public void setBibleInfoList(List<BaseInfo> bibleInfoList) {
            this.bibleInfoList = bibleInfoList;
            notifyDataSetChanged();
        }


        public boolean isDataEmpty() {
            return bibleInfoList.isEmpty();
        }


        private int oldPositionSelected = -1;

        @NonNull
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bible_info_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
            BaseInfo bibleInfo = bibleInfoList.get(holder.getAbsoluteAdapterPosition());
            holder.language.setText(bibleInfo.getLanguage());
            holder.title.setText(bibleInfo.getName());

            holder.download.setVisibility(bibleInfo.isDownloaded() ? View.GONE : View.VISIBLE);

            holder.testament.setText( origin.equalsIgnoreCase("BIBLE")?getStringTestament(bibleInfo.getTestament()):bibleInfo.getTestament()+""+getString(R.string.part) );


            holder.checkBox.setVisibility(!bibleInfo.isDownloaded() ? View.GONE : View.VISIBLE);

            if (bibleInfo.getId().equalsIgnoreCase(ckPreferences.getNextBibleName())) {
                oldPositionSelected = holder.getAbsoluteAdapterPosition();
            }

            if (bibleInfo instanceof BibleInfo)
                holder.checkBox.setChecked(bibleInfo.getId().equalsIgnoreCase(ckPreferences.getNextBibleName()));
            else  if (bibleInfo instanceof SongInfo)
                holder.checkBox.setChecked( bibleInfo.getId().equalsIgnoreCase(ckPreferences.getNextSongName()) );

            if (bibleInfo.getId().equalsIgnoreCase(ckPreferences.getNextBibleName())) {
                oldPositionSelected = holder.getAbsoluteAdapterPosition();
            }

            holder.download.setOnClickListener(v -> {

                v.setVisibility(View.GONE);
                if (bibleInfo instanceof BibleInfo)
                    downloadDataFromFireBaseStorage(bibleInfo.getId(), (BibleInfo) bibleInfo, holder);
                if (bibleInfo instanceof SongInfo)
                    downloadDataFromFireBaseStorage(bibleInfo.getId(), (SongInfo) bibleInfo, holder);//temp
                holder.info.setVisibility(View.VISIBLE);
            });

            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                BaseInfo bibleInfo1 = bibleInfoList.get(holder.getAbsoluteAdapterPosition());

                if (isChecked) {
                    if (bibleInfo instanceof BibleInfo)
                        ckPreferences.setNextBibleName(bibleInfo1.getId());
                    else if (bibleInfo instanceof SongInfo)
                        ckPreferences.setNextSongName( bibleInfo.getId() );
                    notifyItemChanged(oldPositionSelected);
                    oldPositionSelected = holder.getAbsoluteAdapterPosition();
                    defaultBibleId = bibleInfo1.getId();
                }

                if (bibleInfo instanceof BibleInfo)
                    info.setVisibility(ckPreferences.isCurrentAndNextBibleEqual() ? View.GONE : View.VISIBLE);
                else if (bibleInfo instanceof SongInfo)
                    info.setVisibility(ckPreferences.isCurrentAndNextSongEqual() ? View.GONE : View.VISIBLE);



            });

        }

        @Override
        public int getItemCount() {
            return bibleInfoList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView title, language, info, testament;
            ImageButton download;
            RadioButton checkBox;
            ProgressBar downloadProgressBar, prepopulateProgressBar;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.title);
                testament = itemView.findViewById(R.id.testament);
                language = itemView.findViewById(R.id.language);
                info = itemView.findViewById(R.id.info);
                download = itemView.findViewById(R.id.download);
                checkBox = itemView.findViewById(R.id.checkbox);
                downloadProgressBar = itemView.findViewById(R.id.progressBar3);
                prepopulateProgressBar = itemView.findViewById(R.id.progressBar2);
            }
        }

        public String getStringTestament(int testament) {
            switch (testament) {
                case 1:
                    return getString(R.string.new_testament);
                case 2:
                    return getString(R.string.old) + " " + getString(R.string.and) + " " + getString(R.string.new_testament);
                case -1:
                    return getString(R.string.old) + " " + getString(R.string.testament);
                default:
                    return "";
            }
        }
    }

    @Override
    public void onBackPressed() {
        //int number = Integer.parseInt(editText.getText().toString());
        Intent intent = new Intent();

        intent.putExtra("DEFAULT_BIBLE_ID", defaultBibleId);
        setResult(Activity.RESULT_OK, intent);
        finish();

        super.onBackPressed();
    }

    public void prepopulateBibleFromJSonFile(String jsonStr, MyAdapter.MyViewHolder myViewHolder, BaseInfo bibleInfo) {

        BibleDaoGeneral4Insert bibleDaoGeneral4Insert = CKBibleDb.getInstance4Insert(getBaseContext(), bibleInfo.getId()).bibleDaoGeneral4Insert();
        Flowable.fromAction(() -> {
                    try {

                        JSONObject jsonObject = new JSONObject(jsonStr);
                        JSONArray data = jsonObject.getJSONArray("data");

                        for (int a = 0; a < data.length(); a++) {
                            JSONObject songBookJson = data.getJSONObject(a);

                            int testament = songBookJson.getString("testament").equalsIgnoreCase("OT") ? -1 : 1;

                            BibleBook bibleBook = new BibleBook(
                                    songBookJson.getString("name") + songBookJson.getInt("position"),
                                    songBookJson.getString("abbr"),
                                    songBookJson.getString("name"),
                                    songBookJson.getInt("position"),
                                    testament,
                                    songBookJson.getInt("amountChapter"));

                            JSONArray bibleChapterList = songBookJson.getJSONArray("bibleChapterList");
                            List<BibleChapter> bibleChapters = new ArrayList<>(50);

                            for (int jj = 0; jj < bibleChapterList.length(); jj++) {
                                JSONObject chapterJson = bibleChapterList.getJSONObject(jj);


                                BibleChapter bibleChapter = new BibleChapter(
                                        chapterJson.getString("bookName") + chapterJson.getInt("position"),
                                        songBookJson.getString("name"),
                                        chapterJson.getInt("position"),
                                        songBookJson.getString("name") + songBookJson.getInt("position"),
                                        songBookJson.getString("abbr")
                                );
                                bibleChapters.add(bibleChapter);
                                JSONArray bibleVerseArray = chapterJson.getJSONArray("bibleVerses");

                                List<BibleVerse> bibleVerseList = new ArrayList<>(30);
                                for (int i = 0; i < bibleVerseArray.length(); i++) {
                                    JSONObject verseJson = bibleVerseArray.getJSONObject(i);

                                    String reference = songBookJson.getString("abbr") + " " + chapterJson.getInt("position") + ":" + verseJson.getInt("position");

                                    bibleVerseList.add(
                                            new BibleVerse(
                                                    /*verseJson.getString("reference")*/reference + verseJson.getInt("position")
                                                    , verseJson.getInt("position")
                                                    ,/*verseJson.getString("reference")*/ reference
                                                    , verseJson.getString("verseText")
                                                    , chapterJson.getString("bookName") + chapterJson.getInt("position"))

                                    );
                                }

                                // bibleViewModelGeneral4Insert.insert(bibleBook, bibleChapters, bibleVerseList);
                                bibleDaoGeneral4Insert.insertBibleBook(bibleBook);
                                bibleDaoGeneral4Insert.insertBibleChapter(new ArrayList<>(bibleChapters));
                                bibleDaoGeneral4Insert.insertBibleVerses(bibleVerseList);
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(new FlowableSubscriber<Object>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Subscription s) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                        System.out.println("onComplete: ");
                        myViewHolder.prepopulateProgressBar.setVisibility(View.GONE);
                        bibleInfo.setDownloaded(true);
                        viewModel.insert((BibleInfo) bibleInfo);
                        myViewHolder.info.setVisibility(View.GONE);
                        myViewHolder.checkBox.setVisibility(View.VISIBLE);
                    }
                });


    }

    public void prepopulateSongFromJSonFile(String jsonStr, MyAdapter.MyViewHolder myViewHolder, BaseInfo bibleInfo) {

        SongDaoGeneral4Insert bibleDaoGeneral4Insert = CKSongDb.getInstance4Insert(getBaseContext(), bibleInfo.getId()).songDaoGeneral4Insert();
        Flowable.fromAction(() -> {
                    try {
                        Gson gson = new Gson();

                        JSONObject jsonObject = new JSONObject(jsonStr);
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject songBookJson = dataArray.getJSONObject(i);

                            SongBook songBook = gson.fromJson(songBookJson.toString(),SongBook.class);
                            songBook.setId( songBookJson.getString("id") );
                            songBook.setChildAmount(songBookJson.getInt("songAmount"));
                            songBook.setTitle( songBookJson.getString("name") );



                            JSONArray songArrayJson = songBookJson.getJSONArray("songList");
                            List<Song>songList = new ArrayList<>();
                            for (int j = 0; j < songArrayJson.length(); j++) {

                                JSONObject songObj = songArrayJson.getJSONObject(j);
                                Song song = gson.fromJson(songObj.toString(),Song.class);
                                song.setId( songObj.getString("id") );
                                song.setBookId( songBookJson.getString("id") );

                                song.setBookTitle( songBook.getTitle() );
                                song.setBookAbbreviation( songBook.getAbbreviation() );
                                songList.add(song);



                                JSONArray verseArray = songObj.getJSONArray("verseList");
                                List<Verse>verseList = new ArrayList<>();
                                for (int k = 0; k < verseArray.length(); k++) {
                                    JSONObject verseObj = verseArray.getJSONObject(k);
                                    Verse verse = gson.fromJson(verseObj.toString(),Verse.class);
                                    verse.setVerseId( verseObj.getInt("position")+songObj.getString("id") );
                                    verse.setSongId( songObj.getString("id") );
                                    verse.setReference(song.getPosition()+" "+ songBook.getAbbreviation()+" "+verseObj.getInt("position") );
                                    verseList.add( verse );
                                }

                                bibleDaoGeneral4Insert.insertSongBook(songBook);
                                bibleDaoGeneral4Insert.insertSong(new ArrayList<>(songList));
                                bibleDaoGeneral4Insert.insertSongVerses(verseList);


                            }
                        }





                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(new FlowableSubscriber<Object>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Subscription s) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {


                        myViewHolder.prepopulateProgressBar.setVisibility(View.GONE);
                        bibleInfo.setDownloaded(true);
                        if (bibleInfo instanceof BibleInfo)
                        viewModel.insert((BibleInfo) bibleInfo);
                        else if(bibleInfo instanceof SongInfo)
                        songInfoViewModel.insert( (SongInfo) bibleInfo );
                        myViewHolder.info.setVisibility(View.GONE);
                        myViewHolder.checkBox.setVisibility(View.VISIBLE);
                    }
                });


    }

    private void downloadDataFromFireBaseStorage(String bibleName, BaseInfo bibleInfo, MyAdapter.MyViewHolder myViewHolder) {

        String path = origin.equalsIgnoreCase("BIBLE")?"bible/" + bibleName + "-v1" + ".json":"song/" + bibleName + "-v1" + ".json";
        FirebaseStorage fs = FirebaseStorage.getInstance();
        StorageReference storageRef = fs.getReference().child(path);

        /*progressBar*/
        myViewHolder.downloadProgressBar.setVisibility(View.VISIBLE);

        File localFile = new File(getCacheDir(), bibleName + "-v1" + ".json");


        FileDownloadTask downloadTask = storageRef.getFile(localFile);


        downloadTask.addOnProgressListener(snapshot -> {
            double percent = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
            myViewHolder.downloadProgressBar.setProgress((int) percent);
            System.out.println("percent: " + percent);
            if (percent == 100) {
                myViewHolder.downloadProgressBar.setVisibility(View.GONE);

                readFromCache(bibleName, myViewHolder, bibleInfo);

                /*prepopulateProgressBar*/
                myViewHolder.prepopulateProgressBar.setVisibility(View.VISIBLE);
            }
        });

    }

    private void readFromCache(String bibleName, MyAdapter.MyViewHolder myViewHolder, BaseInfo bibleInfo) {
        File localFile = new File(getCacheDir(), bibleName + "-v1" + ".json");
        StringBuilder stringBuilder = new StringBuilder();

        try {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(localFile)));


            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            if (bibleInfo instanceof BibleInfo)
                prepopulateBibleFromJSonFile(stringBuilder.toString(), myViewHolder, bibleInfo);
            if (bibleInfo instanceof SongInfo)
                prepopulateSongFromJSonFile(stringBuilder.toString(), myViewHolder, bibleInfo);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

