package com.example.danielmargosian.newsmapper;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * Created by danielmargosian on 11/14/14.
 */
public class SubredditRequest extends AsyncTaskLoader<List<NewsItem>> {
    private LatLng latLng;

    public SubredditRequest(Context context, LatLng latLng) {

        super(context);
        this.latLng=latLng;
    }

    @Override
    public List<NewsItem> loadInBackground() {
        InputStream is = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        try {
            URL url = new URL("http://5a368771.ngrok.com/reddit_api/r");
            is = url.openConnection().getInputStream();
        }
        catch (MalformedURLException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}

        //get JsonArray from inputStream
        JsonReader rdr = Json.createReader(is);
        JsonObject obj = rdr.readObject();
        JsonArray subredditsArray  = obj.getJsonArray("subreddits");

        List<Subreddit> subreddits = new ArrayList<Subreddit>();
        for (int i = 0; i < subredditsArray.size(); i++)
            subreddits.add(new Subreddit(subredditsArray.getJsonObject(i)));

        List<Subreddit> subredditsLocationIn = new ArrayList<Subreddit>();
        for (int i = 0; i < subreddits.size(); i++) {
            Subreddit subreddit = subreddits.get(i);
            if (subreddit.getBoundary().contains(latLng) && !subreddit.getName().equals("america"))
                subredditsLocationIn.add(subreddits.get(i));
        }

        List<NewsItem> topics = new ArrayList<NewsItem>();
        for (int i=0;i<subredditsLocationIn.size();i++) {
            try {
                URL url = new URL("http://5a368771.ngrok.com/reddit_api/r/" + subredditsLocationIn.get(i).getRid());
                is = url.openConnection().getInputStream();
            }
            catch (MalformedURLException e) {e.printStackTrace();}
            catch (IOException e) {e.printStackTrace();}

            rdr = Json.createReader(is);
            obj = rdr.readObject();
            JsonArray topicsArray  = obj.getJsonArray("topics");

            for (int j = 0; j < topicsArray.size(); j++) {
                topics.add(new SubredditTopic(topicsArray.getJsonObject(j)));
            }
        }

        try {
            URL url = new URL("http://5bd3457.ngrok.com/api/location?latlng="+latLng.latitude+","+latLng.longitude);
            URLConnection c = url.openConnection();
            is = c.getInputStream();
        }
        catch (MalformedURLException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}

        //get JsonArray from inputStream
        rdr = Json.createReader(is);
        obj = rdr.readObject();
        JsonArray newsArray  = obj.getJsonArray("news");

        //parse JsonArray and create a list of new NewsItems
        for (int i = 0; i < newsArray.size(); i++)
            topics.add(new NewsItem(newsArray.getJsonObject(i)));
        return topics;
    }
}
