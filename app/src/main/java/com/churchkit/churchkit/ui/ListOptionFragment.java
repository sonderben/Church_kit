package com.churchkit.churchkit.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.churchkit.churchkit.Model.Part;
import com.churchkit.churchkit.R;
import com.churchkit.churchkit.adapter.PartAdapter;

import java.util.ArrayList;
import java.util.List;


public class ListOptionFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public ListOptionFragment() {
        // Required empty public constructor
    }


    public static ListOptionFragment newInstance(String param1, String param2) {
        ListOptionFragment fragment = new ListOptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_option, container, false);
        recyclerView = root.findViewById(R.id.recyclerview);
        partAdapter = new PartAdapter();

        parts =new ArrayList<>();
        parts.add(new Part("Reveillons nous","RN",getResources().getColor(R.color.purple_200) ));
        parts.add(new Part("Chant d'esperance","CE",getResources().getColor(R.color.teal_200)));
        parts.add(new Part("La voix du reveil creole","VC",getResources().getColor(R.color.teal_700)));
        parts.add(new Part("Haïti chante avec radio lumière","HC",getResources().getColor(R.color.purple_700)));


        partAdapter.setPart(parts);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2,RecyclerView.VERTICAL,false));
        //recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(partAdapter);

        return root;
    }
    RecyclerView recyclerView;
    PartAdapter partAdapter;
    List<Part> parts;
}