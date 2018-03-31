package lk.nirmalsakila.newsfeedreader.views;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lk.nirmalsakila.newsfeedreader.R;
import lk.nirmalsakila.newsfeedreader.utils.GlobalClass;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    GlobalClass globalClass;

    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        globalClass = (GlobalClass)this.getActivity().getApplication();

        

        rootView.findViewById(R.id.btnCNNNews).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewsFeedActivity.class);
                intent.putExtra(globalClass.TAG_SERVICE_TYPE, "cnn");
                intent.putExtra(globalClass.TAG_SERVICE_TITLE, "CNN News");
                NewsFragment.this.startActivity(intent);
            }
        });

        return rootView;
    }

}
