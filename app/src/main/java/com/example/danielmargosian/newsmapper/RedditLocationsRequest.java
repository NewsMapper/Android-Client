package com.example.danielmargosian.newsmapper;

import android.content.Context;
import android.content.AsyncTaskLoader;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * Created by danielmargosian on 11/13/14.
 */
public class RedditLocationsRequest extends AsyncTaskLoader<List<Subreddit>> {

    private InputStream is;

    public RedditLocationsRequest(Context context) {
        super(context);
    }

    @Override
    public List<Subreddit> loadInBackground() {

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
        return subreddits;
    }
}
