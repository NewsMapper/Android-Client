package com.example.danielmargosian.newsreader;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class NewsScrape extends Activity implements LoaderManager.LoaderCallbacks<List<NewsItem>> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_scrape);
        getLoaderManager().initLoader(0, null, this).forceLoad();

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

    @Override
    public AsyncTaskLoader<List<NewsItem>> onCreateLoader(int id, Bundle args) {
        return new NewsItemRequest(this);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> list) {
        List<Spanned> titleList = new ArrayList<Spanned>();
        for (int i = 0; i < list.size(); i++)
        {
            Spanned t = Html.fromHtml(list.get(i).getTitle());
            titleList.add(t);
        }
        ListView lvNews = (ListView) findViewById((R.id.lvNews));
        ArrayAdapter<Spanned> newsAdapter = new ArrayAdapter<Spanned>(this, android.R.layout.simple_list_item_1, titleList);
        lvNews.setAdapter((newsAdapter));
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
        final ListView listview = (ListView) findViewById(R.id.lvNews);
        listview.setAdapter(null);
    }
}
