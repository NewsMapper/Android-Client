package com.example.danielmargosian.newsmapper;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.IntentSender;
import android.content.Loader;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.model.LatLng;

public class NewsMapper extends Activity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    private static Location location;
    private static LocationClient mLocationClient;
    private static List<Subreddit> subreddits;
    private static List<Subreddit> subredditsLocationIn;
    private static ArrayAdapter<Spanned> newsAdapter;
    private static String locationString;
    private static double lat;
    private static double lng;

    public static final String EXTRA_URL = "com.example.danielmargosian.newsmapper.URL";
    public static final String EXTRA_LONGITUDE = "com.example.danielmargosian.newsmapper.LONGITUDE";
    public static final String EXTRA_LATITUDE = "com.example.danielmargosian.newsmapper.LATITUDE";


    private static final int REDDIT_LOCATIONS_REQUEST_LOADER_ID = 0;
    private static final int SUBBREDIT_TOPIC_LOADER_ID= 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //start the activity and the location client
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_mapper);
        mLocationClient = new LocationClient(this, this, this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        /*this checks whether the mapView has been opened. If it has, it sets the location to the
         *current view of the map, so the loader can get news items from the desired location
         */
        if (intent.getBooleanExtra(DisplayMapViewActivity.EXTRA_OPEN, false)) {
            lat = intent.getDoubleExtra(DisplayMapViewActivity.EXTRA_LAT, 40.101953);
            lng = intent.getDoubleExtra(DisplayMapViewActivity.EXTRA_LNG, -88.227152);
            locationString = String.valueOf(lat) + "," + String.valueOf(lng);
            getLoaderManager().initLoader(REDDIT_LOCATIONS_REQUEST_LOADER_ID, null, RedditLocationsRequestLoaderListener).forceLoad();
        }
        //otherwise it finds the users location to give them news for their current location
        else
            mLocationClient.connect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.news_mapper, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_map:
                openMap();
                return true;
            case R.id.action_settings:
                //openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //starts DisplayMapViewActivity and provides it with coordinates to center on
    public void openMap() {
        Intent intent = new Intent(NewsMapper.this, DisplayMapViewActivity.class);
        intent.putExtra(EXTRA_LATITUDE, lat);
        intent.putExtra(EXTRA_LONGITUDE, lng);
        startActivity(intent);
    }

    private LoaderManager.LoaderCallbacks<List<Subreddit>> RedditLocationsRequestLoaderListener
            = new LoaderManager.LoaderCallbacks<List<Subreddit>>() {
        @Override
        public AsyncTaskLoader<List<Subreddit>> onCreateLoader(int id, Bundle args) {
            //creates new NewsItemRequest for the loader
            return new RedditLocationsRequest(NewsMapper.this);
        }

        @Override
        public void onLoadFinished(Loader<List<Subreddit>> loader, List<Subreddit> list) {
            //after done loading, make a list of html formatted titles
            subreddits = list;
            subredditsLocationIn = new ArrayList<Subreddit>();
            LatLng latLng = new LatLng(lat, lng);
            for (int i = 0; i < subreddits.size(); i++) {
                if (subreddits.get(i).getBoundary().contains(latLng))
                    subredditsLocationIn.add(subreddits.get(i));
            }
            /*List<Spanned> titleList = new ArrayList<Spanned>();
            for (int i = 0; i < list.size(); i++) {
                Spanned t = Html.fromHtml(list.get(i).getTitle());
                titleList.add(t);
            }
            //create an ArrayAdapter using the titleList and set it to display on the listview;
            newsAdapter = new ArrayAdapter<Spanned>(this, android.R.layout.simple_list_item_1, titleList);
            ListView lvNews = (ListView) findViewById((R.id.lvNews));
            lvNews.setAdapter(newsAdapter);
            //when article title is clicked, open article in new webview activity
            lvNews.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                            Intent intent = new Intent(NewsMapper.this, DisplayWebViewActivity.class);
                            String url = newsItems.get(position).getUrl();
                            intent.putExtra(EXTRA_URL, url);
                            startActivity(intent);
                        }
                    }
            );
            */
        }

        //if the loader resets, set the listView adapter to the newsAdapter
        @Override
        public void onLoaderReset(Loader<List<Subreddit>> loader) {
            setContentView(R.layout.activity_news_mapper);
            ListView lvNews = (ListView) findViewById(R.id.lvNews);
            lvNews.setAdapter(newsAdapter);
        }
    };

    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }

    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        location = mLocationClient.getLastLocation();
        lat = location.getLatitude();
        lng = location.getLongitude();
        locationString = String.valueOf(lat) + "," + String.valueOf(lng);
        getLoaderManager().initLoader(SUBBREDIT_TOPIC_LOADER_ID, null, RedditLocationsRequestLoaderListener).forceLoad();

    }
    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
        // Display the connection status
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }
    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            //TODO:Create method
            //showErrorDialog(connectionResult.getErrorCode());
        }
    }

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    /*
     * Handle results returned to the FragmentActivity
     * by Google Play services
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {
            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
            /*
             * If the result code is Activity.RESULT_OK, try
             * to connect again
             */
                switch (resultCode) {
                    case Activity.RESULT_OK:
                    /*
                     * Try the request again
                     */
                        break;
                }
        }
    }
}