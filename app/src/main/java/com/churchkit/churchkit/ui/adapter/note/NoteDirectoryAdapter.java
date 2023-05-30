package com.churchkit.churchkit.ui.adapter.note;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.churchkit.churchkit.R;
import com.churchkit.churchkit.database.MyDataDb;
import com.churchkit.churchkit.database.entity.note.BaseNoteEntity;
import com.churchkit.churchkit.database.entity.note.NoteDirectoryEntity;
import com.churchkit.churchkit.database.entity.note.NoteEntity;
import com.churchkit.churchkit.modelview.note.NoteDirectoryViewModel;
import com.churchkit.churchkit.modelview.note.NoteViewModel;
import com.churchkit.churchkit.ui.note.ListNoteFragment;
import com.churchkit.churchkit.ui.note.NoteFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class NoteDirectoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<BaseNoteEntity> baseNoteEntityList=new ArrayList<>(1);
    NoteDirectoryEntity noteDirectory;

    public boolean baseNoteEntityListIsEmpty(){
        return baseNoteEntityList.isEmpty();
    }



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

            dvh.itemView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putSerializable("DIRECTORY",directory);
                NavController navController = Navigation.findNavController(v);
               // navController.getGraph().findNode(R.id.listChapterByTestamentFragment).setLabel(holder.itemView.getContext().getString( R.string.old_testament ));

                    navController.navigate(R.id.action_noteFragment_to_listNoteFragment,bundle);
            });
            dvh.itemView.setOnLongClickListener(v -> {
                modifyDialog(activity,baseNoteEntityList.get( holder.getAbsoluteAdapterPosition() ));
                //Toast.makeText(dvh.itemView.getContext(), baseNoteEntityList.get(position).getTitle(),Toast.LENGTH_SHORT).show();
                return true;
            });

        }else {
            NoteViewHolder nvh = ((NoteViewHolder)holder);
            NoteEntity note = (NoteEntity) baseNoteEntityList.get( holder.getAbsoluteAdapterPosition() );
            nvh.title.setText( note.getTitle() );
            nvh.description.setText(/* removeHtmlTags(*/ note.getNoteText() /*).trim()*/ );
            nvh.date.setText( dateFormat.format(note.getDate().getTime()) );
            nvh.img.setVisibility(View.GONE);

            String hashtag = "";
            if (note.getHashtag() != null){
                String [] hashtags = note.getHashtag().split(",");

                for (int i = 0; i < hashtags.length; i++) {
                    hashtag+=hashtags[i];

                    if (i <hashtags.length-1)
                        hashtag+=" &#x2022; ";
                }
            }
            nvh.hashtag.setText(Html.fromHtml(hashtag));
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

            nvh.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    modifyDialog(activity, baseNoteEntityList.get(holder.getAbsoluteAdapterPosition() ));
                    //Toast.makeText(nvh.itemView.getContext(), baseNoteEntityList.get(holder.getAbsoluteAdapterPosition()).getTitle(),Toast.LENGTH_SHORT).show();
                    return true;
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
        TextView title, description,date,hashtag;ImageView img;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title  = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date);
            img = itemView.findViewById(R.id.img);
            hashtag = itemView.findViewById(R.id.hashtag);

        }
    }

    public static String removeHtmlTags(String html) {
        if (html == null || html.trim().length() == 0) {
            return "";
        }
        // Remove HTML tags using regex
        return html.replaceAll("<[^>]*>", "");
    }

    /*private void newDirectoryDialog(NavController navController,Bundle bundle,NoteDirectoryEntity noteDirectory) {

        Drawable drawableSelected = activity.getResources().getDrawable(R.drawable.bg_point_circle_selected);
        Drawable drawableUnSelected = activity.getResources().getDrawable(R.drawable.bg_point_circle_unselected);
        //view.setBackground(drawable);

        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);


        StringBuilder passwordString = new StringBuilder(10);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.password_dialog, null);
        GridLayout gridLayout = view.findViewById(R.id.keypad_lay);
        LinearLayout linearLayout = view.findViewById(R.id.preview_code);
        ImageView icon = view.findViewById(R.id.icon);
        builder.setView(view);
        dialog = builder.create();
        Button cancelButton = view.findViewById(R.id.cancel);

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
                            if (navController!=null)
                                navController.navigate(R.id.action_noteFragment_to_listNoteFragment,bundle);
                            else {
                                NoteDirectoryViewModel noteDirectoryViewModel = new NoteDirectoryViewModel(
                                        MyDataDb.getInstance(activity.getApplicationContext()).noteDirectoryDao()
                                );
                                newDirectoryDialog(noteDirectoryViewModel,noteDirectory);
                            }

                            dialog.dismiss();
                        }else {
                            shake(icon,activity);
                            changePreviewPassword(null,linearLayout,drawableSelected,drawableUnSelected);
                            passwordString.delete(0, passwordString.length());


                        }
                    }
                    //Toast.makeText(v.getContext(),""+passwordString.toString(),Toast.LENGTH_SHORT).show();
                    changePreviewPassword(passwordString,linearLayout,drawableSelected,drawableUnSelected);
                   *//* switch (passwordString.length()){
                        *//**//*case 6:
                            linearLayout.getChildAt(5).setBackground(drawableSelected);*//**//*
                        case 5:
                            linearLayout.getChildAt(4).setBackground(drawableSelected);
                        case 4:
                            linearLayout.getChildAt(3).setBackground(drawableSelected);
                        case 3:
                            linearLayout.getChildAt(2).setBackground(drawableSelected);
                        case 2:
                            linearLayout.getChildAt(1).setBackground(drawableSelected);
                        case 1:
                            linearLayout.getChildAt(0).setBackground(drawableSelected);
                            break;
                        default:
                            for (int j = 0; j < 6; j++) {
                                linearLayout.getChildAt(j).setBackground(drawableUnSelected);
                            }
                    }*//*
                    cancelButton.setText(passwordString.toString().length()==0?"Cancelar":"Eliminar");
                }
            });


        }

        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelButton.setText(passwordString.toString().length()==1?"Cancelar":"Eliminar");
                if (passwordString.toString().length() == 0)
                    dialog.dismiss();
                else {
                    if (passwordString.length() > 0) {
                        passwordString.deleteCharAt(passwordString.length() - 1);
                        Toast.makeText(dialog.getContext(), ""+passwordString.length(),Toast.LENGTH_SHORT).show();
                        changePreviewPassword(passwordString,linearLayout,drawableSelected,drawableUnSelected);
                    }
                }
            }
        });




        dialog.show();
    }*/

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

    private void changePreviewPassword(StringBuilder passwordString,LinearLayout linearLayout, Drawable drawableSelected,Drawable drawableUnSelected){


       if (passwordString == null){
           unSelectAll(linearLayout, drawableUnSelected);
           return;
       }
        unSelectAll(linearLayout, drawableUnSelected);
        switch (  passwordString.length()  ){
                        /*case 6:
                            linearLayout.getChildAt(5).setBackground(drawableSelected);*/
            case 5:
                linearLayout.getChildAt(4).setBackground(drawableSelected);
            case 4:
                linearLayout.getChildAt(3).setBackground(drawableSelected);
            case 3:
                linearLayout.getChildAt(2).setBackground(drawableSelected);
            case 2:
                linearLayout.getChildAt(1).setBackground(drawableSelected);
            case 1:
                linearLayout.getChildAt(0).setBackground(drawableSelected);
                break;
            default:
                for (int j = 0; j < 6; j++) {
                    linearLayout.getChildAt(j).setBackground(drawableUnSelected);
                }
        }
    }
    private  void unSelectAll(LinearLayout linearLayout,Drawable drawableUnSelected){
        for (int j = 0; j < 6; j++) {
            linearLayout.getChildAt(j).setBackground(drawableUnSelected);
        }
    }
    private void modifyDialog(Activity activity,BaseNoteEntity baseNoteEntity) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.modify_dialog, (ViewGroup)null);
        TextView edit = view.findViewById(R.id.edit);
        TextView delete = view.findViewById(R.id.delete);

        if (baseNoteEntity instanceof  NoteEntity)
            edit.setVisibility(View.GONE);



        NoteViewModel noteViewModel = new NoteViewModel(MyDataDb.getInstance(activity.getApplicationContext()).noteDao());
        NoteDirectoryViewModel directoryViewModel = new NoteDirectoryViewModel(
                MyDataDb.getInstance(activity.getApplicationContext()).noteDirectoryDao()
        );


        builder.setView(view).setPositiveButton("Cancel", (dialog, id) -> {


        });

        AlertDialog alertDialog  = builder.create();

        alertDialog.show();
        delete.setOnClickListener(v -> {
            if (baseNoteEntity instanceof NoteDirectoryEntity){
                directoryViewModel.delete( (NoteDirectoryEntity) baseNoteEntity );

            }else {
                noteViewModel.delete((NoteEntity) baseNoteEntity);
            }
            alertDialog.dismiss();
        });

        edit.setOnClickListener(v -> {

            if (baseNoteEntity instanceof NoteDirectoryEntity){
                NoteDirectoryEntity temp = (NoteDirectoryEntity) baseNoteEntity;
                editDirectoryDialog(directoryViewModel, temp);
            }else {

            }

            alertDialog.dismiss();
        });
    }

    private void editDirectoryDialog(NoteDirectoryViewModel noteDirectoryViewModel, NoteDirectoryEntity e) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater layoutInflater = activity.getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.new_directory_layout, (ViewGroup)null);
        TextInputEditText title = view.findViewById(R.id.title);
        TextInputEditText password = view.findViewById(R.id.pwd);

        TextView titleDialog = view.findViewById(R.id.dialog_title);
        titleDialog.setText("update directory");

        title.setText(e.getTitle());
        password.setText(e.getPassword());



        builder.setView(view).setPositiveButton("Update", (dialog, id) -> {
            e.setTitle( title.getText().toString() );
            noteDirectoryViewModel.insert(e);
        } );

        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
