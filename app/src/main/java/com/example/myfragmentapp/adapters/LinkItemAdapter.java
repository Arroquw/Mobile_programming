package com.example.myfragmentapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.myfragmentapp.R;
import com.example.myfragmentapp.models.LinkItem;

import java.util.ArrayList;

public class LinkItemAdapter extends ArrayAdapter<LinkItem> {
    private Activity myContext;
    private ArrayList<LinkItem> datas;

    public LinkItemAdapter(Context context, int textViewResourceId,
                   ArrayList<LinkItem> objects) {
        super(context, textViewResourceId, objects);
        // TODO Auto-generated constructor stub
        myContext = (Activity) context;
        datas = objects;
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
            convertView = inflater.inflate(R.layout.link_item, null);

            viewHolder = new LinkItemAdapter.ViewHolder();
            viewHolder.titleView = (TextView) convertView
                    .findViewById(R.id.itemTitle);
            viewHolder.linkView = (TextView) convertView
                    .findViewById(R.id.itemLink);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (LinkItemAdapter.ViewHolder) convertView.getTag();
        }


        viewHolder.titleView.setText(datas.get(position).Title);
        viewHolder.linkView.setText(datas.get(position).Link);

        return convertView;
    }
}
