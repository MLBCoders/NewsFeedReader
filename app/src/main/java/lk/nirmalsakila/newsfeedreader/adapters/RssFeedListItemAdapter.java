package lk.nirmalsakila.newsfeedreader.adapters;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ernieyu.feedparser.Item;

import java.text.DateFormat;
import java.util.List;

import lk.nirmalsakila.newsfeedreader.R;
import lk.nirmalsakila.newsfeedreader.utils.GlobalClass;

public class RssFeedListItemAdapter extends RecyclerView.Adapter<RssFeedListItemAdapter.FeedModelViewHolder> {
    private static List<Item> mRssFeedModels;
    static GlobalClass globalClass;

    public static final String CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";
    CustomTabsClient mClient;
    private static CustomTabsSession mCustomTabsSession;
    private static CustomTabsServiceConnection mCustomTabsServiceConnection;
    private static CustomTabsIntent customTabsIntent;

    public static class FeedModelViewHolder extends RecyclerView.ViewHolder {
        private View rssFeedView;

        public FeedModelViewHolder(View v) {
            super(v);
            rssFeedView = v;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToUrl(itemView.getContext(), mRssFeedModels.get(getAdapterPosition()).getLink());
                }
            });

        }

    }

    public RssFeedListItemAdapter(List<Item> mRssFeedModels,Context mContext) {
        this.mRssFeedModels = mRssFeedModels;

        globalClass = (GlobalClass) mContext.getApplicationContext();

        mCustomTabsServiceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                mClient = customTabsClient;
                mClient.warmup(0L);
                mCustomTabsSession = mClient.newSession(null);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mClient = null;
            }
        };
        CustomTabsClient.bindCustomTabsService(mContext, CUSTOM_TAB_PACKAGE_NAME, mCustomTabsServiceConnection);
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(mCustomTabsSession);
        builder.setCloseButtonIcon(drawableToBitmap(globalClass.getApplicationContext().getDrawable(R.drawable.ic_arrow_back_black_24dp)));
        builder.setShowTitle(true);
        builder.setStartAnimations(mContext, R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(mContext, R.anim.slide_in_left, R.anim.slide_out_right);
        customTabsIntent = builder.build();
    }

    @Override
    public FeedModelViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rss, parent, false);
        FeedModelViewHolder holder = new FeedModelViewHolder(v);
        return holder;
    }


    private static void goToUrl(Context context, String url) {
        if(globalClass.isLinkOpenInDefaultBrowser()){
            Uri uriUrl = Uri.parse(url);
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            context.startActivity(launchBrowser);
        }else{
            customTabsIntent.launchUrl(context, Uri.parse(url));
        }
    }

    @Override
    public void onBindViewHolder(FeedModelViewHolder holder, int position) {

        final Item rssFeedModel = mRssFeedModels.get(position);
        Log.d("RSS","RSS Model : " + rssFeedModel.toString());
        ((TextView)holder.rssFeedView.findViewById(R.id.txtRssTitle)).setText(rssFeedModel.getTitle());
        String date = DateFormat.getDateTimeInstance().format(rssFeedModel.getPubDate());
        ((TextView)holder.rssFeedView.findViewById(R.id.txtRssPubDate)).setText(date);
        ((TextView)holder.rssFeedView.findViewById(R.id.txtRssContent)).setText(rssFeedModel.getDescription());
    }

    @Override
    public int getItemCount() {
        return mRssFeedModels.size();
    }

    //    http://corochann.com/convert-between-bitmap-and-drawable-313.html
    public Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap ;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
