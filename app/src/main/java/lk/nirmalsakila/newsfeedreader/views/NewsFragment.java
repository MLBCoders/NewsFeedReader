package lk.nirmalsakila.newsfeedreader.views;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

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

        HashMap<Integer,String[]>  feedSelectorButtons = new HashMap<>();
        feedSelectorButtons.put(R.id.btnCNNNews,new String[]{"cnn","CNN News"});
        feedSelectorButtons.put(R.id.btnBBCNews,new String[]{"bbc-news","BBC News"});
        feedSelectorButtons.put(R.id.btnABCNews,new String[]{"abc-news","ABC News"});
        feedSelectorButtons.put(R.id.btnNewYorkTimes,new String[]{"the-new-york-times","The New York Times"});
        feedSelectorButtons.put(R.id.btnNews24,new String[]{"news24","News 24"});

        for (Map.Entry<Integer,String[]> entry : feedSelectorButtons.entrySet()){
            int btnId = entry.getKey();
            final String feedType = entry.getValue()[0];
            final String feedTitle = entry.getValue()[1];

            rootView.findViewById(btnId).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), NewsFeedActivity.class);
                    intent.putExtra(globalClass.TAG_SERVICE_TYPE, feedType);
                    intent.putExtra(globalClass.TAG_SERVICE_TITLE, feedTitle);
                    NewsFragment.this.startActivity(intent);
                }
            });
        }

        return rootView;
    }

}
