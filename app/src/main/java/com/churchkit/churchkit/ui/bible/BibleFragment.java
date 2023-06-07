package com.churchkit.churchkit.ui.bible;

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
import com.churchkit.churchkit.database.entity.bible.BibleBook;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.bible.BibleInfo;
import com.churchkit.churchkit.database.entity.bible.BibleVerse;
import com.churchkit.churchkit.databinding.FragmentBibleBinding;
import com.churchkit.churchkit.modelview.bible.BibleBookViewModel;
import com.churchkit.churchkit.modelview.bible.BibleChapterViewModel;
import com.churchkit.churchkit.modelview.bible.BibleInfoViewModel;
import com.churchkit.churchkit.modelview.bible.BibleVerseViewModel;
import com.churchkit.churchkit.ui.adapter.AutoCompleteTextViewAdapter;
import com.churchkit.churchkit.ui.adapter.InfoAdapterSpinner;
import com.churchkit.churchkit.ui.adapter.bible.BibleAdapter;
import com.churchkit.churchkit.ui.util.GridSpacingIDeco;
import com.churchkit.churchkit.ui.util.OptionSearchView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

public class BibleFragment extends Fragment {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        bookmarkBinding = FragmentBibleBinding.inflate(getLayoutInflater());
        mRecyclerView = bookmarkBinding.recyclerview;
        autoCompleteTextView = bookmarkBinding.search;

