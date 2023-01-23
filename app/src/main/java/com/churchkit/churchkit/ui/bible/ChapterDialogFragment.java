package com.churchkit.churchkit.ui.bible;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.churchkit.churchkit.R;
import com.churchkit.churchkit.ui.util.Util;

public class ChapterDialogFragment extends DialogFragment {
    public ChapterDialogFragment(){}
    public static ChapterDialogFragment newInstance(){
        return new ChapterDialogFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ConstraintLayout root = (ConstraintLayout) inflater.inflate(R.layout.fragment_list_chapter,container,false);


        closeButton = root.findViewById(R.id.close);
        closeButton.setOnClickListener(x->this.dismissNow());
        tv = root.findViewById(R.id.text);
        tv.setText(getSomeString());

        return root;
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

    ImageView closeButton;
    TextView tv;

}
