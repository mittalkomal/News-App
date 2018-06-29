package com.example.komal.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.content.Loader;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsArray>>, SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout RefreshSwipe;
    private static final int NEWS_LOADER_ID = 0;
    private Adapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RefreshSwipe = findViewById(R.id.RefreshSwipe);
        RefreshSwipe.setOnRefreshListener(this);

        ListView newslistView = findViewById(R.id.list_view);
        newsAdapter = new Adapter(this);
        newslistView.setAdapter(newsAdapter);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loader = getLoaderManager();
            loader.initLoader(NEWS_LOADER_ID, null, MainActivity.this);

           newslistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    NewsArray news = newsAdapter.getItem(i);
                    String detailurl = news.getURL();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(detailurl));
                    startActivity(intent);
                }
            });

        } else {
        }
    }

    @Override
    public Loader<List<NewsArray>> onCreateLoader(int id, Bundle args) {
        return new com.example.komal.newsapp.Loader(this);
    }
    @Override
    public void onLoadFinished(Loader<List<NewsArray>> loader, List<NewsArray> news) {
        RefreshSwipe.setRefreshing(false);
        if (news != null) {
            newsAdapter.setNotifyOnChange(false);
            newsAdapter.clear();
            newsAdapter.setNotifyOnChange(true);
            newsAdapter.addAll(news);
        }

    }

    @Override
    public void onRefresh() {
        LoaderManager loader = getLoaderManager();
        loader.restartLoader(NEWS_LOADER_ID, null, MainActivity.this);
    }

    @Override
    public void onLoaderReset(Loader<List<NewsArray>> loader) {

    }

}