package com.example.rssreader.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rssreader.R;
import com.example.rssreader.models.RssData;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class RssItemAdapter extends RecyclerView.Adapter<RssItemAdapter.ViewHolder> {
    private ArrayList<RssData> rssDataArrayList;
    private final RssOnItemClickHandler mClickHandler;

    public RssItemAdapter(ArrayList<RssData> list, RssOnItemClickHandler clickHandler) {
        rssDataArrayList = list;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.rss_item, parent, false);

        // Inflate the custom layout
        // Return a new holder instance
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
        RssData data = rssDataArrayList.get(position);

        // Set item views based on your views and data model
        TextView titleView = holder.postTitleView;
        titleView.setText(data.rssTitle);

        TextView dateView = holder.postDateView;
        dateView.setText(data.rssDate);

        ImageViewHolder image = holder.postThumbView;
        image.imageURL = data.rssThumbUrl;

        if (data.rssThumbUrl == null) {
            image.imageView
                    .setImageResource(R.drawable.ic_menu_gallery);
        } else {

            image.imageURL = data.rssThumbUrl;
            new DownloadAsyncTask().execute(image);
        }
    }

    @Override
    public int getItemCount() {
        return rssDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView postTitleView;
        TextView postDateView;
        ImageViewHolder postThumbView;

        ViewHolder(View v) {
            super(v);
            postTitleView = v.findViewById(R.id.postTitleLabel);
            postDateView = v.findViewById(R.id.postDateLabel);
            postThumbView = new ImageViewHolder();
            postThumbView.imageView = v.findViewById(R.id.postThumb);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            RssData data = rssDataArrayList.get(pos);
            mClickHandler.onItemClick(data);
        }
    }

    static class ImageViewHolder {
        String imageURL;
        Bitmap bitmap;
        ImageView imageView;
    }

    public interface RssOnItemClickHandler {
        void onItemClick(RssData data);
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