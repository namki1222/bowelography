package com.rightcode.bowelography.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rightcode.bowelography.R;
import com.rightcode.bowelography.databinding.FragmentOutsideBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link outsideFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class outsideFragment extends BaseFragment<FragmentOutsideBinding> {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public outsideFragment() {
        // Required empty public constructor
    }

    public static outsideFragment newInstance() {
        outsideFragment fragment = new outsideFragment();
        Bundle args = new Bundle();
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
        // Inflate the layout for this fragment
        return bindView(R.layout.fragment_outside, inflater, container);
    }

    @Override
    protected void initBinding() {

    }

    @Override
    protected void initFragment() {

    }

    @Override
    protected void initClickListener() {

    }
}