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

        try {
            URL url = new URL("http://7a298fd8.ngrok.com/api/location?address="+location);
            is = url.openConnection().getInputStream();
        }
        catch (MalformedURLException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}
        JsonReader rdr = Json.createReader(is);
        JsonObject obj = rdr.readObject();
        JsonArray newsArray  = obj.getJsonArray("news");
        List<NewsItem> newsItems = new ArrayList<NewsItem>();
        for (int i = 0; i < newsArray.size(); i++)
            newsItems.add(new NewsItem(newsArray.getJsonObject(i)));
        return newsItems;
    }
}
