package com.example.komal.newsapp;

import android.net.Uri;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Komal on 07-04-2018.
 */

public class Utils {

    public static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("mainActivity", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("Utils", "Problem retrieving the news results ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    public static URL createUrl() {
        String Urlstring = UrlString();
        try {
            return new URL(Urlstring);
        } catch (MalformedURLException e) {
            Log.e("Utils", "Error generating URL: ", e);
            return null;
        }
    }

    private static String UrlString() {
        Uri.Builder newsbuilder = new Uri.Builder();
        newsbuilder.scheme("https")
                .encodedAuthority("content.guardianapis.com").appendPath("search").appendQueryParameter("order-by", "newest")
                .appendQueryParameter("show-references", "author").appendQueryParameter("show-tags", "contributor")
                .appendQueryParameter("api-key", "test");
        String url = newsbuilder.build().toString();
        return url;
    }

    private static String formatNewsDate(String Date) {
        String jsonDateformat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat jsonFormatter = new SimpleDateFormat(jsonDateformat, Locale.US);
        try {
            Date parsedJsonDate = jsonFormatter.parse(Date);
            String finalDatePattern = "MMM d, yyy";
            SimpleDateFormat finalDateFormatter = new SimpleDateFormat(finalDatePattern, Locale.US);
            return finalDateFormatter.format(parsedJsonDate);
        } catch (ParseException e) {
            Log.e("QueryUtils", "Error generating JSON date: ", e);
            return "";
        }
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<NewsArray> parseJson(String response) {
        ArrayList<NewsArray> NewsList = new ArrayList<>();
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONObject jsonResult = jsonResponse.getJSONObject("response");
            JSONArray resultArray = jsonResult.getJSONArray("results");

            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject result = resultArray.getJSONObject(i);

                String title = result.getString("webTitle");

                String newsDate = result.getString("webPublicationDate");
                newsDate = formatNewsDate(newsDate);

                String newsSection = result.getString("sectionName");

                JSONArray authorArray = result.getJSONArray("tags");
                String newsAuthor = "";
                if (authorArray.length() == 0) {
                    newsAuthor = null;
                } else {
                    for (int j = 0; j < authorArray.length(); j++) {
                        JSONObject firstObject = authorArray.getJSONObject(j);
                        newsAuthor = newsAuthor + firstObject.getString("webTitle") + ", ";
                    }
                }
                String newsUrl = result.getString("webUrl");
                NewsList.add(new NewsArray(title, newsAuthor, newsDate, newsSection, newsUrl));
            }
        } catch (JSONException e) {
            Log.e("Utils", "Error generating JSON response", e);
        }
        return NewsList;
    }

}
