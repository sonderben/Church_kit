package com.churchkit.churchkit.ui.song;


import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.customview.widget.ViewDragHelper;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.churchkit.churchkit.Model.Song;
import com.churchkit.churchkit.R;
import com.churchkit.churchkit.database.ChurchKitDb;
import com.churchkit.churchkit.database.entity.Verse;
import com.churchkit.churchkit.ui.util.Util;

import java.util.ArrayList;
import java.util.List;


public class SongDialogFragment extends DialogFragment {
    public static SongDialogFragment newInstance(Long idSong,String reference,String title){
        mSongId = idSong;
        mSongTitle = title;
        mReference = reference;
        return new SongDialogFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
          root = (ViewGroup) inflater.inflate(R.layout.fragment_list_chapter,container,false);



         churchKitDd = ChurchKitDb.getInstance(requireContext());


         tv  = root.findViewById(R.id.text);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

       // close = root.findViewById(R.id.close);
        bookTitle = root.findViewById(R.id.book_name);
        songTitle = root.findViewById(R.id.chap_);
        bookTitle.setText(mReference);
        songTitle.setText(mSongTitle);

//        close.setOnClickListener(x->this.dismiss());
        root.findViewById(R.id.fab_clos).setOnClickListener(x->this.dismiss());

        churchKitDd.verseDao().getAllVerseByIdSong(mSongId).observe(requireActivity(), new Observer<List<Verse>>() {
            @Override
            public void onChanged(List<Verse> verses) {
                tv.setText(listVerseToString(verses));
            }
        });




        return root;
    }
   // ImageView close;
    ViewGroup root;
    TextView tv,bookTitle,songTitle;
    static long mSongId;
    static String mSongTitle;
    static String mReference;
    private String getSomeString(){
        return "1.\n" +

                "Je me rappelle un jour, où dans le ciel je vis\n" +
                "Le livre dans lequel, tout péché est écrit\n" +
                "Je vis mon nom très clair\n" +
                "Je dis mon Dieu que faire?\n" +
                "En tombant à ses pieds, je réglai mon affaire.\n" +

                "\n" +
                "Choeur\n" +
                "\n" +
                "A l'instant, (bis)\n" +
                "Oui le vieux compte fut réglé a l'instant,\n" +
                "Je ne suis plus effrayé,\n" +
                "Ma dette est déjà payée\n" +
                "Et mon compte avec mon Juge est tout réglé.\n\n" +

                "2.\n" +
                "\n" +
                "Mon compte était très lourd\n" +
                "Et grandissant toujours\n" +
                "O j'étais égaré, pêchant plus chaque jour.\n" +
                "Mais quand mes yeux s'ouvrent\n" +
                "Et je vis mon état,\n" +
                "Je me jetais aux pieds du Sauveur a l'instant.\n" +

                "\n" +
                "3.\n" +
                "\n" +
                "Au jour du jugement, devant le trône blanc,\n" +
                "Quand tous les morts viendront\n" +
                "Les livres s'ouvriront\n" +
                "Et quand on cherchera, mon nom n'y sera pas\n" +
                "Car Jésus l'a écrit, au livre de la vie.\n" +
                "\n" +
                "4.\n" +
                "\n" +
                "O pêcheur viens à Lui\n" +
                "Mets-toi en règle aussi\n" +
                "Et fais-le aujourd'hui, car l'occasion s'enfuit\n" +
                "Il te pardonnera, si tu fais comme moi\n" +
                "En venant à ton Sauveur\n" +
                "Tu fais de Lui ton Roi, a l'instant.\n".trim().toUpperCase();
    }
    ChurchKitDb churchKitDd;

    private String listVerseToString(List<Verse> verses){
        StringBuilder verseString = new StringBuilder();
        if (verses != null){
            for (int i=0;i< verses.size();i++){
                verseString.append(verses.get(i).getNum());
                verseString.append("\n");
                verseString.append(verses.get(i).getVerse());
                verseString.append("\n");
            }
        }else {
            verseString.append("Error");
        }
        return verseString.toString();
    }



    @Override
    public void onResume() {
        super.onResume();
        ////set width and height of ListPartDialogFragment to 100 % to the width of screen
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width =(int) (Util.getScreenDisplayMetrics(getContext()).widthPixels * 1f);
        params.height = (int) (Util.getScreenDisplayMetrics(getContext()).heightPixels * 1f);
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

}
