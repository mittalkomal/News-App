package com.example.komal.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Komal on 07-04-2018.
 */

public class Adapter extends ArrayAdapter<NewsArray> {
    public Adapter(Context context) {
        super(context, -1, new ArrayList<NewsArray>());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_view, parent, false);
        }
        NewsArray News = getItem(position);

        TextView title = listItemView.findViewById(R.id.title1);
        title.setText(News.getTitle());

        TextView author = listItemView.findViewById(R.id.author);
        author.setText(News.getAuthor());

        TextView date = listItemView.findViewById(R.id.date);
        date.setText(News.getDate());

        TextView section = listItemView.findViewById(R.id.section);
        section.setText(News.getSection());

        return listItemView;
    }


}
