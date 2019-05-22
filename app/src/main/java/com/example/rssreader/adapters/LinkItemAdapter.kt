package com.example.rssreader.adapters

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

import com.example.rssreader.R
import com.example.rssreader.models.LinkItem

import java.util.ArrayList

class LinkItemAdapter(context: Context, textViewResourceId: Int,
                      private val linkItemArrayList: ArrayList<LinkItem>) : ArrayAdapter<LinkItem>(context, textViewResourceId, linkItemArrayList) {
    private val myContext: Activity = context as Activity

    internal class ViewHolder {
        var titleView: TextView? = null
        var linkView: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView1 = convertView
        val viewHolder: ViewHolder

        if (convertView1 == null) {
            val inflater = myContext.layoutInflater
            convertView1 = inflater.inflate(R.layout.link_item, parent, false)

            viewHolder = ViewHolder()
            viewHolder.titleView = convertView1!!
                    .findViewById(R.id.itemTitle)
            viewHolder.linkView = convertView1
                    .findViewById(R.id.itemLink)
            convertView1.tag = viewHolder
        } else {
            viewHolder = convertView1.tag as ViewHolder
        }

        viewHolder.titleView!!.text = linkItemArrayList[position].Title
        viewHolder.linkView!!.text = linkItemArrayList[position].Link

        return convertView1
    }
}
