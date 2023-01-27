package com.churchkit.churchkit.ui.song;


import android.graphics.Rect;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;


import com.churchkit.churchkit.R;
import com.churchkit.churchkit.database.ChurchKitDb;
import com.churchkit.churchkit.database.entity.Verse;
import com.churchkit.churchkit.ui.util.Util;

import java.util.Collections;
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
        chorus = root.findViewById(R.id.chorus);
        scrollView = root.findViewById(R.id.scrollView);

        chorus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToChorus();
            }
        });


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
                tv.setText( listVerseToString(verses) );

            }
        });

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                setChorusButtonVisibility();
            }
        });





        return root;
    }

    private  void setChorusButtonVisibility(){
        if (isPhraseVisible("-Chorus-")){
            chorus.setVisibility(View.GONE);
        }
        else
            chorus.setVisibility(View.VISIBLE);
    }

   // ImageView close;
    ViewGroup root;
    TextView tv,bookTitle,songTitle,chorus;
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
    ScrollView scrollView;


    private String listVerseToString(List<Verse> verses){
        StringBuilder verseString = new StringBuilder();
        Verse verse=null;

        if (verses != null){

            if (verses.size()!=0 && verses.get(0).getNum()<0)
                Collections.swap(verses,0,1);

            for (int i=0;i< verses.size();i++){

                verse = verses.get(i);
                verseString.append( verse.getNum()<0?"-Chorus-":verse.getNum() );
                verseString.append("\n");
                verseString.append(verse.getVerse());
                verseString.append("\n");
            }


        }else {
            verseString.append("Error");
        }

        return verseString.toString();
    }

    private void scrollToChorus(){
        int index = tv.getText().toString().indexOf("-Chorus-");
        Layout layout = tv.getLayout();
        int line = layout.getLineForOffset(index);
        int y = layout.getLineTop(line);
        scrollView.smoothScrollTo(0,y);

    }
    public void scrollToSpecificPhrase(final ScrollView scrollView, final TextView textView, final String phrase) {
        textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int index = textView.getText().toString().indexOf(phrase);
                if (index != -1) {
                    Layout layout = textView.getLayout();
                    int line = layout.getLineForOffset(index);
                    int y = layout.getLineTop(line);
                    int y2 = layout.getLineBottom(line);
                    Rect rect = new Rect(0, y, textView.getWidth(), y2);
                    Rect visibleRect = new Rect();
                    textView.getLocalVisibleRect(visibleRect);
                    if (!Rect.intersects(rect, visibleRect)) {
                        scrollView.scrollTo(0, y);
                    }
                    textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }


    public boolean isPhraseVisible(@NonNull final String phrase) {
        int index = tv.getText().toString().indexOf(phrase);
        if (index == -1) {
            return false;
        }
        Layout layout = tv.getLayout();
        int line = layout.getLineForOffset(index);
        int y = layout.getLineTop(line);
        int y2 = layout.getLineBottom(line);
        Rect rect = new Rect(0, y, tv.getWidth(), y2);
        Rect visibleRect = new Rect();
        tv.getLocalVisibleRect(visibleRect);
        return Rect.intersects(rect, visibleRect);
    }



    @Override
    public void onResume() {
        super.onResume();
        setChorusButtonVisibility();
        ////set width and height of ListPartDialogFragment to 100 % to the width of screen
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width =(int) (Util.getScreenDisplayMetrics(getContext()).widthPixels * 1f);
        params.height = (int) (Util.getScreenDisplayMetrics(getContext()).heightPixels * 1f);
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

}
