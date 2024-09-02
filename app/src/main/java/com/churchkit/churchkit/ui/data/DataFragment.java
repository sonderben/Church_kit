package com.churchkit.churchkit.ui.data;



import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.churchkit.churchkit.CKPreferences;
import com.churchkit.churchkit.R;
import com.churchkit.churchkit.modelview.bible.BibleBookViewModel;
//import com.churchkit.churchkit.ui.util.SwipeToDeleteCallback;
import com.churchkit.churchkit.util.InternetBroadcastReceiver;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;


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



        /*SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
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
        itemTouchHelper.attachToRecyclerView(recyclerView);*/


        internetBroadcastReceiver = new InternetBroadcastReceiver();
         intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        internetBroadcastReceiver.setOnInternetStatusChange(isConnected -> {
            isConnectedTextView.setVisibility( !isConnected? View.VISIBLE:View.GONE );
            myAdapter.setIsConnected( isConnected );
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
        super.onStop();
    }
}