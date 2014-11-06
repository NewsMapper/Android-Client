package com.example.danielmargosian.newsmapper;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by danielmargosian on 10/18/14.
 */

public class DisplayMapViewActivity extends Activity {

    private MapFragment mapFrag;
    private GoogleMap map;
    private LatLng latLng;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_map_view);
        mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        map = mapFrag.getMap();
        Intent intent = getIntent();
        double longitude = intent.getDoubleExtra(NewsMapper.EXTRA_LONGITUDE, 40.101953);
        double latitude = intent.getDoubleExtra(NewsMapper.EXTRA_LATITUDE, -88.227152);
        latLng = new LatLng(latitude, longitude);
        centerMapOnMyLocation();


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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void centerMapOnMyLocation() {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
    }

}
