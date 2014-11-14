package com.example.danielmargosian.newsmapper;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * Created by danielmargosian on 11/14/14.
 */
public class SubredditRequest extends AsyncTaskLoader<List<SubredditTopic>> {

    private InputStream is;
    private List<Subreddit> subreddits;

    public SubredditRequest(Context context, List<Subreddit> s) {

        super(context);
        subreddits = s;
    }

    @Override
    public List<SubredditTopic> loadInBackground() {
        List<SubredditTopic> topics = new ArrayList<SubredditTopic>();
        for (int i=0;i<subreddits.size();i++) {
            try {
                URL url = new URL("http://36c84268.ngrok.com/reddit_api/r/" + subreddits.get(i).getRid());
                is = url.openConnection().getInputStream();
            }
            catch (MalformedURLException e) {e.printStackTrace();}
            catch (IOException e) {e.printStackTrace();}

            JsonReader rdr = Json.createReader(is);
            JsonObject obj = rdr.readObject();
            JsonArray topicsArray  = obj.getJsonArray("topics");

            for (int j = 0; j < topicsArray.size(); j++)
                topics.add(new SubredditTopic(topicsArray.getJsonObject(j)));
        }

        return topics;
    }
}
