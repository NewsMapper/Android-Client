//package com.example.danielmargosian.newsreader;
//
//import android.os.AsyncTask;
//import android.content.Context;
//import android.os.AsyncTask;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.List;
//
//import javax.json.Json;
//import javax.json.JsonArray;
//import javax.json.JsonObject;
//import javax.json.JsonReader;
//
//import retrofit.RestAdapter;
//import retrofit.http.GET;
//
///**
//* Created by danielmargosian on 9/13/14.
//*/
//public class NewsItemRequest extends AsyncTask<Void, Void, JsonArray> {
//
//
//    private Context mContext;
//    private List<String> titleList;
//    public JsonArray newsList;
//    public InputStream is;
//
//    public NewsItemRequest() {
//        super();
//    }
//
//    @Override
//    protected JsonArray doInBackground(Void... arg0) {
//        try {
//            URL url = new URL("http://172.17.37.251:5000/api/location?address=chicago");
//            is = url.openConnection().getInputStream();
//
//
//            JsonReader rdr = Json.createReader(is);
//            JsonObject obj = rdr.readObject();
//            newsList = obj.getJsonArray("feed");
//            NewsScrape.newsItems = newsList;
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            return null;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//        return newsList;
//    }
//    protected JsonArray onPostExectute()
//    {
//        return newsList;
//    }
//}