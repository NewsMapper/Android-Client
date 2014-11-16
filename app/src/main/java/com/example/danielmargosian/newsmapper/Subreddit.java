package com.example.danielmargosian.newsmapper;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import javax.json.JsonObject;

/**
 * Created by danielmargosian on 11/13/14.
 */
public class Subreddit {

    private LatLngBounds boundary;
    private String name;
    private String rid;

    public Subreddit(JsonObject jo) {
        String NeLat = jo.getJsonObject("location").getJsonObject("boundary").getJsonObject("northEast").getString("latitude");
        String NeLon = jo.getJsonObject("location").getJsonObject("boundary").getJsonObject("northEast").getString("longitude");
        String SwLat = jo.getJsonObject("location").getJsonObject("boundary").getJsonObject("southWest").getString("latitude");
        String SwLon = jo.getJsonObject("location").getJsonObject("boundary").getJsonObject("southWest").getString("longitude");
        LatLng northEast = new LatLng(Double.parseDouble(NeLat), Double.parseDouble(NeLon));
        LatLng southWest = new LatLng(Double.parseDouble(SwLat), Double.parseDouble(SwLon));
        boundary = new LatLngBounds(southWest, northEast);
        name = jo.getString("name");
        rid = jo.getString("rid");
    }

    public String getRid() {
        return rid;
    }
    public LatLngBounds getBoundary() {
        return boundary;
    }
}
