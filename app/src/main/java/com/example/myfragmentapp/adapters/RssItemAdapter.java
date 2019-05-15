package com.example.myfragmentapp.adapters;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myfragmentapp.R;
import com.example.myfragmentapp.models.RssData;

import java.util.ArrayList;

public class RssItemAdapter extends ArrayAdapter<RssData> {
    private Activity myContext;
    private ArrayList<RssData> datas;

    public RssItemAdapter(Context context, int textViewResourceId,
                   ArrayList<RssData> objects) {
        super(context, textViewResourceId, objects);
        // TODO Auto-generated constructor stub
        myContext = (Activity) context;
        datas = objects;
    }

    static class ViewHolder {
        TextView postTitleView;
        TextView postDateView;
        ImageView postThumbView;
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = myContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.rss_item, null);

            viewHolder = new ViewHolder();
            viewHolder.postThumbView = (ImageView) convertView
                    .findViewById(R.id.postThumb);
            viewHolder.postTitleView = (TextView) convertView
                    .findViewById(R.id.postTitleLabel);
            viewHolder.postDateView = (TextView) convertView
                    .findViewById(R.id.postDateLabel);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (datas.get(position).rssThumbUrl == null) {
            viewHolder.postThumbView
                    .setImageResource(R.drawable.ic_menu_gallery);
        }

        viewHolder.postTitleView.setText(datas.get(position).rssTitle);
        viewHolder.postDateView.setText(datas.get(position).rssDate);

        return convertView;
    }
}