        ckPreferences = new CKPreferences(getContext());
        bibleChapterViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().
                getApplication()).create(BibleChapterViewModel.class);
        bibleBookViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity()
                .getApplication()).create(BibleBookViewModel.class);
        bibleVerseViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().
                getApplication()).create(BibleVerseViewModel.class);

        bibleInfoViewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication()).create(BibleInfoViewModel.class);

        infoDownloadSong = bookmarkBinding.infoDownloadBible;


        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        infoDownloadSong.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireView());
            navController.getGraph().findNode(R.id.dataFragment).setLabel("Download a Bible");
            Bundle bundle = new Bundle();
            bundle.putString("FROM", "BIBLE");
            navController.navigate(R.id.action_bookmarkFragment_to_dataFragment, bundle);
        });


        bibleInfoId = ckPreferences.getBibleName();

        mAdapter = new BibleAdapter(getActivity().getSupportFragmentManager());
        bibleBooksLiveData = bibleBookViewModel.getAllBibleBook(bibleInfoId);

        bibleBooksLiveData.observe(requireActivity(), bibleBooks -> {
            mAdapter.config(sharedPreferences.getBoolean(IS_GROUP_BY_TESTAMENT, false) ? 0 : 1, bibleBooks);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        });


        onBibleInfoChange(ckPreferences.getBibleName());


        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mRecyclerView.addItemDecoration(new GridSpacingIDeco(32));


        autoCompleteAdapter = new AutoCompleteTextViewAdapter(getContext(), BibleFragment.class);
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            onItemAutoCompleteClick(position);
        });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (ckPreferences.getBibleTypeSearch() == CKPreferences.BIBLE_NORMAL_SEARCH_TYPE) {
                    searchBibleVerseLiveData = bibleVerseViewModel.normalSearch(ckPreferences.getBibleName(), "%" + s.toString() + "%");
                    searchBibleVerseLiveData.observe(getViewLifecycleOwner(), bibleVerses -> {
                        autoCompleteAdapter.setSongs(bibleVerses);
                        autoCompleteTextView.setAdapter(autoCompleteAdapter);
                    });
                } else {
                    searchBibleVerseLiveData = bibleVerseViewModel.fullTextSearch(ckPreferences.getBibleName(), "*" + s.toString() + "*");
                    searchBibleVerseLiveData.observe(getViewLifecycleOwner(), bibleVerses -> {
                        autoCompleteAdapter.setSongs(bibleVerses);
                        autoCompleteTextView.setAdapter(autoCompleteAdapter);
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        autoCompleteTextView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (autoCompleteTextView.getRight() - autoCompleteTextView.getCompoundDrawables()[2].getBounds().width())) {
                    if (MotionEvent.ACTION_UP == event.getAction()) {
                        final OptionSearchView osv = new OptionSearchView(autoCompleteTextView, BibleFragment.class);
                        osv.showDialog();
                        return true;
                    }
                }
            }
            return false;
        });
        onCreateMenu();


        return bookmarkBinding.getRoot();


    }

    private void onBibleInfoChange(String bibleInfoId) {
        amountBookOldTestamentLiveData = bibleBookViewModel.getAmountBookOldTestament(bibleInfoId);
        amountBookOldTestamentLiveData.observe(getViewLifecycleOwner(), integer -> {
            mAdapter.setAmountOldTestament(integer);
            mAdapter.notifyItemChanged(0);
        });

        amountBookNewTestamentLiveData = bibleBookViewModel.getAmountBookNewTestament(bibleInfoId);
        amountBookNewTestamentLiveData.observe(getViewLifecycleOwner(), integer -> {
            mAdapter.setAmountNewTestament(integer);
            mAdapter.notifyItemChanged(1);
        });
    }

    private void onItemAutoCompleteClick(int position) {

            BibleVerse verse = (BibleVerse) autoCompleteAdapter.getItem(position);
            bibleChapterByVerseIdLiveData = bibleChapterViewModel.getChapterByVerseId(verse.getBibleChapterId());
            bibleChapterByVerseIdLiveData.observe(getViewLifecycleOwner(), bibleChapter -> {
                ChapterDialogFragment dialogFragment = ChapterDialogFragment.newInstance(bibleChapter);
                dialogFragment.show(getChildFragmentManager(), "");
            });

    }

    private void onCreateMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {


            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                //menu.clear();
                menuInflater.inflate(R.menu.menu_song_hope, menu);

                Switch switchMenuItem = menu.findItem(R.id.app_bar_switch).getActionView().findViewById(R.id.switch1);
                switchMenuItem.setChecked(sharedPreferences.getBoolean(IS_GROUP_BY_TESTAMENT, true));
                switchMenuItem.setOnCheckedChangeListener((compoundButton, isGroupedByLang) -> {
                    mAdapter.setTypeView(isGroupedByLang ? 0 : 1);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(IS_GROUP_BY_TESTAMENT, isGroupedByLang);
                    editor.apply();
                });

                spinnerBibleInfo = (Spinner) menu.findItem(R.id.app_bar_spinner).getActionView();
                List<BaseInfo> baseInfos = new ArrayList<>();
                bibleInfoViewModel.getAllBibleInfo().observe(getViewLifecycleOwner(), new Observer<List<BibleInfo>>() {
                    @Override
                    public void onChanged(List<BibleInfo> bibleInfos) {

                        if (!isForcingUpdate){
                            for (int i = 0; i < bibleInfos.size(); i++) {
                                if (bibleInfos.get(i).isDownloaded())
                                    baseInfos.add( bibleInfos.get(i) );
                            }


                            infoDownloadSong.setVisibility( baseInfos.size() == 0?View.VISIBLE:View.GONE );
                            spinnerBibleInfo.setAdapter(new InfoAdapterSpinner(getContext(), baseInfos));
                        }else{
                            isForcingUpdate = false;
                        }
                    }
                });



                spinnerBibleInfo.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        wasTouch = true;
                        return false;
                    }
                });

                spinnerBibleInfo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        BaseInfo baseInfo = ((InfoAdapterSpinner) parent.getAdapter()).getBaseInfoList().get(position);

                        ckPreferences.setBibleName(baseInfo.getId() );
                        baseInfo.setForceUpdate( !baseInfo.isForceUpdate() );
                        isForcingUpdate = true;
                        bibleInfoViewModel.insert((BibleInfo) baseInfo);



                        if (wasTouch){
                             wasTouch = false;

                            bibleBookViewModel.getAllBibleBook(baseInfo.getId()).observe(getViewLifecycleOwner(), new Observer<List<BibleBook>>() {
                                @Override
                                public void onChanged(List<BibleBook> bibleBooks) {
                                    mAdapter.config(sharedPreferences.getBoolean(IS_GROUP_BY_TESTAMENT, false) ? 0 : 1, bibleBooks);
                                    mRecyclerView.setAdapter(mAdapter);
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {


                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private boolean existedBibleDownload(List<BibleInfo> bibleInfos) {
        for (int i = 0; i < bibleInfos.size(); i++) {
            if (bibleInfos.get(i).isDownloaded())
                return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        autoCompleteTextView.setHint(ckPreferences.isBibleTypeSearchIsFullTextSearch() ?
                getString(R.string.full_text_search) : getString(R.string.normal_search));

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        isForcingUpdate = false;
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private RecyclerView mRecyclerView;
    FragmentBibleBinding bookmarkBinding;
    private BibleAdapter mAdapter;
    private TextView infoDownloadSong;
    BibleInfoViewModel bibleInfoViewModel;
    private MaterialAutoCompleteTextView autoCompleteTextView;
    boolean wasTouch = false;
    private final String IS_GROUP_BY_TESTAMENT = "IS_GROUP_BY_TESTAMENT";
    private SharedPreferences sharedPreferences;
    private BibleChapterViewModel bibleChapterViewModel;
    private AutoCompleteTextViewAdapter autoCompleteAdapter;
    private CKPreferences ckPreferences;
    private BibleBookViewModel bibleBookViewModel;
    private BibleVerseViewModel bibleVerseViewModel;

    private LiveData<List<BibleBook>> bibleBooksLiveData;
    private LiveData<Integer> amountBookOldTestamentLiveData;
    private LiveData<Integer> amountBookNewTestamentLiveData;
    private LiveData<List<BibleChapter>> searchBibleChapterLiveData;
    private LiveData<List<BibleVerse>> searchBibleVerseLiveData;
    private LiveData<BibleChapter> bibleChapterByVerseIdLiveData;
    private String bibleInfoId;
    private boolean isForcingUpdate = false;

    Spinner spinnerBibleInfo;

    @Override
    public void onDestroy() {
        //removeLiveDataObservers();
        bookmarkBinding = null;

        super.onDestroy();

    }

    @Override
    public void onStop() {
        removeLiveDataObservers();
        super.onStop();
    }

    private void removeLiveDataObservers() {
        if (bibleBooksLiveData != null && bibleBooksLiveData.hasObservers()) {
            bibleBooksLiveData.removeObservers(getViewLifecycleOwner());
            bibleBooksLiveData = null;
        }
        if (amountBookOldTestamentLiveData != null && amountBookOldTestamentLiveData.hasObservers()) {
            amountBookOldTestamentLiveData.removeObservers(getViewLifecycleOwner());
            amountBookOldTestamentLiveData = null;
        }
        if (amountBookNewTestamentLiveData != null && amountBookNewTestamentLiveData.hasObservers()) {
            amountBookNewTestamentLiveData.removeObservers(getViewLifecycleOwner());
            amountBookOldTestamentLiveData = null;
        }
        if (searchBibleChapterLiveData != null && searchBibleChapterLiveData.hasObservers()) {
            searchBibleChapterLiveData.removeObservers(getViewLifecycleOwner());
            searchBibleChapterLiveData = null;
        }
        if (searchBibleVerseLiveData != null && searchBibleVerseLiveData.hasObservers()) {
            searchBibleVerseLiveData.removeObservers(getViewLifecycleOwner());
            searchBibleVerseLiveData = null;
        }
        if (bibleChapterByVerseIdLiveData != null && bibleChapterByVerseIdLiveData.hasObservers()) {
            bibleChapterByVerseIdLiveData.removeObservers(getViewLifecycleOwner());
            bibleChapterByVerseIdLiveData = null;
        }

    }


}