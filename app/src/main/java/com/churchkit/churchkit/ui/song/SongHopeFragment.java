package com.churchkit.churchkit.ui.song;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.churchkit.churchkit.CKPreferences;
import com.churchkit.churchkit.R;
import com.churchkit.churchkit.database.entity.base.BaseInfo;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongBook;
import com.churchkit.churchkit.database.entity.song.SongInfo;
import com.churchkit.churchkit.database.entity.song.Verse;
import com.churchkit.churchkit.databinding.FragmentSongHopeBinding;
import com.churchkit.churchkit.modelview.song.SongBookViewModel;
import com.churchkit.churchkit.modelview.song.SongInfoViewModel;
import com.churchkit.churchkit.modelview.song.SongVerseViewModel;
import com.churchkit.churchkit.modelview.song.SongViewModel;
import com.churchkit.churchkit.ui.adapter.AutoCompleteTextViewAdapter;
import com.churchkit.churchkit.ui.adapter.InfoAdapterSpinner;
import com.churchkit.churchkit.ui.adapter.song.SongHopeAdapter;
import com.churchkit.churchkit.ui.util.GridSpacingItemDecoration;
import com.churchkit.churchkit.ui.util.OptionSearchView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

public class SongHopeFragment extends Fragment {


    private SongInfoViewModel songInfoViewModel;
    private boolean wasTouched = false;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentSongHopeBinding songBinding = FragmentSongHopeBinding.inflate(getLayoutInflater());
        View root = songBinding.getRoot();

        infoDownloadBible = songBinding.infoDownloadBible;

        songBookViewModel = ViewModelProvider.AndroidViewModelFactory.
                getInstance(getActivity().getApplication()).create(SongBookViewModel.class);

        songViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(SongViewModel.class);

        songVerseViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(SongVerseViewModel.class);

        songInfoViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(SongInfoViewModel.class);
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        autoCompleteTextView = songBinding.search;
        mRecyclerView = songBinding.recyclerview;
        homeAdapter = new SongHopeAdapter(getActivity().getApplicationContext(), getChildFragmentManager());
        gridLayoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerView.addItemDecoration(listGridItemDeco);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(homeAdapter);
        /*DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);*/
        ckPreferences = new CKPreferences(getContext());

