package com.churchkit.churchkit.ui.data;

import static com.churchkit.churchkit.util.Constant.BIBLE;
import static com.churchkit.churchkit.util.Constant.SONG;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.bumptech.glide.Glide;
import com.churchkit.churchkit.CKPreferences;
import com.churchkit.churchkit.DownloadDataWork;
import com.churchkit.churchkit.R;
import com.churchkit.churchkit.database.CKBibleDb;
import com.churchkit.churchkit.database.CKSongDb;
import com.churchkit.churchkit.database.entity.base.BaseInfo;
import com.churchkit.churchkit.database.entity.bible.BibleInfo;
import com.churchkit.churchkit.database.entity.song.SongInfo;
import com.churchkit.churchkit.modelview.bible.BibleInfoViewModel;
import com.churchkit.churchkit.modelview.song.SongInfoViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;


public  class AdapterData extends RecyclerView.Adapter<AdapterData.MyViewHolder> {


    private boolean isConnected = false;
    private String ORIGIN ;
    private FragmentActivity activity;
    public void setIsConnected(boolean isConnected){
        this.isConnected = isConnected;
    }
    List<BaseInfo> baseInfoList = new ArrayList<>();
    private BibleInfoViewModel bibleInfoViewModel;
    private SongInfoViewModel songInfoViewModel;


    public List<BaseInfo> getBaseInfoList() {
        return baseInfoList;
    }

    private void setBaseInfoList(List<BaseInfo> baseInfoList) {
        this.baseInfoList = baseInfoList;
        notifyDataSetChanged();

        if (baseInfoList.size()>0){

            if (baseInfoList.get(0) instanceof BibleInfo && ckPreferences.getBibleName().equalsIgnoreCase("")){
                for (int i = 0; i < baseInfoList.size(); i++) {
                    if (baseInfoList.get(i).isDownloaded()) {
                        ckPreferences.setBibleName(baseInfoList.get(i).getId());
                        break;
                    }
                }
            }
            else if (baseInfoList.get(0) instanceof SongInfo && ckPreferences.getSongName().equalsIgnoreCase("")){
                for (int i = 0; i < baseInfoList.size(); i++) {
                    if (baseInfoList.get(i).isDownloaded()) {
                        ckPreferences.setSongName(baseInfoList.get(i).getId());
                        break;
                    }
                }
            }

        }

    }

    CKPreferences ckPreferences;

