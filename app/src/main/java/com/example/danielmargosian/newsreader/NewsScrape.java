package com.example.danielmargosian.newsreader;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import javax.json.*;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class NewsScrape extends Activity {

    ArrayAdapter<String> newsAdapter;
    ListView lvNews;
    //NewsItemRequest request;
    JsonArray newsItems;
    List<String> titleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_scrape);
        lvNews = (ListView) findViewById((R.id.lvNews));
        //request = new NewsItemRequest();

        //request.execute();
        titleList = new ArrayList<String>();
        newsItems = Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("title", "breaking news"))
                .add(Json.createObjectBuilder()
                        .add("title", "lame news"))
                .build();
        titleList = getTitleList();
        newsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titleList);
        lvNews.setAdapter((newsAdapter));
        titleList.add("Hello world");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news_scrape, menu);
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

    public List<String> getTitleList()
    {
        for (int i = 0; i < newsItems.size(); i++)
        {
            String t = newsItems.getJsonObject(i).getString("title");
            titleList.add(t);
        }
        return titleList;
    }
}
