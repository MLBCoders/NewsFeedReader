package lk.nirmalsakila.newsfeedreader.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import lk.nirmalsakila.newsfeedreader.R;

public class GlobalClass extends Application {

    public final String TAG = "NewsFeedReader";
    public final String TAG_SERVICE_TYPE = "service_type";
    public final String TAG_SERVICE_TITLE = "service_title";

    public String KEY_APP_THEME;
    public String KEY_DARK_THEME_ENABLED;
    public String KEY_DATA_SAVER;
    public String KEY_OPEN_LINKS;

    private SharedPreferences sharedPreferences;
    private Context globalApplicationContext;

    private int appThemeId;
    private boolean darkThemeEnabled;
    private boolean dataSaverEnabled;
    private boolean linkOpenInDefaultBrowser;

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;

    }

    public Context getGlobalApplicationContext() {
        return globalApplicationContext;
    }

    public void setGlobalApplicationContext(Context globalApplicationContext) {
        this.globalApplicationContext = globalApplicationContext;
        KEY_APP_THEME = this.globalApplicationContext.getResources().getString(R.string.settings_app_theme_key);
        KEY_DATA_SAVER = this.globalApplicationContext.getResources().getString(R.string.settings_data_saver_key);
        KEY_OPEN_LINKS = this.globalApplicationContext.getResources().getString(R.string.settings_open_links_key);

        KEY_DARK_THEME_ENABLED = this.globalApplicationContext.getResources().getString(R.string.settings_dark_theme_enabled_key);
    }

    public boolean isDarkThemeEnabled() {
        return darkThemeEnabled;
    }

    public void setDarkThemeEnabled(boolean darkThemeEnabled) {
        this.darkThemeEnabled = darkThemeEnabled;
    }

    public boolean isDataSaverEnabled() {
        return dataSaverEnabled;
    }

    public void setDataSaverEnabled(boolean dataSaverEnabled) {
        this.dataSaverEnabled = dataSaverEnabled;
    }

    public boolean isLinkOpenInDefaultBrowser() {
        return linkOpenInDefaultBrowser;
    }

    public void setLinkOpenInDefaultBrowser(boolean linkOpenInDefaultBrowser) {
        this.linkOpenInDefaultBrowser = linkOpenInDefaultBrowser;
    }
}
