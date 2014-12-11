package com.example.danielmargosian.newsmapper;

import javax.json.JsonObject;

/**
 * Created by danielmargosian on 9/11/14.
 */
public class NewsItem {
    protected String publishedDate;
    protected String publisher;
    protected String summary;
    protected String title;
    protected String url;
    //default constructor
    public NewsItem() {
        publishedDate=null;
        publisher=null;
        summary=null;
        title=null;
        url=null;
    }

    public NewsItem(String publishedDate, String publisher, String summary, String title, String url)
    {
        this.publishedDate = publishedDate;
        this.publisher = publisher;
        this.summary = summary;
        this.title = title;
        this.url = url;
    }
    //creates a NewsItem from a JsonObject
    public NewsItem(JsonObject jo)
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
        return String.format("%s published by %s on %s at %s. Summary: %s", title, publisher, publishedDate, url, summary);
    }
}
