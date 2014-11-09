package com.example.danielmargosian.newsmapper;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.google.android.gms.internal.on;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by danielmargosian on 10/18/14.
 */

public class DisplayMapViewActivity extends Activity {

    private MapFragment mapFrag;
    private GoogleMap map;
    private LatLng latLng;
    private boolean opened=false;
    private double lat;
    private double lng;

    public static final String EXTRA_OPEN = "com.example.danielmargosian.newsmapper.OPEN";
    public static final String EXTRA_LAT = "com.example.danielmargosian.newsmapper.LAT";
    public static final String EXTRA_LNG = "com.example.danielmargosian.newsmapper.LNG";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_map_view);
        mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        map = mapFrag.getMap();
        Intent intent = getIntent();
        lat = intent.getDoubleExtra(NewsMapper.EXTRA_LATITUDE, 40.101953);
        lng = intent.getDoubleExtra(NewsMapper.EXTRA_LONGITUDE, -88.227152);
        latLng = new LatLng(lat, lng);
        centerMapOnMyLocation();
    }

    public void openList() {
        opened=true;
        Intent intent = new Intent(DisplayMapViewActivity.this, NewsMapper.class);
        CameraPosition cameraPosition = map.getCameraPosition();
        LatLng latLng = cameraPosition.target;
        lat = latLng.latitude;
        lng = latLng.longitude;
        intent.putExtra(EXTRA_LAT, lat);
        intent.putExtra(EXTRA_LNG, lng);
        intent.putExtra(EXTRA_OPEN, opened);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_map_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.action_list:
                openList();
                return true;
            case R.id.action_settings:
                //openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void centerMapOnMyLocation() {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.5f));
    }

}
