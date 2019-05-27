package com.example.rssreader.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.rssreader.R;
import com.example.rssreader.models.LinkItem;

import java.util.ArrayList;

public class LinkItemAdapter extends ArrayAdapter<LinkItem> {
    private Activity myContext;
    private ArrayList<LinkItem> linkItemArrayList;

    public LinkItemAdapter(Context context, int textViewResourceId,
                           ArrayList<LinkItem> objects) {
        super(context, textViewResourceId, objects);
        myContext = (Activity) context;
        linkItemArrayList = objects;
    }

    static class ViewHolder {
        TextView titleView;
        TextView linkView;
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LinkItemAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = myContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.link_item, parent, false);

            viewHolder = new LinkItemAdapter.ViewHolder();
            viewHolder.titleView = convertView
                    .findViewById(R.id.itemTitle);
            viewHolder.linkView = convertView
                    .findViewById(R.id.itemLink);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (LinkItemAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.titleView.setText(linkItemArrayList.get(position).Title);
        viewHolder.linkView.setText(linkItemArrayList.get(position).Link);

        return convertView;
    }
}
