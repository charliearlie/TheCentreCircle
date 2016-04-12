package uk.ac.prco.plymouth.thecentrecircle.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.ac.prco.plymouth.thecentrecircle.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerDetailFragment extends Fragment {


    public PlayerDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player_detail, container, false);
    }

}
