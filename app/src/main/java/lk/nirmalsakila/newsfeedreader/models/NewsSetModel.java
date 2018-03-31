package lk.nirmalsakila.newsfeedreader.models;

import java.util.List;

/**
 * Created by user on 3/17/2018.
 */

public class NewsSetModel {
    String status;
    String totalResults;
    List<NewsModel> articles;

    public String getStatus() {
        return status;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public List<NewsModel> getArticles() {
        return articles;
    }
}
