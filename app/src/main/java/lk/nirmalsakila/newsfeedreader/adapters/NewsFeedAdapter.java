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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.DateFormat;
import java.util.List;

import lk.nirmalsakila.newsfeedreader.R;
import lk.nirmalsakila.newsfeedreader.models.NewsModel;
import lk.nirmalsakila.newsfeedreader.utils.GlobalClass;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.NewsFeedViewHolder> {

    static GlobalClass globalClass;

    private static List<NewsModel> mDataSet;

    private static final String CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";
    CustomTabsClient mClient;
    private static CustomTabsSession mCustomTabsSession;
    private static CustomTabsServiceConnection mCustomTabsServiceConnection;
    private static CustomTabsIntent customTabsIntent;

    public NewsFeedAdapter(List<NewsModel> pDataSet, Context mContext) {
        mDataSet = pDataSet;

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

    public class NewsFeedViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtNewsTitle;
        private final TextView txtNewsDescription;
        private final TextView txtNewsDateTime;
        private final TextView txtNewsAuthor;
        private final ImageView imgNews;
        private final Button btnNewsImageDownload;
        private View newsView;

        NewsFeedViewHolder(final View itemView) {
            super(itemView);
            newsView = itemView;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToUrl(itemView.getContext(), mDataSet.get(getAdapterPosition()).getUrl());
                }
            });

            txtNewsTitle = itemView.findViewById(R.id.txtNewsTitle);
            txtNewsDescription = itemView.findViewById(R.id.txtNewsDescription);
            imgNews = itemView.findViewById(R.id.imgNews);
            btnNewsImageDownload = itemView.findViewById(R.id.btnNewsImageDownload);
            txtNewsDateTime = itemView.findViewById(R.id.txtNewsDateTime);
            txtNewsAuthor = itemView.findViewById(R.id.txtNewsAuthor);
        }

        public TextView getTxtNewsTitle() {
            return txtNewsTitle;
        }

        public TextView getTxtNewsDescription() {
            return txtNewsDescription;
        }

        public ImageView getImgNews() {
            return imgNews;
        }

        public Button getBtnNewsImageDownload() {
            return btnNewsImageDownload;
        }

        View getNewsView() {
            return newsView;
        }

        public TextView getTxtNewsDateTime() {
            return txtNewsDateTime;
        }

        public TextView getTxtNewsAuthor() {
            return txtNewsAuthor;
        }
    }

    @Override
    public NewsFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);

        return new NewsFeedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final NewsFeedViewHolder holder, int position) {
        final NewsModel newsModel = mDataSet.get(position);
        Context context = holder.getNewsView().getContext();

        holder.getTxtNewsTitle().setText(newsModel.getTitle());
        holder.getTxtNewsDescription().setText(newsModel.getDescription());
        if (newsModel.getPublishedAt() != null && !TextUtils.isEmpty(newsModel.getPublishedAt().toString())) {
            String date = DateFormat.getDateTimeInstance().format(newsModel.getPublishedAt());
            holder.getTxtNewsDateTime().setText(date);
        }else{
            holder.getTxtNewsDateTime().setVisibility(View.GONE);
        }
        if(newsModel.getAuthor() != null){
            holder.getTxtNewsAuthor().setText(context.getString(R.string.news_author_format, newsModel.getAuthor()));
        }else{
            holder.getTxtNewsAuthor().setVisibility(View.GONE);
        }

        if (globalClass.isDataSaverEnabled()){
            holder.getBtnNewsImageDownload().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadAndLoadImageToImageView(holder,newsModel.getUrlToImage());
                    holder.getBtnNewsImageDownload().setVisibility(View.GONE);
                }
            });
        }else{
            downloadAndLoadImageToImageView(holder,newsModel.getUrlToImage());
            holder.getBtnNewsImageDownload().setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    private void downloadAndLoadImageToImageView(NewsFeedViewHolder holder, String url) {
        Context context = holder.getImgNews().getContext();
        holder.getImgNews().setVisibility(View.VISIBLE);
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.getImgNews());
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


    private Bitmap drawableToBitmap(Drawable drawable) {
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
