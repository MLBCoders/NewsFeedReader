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
public class SportsFragment extends Fragment {

    GlobalClass globalClass;

    public SportsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sports, container, false);
        globalClass = (GlobalClass)this.getActivity().getApplication();

        HashMap<Integer, String[]> feedSelectorButtons = new HashMap<>();
        feedSelectorButtons.put(R.id.btnBBCSports, new String[]{"bbc-sport", "BBC Sports","top-headlines"});
        feedSelectorButtons.put(R.id.btnESPN, new String[]{"espn", "ESPN","top-headlines"});
        feedSelectorButtons.put(R.id.btnESPNCric, new String[]{"espn-cric-info", "ESPN Cric Info","everything"});

        for (Map.Entry<Integer, String[]> entry : feedSelectorButtons.entrySet()) {
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
                    SportsFragment.this.startActivity(intent);
                }
            });
        }

        return rootView;
    }

}
