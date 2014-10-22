package com.example.danielmargosian.newsmapper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.MapView;

/**
 * Created by danielmargosian on 10/18/14.
 */
public class DisplayMapView extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_map_view);
        Intent intent = getIntent();
    }

}
