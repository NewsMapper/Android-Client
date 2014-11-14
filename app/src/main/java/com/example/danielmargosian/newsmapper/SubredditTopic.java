package com.example.danielmargosian.newsmapper;

import javax.json.JsonObject;

/**
 * Created by danielmargosian on 11/13/14.
 */
public class SubredditTopic {

    private String title;
    private String url;

    public SubredditTopic(JsonObject jo) {
        title = jo.getString("title", "text");
        title = jo.getString("title", "link");

    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
