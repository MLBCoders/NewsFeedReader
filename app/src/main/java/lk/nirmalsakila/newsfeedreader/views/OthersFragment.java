package lk.nirmalsakila.newsfeedreader.views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lk.nirmalsakila.newsfeedreader.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OthersFragment extends Fragment {


    public OthersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_others, container, false);



        return rootView;
    }

}
