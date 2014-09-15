package com.example.danielmargosian.newsreader;

import javax.json.JsonObject;
import javax.json.JsonValue;

/**
 * Created by danielmargosian on 9/11/14.
 */
public class NewsItem {
    private String publishedDate;
    private String publisher;
    private String summary;
    private String title;
    private String url;

    public NewsItem(String publishedDate, String publisher, String summary, String title, String url)
    {
        this.publishedDate = publishedDate;
        this.publisher = publisher;
        this.summary = summary;
        this.title = title;
        this.url = url;
    }
    public NewsItem (JsonObject jo)
    {
        publishedDate = jo.getString("publishedDate");
        publisher = jo.getString("publisher");
        summary = jo.getString("summary");
        title = jo.getString("title");
        url = jo.getString("url");
    }

    public String getPublishedDate()
    {
        return publishedDate;
    }

    public String getPublisher()
    {
        return publishedDate;
    }

    public String getSummary()
    {
        return summary;
    }

    public String getTitle()
    {
        return title;
    }

    public String getUrl()
    {
        return url;
    }

    public String toString()
    {
        return String.format("%s published by %s on %s at %. Summary: %s", title, publisher, publishedDate, url, summary);
    }
}