    public AdapterData( FragmentActivity activity, CKPreferences ckPreferences,String origin){
        this.ckPreferences = ckPreferences;
        this.activity = activity;
        this.ORIGIN = origin;
        if ( ORIGIN.equals("SONG") ){
            songInfoViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity.getApplication()).create(SongInfoViewModel.class);
            songInfoViewModel.getAllSongInfo().observe((LifecycleOwner) activity, new Observer<List<SongInfo>>() {
                @Override
                public void onChanged(List<SongInfo> songInfos) {
                    setBaseInfoList( new ArrayList<>( songInfos ) );
                }
            });
        }else {
            bibleInfoViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance( activity.getApplication() ).create( BibleInfoViewModel.class );
            bibleInfoViewModel.getAllBibleInfo().observe((LifecycleOwner) activity, new Observer<List<BibleInfo>>() {
                @Override
                public void onChanged(List<BibleInfo> bibleInfos) {
                    setBaseInfoList( new ArrayList<>( bibleInfos ) );
                }
            });
        }
    }






    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bible_info_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BaseInfo bibleInfo = baseInfoList.get(holder.getAbsoluteAdapterPosition());
        holder.language.setText(bibleInfo.getLanguage());
        holder.title.setText(bibleInfo.getName());
        holder.size.setText( bibleInfo.getSize() );

        holder.download.setVisibility(bibleInfo.isDownloaded() ? View.GONE : View.VISIBLE);
        holder.deleteImg.setVisibility(!bibleInfo.isDownloaded() ? View.GONE : View.VISIBLE);

        holder.deleteImg.setOnClickListener(v -> deleteBookDialogInfo( bibleInfo) );

        downloadInBackground( null,holder, bibleInfo.getId() );


        holder.download.setOnClickListener(download -> {

            if (isConnected){
                download.setVisibility(View.GONE);
                if (bibleInfo instanceof BibleInfo) {
                    downloadInBackground(bibleInfo,holder,null);
                }
                if (bibleInfo instanceof SongInfo){
                    downloadInBackground(bibleInfo,holder,null);
                }


            }
            else {
                Toast toast = Toast.makeText(activity, R.string.pls_conect_internet,Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,20);
                toast.show();
            }
        });






    }

    private void downloadInBackground( BaseInfo bibleInfo,MyViewHolder holder,String idBook) {
        OneTimeWorkRequest request = null;
        UUID idRequest = null;
        if (idBook == null){
            Data data = new Data.Builder()
                    .putString( DownloadDataWork.BASE_INFO_URL,bibleInfo.getUrl() )
                    .putString( DownloadDataWork.BASE_INFO_ID,bibleInfo.getId() )
                    .putString( DownloadDataWork.BASE_INFO_NAME,bibleInfo.getName() )
                    .putString( DownloadDataWork.BASE_INFO_TYPE, bibleInfo instanceof BibleInfo? BIBLE : SONG)
                    .build();
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

             request = new OneTimeWorkRequest.Builder(DownloadDataWork.class)
                    .setInputData(data)
                    .setConstraints(constraints)
                    .build();
            WorkManager.getInstance( activity ).enqueue(request);
            //if (request.getId() != null)
                ckPreferences.setWorkRequestId(bibleInfo.getId(), request.getId());
        }
        else {
            idRequest = ckPreferences.getWorkRequestId( idBook );
        }

        if( idRequest !=null || request !=null ){
            WorkManager.getInstance(activity).getWorkInfoByIdLiveData( idRequest==null? request.getId():idRequest )
                    .observe((LifecycleOwner) activity, new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(WorkInfo workInfo) {
                            if (workInfo != null) {
                                Data progress = workInfo.getProgress();
                                int value = progress.getInt(DownloadDataWork.PROGRESS,-1);
                                String infoPrepo = progress.getString( DownloadDataWork.INFO_PREPOPULATE );


                                holder.infoPrepopulate.setVisibility(View.VISIBLE);
                                if (infoPrepo!=null)
                                    holder.infoPrepopulate.setText( infoPrepo );


                                if ( workInfo.getState().equals( WorkInfo.State.SUCCEEDED ) ){
                                    holder.infiniteProgressBar.setVisibility( View.GONE );
                                    holder.prepopulateProgressBar.setVisibility( View.GONE );
                                    holder.infoPrepopulate.setVisibility( View.GONE );
                                    holder.deleteImg.setVisibility(View.VISIBLE);
                                } else if (workInfo.getState().equals(WorkInfo.State.RUNNING)) {
                                    if (value>=0 || value<100){
                                        holder.infiniteProgressBar.setVisibility( View.VISIBLE );
                                        holder.prepopulateProgressBar.setVisibility( View.VISIBLE );
                                        if ( value > 0 )
                                            holder.prepopulateProgressBar.setProgress( value );
                                        holder.infoPrepopulate.setVisibility( View.VISIBLE );
                                    }
                                }

                            }

                        }
                    });
        }
    }

    private void deleteBookDialogInfo(BaseInfo bibleInfo) {

        AlertDialog.Builder builder = new AlertDialog.Builder( activity );

        LayoutInflater layoutInflater = activity.getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.dialog_delete_book_info, (ViewGroup)null);
        TextView title = view.findViewById(R.id.description);


        builder.setView(view);


         DeleteDataDialogFragment deleteDataDialogFragment = new DeleteDataDialogFragment();
         deleteDataDialogFragment.setActivity( activity );

        builder.setPositiveButton(activity.getResources().getString(R.string.delete), (dialog, which) -> {
            bibleInfo.setDownloaded(false);
            if (ORIGIN.equalsIgnoreCase("BIBLE")){

                deleteDataDialogFragment.show(activity.getSupportFragmentManager(),bibleInfo.getId());
                Flowable.fromAction(() -> CKBibleDb.getInstance( activity.getApplicationContext()).runInTransaction(new Runnable() {
                            @Override
                            public void run() {

                                bibleInfoViewModel.delete( (BibleInfo) bibleInfo );
                                CKBibleDb.getInstance(activity.getApplicationContext())
                                        .bibleBookDao().deleteAll( bibleInfo.getId() );
                                ckPreferences.setBibleName("");


                            }
                        })).subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableSubscriber<Object>() {
                            @Override
                            public void onNext(Object o) {

                            }

                            @Override
                            public void onError(Throwable t) {
                                deleteDataDialogFragment.dismissNow();
                            }

                            @Override
                            public void onComplete() {
                                deleteDataDialogFragment.dismissNow();
                            }
                        });

            }else {

                Flowable.fromAction(() -> CKBibleDb.getInstance(activity.getApplicationContext()).runInTransaction(new Runnable() {
                            @Override
                            public void run() {

                                songInfoViewModel.delete( (SongInfo) bibleInfo );
                                CKSongDb.getInstance(activity.getApplicationContext())
                                        .songBookDao().deleteAll(bibleInfo.getId());
                                ckPreferences.setSongName("");


                            }
                        })).subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableSubscriber<Object>() {
                            @Override
                            public void onNext(Object o) {

                            }

                            @Override
                            public void onError(Throwable t) {
                                deleteDataDialogFragment.dismissNow();
                            }

                            @Override
                            public void onComplete() {
                                deleteDataDialogFragment.dismissNow();
                            }
                        });
            }
        });



        builder.setNegativeButton(activity.getResources().getString(R.string.cancel), (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialog1 -> {
            Button delete = ((AlertDialog) dialog1).getButton(AlertDialog.BUTTON_POSITIVE);
            delete.setTextColor(Color.RED);

        });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return baseInfoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, language,  testament,size,infoPrepopulate;
        ImageButton download;
        ImageView deleteImg;
        ProgressBar prepopulateProgressBar, infiniteProgressBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            size = itemView.findViewById(R.id.size);
            testament = itemView.findViewById(R.id.testament);
            language = itemView.findViewById(R.id.language);

            download = itemView.findViewById(R.id.download);
            deleteImg = itemView.findViewById(R.id.checkbox);
            prepopulateProgressBar = itemView.findViewById(R.id.progressBar3);
            infiniteProgressBar = itemView.findViewById(R.id.progressBar2);
            infoPrepopulate = itemView.findViewById(R.id.info_prepopulate);

        }
    }
    public static class DeleteDataDialogFragment extends DialogFragment {
        private FragmentActivity activity;
        public void setActivity(FragmentActivity activity){
            this.activity = activity;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater layoutInflater = activity.getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.dialog_delete_book,null);

            ImageView imageView = view.findViewById( R.id.gif) ;


            Glide.with(activity)
                    .load(R.mipmap.delete)
                    .into(imageView);

            builder.setView( view );

            /*builder.setMessage(R.string.dialog_start_game)
                    .setPositiveButton(R.string.start, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // START THE GAME!
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            */

            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}