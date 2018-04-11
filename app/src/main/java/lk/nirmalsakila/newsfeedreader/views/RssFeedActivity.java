package lk.nirmalsakila.newsfeedreader.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ernieyu.feedparser.Feed;
import com.ernieyu.feedparser.FeedParser;
import com.ernieyu.feedparser.FeedParserFactory;
import com.ernieyu.feedparser.Item;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import lk.nirmalsakila.newsfeedreader.R;
import lk.nirmalsakila.newsfeedreader.adapters.RssFeedListItemAdapter;
import lk.nirmalsakila.newsfeedreader.utils.GlobalClass;

public class RssFeedActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeLayout;

    private List<Item> mFeedModelList;

    GlobalClass globalClass;
    Snackbar snackBar;
    String endpoint;
    boolean darkThemeEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        globalClass = (GlobalClass)this.getApplication();
        setTheme(globalClass.getAppThemeId());
        darkThemeEnabled = globalClass.isDarkThemeEnabled();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_feed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setPopupTheme(darkThemeEnabled ? R.style.AppThemeDark_PopupOverlay : R.style.AppTheme_PopupOverlay);
        toolbar.setTitle(getIntent().getStringExtra(globalClass.TAG_SERVICE_TITLE));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = findViewById(R.id.rssFeedRecyclerView);
        mSwipeLayout = findViewById(R.id.rssFeedSwipeRefreshLayout);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchFeedTask().execute((Void) null);
            }
        });

        endpoint = getIntent().getStringExtra(globalClass.TAG_SERVICE_TYPE);
    }

    private class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            mSwipeLayout.setRefreshing(true);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (TextUtils.isEmpty(endpoint))
                return false;

            try {
                URL url = new URL(endpoint);
                InputStream inputStream = url.openConnection().getInputStream();

                FeedParser parser = FeedParserFactory.newParser();
                Feed feed = parser.parse(inputStream);
                mFeedModelList = feed.getItemList();

                return true;
            } catch (Exception e) {
                Log.e(globalClass.TAG, e.getLocalizedMessage());

            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mSwipeLayout.setRefreshing(false);

            if (success) {
                // Fill RecyclerView
               mRecyclerView.setAdapter(new RssFeedListItemAdapter(mFeedModelList,globalClass.getGlobalApplicationContext()));
            } else {
                snackBar = Snackbar.make(findViewById(R.id.activity_rss_feed), getString(R.string.error_network_connection), Snackbar.LENGTH_INDEFINITE);

                snackBar.setAction("Connect", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(Settings.ACTION_SETTINGS), 0);
                        snackBar.dismiss();
                    }
                });
                snackBar.setActionTextColor(Color.RED);
                View sbView = snackBar.getView();
                TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackBar.show();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        boolean nowDarkThemeEnabled = globalClass.isDarkThemeEnabled();
        if(darkThemeEnabled!= nowDarkThemeEnabled){
            RssFeedActivity.this.recreate();
        }
        new FetchFeedTask().execute((Void) null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(RssFeedActivity.this,SettingsActivity.class);
            intent.putExtra(SettingsActivity.EXTRA_SHOW_FRAGMENT,SettingsActivity.GeneralPreferenceFragment.class.getName());
            intent.putExtra(SettingsActivity.EXTRA_NO_HEADERS,true);
            startActivity(intent);
            return true;
        }else if (id == R.id.action_about) {
            Intent intent = new Intent(RssFeedActivity.this,AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


