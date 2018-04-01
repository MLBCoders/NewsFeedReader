package lk.nirmalsakila.newsfeedreader.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import lk.nirmalsakila.newsfeedreader.R;
import lk.nirmalsakila.newsfeedreader.adapters.NewsFeedAdapter;
import lk.nirmalsakila.newsfeedreader.models.NewsModel;
import lk.nirmalsakila.newsfeedreader.models.NewsSetModel;
import lk.nirmalsakila.newsfeedreader.utils.GlobalClass;

public class NewsFeedActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private Gson gson;

    private SwipeRefreshLayout newsFeedSwipeRefreshLayout;
    private RecyclerView newsFeedRecyclerView;
    List<NewsModel> newsModelList = new ArrayList<>();

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
        setContentView(R.layout.activity_news_feed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setPopupTheme(R.style.AppTheme_PopupOverlay);
        toolbar.setTitle(getIntent().getStringExtra(globalClass.TAG_SERVICE_TITLE));
        setSupportActionBar(toolbar);

        newsFeedSwipeRefreshLayout = findViewById(R.id.newsFeedSwipeRefreshLayout);
        newsFeedRecyclerView = findViewById(R.id.newsFeedRecyclerView);

        newsFeedRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestQueue = Volley.newRequestQueue(this);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        String service = getIntent().getStringExtra(globalClass.TAG_SERVICE_TYPE);
        endpoint = getServiceEndpoint(service);
        Log.d(globalClass.TAG,"Service endpoint : " + endpoint);

        newsFeedSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPosts(endpoint);
            }
        });
    }

    private String getServiceEndpoint(String serviceType) {
        return "https://newsapi.org/v2/" + "everything" + "?sources=" + serviceType + "&apiKey=1c8530ec3214460bbfc19f8db75c28bb";
    }

    private void fetchPosts(String ENDPOINT) {
        Log.d(globalClass.TAG, "Fetching Started");
        newsFeedSwipeRefreshLayout.setRefreshing(true);
        if (snackBar!=null && snackBar.isShown()) {
            snackBar.dismiss();
        }
        StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT, onNewsLoaded, onNewsError);

        requestQueue.add(request);
    }

    private final Response.Listener<String> onNewsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.i(globalClass.TAG, response);

            Log.i(globalClass.TAG, "NewsList fetching started ");
            NewsSetModel newsSetModel = gson.fromJson(response, NewsSetModel.class);
            Log.i(globalClass.TAG, "NewsList ==> Status : " + newsSetModel.getStatus());
            newsModelList = newsSetModel.getArticles();
            newsFeedRecyclerView.setAdapter(new NewsFeedAdapter(newsModelList, NewsFeedActivity.this.getApplicationContext()));
            newsFeedSwipeRefreshLayout.setRefreshing(false);
        }
    };

    private final Response.ErrorListener onNewsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(globalClass.TAG, "ERROR : " + error.toString());

            snackBar = Snackbar.make(findViewById(R.id.activity_news_feed), getString(R.string.error_network_connection), Snackbar.LENGTH_INDEFINITE);

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
            newsFeedSwipeRefreshLayout.setRefreshing(false);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        boolean nowDarkThemeEnabled = globalClass.isDarkThemeEnabled();
        if(darkThemeEnabled!= nowDarkThemeEnabled){
            NewsFeedActivity.this.recreate();
        }
        fetchPosts(endpoint);
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
            Intent intent = new Intent(NewsFeedActivity.this,SettingsActivity.class);
            intent.putExtra(SettingsActivity.EXTRA_SHOW_FRAGMENT,SettingsActivity.GeneralPreferenceFragment.class.getName());
            intent.putExtra(SettingsActivity.EXTRA_NO_HEADERS,true);
            startActivity(intent);
            return true;
        }else if (id == R.id.action_about) {
            Intent intent = new Intent(NewsFeedActivity.this,AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
