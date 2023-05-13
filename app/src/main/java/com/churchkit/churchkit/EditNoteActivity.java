package com.churchkit.churchkit;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;

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
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Italic;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Link;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Underline;
import com.chinalwb.are.styles.toolitems.IARE_ToolItem;
import com.churchkit.churchkit.are.CK_ToolItem_Reference;
import com.churchkit.churchkit.database.MyDataDb;
import com.churchkit.churchkit.database.entity.note.NoteDirectoryEntity;
import com.churchkit.churchkit.database.entity.note.NoteEntity;
import com.churchkit.churchkit.databinding.ActivityEditNoteBinding;
import com.churchkit.churchkit.modelview.note.NoteDirectoryViewModel;
import com.churchkit.churchkit.modelview.note.NoteViewModel;
import com.google.android.material.appbar.MaterialToolbar;

public class EditNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = ActivityEditNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().getExtras() != null) {
            noteDirectory = (NoteDirectoryEntity) getIntent().getExtras().getSerializable("NOTE_DIRECTORY");
            note = (NoteEntity) getIntent().getExtras().getSerializable("NOTE");

        }


        init();
        onCreateMenu();







        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);







    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.save) {
            if (note == null) {
                note = new NoteEntity(editText.getText().toString());


                note.setNoteDirectoryEntityId(noteDirectory.getId());
                noteDirectory.setNoteAmount( noteDirectory.getNoteAmount() +1 );
                directoryViewModel.insert(noteDirectory);
            }
            note.setNoteText( arEditText.getHtml() );
            noteViewModel.insert(note);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    private void onCreateMenu() {
        addMenuProvider(new MenuProvider() {
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
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {


                return false;
            }
        }, this, Lifecycle.State.RESUMED);
    }

    public void init(){
        toolbar = binding.toolbar;
         arEditText = findViewById(R.id.areditor);

        arEditText.setMovementMethod(LinkMovementMethod.getInstance());

        noteViewModel = new NoteViewModel(MyDataDb.getInstance(getApplicationContext()).noteDao());
        directoryViewModel = new NoteDirectoryViewModel( MyDataDb.getInstance( getApplicationContext() ).noteDirectoryDao() );

        ARE_ToolbarDefault toolbarDefault = findViewById(R.id.areToolbar);
        IARE_ToolItem bold = new ARE_ToolItem_Bold();
        IARE_ToolItem italic = new ARE_ToolItem_Italic();
        IARE_ToolItem al = new ARE_ToolItem_AlignmentLeft();
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

    MaterialToolbar toolbar;
    AREditText arEditText;
    ActivityEditNoteBinding binding;
    NoteEntity note;
    NoteViewModel noteViewModel;
    NoteDirectoryViewModel directoryViewModel;

    EditText editText;
    NoteDirectoryEntity noteDirectory;

    @Override
    protected void onDestroy() {
        binding = null;
        super.onDestroy();
    }

    private void setHtml() {
        String html = "<p style=\"text-align: center;\"><strong>New Feature in 0.1.2</strong></p>\n" +
                "<p style=\"text-align: center;\">&nbsp;</p>\n" +
                "<p style=\"text-align: left;\"><span style=\"color: #3366ff;\">In this release, you have a new usage with ARE.</span></p>\n" +
                "<p style=\"text-align: left;\">&nbsp;</p>\n" +
                "<p style=\"text-align: left;\"><span style=\"color: #3366ff;\">AREditText + ARE_Toolbar, you are now able to control the position of the input area and where to put the toolbar at and, what ToolItems you'd like to have in the toolbar. </span></p>\n" +
                "<p style=\"text-align: left;\">&nbsp;</p>\n" +
                "<p style=\"text-align: left;\"><span style=\"color: #3366ff;\">You can not only define the Toolbar (and it's style), you can also add your own ARE_ToolItem with your style into ARE.</span></p>\n" +
                "<p style=\"text-align: left;\">&nbsp;</p>\n" +
                "<p style=\"text-align: left;\"><span style=\"color: #ff00ff;\"><em><strong>Why not give it a try now?</strong></em></span></p>";
        arEditText.fromHtml(html);
    }

}