        songBookViewModel.getAllSongBookBySongInfoId(ckPreferences.getSongName() ).observe(requireActivity(), songBooks -> {
            if (songBooks != null && songBooks.size() > 1) {
                homeAdapter.setSongBooks(songBooks);
            } else if (songBooks != null && songBooks.size() == 1) {
                if (getView() != null)
                    songViewModel.getAllSongWithVerseById(ckPreferences.getSongName(), songBooks.get(0).getId()).observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
                        @Override
                        public void onChanged(List<Song> songs) {
                            homeAdapter.setSongs(songs);
                        }
                    });
            }
        });




        infoDownloadBible.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireView());
            navController.getGraph().findNode(R.id.dataFragment).setLabel(getString(R.string.download_song));

            Bundle bundle = new Bundle();
            bundle.putString("FROM", "SONG");

            navController.navigate(R.id.action_homeFragment_to_dataFragment, bundle);

        });


        if (sharedPreferences.getInt(LIST_GRID, LIST) == GRID) {
            gridLayoutManager.setSpanCount(GRID);
        } else {
            gridLayoutManager.setSpanCount(sharedPreferences.getInt(LIST_GRID, LIST));
        }


        AutoCompleteTextViewAdapter autoCompleteAdapter = new AutoCompleteTextViewAdapter(getContext(), SongHopeFragment.class);


        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (ckPreferences.getSongTypeSearch() == CKPreferences.SONG_NORMAL_SEARCH_TYPE) {
                    songVerseViewModel.normalTextSearch(ckPreferences.getSongName(), "%" + s.toString() + "%").observe(getViewLifecycleOwner(), verses -> {
                        if (s.length() > 1) {
                            autoCompleteAdapter.setSongs(verses);
                            autoCompleteTextView.setAdapter(autoCompleteAdapter);
                        }
                    });
                } else {
                    songVerseViewModel.fullTextSearch(ckPreferences.getSongName(), "*" + s.toString() + "*").observe(getViewLifecycleOwner(), verses -> {
                        if (s.length() > 1) {
                            autoCompleteAdapter.setSongs(verses);
                            autoCompleteTextView.setAdapter(autoCompleteAdapter);
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        autoCompleteTextView.setOnTouchListener((v, event) -> {

            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (event.getRawX() >= (autoCompleteTextView.getRight() - autoCompleteTextView.getCompoundDrawables()[2].getBounds().width())) {

                    final OptionSearchView osv = new OptionSearchView(autoCompleteTextView, SongHopeFragment.class);
                    osv.showDialog();
                    return true;

                }
            }
            return false;
        });

        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            /*if (ckPreferences.getSongTypeSearch() == CKPreferences.SONG_NORMAL_SEARCH_TYPE) {
                Song song = (Song) autoCompleteAdapter.getItem(position);
                SongDialogFragment songDialogFragment = SongDialogFragment.newInstance(
                        song);
                songDialogFragment.show(getChildFragmentManager(), "");
            } else {*/
                Verse verse = (Verse) autoCompleteAdapter.getItem(position);
                songViewModel.getSongById(verse.getSongId()).observe(SongHopeFragment.this.getViewLifecycleOwner(), song -> {
                    SongDialogFragment songDialogFragment = SongDialogFragment.newInstance(
                            song);
                    songDialogFragment.show(getChildFragmentManager(), "");
                });
            //}
        });

        songInfoId = ckPreferences.getSongName();
        /*allSongBooksViewModel = songBookViewModel.getAllSongBookBySongInfoId( songInfoId );
        allSongBooksViewModel.observe(requireActivity(), songBooks -> {
            if (songBooks != null && songBooks.size() > 1) {
                homeAdapter.setSongBooks(songBooks);
            }else if (songBooks != null && songBooks.size() == 1 ){
                songViewModel.getAllSongWithVerseById(ckPreferences.getSongName(), songBooks.get(0).getId() ).observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
                    @Override
                    public void onChanged(List<Song> songs) {
                        homeAdapter.setSongs( songs );
                    }
                });
            }
        });
*/

        onCreateMenu();


        return root;
    }

    private int getTypeView() {
        return sharedPreferences.getInt(LIST_GRID, LIST);
    }


    private void onCreateMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                //menu.clear();
                menuInflater.inflate(R.menu.menu_song_hope, menu);

                Switch switchMenuItem = menu.findItem(R.id.app_bar_switch).getActionView().findViewById(R.id.switch1);
                switchMenuItem.setVisibility(View.GONE);
                Spinner spinner = (Spinner) menu.findItem(R.id.app_bar_spinner).getActionView();
                List<BaseInfo> baseInfos = new ArrayList<>();


                spinner.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        wasTouched = true;
                        return false;
                    }
                });


                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        BaseInfo baseInfo = ((InfoAdapterSpinner) parent.getAdapter()).getBaseInfoList().get(position);

                        ckPreferences.setSongName(baseInfo.getId());
                        baseInfo.setForceUpdate(!baseInfo.isForceUpdate());
                        isForcingUpdate = true;
                        songInfoViewModel.insert((SongInfo) baseInfo);


                        //allSongBooksViewModel = songBookViewModel.getAllSongBookBySongInfoId(baseInfo.getId() );
                        if (wasTouched) {
                            wasTouched = false;
                            songBookViewModel.getAllSongBookBySongInfoId(baseInfo.getId()).observe(requireActivity(), songBooks -> {
                                if (songBooks != null && songBooks.size() > 1) {
                                    homeAdapter.setSongBooks(songBooks);
                                } else if (songBooks != null && songBooks.size() == 1) {
                                    if (getView() != null)
                                        songViewModel.getAllSongWithVerseById(ckPreferences.getSongName(), songBooks.get(0).getId()).observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
                                            @Override
                                            public void onChanged(List<Song> songs) {
                                                homeAdapter.setSongs(songs);
                                            }
                                        });
                                }
                            });
                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {


                    }
                });


                songInfoViewModel.getAllSongInfo().observe(getViewLifecycleOwner(), songInfos -> {


                    if (!isForcingUpdate) {
                        for (int i = 0; i < songInfos.size(); i++) {
                            if (songInfos.get(i).isDownloaded())
                                baseInfos.add(songInfos.get(i));
                        }

                        infoDownloadBible.setVisibility(baseInfos.size() == 0 ? View.VISIBLE : View.GONE);
                        spinner.setAdapter(new InfoAdapterSpinner(getContext(), baseInfos));

                    } else {
                        isForcingUpdate = false;
                    }

                });


            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private boolean existedSongDownload(List<SongInfo> songInfos) {
        for (int i = 0; i < songInfos.size(); i++) {
            if (songInfos.get(i).isDownloaded())
                return true;
        }
        return false;
    }


    @Override
    public void onResume() {


        autoCompleteTextView.setHint(ckPreferences.isSongTypeSearchIsFullTextSearch() ?
                getString(R.string.full_text_search) : getString(R.string.normal_search));
        super.onResume();
    }


    private final int LIST = 1;
    private final int GRID = 2;
    private final String LIST_GRID = "LIST_GRID";
    CKPreferences ckPreferences;
    TextView infoDownloadBible;
    MaterialAutoCompleteTextView autoCompleteTextView;
    RecyclerView mRecyclerView;
    SongHopeAdapter homeAdapter;
    SharedPreferences sharedPreferences;
    GridLayoutManager gridLayoutManager;
    private boolean isForcingUpdate = false;
    private SongViewModel songViewModel;
    private SongVerseViewModel songVerseViewModel;
    private SongBookViewModel songBookViewModel;
    private LiveData<List<SongBook>> allSongBooksViewModel;
    private String songInfoId;

    GridSpacingItemDecoration listGridItemDeco = new GridSpacingItemDecoration(2, 32, false);


}