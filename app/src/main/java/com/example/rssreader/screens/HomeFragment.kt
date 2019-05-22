package com.example.rssreader.screens

import android.app.AlertDialog
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.rssreader.MainActivity
import com.example.rssreader.MainActivity.Companion.urls
import com.example.rssreader.R
import com.example.rssreader.adapters.LinkItemAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

class HomeFragment : Fragment() {
    private var itemAdapter: LinkItemAdapter? = null
    private var listView: ListView? = null

    private val onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
        val action = HomeFragmentDirections.actionHomeFragmentToRssFragment(urls!![position].Link, urls!![position].Title)
        NavHostFragment.findNavController(this@HomeFragment).navigate(action)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        (Objects.requireNonNull<FragmentActivity>(activity) as MainActivity).saveListInPreferences()
        listView = view.findViewById(R.id.postListView)
        itemAdapter = activity?.let { urls?.let { it1 -> LinkItemAdapter(it, R.layout.link_item, it1) } }
        listView!!.adapter = itemAdapter
        listView!!.onItemClickListener = onItemClickListener
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val button1 = button2
        button1.setOnClickListener {
            val alert = AlertDialog.Builder(activity)
            alert.setTitle("Custom link")
            alert.setMessage("Put your custom RSS link here!")
            val input = EditText(activity)
            alert.setView(input)
            alert.setPositiveButton("Ok") { _, _ ->
                val message = input.text.toString()
                if (Patterns.WEB_URL.matcher(message.toLowerCase()).matches() && message.toLowerCase().contains("rss")) {
                    val action = HomeFragmentDirections.actionHomeFragmentToRssFragment(message, "Custom RSS Link")
                    NavHostFragment.findNavController(this@HomeFragment).navigate(action)
                } else {
                    Toast.makeText(activity, "custom RSS link invalid!", Toast.LENGTH_LONG).show()
                }
            }

            alert.show()
        }
    }

    override fun onPause() {
        super.onPause()
        (Objects.requireNonNull<FragmentActivity>(activity) as MainActivity).saveListInPreferences()
    }

    override fun onResume() {
        super.onResume()
        (Objects.requireNonNull<FragmentActivity>(activity) as MainActivity).listFromPreferences

        if (itemAdapter != null)
            itemAdapter!!.notifyDataSetChanged()
        else {
            itemAdapter = activity?.let { urls?.let { it1 -> LinkItemAdapter(it, R.layout.link_item, it1) } }
            listView!!.adapter = itemAdapter
            listView!!.onItemClickListener = onItemClickListener
        }

    }
}
