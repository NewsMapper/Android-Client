package com.example.danielmargosian.newsmapper;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.IntentSender;
import android.content.Loader;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
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
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.model.LatLng;

public class NewsMapper extends Activity implements LoaderManager.LoaderCallbacks<List<NewsItem>>, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener{

    private Location mCurrentLocation;
    private LocationClient mLocationClient;
    private List<NewsItem> newsItems;
    private ArrayAdapter<Spanned> newsAdapter;
    private String location;
    private double lat;
    private double lng;

    public static final String EXTRA_URL = "com.example.danielmargosian.newsmapper.URL";
    public static final String EXTRA_LONGITUDE = "com.example.danielmargosian.newsmapper.LONGITUDE";
    public static final String EXTRA_LATITUDE = "com.example.danielmargosian.newsmapper.LATITUDE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //start the activity and the location client
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_mapper);mLocationClient = new LocationClient(this, this, this);

    }
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if (intent.getBooleanExtra(DisplayMapViewActivity.EXTRA_OPEN, false)) {
            lat = intent.getDoubleExtra(DisplayMapViewActivity.EXTRA_LAT, 40.101953);
            lng = intent.getDoubleExtra(DisplayMapViewActivity.EXTRA_LNG, -88.227152);
            location = String.valueOf(lat) + "," + String.valueOf(lng);
            getLoaderManager().initLoader(0, null, this).forceLoad();
        }
        // Connect the client.
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

    public void openMap() {
        Intent intent = new Intent(NewsMapper.this, DisplayMapViewActivity.class);
        intent.putExtra(EXTRA_LATITUDE, lat);
        intent.putExtra(EXTRA_LONGITUDE, lng);
        startActivity(intent);
    }

    @Override
    public AsyncTaskLoader<List<NewsItem>> onCreateLoader(int id, Bundle args) {
        //creates new NewsItemRequest for the loader
        return new NewsItemRequest(this, location);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> list) {
        //after done loading, make a list of html formatted titles
        newsItems = list;
        List<Spanned> titleList = new ArrayList<Spanned>();
        for (int i = 0; i < list.size(); i++)
        {
            Spanned t = Html.fromHtml(list.get(i).getTitle());
            titleList.add(t);
        }
        //create an ArrayAdapter using the titleList and set it to display on the listview;
        newsAdapter = new ArrayAdapter<Spanned>(this, android.R.layout.simple_list_item_1, titleList);
        ListView lvNews = (ListView) findViewById((R.id.lvNews));
        lvNews.setAdapter(newsAdapter);
        //when article title is clicked, open article in new webview activity
        lvNews.setOnItemClickListener(
                new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                        Intent intent = new Intent(NewsMapper.this, DisplayWebViewActivity.class);
                        String url = newsItems.get(position).getUrl();
                        intent.putExtra(EXTRA_URL,url);
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
        setContentView(R.layout.activity_news_mapper);
        ListView lvNews = (ListView) findViewById(R.id.lvNews);
        lvNews.setAdapter(newsAdapter);
    }
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }

    // Check for Google Play Services
    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }
    /*
     * Handle results returned to the FragmentActivity
     * by Google Play services
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {
            case CONNECTION_FAILURE_RESOLUTION_REQUEST :
            /*
             * If the result code is Activity.RESULT_OK, try
             * to connect again
             */
                switch (resultCode) {
                    case Activity.RESULT_OK :
                    /*
                     * Try the request again
                     */
                        break;
                }
        }
    }
    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates",
                    "Google Play services is available.");
            // Continue
            return true;
            // Google Play services was not available for some reason.
            // resultCode holds the error code.
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    resultCode,
                    this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment =
                        new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(getFragmentManager(),
                        "Location Updates");
            }

            return false;
        }
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
        mCurrentLocation = mLocationClient.getLastLocation();
        lat = mCurrentLocation.getLatitude();
        lng = mCurrentLocation.getLongitude();
        location = String.valueOf(lat) + "," + String.valueOf(lng);
        getLoaderManager().initLoader(0, null, this).forceLoad();

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
}
