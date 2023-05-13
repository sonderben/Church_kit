package com.churchkit.churchkit.ui.note;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.churchkit.churchkit.R;
import com.churchkit.churchkit.Util;
import com.churchkit.churchkit.adapter.note.NoteDirectoryAdapter;
import com.churchkit.churchkit.database.MyDataDb;
import com.churchkit.churchkit.database.entity.note.BaseNoteEntity;
import com.churchkit.churchkit.database.entity.note.DirectoryWithNote;
import com.churchkit.churchkit.database.entity.note.NoteDirectoryEntity;
import com.churchkit.churchkit.modelview.note.NoteDirectoryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.List;


public class NoteFragment extends Fragment implements View.OnClickListener{

    public NoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         root = inflater.inflate(R.layout.fragment_note, container, false);

        init();
        onCreateMenu();


        return  root;
    }

    FloatingActionButton newNote,add,newDirectory;
    RecyclerView recyclerView;
    Animation rotate0to45 ;
    Animation rotate45To0 ;


    Animation fromBottom ;
    Animation toBottom ;
    View root;
    boolean isAddNoteOpened = false;
    NoteDirectoryViewModel noteDirectoryViewModel;
    NoteDirectoryAdapter adapter;
//https://youtu.be/x_0QKBsF56U?t=4404
    public  void init(){
         rotate0to45 = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_to_45);
         rotate45To0 = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_to_0);

         fromBottom = AnimationUtils.loadAnimation(getContext(),R.anim.from_bottom);
         toBottom = AnimationUtils.loadAnimation(getContext(),R.anim.from_bottom);
        newNote = root.findViewById(R.id.new_note);
        recyclerView = root.findViewById(R.id.recyclerview);
        add = root.findViewById(R.id.add);
        newDirectory = root.findViewById(R.id.new_file);
        //recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
         adapter = new NoteDirectoryAdapter(getActivity(), NoteFragment.class);
        recyclerView.setAdapter( adapter );
        noteDirectoryViewModel = new NoteDirectoryViewModel( MyDataDb.getInstance(getContext()).noteDirectoryDao() ) /*ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(NoteDirectoryViewModel.class)*/;



        newNote.setOnClickListener(this::onClick);
        newDirectory.setOnClickListener(this::onClick);
        add.setOnClickListener(this::onClick);
        noteDirectoryViewModel.getAllDirectoryWithNote().observe(getViewLifecycleOwner(), new Observer<List<DirectoryWithNote>>() {
            @Override
            public void onChanged(List<DirectoryWithNote> directoryWithNotes) {

                List<BaseNoteEntity> baseNoteEntityList = Util.convertToBaseNoteEntityList(directoryWithNotes);



                adapter.setBaseNoteEntityList( baseNoteEntityList );
            }
        });



    }

    public void setVisibility(){
        if (isAddNoteOpened){ // is visible


            newDirectory.setVisibility(View.GONE);
            newNote.setVisibility(View.GONE);
            //newDirectory.startAnimation(toBottom);
            //newNote.startAnimation(toBottom);
            add.startAnimation(rotate45To0);
            //

        }else { //not visible
            //newDirectory.startAnimation(fromBottom);
            //newNote.startAnimation(fromBottom);

            newDirectory.setVisibility(View.VISIBLE);
            newNote.setVisibility(View.VISIBLE);
            add.startAnimation(rotate0to45);

        }
        isAddNoteOpened = !isAddNoteOpened;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
                setVisibility();
                break;
            case R.id.new_note:
                /*Intent intent = new Intent(getActivity(), EditNoteActivity.class);
                getActivity().startActivity(intent);*/
                Bundle bundle = new Bundle();
                bundle.putSerializable("NOTE",null);


                NavController navController = Navigation.findNavController(v);
                navController.navigate( R.id.action_noteFragment_to_editerNoteFragment ,bundle);
                break;
            case R.id.new_file:
                newDirectoryDialog();
                break;
        }
    }


    private void newDirectoryDialog() {
        //Activity activity = (Activity)this.mLinkImageView.getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //builder.setTitle("Add Reference");
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.new_directory_layout, (ViewGroup)null);
        TextInputEditText title = view.findViewById(R.id.title);
        TextInputEditText password = view.findViewById(R.id.pwd);
        builder.setView(view).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (!title.getText().toString().isEmpty()){
                    NoteDirectoryEntity e = new NoteDirectoryEntity( title.getText().toString() );
                    e.setDate(Calendar.getInstance());
                    if (password.getText().toString().length()>3)
                        e.setPassword( password.getText().toString() );
                    noteDirectoryViewModel.insert(e);
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

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


}


