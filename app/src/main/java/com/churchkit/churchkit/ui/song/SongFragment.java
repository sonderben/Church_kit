package com.churchkit.churchkit.ui.song;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.churchkit.churchkit.R;
import com.churchkit.churchkit.databinding.FragmentSongBinding;


public class SongFragment extends Fragment {


    public SongFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSongBinding songBinding = FragmentSongBinding.inflate( getLayoutInflater() );

        TextView song = songBinding.song;
        song.setText(getSomeString());
        onCreateMenu();

        return  songBinding.getRoot();
    }

    private String getSomeString(){
        return "1.\n" +
                "\n" +
                "Je me rappelle un jour, où dans le ciel je vis\n" +
                "\n" +
                "Le livre dans lequel, tout péché est écrit\n" +
                "\n" +
                "Je vis mon nom très clair\n" +
                "\n" +
                "Je dis mon Dieu que faire?\n" +
                "\n" +
                "En tombant à ses pieds, je réglai mon affaire.\n" +
                "\n" +
                "\n" +
                "Choeur\n" +
                "\n" +
                "A l'instant, (bis)\n" +
                "\n" +
                "Oui le vieux compte fut réglé a l'instant,\n" +
                "\n" +
                "Je ne suis plus effrayé,\n" +
                "\n" +
                "Ma dette est déjà payée\n" +
                "\n" +
                "Et mon compte avec mon Juge est tout réglé.\n" +
                "\n" +
                "\n" +
                "2.\n" +
                "\n" +
                "Mon compte était très lourd\n" +
                "\n" +
                "Et grandissant toujours\n" +
                "\n" +
                "O j'étais égaré, pêchant plus chaque jour.\n" +
                "\n" +
                "Mais quand mes yeux s'ouvrent\n" +
                "\n" +
                "Et je vis mon état,\n" +
                "\n" +
                "Je me jetais aux pieds du Sauveur a l'instant.\n" +
                "\n" +
                "\n" +
                "3.\n" +
                "\n" +
                "Au jour du jugement, devant le trône blanc,\n" +
                "\n" +
                "Quand tous les morts viendront\n" +
                "\n" +
                "Les livres s'ouvriront\n" +
                "\n" +
                "Et quand on cherchera, mon nom n'y sera pas\n" +
                "\n" +
                "Car Jésus l'a écrit, au livre de la vie.\n" +
                "\n" +
                "\n" +
                "4.\n" +
                "\n" +
                "O pêcheur viens à Lui\n" +
                "\n" +
                "Mets-toi en règle aussi\n" +
                "\n" +
                "Et fais-le aujourd'hui, car l'occasion s'enfuit\n" +
                "\n" +
                "Il te pardonnera, si tu fais comme moi\n" +
                "\n" +
                "En venant à ton Sauveur\n" +
                "\n" +
                "Tu fais de Lui ton Roi, a l'instant.\n".trim();
    }
    private void onCreateMenu(){
        getActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                MenuItem settingsItem = menu.add("Settings");
                settingsItem.setIcon(R.drawable.donate_24);
                settingsItem.setTitle("Donate");
                settingsItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        },getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

}