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
public class OthersFragment extends Fragment {

    GlobalClass globalClass;

    public OthersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_others, container, false);

        globalClass = (GlobalClass) this.getActivity().getApplication();

        HashMap<Integer, String[]> feedSelectorButtons = new HashMap<>();
        feedSelectorButtons.put(R.id.btnEngadget, new String[]{"engadget", "ENGADGET"});
        feedSelectorButtons.put(R.id.btnMedicalNewsToday, new String[]{"medical-news-today", "Medical News Today"});

        for (Map.Entry<Integer, String[]> entry : feedSelectorButtons.entrySet()) {
            int btnId = entry.getKey();
            final String feedType = entry.getValue()[0];
            final String feedTitle = entry.getValue()[1];

            rootView.findViewById(btnId).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), NewsFeedActivity.class);
                    intent.putExtra(globalClass.TAG_SERVICE_TYPE, feedType);
                    intent.putExtra(globalClass.TAG_SERVICE_TITLE, feedTitle);
                    OthersFragment.this.startActivity(intent);
                }
            });
        }

        rootView.findViewById(R.id.btnAndroidAuthority).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RssFeedActivity.class);
                intent.putExtra(globalClass.TAG_SERVICE_TYPE, "http://feed.androidauthority.com/");
                intent.putExtra(globalClass.TAG_SERVICE_TITLE, "Android Authority");
                OthersFragment.this.startActivity(intent);
            }
        });

        rootView.findViewById(R.id.btnPhoneArena).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RssFeedActivity.class);
                intent.putExtra(globalClass.TAG_SERVICE_TYPE, "http://feeds.feedburner.com/PhoneArena-LatestNews");
                intent.putExtra(globalClass.TAG_SERVICE_TITLE, "Phone Arena");
                OthersFragment.this.startActivity(intent);
            }
        });

        return rootView;
    }

}
