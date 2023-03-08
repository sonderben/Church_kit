package com.churchkit.churchkit.ui;

import static com.churchkit.churchkit.Util.BOOK_MARK;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.churchkit.churchkit.R;
import com.churchkit.churchkit.Util;
import com.churchkit.churchkit.ui.util.ColorPicker;
import com.churchkit.churchkit.ui.util.DrawerCitacion;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
        imageView = root.findViewById(R.id.my_image_view);
        layoutPhoto = root.findViewById(R.id.layout_photo);
        action = root.findViewById(R.id.action);
        randomColor = root.findViewById(R.id.random_color);
        randomImage = root.findViewById(R.id.random_img);

        TextView textColor = root.findViewById(R.id.text_color);
        TextView openGallery = root.findViewById(R.id.open_gallery);


        List<Integer> imgId = Arrays.asList(
               R.drawable.img1 ,
         R.drawable.img2,
         R.drawable.img3,
         R.drawable.img4,
         R.drawable.img5,
         R.drawable.img6,
         R.drawable.img7,
         R.drawable.img8
        );


        List<Integer> colorId = Arrays.asList(
                /*Color.TRANSPARENT ,
                Color.RED,
                Color.GREEN,
                Color.BLACK,
                Color.BLUE,
                Color.GRAY,
                Color.MAGENTA*/
                com.churchkit.churchkit.ui.util.Util.getColorByPosition(7),
                com.churchkit.churchkit.ui.util.Util.getColorByPosition(2),
                com.churchkit.churchkit.ui.util.Util.getColorByPosition(3),
                //com.churchkit.churchkit.ui.util.Util.getColorByPosition(4),
                Color.WHITE,
                com.churchkit.churchkit.ui.util.Util.getColorByPosition(5),
                com.churchkit.churchkit.ui.util.Util.getColorByPosition(6),
                com.churchkit.churchkit.ui.util.Util.getColorByPosition(1),
                com.churchkit.churchkit.ui.util.Util.getColorByPosition(8),
                com.churchkit.churchkit.ui.util.Util.getColorByPosition(9)
        );
        List<Integer> colorFgId = Arrays.asList(

                Color.WHITE,
                Color.GREEN,
                Color.BLACK,
                Color.BLUE,
                Color.GRAY,
                Color.MAGENTA,
                Color.RED

        );

        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2);
            }
        });

        textColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (colorFgId.size()-1>newColorIndex) {
                    bitmapToSave = drawerCitacion.getCitationWithNewFgColor(colorFgId.get(newColorIndex));
                    newColorIndex += 1;
                    imageView.setImageBitmap(bitmapToSave);
                }
                else {
                    newColorIndex = 0;
                }


                //Toast.makeText(getContext(),"clicked: "+newColorIndex,Toast.LENGTH_SHORT).show();
            }
        });
        randomColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmapToSave=drawerCitacion.getCitationWithBgColor(colorId.get( colorIndex ));
                colorIndex+=1;
                imageView.setImageBitmap(bitmapToSave);
                if (colorId.size()-1 ==colorIndex)
                    colorIndex=0;

            }
        });
        randomImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable drawable = getContext().getDrawable( imgId.get( imageIndex ) );
                imageIndex+=1;
                Bitmap bitmap = ( (BitmapDrawable) drawable ).getBitmap(); //
                Bitmap bit = bitmap.copy(Bitmap.Config.ARGB_8888,true);
                bitmapToSave=drawerCitacion.getCitationWithBgColor(bit);
                imageView.setImageBitmap(bitmapToSave);
                if (imgId.size()-1 ==imageIndex)
                    imageIndex=0;
            }
        });

       if (M_TYPE == BOOK_MARK) {
           layoutPhoto.setVisibility(View.GONE);
           action.setVisibility(View.GONE);
           linearLayout.addView(new ColorPicker(getContext(), (v, stringColor) -> {
               Spannable spannable = (Spannable) mTextSelected.getText();
               int start = mTextSelected.getSelectionStart();
               int end = mTextSelected.getSelectionEnd();

               int color = Color.parseColor(stringColor.replace("#", "#6b"));
               spannable.setSpan(new BackgroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
               if (mMode != null)
                mMode.finish();
               EditorBottomSheet.this.dismiss();
           }));
       }
       else {

           AdapterRecyclerView adapterRecyclerView = new AdapterRecyclerView();

               drawerCitacion = new DrawerCitacion(EditorBottomSheet.this.getContext(), Util.getSelectedText(mTextSelected));
               adapterRecyclerView.setDrawerCitation(drawerCitacion,imageView,bitmapToSave);


           bitmapToSave = drawerCitacion.getCitationWithNewFgColor(Color.RED);
           colorIndex+=1;
           //bitmapToSave = BitmapFactory.decodeByteArray(drawnImageData, 0, drawnImageData.length);

           imageView.setImageBitmap(bitmapToSave);

       }



       TextView downloadImage = root.findViewById(R.id.download_image);
       TextView statusWhatsapp = root.findViewById(R.id.status_whatsapp);
        TextView instagram = root.findViewById(R.id.instagram);

       statusWhatsapp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               sendBitmapToOtherApp(getContext(),"com.whatsapp",bitmapToSave);
           }
       });
       instagram.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               sendBitmapToOtherApp(getContext(),"com.instagram.android",bitmapToSave);
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
        if (mMode != null)
            mMode.finish();
        // Your code here
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK){
            try {
                bitmapToSave = drawerCitacion.getCitationWithBgColor(getContext(),data.getData());
                imageView.setImageBitmap(bitmapToSave);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


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
                         bitmapToSave=drawerCitation.getCitationWithBgColor(bit);
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


    public static void sendBitmapToOtherApp(Context context,String packageApp, Bitmap bitmap) {
        FileOutputStream outputStream = null;
        File file = new File(context.getCacheDir(), "image.jpg");
        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Uri contentUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".fileprovider", file);

        Intent sendIntent = new Intent("android.intent.action.SEND");
        sendIntent.setPackage(packageApp);
        sendIntent.setType("image/jpg");
        sendIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(sendIntent);
            //file.delete();
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "La aplicacion no está instalado en tu dispositivo", Toast.LENGTH_SHORT).show();
        }
    }





    private Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getActivity(). getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }




    private LinearLayout linearLayout;
    private static ActionMode mMode;
    private static TextView mTextSelected;
    private static int M_TYPE;
    private LinearLayout action,layoutPhoto;
    private TextView randomColor,randomImage;
    DrawerCitacion drawerCitacion = null;
    private int imageIndex = 0,colorIndex=0;
    private int newColorIndex = 0;


    ImageView imageView;
    Bitmap bitmapToSave;

    @Override
    public void onDestroy() {
        File file = new File(getContext().getCacheDir(), "image.jpg");
        if (file.exists())
            file.delete();
        super.onDestroy();

    }
}
