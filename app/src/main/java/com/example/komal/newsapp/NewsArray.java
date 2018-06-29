package com.example.komal.newsapp;

/**
 * Created by Komal on 07-04-2018.
 */
public class NewsArray {
    private String Title;
    private String author;
    private String date;
    private String section;
    private String URL;

    public NewsArray(String newsTitle, String newsAuthor, String newsSection, String newsDate, String newsURL) {

        Title = newsTitle; author = newsAuthor; section = newsSection; date = newsDate; URL = newsURL;
    }

    public String getAuthor() {
        return author;
    }
    public String getTitle() {
        return Title;
    }
    public String getDate() {
        return date;
    }

    public String getSection() {
        return section;
    }

    public String getURL() {
        return URL;
    }


}
