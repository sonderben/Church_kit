package com.churchkit.churchkit.adapter.note;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.churchkit.churchkit.R;
import com.churchkit.churchkit.database.entity.note.BaseNoteEntity;
import com.churchkit.churchkit.database.entity.note.NoteDirectoryEntity;
import com.churchkit.churchkit.database.entity.note.NoteEntity;
import com.churchkit.churchkit.ui.note.ListNoteFragment;
import com.churchkit.churchkit.ui.note.NoteFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NoteDirectoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<BaseNoteEntity> baseNoteEntityList=new ArrayList<>(1);
    NoteDirectoryEntity noteDirectory;



    public void setNoteDirectory(NoteDirectoryEntity noteDirectory) {
        this.noteDirectory = noteDirectory;
    }

    Activity activity;
    Class aClass;

    DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);

    /*public int getAmountDefaultRepository(){
        int amount =0;
        for (int i = 0; i < baseNoteEntityList.size(); i++) if (baseNoteEntityList.get(i) instanceof NoteEntity) amount +=1; return amount;
    }*/


    public  NoteDirectoryAdapter(Activity activity,Class aClass) {
        this.activity = activity;
        this.aClass = aClass;
    }

    private final int NOTE_DIRECTORY_VIEW_TYPE = 1;
    private final int NOTE_VIEW_TYPE = 2;

    public void setBaseNoteEntityList(List<BaseNoteEntity> noteDirectoryEntity) {
        this.baseNoteEntityList = noteDirectoryEntity;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType  == NOTE_DIRECTORY_VIEW_TYPE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_directory_item,parent,false);
            return new DirectoryViewHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,parent,false);
        return new NoteViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return baseNoteEntityList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (baseNoteEntityList.get(position) instanceof NoteDirectoryEntity){
            return NOTE_DIRECTORY_VIEW_TYPE;
        }
        return NOTE_VIEW_TYPE;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof DirectoryViewHolder){
            DirectoryViewHolder dvh = ((DirectoryViewHolder)holder);
            NoteDirectoryEntity directory = (NoteDirectoryEntity) baseNoteEntityList.get( holder.getAbsoluteAdapterPosition() );
            String title = directory.getTitle();
            boolean isLock = directory.isLock();

            dvh.title.setText( title );
            dvh.date.setText( dateFormat.format(directory.getDate().getTime()) );
            dvh.lock.setVisibility( isLock? View.VISIBLE:View.GONE );
            int noteAmount = directory.getNoteAmount();
            if (noteAmount>0){
                dvh.amountNote.setVisibility(View.VISIBLE);
                dvh.amountNote.setText(noteAmount+" "+"notes");
            }else {
                dvh.amountNote.setVisibility(View.GONE);
            }

            dvh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("DIRECTORY",directory);
                    NavController navController = Navigation.findNavController(v);
                   // navController.getGraph().findNode(R.id.listChapterByTestamentFragment).setLabel(holder.itemView.getContext().getString( R.string.old_testament ));
                    if (directory.isLock()){
                        newDirectoryDialog(navController,bundle,directory);
                    }else
                        navController.navigate(R.id.action_noteFragment_to_listNoteFragment,bundle);
                }
            });

        }else {
            NoteViewHolder nvh = ((NoteViewHolder)holder);
            NoteEntity note = (NoteEntity) baseNoteEntityList.get( holder.getAbsoluteAdapterPosition() );
            nvh.title.setText( note.getTitle() );
            nvh.description.setText( removeHtmlTags( note.getNoteText() ).trim() );
            nvh.date.setText( dateFormat.format(note.getDate().getTime()) );
            nvh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("NOTE",note);

                    NavController navController = Navigation.findNavController(v);

                    if (aClass == ListNoteFragment.class) {
                        bundle.putSerializable("DIRECTORY",NoteDirectoryAdapter.this.noteDirectory);
                        navController.navigate(R.id.action_listNoteFragment_to_editerNoteFragment, bundle);
                    }
                    else if ( aClass == NoteFragment.class ){
                        navController.navigate( R.id.action_noteFragment_to_editerNoteFragment ,bundle);
                    }

                }
            });

        }

    }

    public class DirectoryViewHolder extends RecyclerView.ViewHolder{
        TextView title,amountNote,date;
        ImageView lock;

        public DirectoryViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            lock = itemView.findViewById(R.id.lock);
            amountNote = itemView.findViewById(R.id.note_amount);
            date = itemView.findViewById(R.id.date);
        }
    }
    public class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView title, description,date;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title  = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date);

        }
    }

    public static String removeHtmlTags(String html) {
        if (html == null || html.trim().length() == 0) {
            return "";
        }
        // Remove HTML tags using regex
        return html.replaceAll("<[^>]*>", "");
    }

    private void newDirectoryDialog(NavController navController,Bundle bundle,NoteDirectoryEntity noteDirectory) {

        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);


        StringBuilder passwordString = new StringBuilder(10);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.password_dialog, null);
        GridLayout gridLayout = view.findViewById(R.id.keypad_lay);
        ImageView icon = view.findViewById(R.id.icon);
        builder.setView(view);
        dialog = builder.create();
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            int finalI = i;

            if ( gridLayout.getChildAt(i) instanceof Button && gridLayout.getChildAt(i).getId() != R.id.cancel )
                gridLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (passwordString.toString().trim().length()<6) {
                        passwordString.append( ((Button) gridLayout.getChildAt(finalI)).getText().toString().trim() );
                    }
                    if (passwordString.toString().trim().length()==6) {
                        if (noteDirectory.getPassword().equals(passwordString.toString())){
                            navController.navigate(R.id.action_noteFragment_to_listNoteFragment,bundle);
                            dialog.dismiss();
                        }else {
                            passwordString.delete(0, passwordString.length());
                            shake(icon,activity);
                        }
                    }
                    //Toast.makeText(v.getContext(),""+passwordString.toString(),Toast.LENGTH_SHORT).show();
                }
            });
        }

        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });




        dialog.show();
    }

    private void shake(View viewToShake,Activity activity){


        Animation shakeAnimation = AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.shake_animation);


        shakeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {

                viewToShake.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });


        viewToShake.startAnimation(shakeAnimation);

    }

}
