package com.example.rssreader.adapters

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

import com.example.rssreader.R
import com.example.rssreader.models.RssData

import java.io.IOException
import java.net.URL
import java.util.ArrayList

class RssItemAdapter(context: Context, textViewResourceId: Int,
                     private val rssDataArrayList: ArrayList<RssData>) : ArrayAdapter<RssData>(context, textViewResourceId, rssDataArrayList) {
    private val myContext: Activity = context as Activity

    internal class ImageViewHolder {
        var imageURL: String? = null
        var bitmap: Bitmap? = null
        var imageView: ImageView? = null
    }

    internal class ViewHolder {
        var postTitleView: TextView? = null
        var postDateView: TextView? = null
        var postThumbView: ImageViewHolder = ImageViewHolder()

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView1 = convertView
        var viewHolder: ViewHolder

        if (convertView1 == null) {
            val inflater = myContext.layoutInflater
            convertView1 = inflater.inflate(R.layout.rss_item, parent, false)

            viewHolder = ViewHolder()
            viewHolder.postThumbView.imageView = convertView1!!
                    .findViewById(R.id.postThumb)
            viewHolder.postTitleView = convertView1
                    .findViewById(R.id.postTitleLabel)
            viewHolder.postDateView = convertView1
                    .findViewById(R.id.postDateLabel)
            convertView1.tag = viewHolder
        } else {
            viewHolder = convertView1.tag as ViewHolder
        }

        if (rssDataArrayList[position].rssThumbUrl == null) {
            viewHolder.postThumbView.imageView!!
                    .setImageResource(R.drawable.ic_menu_gallery)
        } else {
            viewHolder = convertView1.tag as ViewHolder
            viewHolder.postThumbView.imageURL = rssDataArrayList[position].rssThumbUrl
            DownloadAsyncTask().execute(viewHolder.postThumbView)
        }

        viewHolder.postTitleView!!.text = rssDataArrayList[position].rssTitle
        viewHolder.postDateView!!.text = rssDataArrayList[position].rssDate

        return convertView1
    }

    private class DownloadAsyncTask : AsyncTask<ImageViewHolder, Void, ImageViewHolder>() {

        override fun doInBackground(vararg params: ImageViewHolder): ImageViewHolder {
            //load image directly
            val viewHolder = params[0]
            try {
                val imageURL = URL(viewHolder.imageURL)
                viewHolder.bitmap = BitmapFactory.decodeStream(imageURL.openStream())
            } catch (e: IOException) {
                Log.e("error", "Downloading Image Failed")
                viewHolder.bitmap = null
            }

            return viewHolder
        }

        override fun onPostExecute(result: ImageViewHolder) {
            if (result.bitmap == null) {
                result.imageView!!.setImageResource(R.drawable.ic_launcher_foreground)
            } else {
                result.imageView!!.setImageBitmap(result.bitmap)
            }
        }
    }
}