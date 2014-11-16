package com.example.danielmargosian.newsmapper;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

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
    private LatLng latLng;

    public SubredditRequest(Context context, LatLng latLng) {

        super(context);
        this.latLng=latLng;
    }

    @Override
    public List<SubredditTopic> loadInBackground() {
        try {
            URL url = new URL("http://36c84268.ngrok.com/reddit_api/r");
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
            if (subreddits.get(i).getBoundary().contains(latLng))
                subredditsLocationIn.add(subreddits.get(i));
        }

        List<SubredditTopic> topics = new ArrayList<SubredditTopic>();
        for (int i=0;i<subredditsLocationIn.size();i++) {
            try {
                URL url = new URL("http://36c84268.ngrok.com/reddit_api/r/" + subredditsLocationIn.get(i).getRid());
                is = url.openConnection().getInputStream();
            }
            catch (MalformedURLException e) {e.printStackTrace();}
            catch (IOException e) {e.printStackTrace();}

            rdr = Json.createReader(is);
            obj = rdr.readObject();
            JsonArray topicsArray  = obj.getJsonArray("topics");

            for (int j = 0; j < topicsArray.size(); j++)
                topics.add(new SubredditTopic(topicsArray.getJsonObject(j)));
        }

        return topics;
    }
}
