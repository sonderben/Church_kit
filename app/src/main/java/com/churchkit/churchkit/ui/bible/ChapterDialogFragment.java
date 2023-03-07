package com.churchkit.churchkit.ui.bible;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;

import com.churchkit.churchkit.R;
import com.churchkit.churchkit.database.ChurchKitDb;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.bible.BibleChapterFavorite;
import com.churchkit.churchkit.database.entity.bible.BibleChapterHistory;
import com.churchkit.churchkit.database.entity.bible.BibleVerse;
import com.churchkit.churchkit.database.entity.song.SongBook;
import com.churchkit.churchkit.database.entity.song.SongFavorite;
import com.churchkit.churchkit.database.entity.song.Verse;
import com.churchkit.churchkit.ui.util.Util;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class ChapterDialogFragment extends DialogFragment {
    public ChapterDialogFragment(){

    }
    public static ChapterDialogFragment newInstance(String id,String reference){
        mId = id;
        mReference = reference;
        return new ChapterDialogFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ConstraintLayout root = (ConstraintLayout) inflater.inflate(R.layout.fragment_list_chapter,container,false);


        //closeButton = root.findViewById(R.id.close);
        //closeButton.setOnClickListener(x->this.dismissNow());
        versets = root.findViewById(R.id.text);
        bookReference = root.findViewById(R.id.book_name);
        chapTitle = root.findViewById(R.id.chap_);
        root.findViewById(R.id.chorus).setVisibility(View.GONE);
        versets.setText(getSomeString());
        bookReference.setText(mReference);
        headerLayout = root.findViewById(R.id.header);
        chapTitle.setText(mChapterhapter);
        fab = root.findViewById(R.id.fab_clos);
        fab.setOnClickListener(x->this.dismiss());
        endingFavoriteImageView = root.findViewById(R.id.favorite);//favorite_anim
        startingImageView = root.findViewById(R.id.favorite_anim);//favorite_anim

        setChapterHistory();

        db.bibleVerseDao().getAllVerse(mId).observe(requireActivity(), new Observer<List<BibleVerse>>() {
            @Override
            public void onChanged(List<BibleVerse> bibleVerseList) {
                versets.setText( listVerseToString(bibleVerseList) );
                setVerseTitle( bibleVerseList);
            }
        });

        endingFavoriteImageView.setOnClickListener(v -> {
            BibleChapterFavorite songFavorite= db.bibleChapterFavoriteDao().isExisted(mId);
            endingFavoriteImageView.setEnabled(false);

            if (songFavorite != null)
                db.bibleChapterFavoriteDao().delete(songFavorite);
            else{

                db.bibleChapterFavoriteDao().insert(
                        new BibleChapterFavorite(mId, Calendar.getInstance().getTimeInMillis(), mReference)
                );
            }

        });


        db.bibleChapterFavoriteDao().existed(mId).observe(getViewLifecycleOwner(), songFavorite -> {

            endingFavoriteImageView.setEnabled(true);
            Drawable drawable = endingFavoriteImageView.getDrawable();
            if (songFavorite != null){

                if (drawable != null) {
                    drawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                    endingFavoriteImageView.setImageDrawable(drawable);
                }
            }
            else{
                int color = getResources().getColor(R.color.white);
                if (drawable != null) {
                    drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                    endingFavoriteImageView.setImageDrawable(drawable);
                }
            }

        });






        /*float endX = favorite.getX();
        float endY = favorite.getY();*/

        /*int[] endImageViewCoordinates = new int[2];
        favorite.getLocationOnScreen(endImageViewCoordinates);

        float endX = endImageViewCoordinates[0];
        float endY = endImageViewCoordinates[1];*/

        final int DOUBLE_CLICK_TIME_DELTA = 300; //milliseconds

        versets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*int[] endImageViewCoordinates = new int[2];
                favorite.getLocationOnScreen(endImageViewCoordinates);

                float endX = endImageViewCoordinates[0];
                float endY = endImageViewCoordinates[1];

                ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(favoriteAnim, "scaleX", 0f, 10.5f);
                ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(favoriteAnim, "scaleY", 0f, 10.5f);

                ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(favoriteAnim, "translationX",  0,200);
                ObjectAnimator translationYAnimator = ObjectAnimator.ofFloat(favoriteAnim, "translationY",  0,2f);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(scaleXAnimator, scaleYAnimator, translationXAnimator, translationYAnimator);
                animatorSet.setDuration(1000);
                animatorSet.start();*/



               /* ImageView startingImageView = findViewById(R.id.starting_image);
                startingImageView.setImageResource(R.drawable.starting_image);*/




                long clickTime = System.currentTimeMillis();
                if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){


                    ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 2.0f,
                            1.0f, 2.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    scaleAnimation.setDuration(1000);

                    TranslateAnimation translateAnimation = new TranslateAnimation(0,
                            380, 0, -950);
                    translateAnimation.setDuration(1000);

                    AnimationSet animationSet = new AnimationSet(false);
                    animationSet.addAnimation(scaleAnimation);
                    animationSet.addAnimation(translateAnimation);

                    startingImageView.setAnimation(animationSet);

                    endingFavoriteImageView.setVisibility(View.INVISIBLE);

                    animationSet.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            startingImageView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            startingImageView.setVisibility(View.INVISIBLE);
                            endingFavoriteImageView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });

                    startingImageView.startAnimation(animationSet);
                }
                lastClickTime = clickTime;




            }
        });


        return root;
    }

    private void setChapterHistory() {
        db.bibleChapterHistoryDao().insert(
                new BibleChapterHistory(mId, Calendar.getInstance().getTimeInMillis(), mReference)
        );

    }

    @Override
    public void onResume() {
        super.onResume();
        ////set width and height of ListChapterDialogFragment to 100 % to the width of screen
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width =(int) (Util.getScreenDisplayMetrics(getContext()).widthPixels * 1f);
        params.height = (int) (Util.getScreenDisplayMetrics(getContext()).heightPixels * 1f);
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    private void setVerseTitle(List<BibleVerse> bibleVerseList){

            chapTitle.setText("Verse: 1 to "+bibleVerseList.size());


    }

    private String listVerseToString(List<BibleVerse> bibleVerseList){
        StringBuilder verseString = new StringBuilder();
        BibleChapter bibleChap=null;

        if (bibleVerseList != null){
            for (int i=0;i< bibleVerseList.size();i++){

                verseString.append(positionToSupIndex(bibleVerseList.get(i).getPosition()));
                verseString.append(bibleVerseList.get(i).getVerseText()+" ");

                //verseString.append("\n");

            }
        }else {
            verseString.append("Error");
        }
        return verseString.toString();
    }

    private Spanned getSomeString(){

        String a = "<sup> <b>1</b> </sup>En el principio creó Dios los cielos y la tierra. <sup> <b>2</b> </sup>Y la tierra estaba desordenada y vacía, y las tinieblas estaban sobre la faz del abismo, y el Espíritu de Dios se movía sobre la faz de las aguas.\n" +
                "<sup>3</sup>Y dijo Dios: Sea la luz; y fue la luz. 4Y vio Dios que la luz era buena; y separó Dios la luz de las tinieblas. 5Y llamó Dios a la luz Día, y a las tinieblas llamó Noche. Y fue la tarde y la mañana un día.\n" +
                "<sup>6</sup>Luego dijo Dios: Haya expansión en medio de las aguas, y separe las aguas de las aguas. <sup>7</sup>E hizo Dios la expansión, y separó las aguas que estaban debajo de la expansión, de las aguas que estaban sobre la expansión. Y fue así. 8Y llamó Dios a la expansión Cielos. Y fue la tarde y la mañana el día segundo.\n" +
                "<sup>9</sup>Dijo también Dios: Júntense las aguas que están debajo de los cielos en un lugar, y descúbrase lo seco. Y fue así. 10Y llamó Dios a lo seco Tierra, y a la reunión de las aguas llamó Mares. Y vio Dios que era bueno. 11Después dijo Dios: Produzca la tierra hierba verde, hierba que dé semilla; árbol de fruto que dé fruto según su género, que su semilla esté en él, sobre la tierra. Y fue así. 12Produjo, pues, la tierra hierba verde, hierba que da semilla según su naturaleza, y árbol que da fruto, cuya semilla está en él, según su género. Y vio Dios que era bueno. 13Y fue la tarde y la mañana el día tercero.\n" +
                "14Dijo luego Dios: Haya lumbreras en la expansión de los cielos para separar el día de la noche; y sirvan de señales para las estaciones, para días y años, 15y sean por lumbreras en la expansión de los cielos para alumbrar sobre la tierra. Y fue así. 16E hizo Dios las dos grandes lumbreras; la lumbrera mayor para que señorease en el día, y la lumbrera menor para que señorease en la noche; hizo también las estrellas. 17Y las puso Dios en la expansión de los cielos para alumbrar sobre la tierra, 18y para señorear en el día y en la noche, y para separar la luz de las tinieblas. Y vio Dios que era bueno. 19Y fue la tarde y la mañana el día cuarto. 20Dijo Dios: Produzcan las aguas seres vivientes, y aves que vuelen sobre la tierra, en la abierta expansión de los cielos. 21Y creó Dios los grandes monstruos marinos, y todo ser viviente que se mueve, que las aguas produjeron según su género, y toda ave alada según su especie. Y vio Dios que era bueno. 22Y Dios los bendijo, diciendo: Fructificad y multiplicaos, y llenad las aguas en los mares, y multiplíquense las aves en la tierra. 23Y fue la tarde y la mañana el día quinto.\n" +
                "24Luego dijo Dios: Produzca la tierra seres vivientes según su género, bestias y serpientes y animales de la tierra según su especie. Y fue así. 25E hizo Dios animales de la tierra según su género, y ganado según su género, y todo animal que se arrastra sobre la tierra según su especie. Y vio Dios que era bueno.\n" +
                "26Entonces dijo Dios: Hagamos al hombre a nuestra imagen, conforme a nuestra semejanza; y señoree en los peces del mar, en las aves de los cielos, en las bestias, en toda la tierra, y en todo animal que se arrastra sobre la tierra. 27Y creó Dios al hombre a su imagen, a imagen de Dios lo creó; varón y hembra los creó. 28Y los bendijo Dios, y les dijo: Fructificad y multiplicaos; llenad la tierra, y sojuzgadla, y señoread en los peces del mar, en las aves de los cielos, y en todas las bestias que se mueven sobre la tierra.\n" +
                "29Y dijo Dios: He aquí que os he dado toda planta que da semilla, que está sobre toda la tierra, y todo árbol en que hay fruto y que da semilla; os serán para comer. 30Y a toda bestia de la tierra, y a todas las aves de los cielos, y a todo lo que se arrastra sobre la tierra, en que hay vida, toda planta verde les será para comer. Y fue así. 31Y vio Dios todo lo que había hecho, y he aquí que era bueno en gran manera. Y fue la tarde y la mañana el día sexto.".trim().toUpperCase();
    return Html.fromHtml(a);
    }
    long lastClickTime = 0;
    ImageView closeButton, endingFavoriteImageView, startingImageView;
    TextView versets,bookReference,chapTitle;
    static String mId;
    static String mReference, mChapterhapter;
    static int mVerseAmount;
    FloatingActionButton fab;
    ConstraintLayout headerLayout;

    public  Point getLocationOnScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        Toast.makeText(getContext(),"endingFavoriteImageView=> x: "+location[0]+" y: "+location[1],Toast.LENGTH_LONG).show();
        return new Point(location[0], location[1]);
    }

    ChurchKitDb db= ChurchKitDb.getInstance(getContext());
    private String toUnicode( int position){
        final String zero = "\u2070";
        final String one = "\u00B9";
        final String two = "\u00B2";
        final String twee = "\u00B3";
        final String four = "\u2074";
        final String five = "\u2075";
        final String six = "\u2076";
        final String seven = "\u2077";
        final String eight = "\u2078";
        final String nine = "\u2079";
        switch (position){
            case 0: return zero;
            case 1: return one;
            case 2: return two;
            case 3: return twee;
            case 4: return four;
            case 5: return five;
            case 6: return six;
            case 7: return seven;
            case 8: return eight;
            case 9: return nine;
            default:
                return "";
        }
    }
    private String positionToSupIndex (int position){
        int pos = position;
        StringBuilder stri = new StringBuilder();
        do{
            stri.insert(    0,toUnicode(pos%10));

            pos = pos/10;

        }while(pos>0);

        return stri.toString();
    }

}
