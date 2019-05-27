package com.example.rssreader.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.rssreader.R;
import com.example.rssreader.models.RssData;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class RssItemAdapter extends ArrayAdapter<RssData> {
    private Activity myContext;
    private ArrayList<RssData> rssDataArrayList;

    public RssItemAdapter(Context context, int textViewResourceId,
                          ArrayList<RssData> objects) {
        super(context, textViewResourceId, objects);
        myContext = (Activity) context;
        rssDataArrayList = objects;
    }

    static class ImageViewHolder {
        String imageURL;
        Bitmap bitmap;
        ImageView imageView;
    }

    static class ViewHolder {
        TextView postTitleView;
        TextView postDateView;
        ImageViewHolder postThumbView;

        ViewHolder() {
            postThumbView = new ImageViewHolder();
        }
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = myContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.rss_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.postThumbView.imageView = convertView
                    .findViewById(R.id.postThumb);
            viewHolder.postTitleView = convertView
                    .findViewById(R.id.postTitleLabel);
            viewHolder.postDateView = convertView
                    .findViewById(R.id.postDateLabel);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (rssDataArrayList.get(position).rssThumbUrl == null) {
            viewHolder.postThumbView.imageView
                    .setImageResource(R.drawable.ic_menu_gallery);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
            viewHolder.postThumbView.imageURL = rssDataArrayList.get(position).rssThumbUrl;
            new DownloadAsyncTask().execute(viewHolder.postThumbView);
        }

        viewHolder.postTitleView.setText(rssDataArrayList.get(position).rssTitle);
        viewHolder.postDateView.setText(rssDataArrayList.get(position).rssDate);

        return convertView;
    }

    private static class DownloadAsyncTask extends AsyncTask<ImageViewHolder, Void, ImageViewHolder> {

        @Override
        protected ImageViewHolder doInBackground(ImageViewHolder... params) {
            //load image directly
            ImageViewHolder viewHolder = params[0];
            try {
                URL imageURL = new URL(viewHolder.imageURL);
                viewHolder.bitmap = BitmapFactory.decodeStream(imageURL.openStream());
            } catch (IOException e) {
                Log.e("error", "Downloading Image Failed");
                viewHolder.bitmap = null;
            }

            return viewHolder;
        }

        @Override
        protected void onPostExecute(ImageViewHolder result) {
            if (result.bitmap == null) {
                result.imageView.setImageResource(R.drawable.ic_menu_gallery);
            } else {
                result.imageView.setImageBitmap(result.bitmap);
            }
        }
    }
}