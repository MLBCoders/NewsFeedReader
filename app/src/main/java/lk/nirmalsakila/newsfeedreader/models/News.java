package lk.nirmalsakila.newsfeedreader.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class News {
    @SerializedName("id")
    long ID;

    Date publishedAt;

    String title;
    String author;
    String description;
    String urlToImage;
    String url;

    public long getID() {
        return ID;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getUrl() {
        return url;
    }
}
