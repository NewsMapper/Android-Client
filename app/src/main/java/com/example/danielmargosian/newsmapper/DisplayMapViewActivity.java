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
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by danielmargosian on 10/18/14.
 */

public class DisplayMapViewActivity extends Activity {

    private static GoogleMap map;

    public static final String EXTRA_OPEN = "com.example.danielmargosian.newsmapper.OPEN";
    public static final String EXTRA_LAT = "com.example.danielmargosian.newsmapper.LAT";
    public static final String EXTRA_LNG = "com.example.danielmargosian.newsmapper.LNG";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_map_view);
        //get GoogleMap
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();;
        //Center the map on the location
        Intent intent = getIntent();
        LatLng latLng = new LatLng(intent.getDoubleExtra(NewsMapper.EXTRA_LATITUDE, 40.101953),
                intent.getDoubleExtra(NewsMapper.EXTRA_LONGITUDE, -88.227152));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.5f));
        MarkerOptions mp = new MarkerOptions();
        mp.position(latLng);
        mp.title("Test Marker");
        mp.draggable(false);
        map.addMarker(mp);

    }
    /*
    opens the list, supplies it the location of the current MapView of which to get the news from
    and tells it to use that location, not the users current location by telling it MapView has
    been opened
    */
    public void openList() {
        Intent intent = new Intent(DisplayMapViewActivity.this, NewsMapper.class);
        LatLng latLng = map.getCameraPosition().target;
        intent.putExtra(EXTRA_LAT, latLng.latitude);
        intent.putExtra(EXTRA_LNG, latLng.longitude);
        intent.putExtra(EXTRA_OPEN, true);
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
        /*
        Handle action bar item clicks here. The action bar will
        automatically handle clicks on the Home/Up button, so long
        as you specify a parent activity in AndroidManifest.xml.
        */
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
}
