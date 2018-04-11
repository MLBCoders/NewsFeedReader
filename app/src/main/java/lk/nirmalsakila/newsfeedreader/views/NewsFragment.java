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
        feedSelectorButtons.put(R.id.btnCNNNews,new String[]{"cnn","CNN News","everything"});
        feedSelectorButtons.put(R.id.btnBBCNews,new String[]{"bbc-news","BBC News","everything"});
        feedSelectorButtons.put(R.id.btnABCNews,new String[]{"abc-news","ABC News","everything"});
//        feedSelectorButtons.put(R.id.btnNewYorkTimes,new String[]{"the-new-york-times","The New York Times"});
        feedSelectorButtons.put(R.id.btnNews24,new String[]{"news24","News 24","everything"});

        for (Map.Entry<Integer,String[]> entry : feedSelectorButtons.entrySet()){
            int btnId = entry.getKey();
            final String feedType = entry.getValue()[0];
            final String feedTitle = entry.getValue()[1];
            final String feeCategory = entry.getValue()[2];

            rootView.findViewById(btnId).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), NewsFeedActivity.class);
                    intent.putExtra(globalClass.TAG_SERVICE_TYPE, feedType);
                    intent.putExtra(globalClass.TAG_SERVICE_CATEGORY, feeCategory);
                    intent.putExtra(globalClass.TAG_SERVICE_TITLE, feedTitle);
                    NewsFragment.this.startActivity(intent);
                }
            });
        }

        rootView.findViewById(R.id.btnNewYorkTimes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RssFeedActivity.class);
                intent.putExtra(globalClass.TAG_SERVICE_TYPE, "http://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml");
                intent.putExtra(globalClass.TAG_SERVICE_TITLE, "The New York Times");
                NewsFragment.this.startActivity(intent);
            }
        });

        return rootView;
    }

}
