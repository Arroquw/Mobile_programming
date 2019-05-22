package com.example.rssreader.screens

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment

import com.example.rssreader.MainActivity
import com.example.rssreader.R
import com.example.rssreader.adapters.RssItemAdapter
import com.example.rssreader.models.RssData
import kotlinx.android.synthetic.main.rss_list.*

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory

import java.io.IOException
import java.io.InputStream
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Locale
import java.util.Objects


//

class RssFragment : Fragment() {
    private var listData: ArrayList<RssData>? = null
    private var mContext: Context? = null
    private var itemAdapter: RssItemAdapter? = null

    private val onItemClickListener = AdapterView.OnItemClickListener { _, _, arg2, _ ->
        val data: RssData = listData!![arg2]
        val action = if (data.rssContent == null) {
            RssFragmentDirections.actionRssFragmentToRssViewFragment(data.rssLink!!, data.rssTitle!!)
        } else {
            RssFragmentDirections.actionRssFragmentToRssViewFragment(data.rssContent!!, data.rssTitle!!)
        }
        NavHostFragment.findNavController(this@RssFragment).navigate(action)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.rss_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        assert(arguments != null)
        val args = RssFragmentArgs.fromBundle(arguments!!)
        val rssString = args.feedLink
        val rssTitle = args.feedTitle

        val activity = (activity as MainActivity?)!!
        activity.updateTitle(rssTitle)
        resetListener(rssString)
        val pullToRefresh = pullToRefresh
        pullToRefresh.setOnRefreshListener {
            resetListener(rssString) // your code
            pullToRefresh.isRefreshing = false
        }


    }

    private fun resetListener(link: String) {
        listData = ArrayList()
        val controller = RssDataController(this)
        controller.execute(link)
        val listView = Objects.requireNonNull<View>(view).findViewById<ListView>(R.id.rss_list_view)
        itemAdapter = mContext?.let { RssItemAdapter(it, R.layout.rss_item, listData!!) }
        listView.adapter = itemAdapter
        listView.onItemClickListener = onItemClickListener
    }

    private enum class RSSXMLTag {
        TITLE, DATE, LINK, CONTENT, IGNORETAG
    }

    private class RssDataController internal constructor(context: RssFragment) : AsyncTask<String, Int, ArrayList<RssData>>() {
        private var currentTag: RSSXMLTag? = null
        private val fragmentReference: WeakReference<RssFragment> = WeakReference(context)

        override fun doInBackground(vararg params: String): ArrayList<RssData> {
            val urlStr = params[0]
            val `is`: InputStream
            val rssDataList = ArrayList<RssData>()
            try {
                val url = URL(urlStr)
                val connection = url
                        .openConnection() as HttpURLConnection
                connection.readTimeout = 10 * 1000
                connection.connectTimeout = 10 * 1000
                connection.requestMethod = "GET"
                connection.doInput = true
                connection.connect()
                val response = connection.responseCode
                Log.d("debug", "The response is: $response")
                `is` = connection.inputStream

                // parse xml after getting the data
                val factory = XmlPullParserFactory
                        .newInstance()
                factory.isNamespaceAware = true
                val xpp = factory.newPullParser()
                xpp.setInput(`is`, null)

                var eventType = xpp.eventType
                var pdData: RssData? = null
                val dateFormat = SimpleDateFormat(
                        "EEE, dd MMM yyyy HH:mm:ss", Locale.getDefault())
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        when (xpp.name) {
                            "item" -> {
                                pdData = RssData()
                                currentTag = RSSXMLTag.IGNORETAG
                            }
                            "title" -> currentTag = RSSXMLTag.TITLE
                            "link" -> currentTag = RSSXMLTag.LINK
                            "pubDate" -> currentTag = RSSXMLTag.DATE
                            "content" -> currentTag = RSSXMLTag.CONTENT
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        if (xpp.name == "item") {
                            // format the data here, otherwise format data in
                            // Adapter
                            assert(pdData != null)
                            val postDate = dateFormat.parse(pdData!!.rssDate)
                            pdData.rssDate = dateFormat.format(postDate)
                            rssDataList.add(pdData)
                        } else {
                            currentTag = RSSXMLTag.IGNORETAG
                        }
                    } else if (eventType == XmlPullParser.TEXT) {
                        var content = xpp.text
                        content = content.trim { it <= ' ' }
                        Log.d("debug", content)
                        val sb: StringBuilder
                        if (pdData != null) {
                            when (currentTag) {
                                RSSXMLTag.TITLE -> if (content.isNotEmpty()) {
                                    if (pdData.rssTitle != null) {
                                        sb = StringBuilder(pdData.rssTitle!!)
                                        sb.append(content)
                                        pdData.rssTitle = sb.toString()
                                    } else {
                                        pdData.rssTitle = content
                                    }
                                }
                                RSSXMLTag.LINK -> if (content.isNotEmpty()) {
                                    if (pdData.rssLink != null) {
                                        sb = StringBuilder(pdData.rssLink!!)
                                        sb.append(content)
                                        pdData.rssLink = sb.toString()
                                    } else {
                                        pdData.rssLink = content
                                    }
                                }
                                RSSXMLTag.DATE -> if (content.isNotEmpty()) {
                                    if (pdData.rssDate != null) {
                                        sb = StringBuilder(pdData.rssDate!!)
                                        sb.append(content)
                                        pdData.rssDate = sb.toString()
                                    } else {
                                        pdData.rssDate = content
                                    }
                                }
                                else -> {
                                }
                            }
                        }
                    }
                    if (currentTag == RSSXMLTag.CONTENT) {
                        assert(pdData != null)
                        if (xpp.attributeCount > 0) {
                            pdData!!.rssThumbUrl = xpp.getAttributeValue(0)
                        }
                    }
                    eventType = xpp.next()
                }
                Log.v("tst", rssDataList.size.toString())
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: XmlPullParserException) {
                e.printStackTrace()
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return rssDataList
        }

        override fun onPostExecute(result: ArrayList<RssData>) {
            val fragment = fragmentReference.get()

            fragment?.listData!!.addAll(result)

            fragment.itemAdapter!!.notifyDataSetChanged()
        }
    }
}
