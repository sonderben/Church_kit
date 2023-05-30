package com.churchkit.churchkit.ui.note;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/*import com.chinalwb.are.AREditText;
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
import com.chinalwb.are.styles.toolitems.IARE_ToolItem;*/
import com.churchkit.churchkit.R;
//import com.churchkit.churchkit.are.CK_ToolItem_Reference;
import com.churchkit.churchkit.Util;
import com.churchkit.churchkit.database.MyDataDb;
import com.churchkit.churchkit.database.entity.note.NoteDirectoryEntity;
import com.churchkit.churchkit.database.entity.note.NoteEntity;

import com.churchkit.churchkit.modelview.note.NoteDirectoryViewModel;
import com.churchkit.churchkit.modelview.note.NoteViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;


public class EditerNoteFragment extends Fragment {




    public EditerNoteFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         view = inflater.inflate(R.layout.fragment_editer_note, container, false);

        init();
        if (getArguments()!=null){
            noteDirectory = (NoteDirectoryEntity) getArguments().getSerializable("DIRECTORY");

            note = (NoteEntity) getArguments().getSerializable("NOTE");
            if (note!=null)
                arEditText.setText( note.getNoteText() );
        }


        activity = requireActivity();
        SpannableString spannableString = new SpannableString("genial");
        SpannableString.valueOf("");




        onCreateMenu();
        return view;
    }
    public void init(){

        arEditText = view.findViewById(R.id.areditor);



        arEditText.setMovementMethod(LinkMovementMethod.getInstance());

        noteViewModel = new NoteViewModel(MyDataDb.getInstance(getActivity().getApplicationContext()).noteDao());
        directoryViewModel = new NoteDirectoryViewModel( MyDataDb.getInstance( getActivity().getApplicationContext() ).noteDirectoryDao() );


        /*ARE_ToolbarDefault toolbarDefault = view.findViewById(R.id.areToolbar);
        IARE_ToolItem bold = new ARE_ToolItem_Bold();
        //IARE_ToolItem italic = new ARE_ToolItem_Italic();
        IARE_ToolItem al = new ARE_ToolItem_AlignmentLeft();
        //IARE_ToolItem img = new ARE_ToolItem_Image();
        IARE_ToolItem ar = new ARE_ToolItem_AlignmentRight();
        IARE_ToolItem ac = new ARE_ToolItem_AlignmentCenter();
        IARE_ToolItem fontSize = new ARE_ToolItem_FontSize();
        IARE_ToolItem link = new ARE_ToolItem_Link();
       // IARE_ToolItem underline = new ARE_ToolItem_Underline();
        IARE_ToolItem at = new ARE_ToolItem_At();

        IARE_ToolItem fc = new ARE_ToolItem_FontColor();
        IARE_ToolItem bc = new ARE_ToolItem_BackgroundColor();

        IARE_ToolItem ref = new CK_ToolItem_Reference();



        toolbarDefault.addToolbarItem(bc);
        toolbarDefault.addToolbarItem(fc);
        //toolbarDefault.addToolbarItem(img);

        toolbarDefault.addToolbarItem(bold);

        //toolbarDefault.addToolbarItem(italic);
        toolbarDefault.addToolbarItem(al);
        toolbarDefault.addToolbarItem(ac);
        toolbarDefault.addToolbarItem(ar);
        toolbarDefault.addToolbarItem(fontSize);
        toolbarDefault.addToolbarItem(link);
        //toolbarDefault.addToolbarItem(underline);
        toolbarDefault.addToolbarItem(at);
        toolbarDefault.addToolbarItem(ref);



        arEditText.setToolbar( toolbarDefault );

        if (note!=null)
            arEditText.fromHtml( note.getNoteText() );*/
        //setHtml();

    }
    private void onCreateMenu() {
        getActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {


                menu.clear();
                menuInflater.inflate(R.menu.menu_editor_note, menu);

                editText= (EditText) menu.findItem(R.id.title).getActionView();
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                //editText.setLayoutParams(params);
                editText.setSingleLine();


                editText.setHint("Title");
                editText.setHintTextColor(ContextCompat.getColor(getContext(),R.color.gray));
                editText.setWidth((int) getResources().getDimension(com.intuit.sdp.R.dimen._120sdp));
                //editText.setMinWidth(600);
                editText.setTextColor(Color.WHITE);
                editText.setGravity(Gravity.START);
                //editText.setText("my title");
                if (note!=null)
                    editText.setText( note.getTitle());

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (note != null)
                            note.setTitle( s.toString() );
                    }
                });





            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == android.R.id.home) {
                    getActivity().onBackPressed();
                    return true;
                } else if (item.getItemId() == R.id.save) {

                    if (editText.getText().toString().isEmpty()){
                        setToolTop( ).showAlignBottom(editText);
                    }else {
                        arEditText.clearFocus();

                        InputMethodManager imm = (InputMethodManager) EditerNoteFragment.this.activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);



                        if (note == null) {
                            note = new NoteEntity(editText.getText().toString());


                            note.setNoteDirectoryEntityId( noteDirectory!=null? noteDirectory.getId() : 1 );


                            directoryViewModel.incrementAmountDefaultDirectory( noteDirectory!=null? noteDirectory.getId() : 1  );
                        }else {
                            //directoryViewModel.incrementAmountDefaultDirectory(  noteDirectory.getId() );
                        }
                        note.setNoteText( arEditText.getText().toString() );
                        note.setHashtag(Util.extractHashtags( arEditText.getText().toString() ));
                        noteViewModel.insert(note);
                        activity.onBackPressed();
                    }

                }

                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }


    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.getMenu().getItem(2).setChecked(true);
        int a = InputType.TYPE_CLASS_TEXT;
    }


    View view;
    Activity activity;
    EditText arEditText;

    NoteEntity note;
    NoteViewModel noteViewModel;
    NoteDirectoryViewModel directoryViewModel;

    EditText editText;
    NoteDirectoryEntity noteDirectory;
    public Balloon setToolTop(){

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
                .setText("Please enter a title first")
                .setTextColor(ContextCompat.getColor(context, R.color.white))
                .setTextIsHtml(true)
                //.setIconDrawable(ContextCompat.getDrawable(context, R.drawable.donate_24))
                .setBackgroundColor(ContextCompat.getColor(context, R.color.info))
                //.setOnBalloonClickListener(onBalloonClickListener)
                .setBalloonAnimation(BalloonAnimation.FADE)
                .setLifecycleOwner(getViewLifecycleOwner())
                .build();
        return balloon;

    }
}