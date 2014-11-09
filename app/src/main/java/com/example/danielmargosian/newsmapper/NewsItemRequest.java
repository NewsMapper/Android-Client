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
 * Created by danielmargosian on 9/11/14.
 */
public class NewsItemRequest extends AsyncTaskLoader<List<NewsItem>>{

    private InputStream is;
    private String location;

    public NewsItemRequest(Context context, String location) {
        super(context);
        this.location = location;
    }

    @Override
    public List<NewsItem> loadInBackground() {
        //Create new input stream
        try {
            URL url = new URL("http://36c84268.ngrok.com/api/location?latlng="+location);
            is = url.openConnection().getInputStream();
        }
        catch (MalformedURLException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}

        //get JsonArray from inputStream
        JsonReader rdr = Json.createReader(is);
        JsonObject obj = rdr.readObject();
        JsonArray newsArray  = obj.getJsonArray("news");

        //parse JsonArray and create a list of new NewsItems
        List<NewsItem> newsItems = new ArrayList<NewsItem>();
        for (int i = 0; i < newsArray.size(); i++)
            newsItems.add(new NewsItem(newsArray.getJsonObject(i)));
        return newsItems;
    }
}
