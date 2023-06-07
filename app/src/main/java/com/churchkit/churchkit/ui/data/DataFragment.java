package com.churchkit.churchkit.ui.data;

import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.churchkit.churchkit.CKPreferences;
import com.churchkit.churchkit.R;
import com.churchkit.churchkit.Util;
import com.churchkit.churchkit.database.CKBibleDb;
import com.churchkit.churchkit.database.CKSongDb;
import com.churchkit.churchkit.database.dao.bible.BibleDaoGeneral4Insert;
import com.churchkit.churchkit.database.dao.song.SongDaoGeneral4Insert;
import com.churchkit.churchkit.database.entity.base.BaseInfo;
import com.churchkit.churchkit.database.entity.bible.BibleBook;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.bible.BibleInfo;
import com.churchkit.churchkit.database.entity.bible.BibleVerse;
import com.churchkit.churchkit.database.entity.note.NoteDirectoryEntity;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongBook;
import com.churchkit.churchkit.database.entity.song.SongInfo;
import com.churchkit.churchkit.database.entity.song.Verse;
import com.churchkit.churchkit.modelview.bible.BibleBookViewModel;
import com.churchkit.churchkit.modelview.bible.BibleInfoViewModel;
import com.churchkit.churchkit.modelview.song.SongInfoViewModel;
import com.churchkit.churchkit.ui.util.SwipeToDeleteCallback;
import com.churchkit.churchkit.util.InternetBroadcastReceiver;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
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
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.functions.Action;
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

        TextView isConnectedTextView = view.findViewById(R.id.internet);





        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int a = viewHolder.getAbsoluteAdapterPosition();
                BaseInfo baseInfo = myAdapter.getBaseInfoList().get(a);

                baseInfo.setDownloaded(false);



                myAdapter.notifyItemChanged(a);


                bibleBookViewModel.deleteAll(baseInfo.getId());
                bibleInfoViewModel.delete((BibleInfo) baseInfo);


                Snackbar snackbar = Snackbar
                        .make(view, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        baseInfo.setDownloaded(true);
                        myAdapter.notifyItemChanged(a);
                        recyclerView.scrollToPosition(a);
                    }
                });



                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


        internetBroadcastReceiver = new InternetBroadcastReceiver();
         intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        internetBroadcastReceiver.setOnInternetStatusChange(new InternetBroadcastReceiver.OnInternetStatusChange() {


            @Override
            public void change(boolean isConnected) {
                    isConnectedTextView.setVisibility( !isConnected? View.VISIBLE:View.GONE );

                myAdapter.setIsConnected(isConnected);

            }
        });





        if (origin.equalsIgnoreCase("BIBLE")) {
            bibleInfoViewModel.getAllBibleInfo().observe(getViewLifecycleOwner(), bibleInfoList -> {
                myAdapter.setBaseInfoList(new ArrayList<>(bibleInfoList));
            });
        } else {

            songInfoViewModel.getAllSongInfo().observe(getViewLifecycleOwner(), songInfoList -> {
                //if (myAdapter.isBaseInfosEmpty())
                    myAdapter.setBaseInfoList(new ArrayList<>(songInfoList));
            });
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(internetBroadcastReceiver, intentFilter);
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.getMenu().getItem(3).setChecked(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver( internetBroadcastReceiver );
    }

    private RecyclerView recyclerView;

    private boolean isConnected = false;
    private DataFragment.MyAdapter myAdapter;
    private BibleInfoViewModel bibleInfoViewModel;
    InternetBroadcastReceiver internetBroadcastReceiver;
    IntentFilter intentFilter;

    private SongInfoViewModel songInfoViewModel;
    private BibleBookViewModel bibleBookViewModel;
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
        bibleInfoViewModel = new BibleInfoViewModel(getActivity().getApplication());
        bibleBookViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(BibleBookViewModel.class);
        songInfoViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(SongInfoViewModel.class);
    }

    public final class MyAdapter extends RecyclerView.Adapter<DataFragment.MyAdapter.MyViewHolder> {

        private boolean isConnected = false;
        public void setIsConnected(boolean isConnected){
            this.isConnected = isConnected;
        }
        List<BaseInfo> baseInfoList = new ArrayList<>();

        public List<BaseInfo> getBaseInfoList() {
            return baseInfoList;
        }

        public void setBaseInfoList(List<BaseInfo> baseInfoList) {
            this.baseInfoList = baseInfoList;
            notifyDataSetChanged();

            if (baseInfoList.size()>0){

                if (baseInfoList.get(0) instanceof BibleInfo && ckPreferences.getBibleName().equalsIgnoreCase("")){
                    for (int i = 0; i < baseInfoList.size(); i++) {
                        if (baseInfoList.get(i).isDownloaded()) {
                            ckPreferences.setBibleName(baseInfoList.get(i).getId());
                            break;
                        }
                    }
                }
                else if (baseInfoList.get(0) instanceof SongInfo && ckPreferences.getSongName().equalsIgnoreCase("")){
                    for (int i = 0; i < baseInfoList.size(); i++) {
                        if (baseInfoList.get(i).isDownloaded()) {
                            ckPreferences.setSongName(baseInfoList.get(i).getId());
                            break;
                        }
                    }
                }

            }

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

            holder.itemView.setOnClickListener(v -> setToolTop("Click to download").showAlignLeft(holder.download));


            if (bibleInfo instanceof SongInfo){
                SongInfo songInfo = (SongInfo) bibleInfo;
                holder.testament.setText( songInfo.isSinglePart()? (songInfo.getAmountSong()+" "+getString(R.string.song)): songInfo.getTestament()+" "+getString(R.string.part) );
            }else {
                holder.testament.setText( getStringTestament(bibleInfo.getTestament() ) );
            }


            holder.deleteImg.setVisibility(!bibleInfo.isDownloaded() ? View.GONE : View.VISIBLE);

            holder.deleteImg.setOnClickListener(v -> {
                deleteBookDialog( bibleInfo);


            });



            holder.download.setOnClickListener(download -> {

                if (isConnected){
                    download.setVisibility(View.GONE);
                    if (bibleInfo instanceof BibleInfo)
                        downloadDataFromFireBaseStorage( (BibleInfo) bibleInfo, holder);
                    if (bibleInfo instanceof SongInfo)
                        downloadDataFromFireBaseStorage( (SongInfo) bibleInfo, holder);
                    holder.info.setVisibility(View.VISIBLE);
                }
                else {
                    setToolTop("Please connect to the internet").showAlignBottom(holder.itemView);
                }
            });



        }

        @Override
        public int getItemCount() {
            return baseInfoList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView title, language, info, testament,size,infoPrepopulate;
            ImageButton download;
            ImageView deleteImg;
            ProgressBar downloadProgressBar, prepopulateProgressBar;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.title);
                size = itemView.findViewById(R.id.size);
                testament = itemView.findViewById(R.id.testament);
                language = itemView.findViewById(R.id.language);
                info = itemView.findViewById(R.id.info);
                download = itemView.findViewById(R.id.download);
                deleteImg = itemView.findViewById(R.id.checkbox);
                downloadProgressBar = itemView.findViewById(R.id.progressBar3);
                prepopulateProgressBar = itemView.findViewById(R.id.progressBar2);
                infoPrepopulate = itemView.findViewById(R.id.info_prepopulate);

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
        myViewHolder.infoPrepopulate.setVisibility(View.VISIBLE);
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
                            System.out.println("test12: "+bibleInfo.getId());
                            String bibleBookId = bibleInfo.getId()+"_"+songBookJson.getString("id");
                            BibleBook bibleBook = new BibleBook(bibleInfo.getId(),
                                    bibleBookId ,
                                    songBookJson.getString("abbr"),
                                    songBookJson.getString("name"),
                                    songBookJson.getInt("position"),
                                    testament,
                                    songBookJson.getInt("amountChapter"),
                                    color,
                                    image);
                            System.out.println(bibleBook);
                            try {
                                bibleDaoGeneral4Insert.insertBibleBook(bibleBook);
                            }catch (Throwable t){
                                System.err.println(t.getMessage());
                            }

                            JSONArray bibleChapterList = songBookJson.getJSONArray("bibleChapterList");


                            for (int jj = 0; jj < bibleChapterList.length(); jj++) {
                                JSONObject chapterJson = bibleChapterList.getJSONObject(jj);

                                String bibleChapterId = bibleBookId + chapterJson.getInt("position");

                                BibleChapter bibleChapter = new BibleChapter(bibleInfo.getId(),
                                        bibleChapterId+"",
                                        songBookJson.getString("name"),
                                        chapterJson.getInt("position"),
                                        bibleBookId+"" ,
                                        songBookJson.getString("abbr")
                                );

                                JSONArray bibleVerseArray = chapterJson.getJSONArray("bibleVerses");


                                try {
                                    bibleDaoGeneral4Insert.insertBibleChapter(bibleChapter);
                                }catch (Throwable t){
                                    System.err.println(t.getMessage());
                                }
                                List<BibleVerse>bibleVerses = new ArrayList<>();
                                for (int i = 0; i < bibleVerseArray.length(); i++) {
                                    JSONObject verseJson = bibleVerseArray.getJSONObject(i);

                                    String reference = songBookJson.getString("abbr") + " " + chapterJson.getInt("position") + ":" + verseJson.getInt("position");

                                    String bibleVerseId = bibleInfo.getId()+" "+reference;


                                    BibleVerse bibleVerse =     new BibleVerse(
                                                    bibleInfo.getId()
                                                    ,bibleVerseId
                                                    , verseJson.getInt("position")
                                                    , reference
                                                    , verseJson.getString("verseText")
                                                    , bibleChapterId);
                                    bibleVerses.add(bibleVerse);




                                }
                                try {
                                    bibleDaoGeneral4Insert.insertBibleVerses(bibleVerses);
                                }catch (Throwable t){
                                    System.err.println(t.getMessage());
                                }


                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        myViewHolder.infoPrepopulate.setText(bibleBook.getTitle()+" / "+ Util.formatNumberToString( bibleChapter.getPosition() ));
                                    }
                                });

                            }



                        }





                       getActivity().runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               myViewHolder.infoPrepopulate.setVisibility(View.GONE);
                           }
                       });


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
                        bibleInfoViewModel.insert((BibleInfo) bibleInfo);
                        myViewHolder.info.setVisibility(View.GONE);
                        myViewHolder.deleteImg.setVisibility(View.VISIBLE);
                    }
                });


    }

    public void prepopulateSongFromJSonFile(String jsonStr, MyAdapter.MyViewHolder myViewHolder, BaseInfo bibleInfo) {

        SongDaoGeneral4Insert bibleDaoGeneral4Insert = CKSongDb.getInstance4Insert(getActivity().getApplicationContext(), bibleInfo.getId()).songDaoGeneral4Insert();
        myViewHolder.infoPrepopulate.setVisibility(View.VISIBLE);
        Flowable.fromAction(() -> {
                    try {
                        Gson gson = new Gson();

                        JSONObject jsonObject = new JSONObject(jsonStr);
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        System.out.println("afiche: ");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject songBookJson = dataArray.getJSONObject(i);

                            SongBook songBook = gson.fromJson(songBookJson.toString(),SongBook.class);
                            songBook.setSongInfoId( bibleInfo.getId() );
                            songBook.setId( bibleInfo.getId()+"_"+songBookJson.getString("id") );
                            songBook.setChildAmount( songBookJson.getInt("songAmount") );
                            songBook.setTitle( songBookJson.getString("name") );


                            try {
                                bibleDaoGeneral4Insert.insertSongBook(songBook);
                            } catch (Throwable t) {
                                // Handle the Throwable
                                System.err.println("Caught Throwable: " + t.getMessage());
                            }



                            JSONArray songArrayJson = songBookJson.getJSONArray("songList");



                            for (int j = 0; j < songArrayJson.length(); j++) {


                                JSONObject songObj = songArrayJson.getJSONObject(j);
                                Song song = gson.fromJson(songObj.toString(),Song.class);
                                String songId = songObj.getString("id")+UUID.randomUUID();
                                song.setId( songId );
                                song.setBookId( bibleInfo.getId()+"_"+songBookJson.getString("id")  );
                                song.setInfoId( bibleInfo.getId() );



                                song.setBookTitle( songBook.getTitle() );
                                song.setBookAbbreviation( songBook.getAbbreviation() );


                                try {
                                    bibleDaoGeneral4Insert.insertSong( song );
                                } catch (Throwable t) {
                                    // Handle the Throwable
                                    System.err.println("Caught Throwable: " + t.getMessage());
                                }

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        myViewHolder.infoPrepopulate.setText(songBook.getTitle()+"/"+song.getTitle());
                                    }
                                });


                                JSONArray verseArray = songObj.getJSONArray("verseList");
                                List<Verse>verseList = new ArrayList<>();

                                for (int k = 0; k < verseArray.length(); k++) {
                                    JSONObject verseObj = verseArray.getJSONObject(k);
                                    Verse verse = gson.fromJson(verseObj.toString(),Verse.class);
                                    verse.setVerseId( songId + verseObj.getInt("position") );
                                    verse.setSongId( songId );
                                    verse.setInfoId( bibleInfo.getId() );
                                    verse.setReference( floatToString( song.getPosition() ) +" "+ songBook.getAbbreviation()+" "+verseObj.getInt("position") );
                                    verse.setSongTitle( song.getTitle() );
                                    verseList.add( verse );

                                }
                                System.out.println("afiche: "+song.getTitle()+" pos: "+song.getPosition());

                                //bibleDaoGeneral4Insert.insertSongBook(songBook);
                                //bibleDaoGeneral4Insert.insertSong(new ArrayList<>(songList));
                                try {
                                    bibleDaoGeneral4Insert.insertSongVerses(verseList);
                                } catch (Throwable t) {
                                    // Handle the Throwable
                                    System.err.println("Caught Throwable: " + t.getMessage());
                                }


                               // myViewHolder.prepopulateProgressBar.setProgress(i);



                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myViewHolder.infoPrepopulate.setVisibility(View.GONE);
                                }
                            });

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
                            bibleInfoViewModel.insert((BibleInfo) bibleInfo);
                        else if(bibleInfo instanceof SongInfo)
                            songInfoViewModel.insert( (SongInfo) bibleInfo );
                        myViewHolder.info.setVisibility(View.GONE);
                        myViewHolder.deleteImg.setVisibility(View.VISIBLE);
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
        }).addOnFailureListener(e -> {
            myViewHolder.downloadProgressBar.setVisibility(View.GONE);
            downloadTask.cancel();

            // hay que presentar un dialogo de porque ocurio el error
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                myViewHolder.downloadProgressBar.setVisibility(View.VISIBLE);
                myViewHolder.prepopulateProgressBar.setVisibility(View.GONE);
                myViewHolder.info.setVisibility(View.GONE);
                myViewHolder.deleteImg.setVisibility(View.GONE);
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

    public Balloon setToolTop(String message){

        Context context = getContext();

        Balloon balloon = new Balloon.Builder(context)
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setArrowPosition(0.5f)
                .setWidth(BalloonSizeSpec.WRAP)
                .setHeight(BalloonSizeSpec.WRAP)
                .setPadding(10)
                .setTextSize(15f)
                .setCornerRadius(4f)
                .setAlpha(0.9f)
                .setText(message)
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

    private void deleteBookDialog(BaseInfo bibleInfo) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.dialog_delete_book, (ViewGroup)null);
        TextView title = view.findViewById(R.id.description);


        builder.setView(view);





        builder.setPositiveButton("Delete", (dialog, which) -> {
            bibleInfo.setDownloaded(false);
            if (origin.equalsIgnoreCase("BIBLE")){

                Flowable.fromAction(() -> CKBibleDb.getInstance(getActivity().getApplicationContext()).runInTransaction(new Runnable() {
                            @Override
                            public void run() {
                                ckPreferences.setBibleName("");
                                bibleInfoViewModel.delete( (BibleInfo) bibleInfo );
                                CKBibleDb.getInstance(getActivity().getApplicationContext())
                                        .bibleBookDao().deleteAll( bibleInfo.getId() );
                            }
                        })).subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();

            }else {

                Flowable.fromAction(() -> CKBibleDb.getInstance(getActivity().getApplicationContext()).runInTransaction(new Runnable() {
                            @Override
                            public void run() {
                                ckPreferences.setSongName("");
                                songInfoViewModel.delete( (SongInfo) bibleInfo );
                                CKSongDb.getInstance(getActivity().getApplicationContext())
                                        .songBookDao().deleteAll(bibleInfo.getId());

                            }
                        })).subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
            }
        });



        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialog1 -> {
            Button delete = ((AlertDialog) dialog1).getButton(AlertDialog.BUTTON_POSITIVE);
            delete.setTextColor(Color.RED);

        });
        dialog.show();
    }

    private String floatToString(float position) {
        try {
            return String.valueOf( (int) position );
        }catch (Exception e){
            return String.valueOf( position );
        }
    }
}