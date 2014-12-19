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
            URL url = new URL("http://7c4a8d8e.ngrok.com/reddit_api/r");
            is = url.openConnection().getInputStream();
        }
        catch (MalformedURLException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}

        //parse list of all geolocated subreddits
        JsonReader rdr = Json.createReader(is);
        JsonObject obj = rdr.readObject();
        JsonArray subredditsArray  = obj.getJsonArray("subreddits");

        //add subreddits that are close to the users location to the list
        List<Subreddit> subreddits = new ArrayList<Subreddit>();
        for (int i = 0; i < subredditsArray.size(); i++) {
            Subreddit subreddit = new Subreddit(subredditsArray.getJsonObject(i));
            if (subreddit.getBoundary().contains(latLng) && !subreddit.getName().equals("america"))
                subreddits.add(subreddit);
        }

        //get list of topics from each subreddit
        List<NewsItem> topics = new ArrayList<NewsItem>();
        for (int i=0;i<subreddits.size();i++) {
            try {
                URL url = new URL("http://7c4a8d8e.ngrok.com/reddit_api/r/" + subreddits.get(i).getRid());
                is = url.openConnection().getInputStream();
            }
            catch (MalformedURLException e) {e.printStackTrace();}
            catch (IOException e) {e.printStackTrace();}

            //parse list of all topics from each individual subreddit
            rdr = Json.createReader(is);
            obj = rdr.readObject();
            JsonArray topicsArray  = obj.getJsonArray("topics");

            //add them to the list
            for (int j = 0; j < topicsArray.size(); j++) {
                topics.add(new SubredditTopic(topicsArray.getJsonObject(j)));
            }
        }

        //get all news articles related to the users location
        try {
            URL url = new URL("http://13d8a794.ngrok.com/api/location?latlng="+latLng.latitude+","+latLng.longitude);
            is = url.openConnection().getInputStream();
        }
        catch (MalformedURLException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}

        rdr = Json.createReader(is);
        obj = rdr.readObject();
        JsonArray newsArray  = obj.getJsonArray("news");

        //add articles to the list
        for (int i = 0; i < newsArray.size(); i++)
            topics.add(new NewsItem(newsArray.getJsonObject(i)));

        return topics;
    }
}
