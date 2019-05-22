package com.example.rssreader


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI

import com.example.rssreader.models.LinkItem
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.util.ArrayList
import java.util.Arrays

/*            */

class MainActivity : AppCompatActivity(), ToolbarTitleListener {
    private var toolbar: Toolbar? = null

    val listFromPreferences: Boolean
        get() {
            val prefs = getSharedPreferences(getString(R.string.add_entry), Context.MODE_PRIVATE)
            if (prefs.contains(getString(R.string.add_entry))) {
                val prefsString = prefs.getString(getString(R.string.add_entry), "")

                val linkItemList = Gson().fromJson<List<LinkItem>>(prefsString, object : TypeToken<List<LinkItem>>() {

                }.type)
                urls = linkItemList as ArrayList<LinkItem>
                return true
            }
            return false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!listFromPreferences) {
            urls = ArrayList(Arrays.asList(
                    LinkItem("LifeHacker RSS Feed", "https://lifehacker.com/rss"),
                    LinkItem("Google News Feed", "https://news.google.com/news/rss"),
                    LinkItem("BBC UK world news", "http://feeds.bbci.co.uk/news/world/rss.xml"),
                    LinkItem("New York times world news", "https://www.nytimes.com/svc/collections/v1/publish/https://www.nytimes.com/section/world/rss.xml"),
                    LinkItem("The Guardian world news", "https://www.theguardian.com/world/rss"),
                    LinkItem("Reuters RSS Feed", "http://feeds.reuters.com/Reuters/worldNews"),
                    LinkItem("Independent UK world news", "http://www.independent.co.uk/news/world/rss")))
        }
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navigationController = Navigation.findNavController(this, R.id.nav_host_fragment)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        NavigationUI.setupActionBarWithNavController(this, navigationController, drawer)
        NavigationUI.setupWithNavController(navigationView, navigationController)

        val toggle = ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), findViewById<View>(R.id.drawer_layout) as DrawerLayout)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        return NavigationUI.onNavDestinationSelected(item, navController)
    }

    fun saveListInPreferences() {
        val linkItems = urls
        val json = Gson().toJson(linkItems)
        val prefs = getSharedPreferences(getString(R.string.add_entry), Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(getString(R.string.add_entry), json)
        editor.apply()
    }

    fun deleteListFromPreferences() {
        val prefs = getSharedPreferences(getString(R.string.add_entry), Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
        urls = null
        urls = ArrayList(Arrays.asList(
                LinkItem("LifeHacker RSS Feed", "https://lifehacker.com/rss"),
                LinkItem("Google News Feed", "https://news.google.com/news/rss"),
                LinkItem("BBC UK world news", "http://feeds.bbci.co.uk/news/world/rss.xml"),
                LinkItem("New York times world news", "https://www.nytimes.com/svc/collections/v1/publish/https://www.nytimes.com/section/world/rss.xml"),
                LinkItem("The Guardian world news", "https://www.theguardian.com/world/rss"),
                LinkItem("Reuters RSS Feed", "http://feeds.reuters.com/Reuters/worldNews"),
                LinkItem("Independent UK world news", "http://www.independent.co.uk/news/world/rss")))
    }

    override fun updateTitle(title: String) {
        toolbar!!.title = title
    }

    companion object {
        var urls: ArrayList<LinkItem>? = null
    }

}



