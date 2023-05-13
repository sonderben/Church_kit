package com.churchkit.churchkit.ui.note;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.styles.toolbar.ARE_ToolbarDefault;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentCenter;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentLeft;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentRight;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_At;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_BackgroundColor;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Bold;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_FontColor;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_FontSize;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Image;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Italic;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Link;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Underline;
import com.chinalwb.are.styles.toolitems.IARE_ToolItem;
import com.churchkit.churchkit.R;
import com.churchkit.churchkit.are.CK_ToolItem_Reference;
import com.churchkit.churchkit.database.MyDataDb;
import com.churchkit.churchkit.database.entity.note.NoteDirectoryEntity;
import com.churchkit.churchkit.database.entity.note.NoteEntity;
import com.churchkit.churchkit.databinding.ActivityEditNoteBinding;
import com.churchkit.churchkit.modelview.note.NoteDirectoryViewModel;
import com.churchkit.churchkit.modelview.note.NoteViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class EditerNoteFragment extends Fragment {




    public EditerNoteFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         view = inflater.inflate(R.layout.fragment_editer_note, container, false);


        if (getArguments()!=null){
            noteDirectory = (NoteDirectoryEntity) getArguments().getSerializable("DIRECTORY");

            note = (NoteEntity) getArguments().getSerializable("NOTE");
        }


        activity = requireActivity();



        init();
        onCreateMenu();
        return view;
    }
    public void init(){
       // toolbar = binding.toolbar;
        arEditText = view.findViewById(R.id.areditor);

        arEditText.setMovementMethod(LinkMovementMethod.getInstance());

        noteViewModel = new NoteViewModel(MyDataDb.getInstance(getActivity().getApplicationContext()).noteDao());
        directoryViewModel = new NoteDirectoryViewModel( MyDataDb.getInstance( getActivity().getApplicationContext() ).noteDirectoryDao() );

        ARE_ToolbarDefault toolbarDefault = view.findViewById(R.id.areToolbar);
        IARE_ToolItem bold = new ARE_ToolItem_Bold();
        IARE_ToolItem italic = new ARE_ToolItem_Italic();
        IARE_ToolItem al = new ARE_ToolItem_AlignmentLeft();
        IARE_ToolItem img = new ARE_ToolItem_Image();
        IARE_ToolItem ar = new ARE_ToolItem_AlignmentRight();
        IARE_ToolItem ac = new ARE_ToolItem_AlignmentCenter();
        IARE_ToolItem fontSize = new ARE_ToolItem_FontSize();
        IARE_ToolItem link = new ARE_ToolItem_Link();
        IARE_ToolItem underline = new ARE_ToolItem_Underline();
        IARE_ToolItem at = new ARE_ToolItem_At();

        IARE_ToolItem fc = new ARE_ToolItem_FontColor();
        IARE_ToolItem bc = new ARE_ToolItem_BackgroundColor();

        IARE_ToolItem ref = new CK_ToolItem_Reference();



        toolbarDefault.addToolbarItem(bc);
        toolbarDefault.addToolbarItem(fc);
        toolbarDefault.addToolbarItem(img);

        toolbarDefault.addToolbarItem(bold);

        toolbarDefault.addToolbarItem(italic);
        toolbarDefault.addToolbarItem(al);
        toolbarDefault.addToolbarItem(ac);
        toolbarDefault.addToolbarItem(ar);
        toolbarDefault.addToolbarItem(fontSize);
        toolbarDefault.addToolbarItem(link);
        toolbarDefault.addToolbarItem(underline);
        toolbarDefault.addToolbarItem(at);
        toolbarDefault.addToolbarItem(ref);



        arEditText.setToolbar( toolbarDefault );

        if (note!=null)
            arEditText.fromHtml( note.getNoteText() );
        //setHtml();

    }
    private void onCreateMenu() {
        getActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {


                menu.clear();
                menuInflater.inflate(R.menu.menu_editor_note, menu);

                editText= (EditText) menu.findItem(R.id.title).getActionView();
                editText.setHint("Title");
                editText.setWidth(300);
                editText.setTextColor(Color.WHITE);
                editText.setGravity(Gravity.START);
                editText.setText("my title");
                if (note!=null)
                    editText.setText( note.getTitle());

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        if (note != null)
                            note.setTitle( s.toString() );
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });



            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == android.R.id.home) {
                    getActivity().onBackPressed();
                    return true;
                } else if (item.getItemId() == R.id.save) {
                    if (note == null) {
                        note = new NoteEntity(editText.getText().toString());


                        note.setNoteDirectoryEntityId( noteDirectory!=null? noteDirectory.getId() : 1 );


                        directoryViewModel.incrementAmountDefaultDirectory( noteDirectory!=null? noteDirectory.getId() : 1  );
                    }else {
                        directoryViewModel.incrementAmountDefaultDirectory(  noteDirectory.getId() );
                    }
                    note.setNoteText( arEditText.getHtml() );
                    noteViewModel.insert(note);
                    activity.onBackPressed();

                }

                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }
    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.getMenu().getItem(2).setChecked(true);
    }

    //MaterialToolbar toolbar;
    View view;
    Activity activity;
    AREditText arEditText;
    ActivityEditNoteBinding binding;
    NoteEntity note;
    NoteViewModel noteViewModel;
    NoteDirectoryViewModel directoryViewModel;

    EditText editText;
    NoteDirectoryEntity noteDirectory;
}