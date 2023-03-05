package com.churchkit.churchkit.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.churchkit.churchkit.MyDrawer;
import com.churchkit.churchkit.R;
import com.churchkit.churchkit.ui.util.ColorPicker;
import com.churchkit.churchkit.ui.util.DrawerCitacion;
import com.churchkit.churchkit.ui.util.Util;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditorBottomSheet extends BottomSheetDialogFragment {
    public static EditorBottomSheet getInstance(ActionMode mode, TextView textSelected,int TYPE){
        mMode = mode;
        mTextSelected = textSelected;
        M_TYPE = TYPE;


        return new EditorBottomSheet();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View root = inflater.inflate(R.layout.fragment_editor_bottom_sheet,container,false);

       linearLayout = root.findViewById(R.id.linear_layout_color_picker);
        recyclerView = root.findViewById(R.id.recyclerview_ebs);
        imageView = root.findViewById(R.id.my_image_view);
        layoutPhoto = root.findViewById(R.id.layout_photo);
        action = root.findViewById(R.id.action);

       if (M_TYPE == BOOK_MARK) {
           layoutPhoto.setVisibility(View.GONE);
           action.setVisibility(View.GONE);
           linearLayout.addView(new ColorPicker(getContext(), (v, stringColor) -> {
               Spannable spannable = (Spannable) mTextSelected.getText();
               int start = mTextSelected.getSelectionStart();
               int end = mTextSelected.getSelectionEnd();

               int color = Color.parseColor(stringColor.replace("#", "#6b"));
               spannable.setSpan(new BackgroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
               mMode.finish();
               EditorBottomSheet.this.dismiss();
           }));
       }
       else {
           DrawerCitacion drawerCitacion = null;
           AdapterRecyclerView adapterRecyclerView = new AdapterRecyclerView();
           recyclerView.setLayoutManager( new GridLayoutManager(getContext(),3));
           recyclerView.setAdapter(adapterRecyclerView);

               drawerCitacion = new DrawerCitacion(EditorBottomSheet.this.getContext(), getSelectedText());
               adapterRecyclerView.setDrawerCitation(drawerCitacion,imageView,bitmapToSave);


           bitmapToSave = drawerCitacion.getCitation(null);
           //bitmapToSave = BitmapFactory.decodeByteArray(drawnImageData, 0, drawnImageData.length);

           imageView.setImageBitmap(bitmapToSave);

       }



       TextView downloadImage = root.findViewById(R.id.download_image);
       TextView statusWhatsapp = root.findViewById(R.id.status_whatsapp);

       statusWhatsapp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //if (isWhatsAppStatusSupported(getContext())){
                   setWhatsAppStatus(bitmapToSave);
                   Toast.makeText(getContext(),"button clicked",Toast.LENGTH_LONG).show();
               /*}else {
                   Toast.makeText(getContext(),"don't have whatsapp",Toast.LENGTH_LONG).show();
               }*/

           }
       });

       downloadImage.setOnClickListener(v -> {
           saveImageToGallery(bitmapToSave);
           Toast.makeText(getContext(),"Image save in gallery",Toast.LENGTH_LONG).show();
       });

       return root;

    }
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mMode.finish();
        // Your code here
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){

                //getImageFromGallery(data.getData());


                DrawerCitacion drawerCitacion = new DrawerCitacion(EditorBottomSheet.this.getContext(), getSelectedText());

            try {
                bitmapToSave = drawerCitacion.getCitation(getContext(),data.getData());
                imageView.setImageBitmap(bitmapToSave);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //bitmapToSave = BitmapFactory.decodeByteArray(drawnImageData, 0, drawnImageData.length);




        }


    }

    private String getSelectedText() {
        String selectedText = null;

        int start = mTextSelected.getSelectionStart();
        int end = mTextSelected.getSelectionEnd();

        if (start != -1 && end != -1) {
            selectedText = mTextSelected.getText().toString().substring(start, end);
        }

        return selectedText;
    }
    private void saveImageToGallery(Bitmap bitmap)  {

        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/MyDirectory/");
        directory.mkdirs();


        String fileName = "myImage"+new Date().getTime() +".png";
        File file = new File(directory, fileName);


        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG,100,out);
            out.flush();
            out.close();

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(file));
            getContext().sendBroadcast(intent);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
     class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.MyViewHolder>{

        List<Integer>  img = new ArrayList<>(10);
        DrawerCitacion drawerCitation;
        ImageView imageView;
        Bitmap bitmapToSave;

         public void setDrawerCitation(DrawerCitacion drawerCitacion,ImageView imageView,Bitmap bitmapToSave) {
             this.drawerCitation = drawerCitacion;
             this.imageView = imageView;
             this.bitmapToSave = bitmapToSave;
         }


         //Context context;
        public AdapterRecyclerView(){
            //this.context = context;

            img.add( R.drawable.img1 );
            img.add( R.drawable.img2 );
            img.add( R.drawable.img3 );
            img.add( R.drawable.img4 );
            img.add( R.drawable.img5 );
            img.add( R.drawable.img6 );
            img.add( R.drawable.img7 );
            img.add( R.drawable.img8 );


        }

         @NonNull
         @Override
         public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
             View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_default_image_editor,parent,false);

             return new MyViewHolder( view );
         }

         @Override
         public int getItemCount() {
             return img.size();
         }

         @Override
         public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

             if (position==0){
                 holder.imageView.setVisibility(View.GONE);
                 holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Intent intent = new Intent(Intent.ACTION_PICK);
                         intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                         startActivityForResult(intent,2);
                     }

                 });
             }else {
                 holder.constraintLayout.setVisibility( View.GONE );
                 holder.imageView.setImageResource( img.get(position) );
                 holder.imageView.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
/*
                         byte[] drawnImageData =drawerCitation.getCitationWithDrawable( getContext().getDrawable( img.get(holder.getAdapterPosition()) ),400,400);

                         bitmapToSave = BitmapFactory.decodeByteArray(drawnImageData, 0, drawnImageData.length);

                         MyDrawer md = new MyDrawer();
                         Bitmap bitmap =md.getCitationWithDrawable(getContext().getDrawable( img.get( holder.getAbsoluteAdapterPosition() ) ));
                         imageView.setImageBitmap(bitmap);*/

                         Drawable drawable = getContext().getDrawable( img.get( holder.getAbsoluteAdapterPosition() ) );
                         Bitmap bitmap = ( (BitmapDrawable) drawable ).getBitmap(); //
                         Bitmap bit = bitmap.copy(Bitmap.Config.ARGB_8888,true);
                         bitmapToSave=drawerCitation.getCitation(bit);
                         imageView.setImageBitmap(bitmapToSave);

                     }
                 });
             }
         }

         private class MyViewHolder extends RecyclerView.ViewHolder{
             ImageView imageView;
             ConstraintLayout constraintLayout;
             public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.default_img);
                 constraintLayout = itemView.findViewById(R.id.select_from_gallery);
            }
        }


    }
    public void  sendToWhatsapp(){
        // Se convierte el bitmap en un array de bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapToSave.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();

// Se crea el intent para compartir el contenido como un estado de WhatsApp
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setPackage("com.whatsapp");
        intent.putExtra(Intent.EXTRA_STREAM, imageBytes);
        intent.setType("image/png");
        intent.putExtra("jid", "status@broadcast");


        getContext().startActivity(Intent.createChooser(intent, "Compartir como estado de WhatsApp"));

    }
    private boolean isWhatsAppStatusSupported(Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.setPackage("com.whatsapp");
        PackageManager pm = context.getPackageManager();
        return pm.resolveActivity(intent, 0) != null;
    }


    private void setWhatsAppStatus(Bitmap bitmap) {
        try {
            Uri imageUri = getImageUri(bitmap);
            Intent intent = new Intent("com.whatsapp.intent.action.SET_IMAGE");
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
            intent.setPackage("com.whatsapp");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getActivity(). getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }


    @Override
    public void onResume() {
        recyclerView.getLayoutParams().height = (int) ( Util.getScreenDisplayMetrics(
                getContext()
        ).heightPixels * 0.40f );
        super.onResume();
    }

    private LinearLayout linearLayout;
    private static ActionMode mMode;
    private static TextView mTextSelected;
    private static int M_TYPE;
    private LinearLayout action,layoutPhoto;
    int IMAGE =1;
    int BOOK_MARK =2;
    RecyclerView recyclerView;
    ImageView imageView;
    Bitmap bitmapToSave;
}
