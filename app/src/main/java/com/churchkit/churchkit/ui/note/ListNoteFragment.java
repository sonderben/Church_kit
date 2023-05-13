package com.churchkit.churchkit.ui.note;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import com.churchkit.churchkit.EditNoteActivity;
import com.churchkit.churchkit.R;
import com.churchkit.churchkit.adapter.note.NoteDirectoryAdapter;
import com.churchkit.churchkit.database.MyDataDb;
import com.churchkit.churchkit.database.entity.note.BaseNoteEntity;
import com.churchkit.churchkit.database.entity.note.NoteDirectoryEntity;
import com.churchkit.churchkit.database.entity.note.NoteEntity;
import com.churchkit.churchkit.modelview.note.NoteViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class ListNoteFragment extends Fragment {



    public ListNoteFragment() {
        // Required empty public constructor
    }

/*    public static ListNoteFragment newInstance(String param1, String param2) {
        ListNoteFragment fragment = new ListNoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_note, container, false);

        NoteDirectoryEntity noteDirectory = (NoteDirectoryEntity) getArguments().getSerializable("DIRECTORY");

        onCreateMenu();

        recyclerView = view.findViewById(R.id.recycler_view);
        newNote = view.findViewById( R.id.new_note );
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        NoteDirectoryAdapter adapter = new NoteDirectoryAdapter(getActivity(), ListNoteFragment.class);
        adapter.setNoteDirectory( noteDirectory );
        recyclerView.setAdapter(adapter);

        noteViewModel = new NoteViewModel(MyDataDb.getInstance(getContext()).noteDao());
        noteViewModel.getAllNoteDirectory(noteDirectory.getId()).observe(getViewLifecycleOwner(), new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(List<NoteEntity> noteEntities) {
                List<BaseNoteEntity>baseNoteEntityList = new ArrayList<>();
                baseNoteEntityList.clear();
                for (int i = 0; i < noteEntities.size(); i++) {
                    baseNoteEntityList.add(noteEntities.get(i));
                }
                adapter.setBaseNoteEntityList(baseNoteEntityList);
            }
        });

        newNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("DIRECTORY",noteDirectory);
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_listNoteFragment_to_editerNoteFragment,bundle);
            }
        });

        return view;
    }
    RecyclerView recyclerView;
    NoteViewModel noteViewModel;
    FloatingActionButton newNote;
    private void onCreateMenu() {
        getActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {


                menu.clear();
                //menuInflater.inflate(R.menu.menu_editor_note, menu);


            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {


                return false;
            }
        }, getActivity(), Lifecycle.State.RESUMED);
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.getMenu().getItem(2).setChecked(true);
    }
}










