package com.churchkit.churchkit.ui;

import static com.churchkit.churchkit.Util.BOOK_MARK;
import static com.churchkit.churchkit.Util.deleteCache;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.churchkit.churchkit.CKPreferences;
import com.churchkit.churchkit.R;
import com.churchkit.churchkit.Util;
import com.churchkit.churchkit.api.PexelsRepository;
import com.churchkit.churchkit.database.MyDataDb;
import com.churchkit.churchkit.database.entity.PexelsPhoto;
import com.churchkit.churchkit.database.entity.base.BaseBookMark;
import com.churchkit.churchkit.database.entity.bible.BookMarkChapter;
import com.churchkit.churchkit.database.entity.song.BookMarkSong;
import com.churchkit.churchkit.modelview.bible.BibleBookMarkViewModel;
import com.churchkit.churchkit.modelview.song.SongBookMarkViewModel;
import com.churchkit.churchkit.ui.bible.ChapterDialogFragment;
import com.churchkit.churchkit.ui.song.SongDialogFragment;
import com.churchkit.churchkit.ui.util.ColorPicker;
import com.churchkit.churchkit.ui.util.DrawerCitacion;
import com.churchkit.churchkit.ui.util.PermissionUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class EditorBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener, ColorPicker.OnClicked {
    private int fontIndex = 0;

    public static EditorBottomSheet getInstanceWithActionMode(ActionMode mode, TextView textSelected, int TYPE_BOOKMARK, int TYPE, String id, String ref) {
        mMode = mode;
        mTextSelected = textSelected;
        mTYPE_BOOKMARK = TYPE_BOOKMARK;
        M_TYPE = TYPE;
        mId = id;
        mRef = ref;
        return new EditorBottomSheet();
    }

    public static EditorBottomSheet getInstance(BaseBookMark baseBookMark, TextView textSelected, int TYPE_BOOKMARK, int TYPE, String id, String ref) {
        mTextSelected = textSelected;
        M_TYPE = TYPE;
        mId = id;
        mBookMark = baseBookMark;
        mTYPE_BOOKMARK = TYPE_BOOKMARK;
        mRef = ref;


        return new EditorBottomSheet();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                 bottomSheetInternal = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheetInternal).setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });


        CoordinatorLayout root = (CoordinatorLayout) inflater.inflate(R.layout.fragment_editor_bottom_sheet, container, false);

        bookMarkLayout = root.findViewById(R.id.linear_layout_color_picker);
        imageView = root.findViewById(R.id.my_image_view);
        layoutPhoto = root.findViewById(R.id.layout_photo);
        action = root.findViewById(R.id.action);
        randomColor = root.findViewById(R.id.random_color);
        //randomImage = root.findViewById(R.id.random_img);
        textFont = root.findViewById(R.id.text_font);
        recyclerViewListImg = root.findViewById(R.id.recycler_view_pexels);

        ConstraintLayout.LayoutParams temp= (ConstraintLayout.LayoutParams) imageView.getLayoutParams();


        applicationContext = getActivity().getApplicationContext();




        imageView.setOnClickListener(v -> {

            if ( a % 2 ==0){
                recyclerViewListImg.setVisibility(View.GONE);
                ViewGroup.LayoutParams pa= new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                imageView.setLayoutParams(pa);

            }else {
                recyclerViewListImg.setVisibility(View.VISIBLE);
                imageView.setLayoutParams(temp);
            }
            a+=1;

        });

        ckPreferences = new CKPreferences(getContext());

        fontSize = root.findViewById(R.id.font_size);
        decreaseTextSize = root.findViewById(R.id.decrease);
        increaseTextSize = root.findViewById(R.id.increase);
        increaseTextSize.setOnClickListener(this::onClick);
        decreaseTextSize.setOnClickListener(this::onClick);

        int s = (int) getResources().getDimension( com.intuit.ssp.R.dimen._10ssp );
        fontSize.setText( s +" px" );

        songBookMarkViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(SongBookMarkViewModel.class);
        bibleBookMarkViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(BibleBookMarkViewModel.class);

        TextView textColor = root.findViewById(R.id.text_color);
        TextView openGallery = root.findViewById(R.id.open_gallery);

        title = root.findViewById(R.id.title);
        description = root.findViewById(R.id.description);

        recyclerViewListImg.setLayoutManager( new GridLayoutManager(getContext(),4));
         adapterListImage = new AdapterListImage();
        recyclerViewListImg.setAdapter( adapterListImage );
        updateAdapter();


        List<Integer> imgId = Arrays.asList(
                R.drawable.img1,
                R.drawable.img2,
                R.drawable.img3,
                R.drawable.img4,
                R.drawable.img5,
                R.drawable.img6,
                R.drawable.img7,
                R.drawable.img8
        );


        List<Integer> colorId = Arrays.asList(

                com.churchkit.churchkit.ui.util.Util.getColorByPosition(7),
                com.churchkit.churchkit.ui.util.Util.getColorByPosition(2),
                com.churchkit.churchkit.ui.util.Util.getColorByPosition(3),
                //com.churchkit.churchkit.ui.util.Util.getColorByPosition(4),
                Color.WHITE, Color.BLACK,
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
                startActivityForResult(intent, 2);
            }
        });

        textColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (colorFgId.size() - 1 > newColorIndex) {
                    bitmapToSave = drawerCitacion.getCitationWithNewFgColor(colorFgId.get(newColorIndex));
                    newColorIndex += 1;
                    imageView.setImageBitmap(bitmapToSave);
                } else {
                    newColorIndex = 0;
                }



            }
        });
        randomColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmapToSave = drawerCitacion.getCitationWithBgColor(colorId.get(colorIndex));
                colorIndex += 1;
                imageView.setImageBitmap(bitmapToSave);
                if (colorId.size() - 1 == colorIndex)
                    colorIndex = 0;

            }
        });

        textFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmapToSave = drawerCitacion.getCitationWithNewFontStyle(Util.getAllFont().get(fontIndex));
                fontIndex += 1;
                imageView.setImageBitmap(bitmapToSave);
                if (Util.getAllFont().size() - 1 == fontIndex)
                    fontIndex = 0;

            }
        });

        //BOOKMARK
        if (M_TYPE == BOOK_MARK) {
            bookMarkLayout.setVisibility(View.VISIBLE);
            layoutPhoto.setVisibility(View.GONE);
            action.setVisibility(View.GONE);

            int tempStart = 0, tempEnd = 0;
            initEditorBookMark();

            bookMarkLayout.addView(new ColorPicker(getContext(), getColorBookMarkSelected(), this), 0);


        } else {
            bookMarkLayout.setVisibility(View.GONE);
            drawerCitacion = new DrawerCitacion(EditorBottomSheet.this.getContext(), Util.getSelectedText(mTextSelected) + "\n\n" + mRef);


            bitmapToSave = drawerCitacion.getCitationWithNewFgColor(Color.RED);
            colorIndex += 1;

            imageView.setImageBitmap(bitmapToSave);

        }


        TextView downloadImage = root.findViewById(R.id.download_image);
        TextView statusWhatsapp = root.findViewById(R.id.status_whatsapp);
        TextView instagram = root.findViewById(R.id.instagram);

        statusWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendBitmapToOtherApp(getContext(), "com.whatsapp", bitmapToSave);
            }
        });
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBitmapToOtherApp(getContext(), "com.instagram.android", bitmapToSave);
            }
        });

        downloadImage.setOnClickListener(v -> {
            if (PermissionUtils.hasWriteExternalStoragePermission(requireActivity())) {
                saveImageToGallery(bitmapToSave);
                Toast.makeText(applicationContext, R.string.img_save_in_gallery, Toast.LENGTH_LONG).show();

            }else {
                PermissionUtils.requestWriteExternalStoragePermission( requireActivity() );
            }

        });

        return root;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    private void initEditorBookMark() {
        if (mBookMark != null) {
            title.setText(mBookMark.getTitle());
            description.setText(mBookMark.getDescription());
        }

    }

    private String getColorBookMarkSelected() {
        return mBookMark != null ? mBookMark.getColor() : null;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mMode != null)
            mMode.finish();

        mBookMark = null;
        mMode = null;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            try {
                bitmapToSave = drawerCitacion.getCitationWithBgColor(getContext(), data.getData());
                imageView.setImageBitmap(bitmapToSave);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    private void saveImageToGallery(Bitmap bitmap) {


        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/MyDirectory/");
        directory.mkdirs();

        String n = mRef.replace("à","a").replace(":","")
                .replaceAll(" ","")+"_";

        String time = String.valueOf( new Date().getTime() );
        time = time.substring(8,time.length());

        String fileName = "CK_"+  n + time + ".png";
        File file = new File(directory, fileName);


        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(file));
            getContext().sendBroadcast(intent);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getTheme() {
        return R.style.AppBottomSheetDialogTheme;
        //return super.getTheme();
    }

    public  void sendBitmapToOtherApp(Context context, String packageApp, Bitmap bitmap) {
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
            Toast.makeText(applicationContext, R.string.app_not_installed_on_device, Toast.LENGTH_SHORT).show();
        }
    }


    private Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }


    private LinearLayout bookMarkLayout;
    private static ActionMode mMode;
    private static TextView mTextSelected, fontSize;
    private ImageButton increaseTextSize, decreaseTextSize;
    private static int M_TYPE;
    View bottomSheetInternal;
    private static String mId;
    private static String mRef;
    private LinearLayout action, layoutPhoto;
    private TextView randomColor, textFont;
    private DrawerCitacion drawerCitacion = null;
    private int imageIndex = 0, colorIndex = 0;
    private int newColorIndex = 0;
    //private CKSongDb ckSongDb;
    //private CKBibleDb ckBibleDb;
    private BibleBookMarkViewModel bibleBookMarkViewModel;
    private SongBookMarkViewModel songBookMarkViewModel;
    private TextInputEditText title, description;
    private static BaseBookMark mBookMark;
    private static int mTYPE_BOOKMARK;
    private CKPreferences ckPreferences;
    private Context applicationContext;
    int a = 2;


    private ImageView imageView;
    private Bitmap bitmapToSave;

    private  RecyclerView recyclerViewListImg;
    AdapterListImage adapterListImage;


    @Override
    public void onDestroy() {
        File file = new File(getContext().getCacheDir(), "image.jpg");
        if (file.exists())
            file.delete();
        mTYPE_BOOKMARK = -1;
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.decrease:
                if (Integer.valueOf(fontSize.getText().toString().split(" ")[0]) > 20) {
                    int i = Integer.valueOf(fontSize.getText().toString().split(" ")[0]) - 1;
                    fontSize.setText(i + " px");

                    bitmapToSave = drawerCitacion.getCitationWithDiffSizeText(i);
                    imageView.setImageBitmap(bitmapToSave);
                }
                break;
            case R.id.increase:
                if (Integer.valueOf(fontSize.getText().toString().split(" ")[0]) <= 120) {

                    int d = Integer.valueOf(fontSize.getText().toString().split(" ")[0]) +
                            (int) getResources().getDimension(com.intuit.ssp.R.dimen._1ssp);
                    fontSize.setText(d + " px");

                    bitmapToSave = drawerCitacion.getCitationWithDiffSizeText(d);
                    imageView.setImageBitmap(bitmapToSave);

                }

                break;
        }
    }


    @Override
    public void clicked(View v, String stringColor) {
        int finalTempStart = 0, finalTempEnd = 0;
        if (mBookMark instanceof BookMarkSong) {

            BookMarkSong bookMark = (BookMarkSong) mBookMark;

            bookMark.setColor(stringColor);
            bookMark.setTitle(title.getText().toString());
            bookMark.setDescription(description.getText().toString());

            // ckSongDb.bookMarkSongDao().insert(bookMark);
            songBookMarkViewModel.insert(bookMark);

        } else if (mBookMark instanceof BookMarkChapter) {
            BookMarkChapter bookMark = (BookMarkChapter) mBookMark;
            bookMark.setColor(stringColor);
            bookMark.setTitle(title.getText().toString());
            bookMark.setDescription(description.getText().toString());

            bibleBookMarkViewModel.insert(bookMark);
            //ckBibleDb.bookMarkBibleDao().insert(bookMark);

        } else {
            ///insert
            final int start = mTextSelected.getSelectionStart();
            final int end = mTextSelected.getSelectionEnd();

            if (SongDialogFragment.SONG_BOOKMARK == mTYPE_BOOKMARK) {
                songBookMarkViewModel.insert(
                        new BookMarkSong(title.getText().toString(),
                                description.getText().toString(),
                                stringColor,
                                start,
                                end,
                                mId)
                );

                songBookMarkViewModel.insert(
                        new BookMarkSong(title.getText().toString(),
                                description.getText().toString(),
                                stringColor,
                                start,
                                end,
                                mId)
                );
                /*ckSongDb.bookMarkSongDao().insert(
                        new BookMarkSong(title.getText().toString(),
                                description.getText().toString(),
                                stringColor,
                                start,
                                end,
                                mId));*/
            } else if (ChapterDialogFragment.BIBLE_BOOKMARK == mTYPE_BOOKMARK) {


                bibleBookMarkViewModel.insert(
                        new BookMarkChapter(title.getText().toString(),
                                description.getText().toString(),
                                stringColor,
                                start,
                                end,
                                mId)
                );

               /*ckBibleDb.bookMarkBibleDao().insert(
                       new BookMarkChapter(title.getText().toString(),
                               description.getText().toString(),
                               stringColor,
                               start,
                               end,
                               mId)
               );*/
            }


        }

        EditorBottomSheet.this.dismiss();

    }

    @Override
    public void deleteBookMark() {


        if (SongDialogFragment.SONG_BOOKMARK == mTYPE_BOOKMARK) {
            songBookMarkViewModel.delete((BookMarkSong) mBookMark);
        } else if (ChapterDialogFragment.BIBLE_BOOKMARK == mTYPE_BOOKMARK) {
            bibleBookMarkViewModel.delete((BookMarkChapter) mBookMark);
        }

        EditorBottomSheet.this.dismiss();

    }

    private class AdapterListImage extends RecyclerView.Adapter<AdapterListImage.ViewHolderImg>{
        List<PexelsPhoto> pexelsPhotoList = new ArrayList<>();



        public void setPexelsPhotoList(List<PexelsPhoto> pexelsPhotoList) {
            this.pexelsPhotoList = pexelsPhotoList;
        }

        @SuppressLint("CheckResult")
        public AdapterListImage() {

        }

        @NonNull
        @Override
        public ViewHolderImg onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_pexels,parent,false);
            return new ViewHolderImg(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolderImg holder, int position) {

            PexelsPhoto pexelsPhoto = pexelsPhotoList.get(holder.getAbsoluteAdapterPosition() );

            holder.photographer.setText( "By "+pexelsPhoto.getPhotographer() );
            Glide.with(holder.itemView.getContext())
                    .load(pexelsPhoto.getUrlSmall())
                    //.apply(new RequestOptions().override(720,900))
                    //.centerCrop()
                    .into(new CustomTarget<Drawable>() {

                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            holder.img.setImageDrawable(resource);

                            holder.itemView.setOnClickListener(v -> {


                                Glide.with(getContext())
                                        .asBitmap()
                                        .load(pexelsPhoto.getUrlPortrait())
                                        .apply(new RequestOptions().override(1024,1820))
                                        .into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                //final Bitmap bitmap = resource; //
                                                bitmapToSave = drawerCitacion.getCitationWithBgColor(resource);
                                                imageView.setImageBitmap(bitmapToSave);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {

                                            }
                                        });




                            });
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            holder.img.setImageDrawable(getActivity().getDrawable(R.drawable.img3));
                        }
                    });



        }

        @Override
        public int getItemCount() {
            return pexelsPhotoList.size();
        }

        private class ViewHolderImg extends RecyclerView.ViewHolder {
            ImageView img;
            TextView photographer;
            public ViewHolderImg(@NonNull View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.img);
                photographer = itemView.findViewById(R.id.photographer);

            }
        }
    }


    @SuppressLint("CheckResult")
    public void updateAdapter(){

        PexelsRepository repo = new PexelsRepository( MyDataDb.getInstance( getActivity().getApplicationContext() ).photoPexelsDao() );


        if (!ckPreferences.existPhotoInDb()){
            repo.makeJ("btoaaaq")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(photoList2 -> {


                        repo.insertAll(photoList2);
                        ckPreferences.setExistPhotoInDb(true);

                        adapterListImage.setPexelsPhotoList(photoList2);
                        adapterListImage.notifyDataSetChanged();

                    },error->{
                        ckPreferences.setExistPhotoInDb(false);
                    });
        }else {
            repo.getAllPexelsPhoto().observe(getViewLifecycleOwner(), new Observer<List<PexelsPhoto>>() {

                @Override
                public void onChanged(List<PexelsPhoto> photoList) {
                    if (photoList != null && photoList.size() != 0){
                        adapterListImage.setPexelsPhotoList(photoList);
                        adapterListImage.notifyDataSetChanged();
                    }
                }
            });
        }



    }

    public void onStop() {
        deleteCache(getContext());
        super.onStop();
    }
}
