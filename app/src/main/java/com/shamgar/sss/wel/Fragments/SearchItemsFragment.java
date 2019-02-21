package com.shamgar.sss.wel.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shamgar.sss.wel.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchItemsFragment extends Fragment {

    private ProgressDialog progressDialog;


    public SearchItemsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_search_items, container, false);




        return view;
    }

}
