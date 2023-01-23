package com.churchkit.churchkit.ui.song;


import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.churchkit.churchkit.Model.Song;
import com.churchkit.churchkit.R;
import com.churchkit.churchkit.ui.util.Util;

import java.util.ArrayList;
import java.util.List;


public class SongDialogFragment extends DialogFragment {
    public static SongDialogFragment newInstance(){
        return new SongDialogFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
         View root =  inflater.inflate(R.layout.fragment_list_chapter,container,false);



         tv  = root.findViewById(R.id.text);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setText(getSomeString());
        close = root.findViewById(R.id.close);
        bookTitle = root.findViewById(R.id.book_name);
        songTitle = root.findViewById(R.id.chap_);
        bookTitle.setText("012 Reveillons nous");
        songTitle.setText("Il est un nom.");

        close.setOnClickListener(x->this.dismiss());





        return root;
    }
    ImageView close;
    TextView tv,bookTitle,songTitle;
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
