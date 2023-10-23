package com.churchkit.churchkit.ui.data;

import static com.churchkit.churchkit.Util.deleteCache;

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
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.churchkit.churchkit.CKPreferences;
import com.churchkit.churchkit.DownloadDataWork;
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






                myAdapter.notifyItemChanged(a);





                Snackbar snackbar = Snackbar
                        .make(view, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


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
            }
        });
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
    private AdapterData myAdapter;

    InternetBroadcastReceiver internetBroadcastReceiver;
    IntentFilter intentFilter;


    private BibleBookViewModel bibleBookViewModel;
    private String origin;
    private CKPreferences ckPreferences;

    private TextView info;
    private View view;


    private void init() {
        recyclerView = view.findViewById(R.id.recyclerview);
        info = view.findViewById(R.id.info);
        ckPreferences = new CKPreferences(getActivity().getApplicationContext());
        bibleBookViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(BibleBookViewModel.class);


        myAdapter = new AdapterData( getActivity(),ckPreferences ,origin);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }


    @Override
    public void onStop() {
        deleteCache(getContext());
        super.onStop();
    }
}