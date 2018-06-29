package com.example.komal.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by Komal on 07-04-2018.
 */

public class Loader extends AsyncTaskLoader<List<NewsArray>> {

    private static final String LOG_TAG = Loader.class.getName();

    public Loader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsArray> loadInBackground() {
        List<NewsArray> newsList = null;
        try {
            URL url = Utils.createUrl();
            String jsonResponse = Utils.makeHttpRequest(url);
            newsList = Utils.parseJson(jsonResponse);
        } catch (IOException e) {
            Log.e("Utils", "Error in Loader LoadInBackground: ", e);
        }
        return newsList;
    }

}
