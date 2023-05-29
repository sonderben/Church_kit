package com.churchkit.churchkit.ui.data;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.churchkit.churchkit.CKPreferences;
import com.churchkit.churchkit.R;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;

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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class DataFragment extends Fragment {



    public DataFragment() {
        // Required empty public constructor
    }


    public static DataFragment newInstance() {
        DataFragment fragment = new DataFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_data, container, false);
        origin = getArguments().getString("FROM");

        init();




        if (origin.equalsIgnoreCase("BIBLE")) {
            info.setVisibility(ckPreferences.isCurrentAndNextBibleEqual() ? View.GONE : View.VISIBLE);
            viewModel.getAllBibleInfo().observe(getViewLifecycleOwner(), bibleInfoList -> {
                if (myAdapter.isBaseInfosEmpty())
                    myAdapter.setBaseInfoList(new ArrayList<>(bibleInfoList));
            });
        } else {
            info.setVisibility(ckPreferences.isCurrentAndNextSongEqual() ? View.GONE : View.VISIBLE);
            songInfoViewModel.getAllSongInfo().observe(getViewLifecycleOwner(), songInfoList -> {
                if (myAdapter.isBaseInfosEmpty())
                    myAdapter.setBaseInfoList(new ArrayList<>(songInfoList));
            });
        }
        return view;
    }
    private RecyclerView recyclerView;
    private DataFragment.MyAdapter myAdapter;
    private BibleInfoViewModel viewModel;
    private SongInfoViewModel songInfoViewModel;
    private String origin;
    private CKPreferences ckPreferences;
    //private String defaultBibleId;
    private TextView info;
    private View view;

    private void init() {
        recyclerView = view.findViewById(R.id.recyclerview);
        info = view.findViewById(R.id.info);

        myAdapter = new MyAdapter();
        ckPreferences = new CKPreferences(getActivity().getApplicationContext());
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        viewModel = new BibleInfoViewModel(getActivity().getApplication());
        songInfoViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(SongInfoViewModel.class);
    }

    private final class MyAdapter extends RecyclerView.Adapter<DataFragment.MyAdapter.MyViewHolder> {
        List<BaseInfo> baseInfoList = new ArrayList<>();


        public void setBaseInfoList(List<BaseInfo> baseInfoList) {
            this.baseInfoList = baseInfoList;
            notifyDataSetChanged();
        }


        public boolean isBaseInfosEmpty() {
            return baseInfoList.isEmpty();
        }


        private int oldPositionSelected = -1;

        @NonNull
        @Override
        public DataFragment.MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bible_info_item, parent, false);
            return new DataFragment.MyAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DataFragment.MyAdapter.MyViewHolder holder, int position) {
            BaseInfo bibleInfo = baseInfoList.get(holder.getAbsoluteAdapterPosition());
            holder.language.setText(bibleInfo.getLanguage());
            holder.title.setText(bibleInfo.getName());
            holder.size.setText( bibleInfo.getSize() );

            holder.download.setVisibility(bibleInfo.isDownloaded() ? View.GONE : View.VISIBLE);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setToolTop().showAlignLeft(holder.download);
                }
            });


            if (bibleInfo instanceof SongInfo){
                SongInfo songInfo = (SongInfo) bibleInfo;
                holder.testament.setText( songInfo.isSinglePart()? (songInfo.getAmountSong()+" "+getString(R.string.song)): songInfo.getTestament()+" "+getString(R.string.part) );
                holder.checkBox.setChecked( bibleInfo.getId().equalsIgnoreCase(ckPreferences.getNextSongName()) );

            }else {
                holder.testament.setText( getStringTestament(bibleInfo.getTestament() ) );
                holder.checkBox.setChecked(bibleInfo.getId().equalsIgnoreCase(ckPreferences.getNextBibleName()));

            }




            holder.checkBox.setVisibility(!bibleInfo.isDownloaded() ? View.GONE : View.VISIBLE);

            if (bibleInfo.getId().equalsIgnoreCase(ckPreferences.getNextBibleName())) {
                oldPositionSelected = holder.getAbsoluteAdapterPosition();
            }
            if (bibleInfo.getId().equalsIgnoreCase(ckPreferences.getNextSongName() )) {
                oldPositionSelected = holder.getAbsoluteAdapterPosition();
            }

           /* if (bibleInfo instanceof BibleInfo)
                holder.checkBox.setChecked(bibleInfo.getId().equalsIgnoreCase(ckPreferences.getNextBibleName()));
            else  if (bibleInfo instanceof SongInfo)
                holder.checkBox.setChecked( bibleInfo.getId().equalsIgnoreCase(ckPreferences.getNextSongName()) );*/

            if (bibleInfo.getId().equalsIgnoreCase(ckPreferences.getNextBibleName())) {
                oldPositionSelected = holder.getAbsoluteAdapterPosition();
            }

            holder.download.setOnClickListener(v -> {

                v.setVisibility(View.GONE);
                if (bibleInfo instanceof BibleInfo)
                    downloadDataFromFireBaseStorage( (BibleInfo) bibleInfo, holder);
                if (bibleInfo instanceof SongInfo)
                    downloadDataFromFireBaseStorage( (SongInfo) bibleInfo, holder);
                holder.info.setVisibility(View.VISIBLE);
            });

            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                BaseInfo bibleInfo1 = baseInfoList.get( holder.getAbsoluteAdapterPosition() );

                if (isChecked) {
                    if (bibleInfo instanceof BibleInfo)
                        ckPreferences.setNextBibleName( bibleInfo1.getId()) ;
                    else if (bibleInfo instanceof SongInfo)
                        ckPreferences.setNextSongName( bibleInfo.getId() );

                    notifyItemChanged(oldPositionSelected);
                    oldPositionSelected = holder.getAbsoluteAdapterPosition();
                }

                if (bibleInfo instanceof BibleInfo)
                    info.setVisibility( ckPreferences.isCurrentAndNextBibleEqual() ? View.GONE : View.VISIBLE );
                else if (bibleInfo instanceof SongInfo)
                    info.setVisibility( ckPreferences.isCurrentAndNextSongEqual() ? View.GONE : View.VISIBLE );


            });

        }

        @Override
        public int getItemCount() {
            return baseInfoList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView title, language, info, testament,size;
            ImageButton download;
            RadioButton checkBox;
            ProgressBar downloadProgressBar, prepopulateProgressBar;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.title);
                size = itemView.findViewById(R.id.size);
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

    public void prepopulateBibleFromJSonFile(String jsonStr, MyAdapter.MyViewHolder myViewHolder, BaseInfo bibleInfo) {

        BibleDaoGeneral4Insert bibleDaoGeneral4Insert = CKBibleDb.getInstance4Insert(getActivity().getApplicationContext(), bibleInfo.getId()).bibleDaoGeneral4Insert();
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
                                color= songBookJson.getString("color");
                                image= songBookJson.getString("image");
                            }catch (JSONException e){
                                color = null;
                                image = null;
                            }

                            BibleBook bibleBook = new BibleBook(
                                    songBookJson.getString("id") ,
                                    songBookJson.getString("abbr"),
                                    songBookJson.getString("name"),
                                    songBookJson.getInt("position"),
                                    testament,
                                    songBookJson.getInt("amountChapter"),
                                    color,
                                    image);
                            System.out.println(bibleBook);

                            JSONArray bibleChapterList = songBookJson.getJSONArray("bibleChapterList");
                            List<BibleChapter> bibleChapters = new ArrayList<>(50);

                            for (int jj = 0; jj < bibleChapterList.length(); jj++) {
                                JSONObject chapterJson = bibleChapterList.getJSONObject(jj);


                                BibleChapter bibleChapter = new BibleChapter(
                                        chapterJson.getString("bookName") + chapterJson.getInt("position"),
                                        songBookJson.getString("name"),
                                        chapterJson.getInt("position"),
                                        songBookJson.getString("id"),
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

        SongDaoGeneral4Insert bibleDaoGeneral4Insert = CKSongDb.getInstance4Insert(getActivity().getApplicationContext(), bibleInfo.getId()).songDaoGeneral4Insert();
        Flowable.fromAction(() -> {
                    try {
                        Gson gson = new Gson();

                        JSONObject jsonObject = new JSONObject(jsonStr);
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            //System.out.println("genial: "+i);
                            JSONObject songBookJson = dataArray.getJSONObject(i);

                            SongBook songBook = gson.fromJson(songBookJson.toString(),SongBook.class);
                            songBook.setId( songBookJson.getString("id") );
                            songBook.setChildAmount( songBookJson.getInt("songAmount") );
                            songBook.setTitle( songBookJson.getString("name") );

                            //System.out.println("name: "+songBookJson.getString("name"));





                            JSONArray songArrayJson = songBookJson.getJSONArray("songList");

                            /*if (songArrayJson.length()==0){
                                continue;
                            }*/

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
                                System.out.println("afiche: "+song.getTitle()+" pos: "+song.getPosition());

                                bibleDaoGeneral4Insert.insertSongBook(songBook);
                                bibleDaoGeneral4Insert.insertSong(new ArrayList<>(songList));
                                bibleDaoGeneral4Insert.insertSongVerses(verseList);

                               // myViewHolder.prepopulateProgressBar.setProgress(i);



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

    private void downloadDataFromFireBaseStorage( BaseInfo bibleInfo, MyAdapter.MyViewHolder myViewHolder) {


        FirebaseStorage fs = FirebaseStorage.getInstance();
        StorageReference storageRef = fs.getReference().child(bibleInfo.getUrl());


        myViewHolder.downloadProgressBar.setVisibility(View.VISIBLE);

        File localFile = new File(getActivity().getCacheDir(), bibleInfo.getId() + "-v1" + ".json");


        FileDownloadTask downloadTask = storageRef.getFile(localFile);


        downloadTask.addOnProgressListener(snapshot -> {
            double percent = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
            myViewHolder.downloadProgressBar.setProgress((int) percent);
            System.out.println("percent: " + percent);
            if (percent == 100) {
                myViewHolder.downloadProgressBar.setVisibility(View.GONE);
                myViewHolder.prepopulateProgressBar.setVisibility(View.VISIBLE);

                readFromCache( myViewHolder, bibleInfo);



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myViewHolder.downloadProgressBar.setVisibility(View.GONE);
                // hay que presentar un dialogo de porque ocurio el error
            }
        });

    }

    private void readFromCache(MyAdapter.MyViewHolder myViewHolder, BaseInfo bibleInfo) {
        File localFile = new File(getActivity().getCacheDir(), bibleInfo.getId() + "-v1" + ".json");
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

    public Balloon setToolTop(){

        Context context = getContext();

        Balloon balloon = new Balloon.Builder(context)
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.END)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setArrowPosition(0.5f)
                .setWidth(BalloonSizeSpec.WRAP)
                .setHeight(BalloonSizeSpec.WRAP)
                .setPadding(10)
                .setTextSize(15f)
                .setCornerRadius(4f)
                .setAlpha(0.9f)
                .setText("Click to download")
                .setTextColor(ContextCompat.getColor(context, R.color.white))
                .setTextIsHtml(true)
                //.setIconDrawable(ContextCompat.getDrawable(context, R.drawable.donate_24))
                .setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
                //.setOnBalloonClickListener(onBalloonClickListener)
                .setBalloonAnimation(BalloonAnimation.FADE)
                .setLifecycleOwner(getViewLifecycleOwner())
                .build();
        return balloon;

    }


}