package lk.nirmalsakila.newsfeedreader.models;

import java.util.List;

/**
 * Created by user on 3/17/2018.
 */

public class NewsSet {
    String status;
    String totalResults;
    List<News> articles;

    public String getStatus() {
        return status;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public List<News> getArticles() {
        return articles;
    }
}
