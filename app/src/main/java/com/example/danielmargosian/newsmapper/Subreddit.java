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
        String NeLat = jo.getString("northEast","latitude");
        String NeLon = jo.getString("northEast","longitude");
        String SwLat = jo.getString("southWest","latitude");
        String SwLon = jo.getString("southWest","longitude");
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
