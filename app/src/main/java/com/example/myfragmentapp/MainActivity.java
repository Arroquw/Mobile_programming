package com.example.myfragmentapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.myfragmentapp.models.LinkItem;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/*            */

public class MainActivity extends AppCompatActivity implements ToolbarTitleListener {
    private Toolbar toolbar;
    public static ArrayList<LinkItem> urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!getListFromPreferences()) {
            urls = new ArrayList<>(Arrays.asList(
                    new LinkItem("LifeHacker RSS Feed", "https://lifehacker.com/rss"),
                    new LinkItem("Google News Feed", "https://news.google.com/news/rss"),
                    new LinkItem("BBC UK world news", "http://feeds.bbci.co.uk/news/world/rss.xml"),
                    new LinkItem("New York times world news", "https://www.nytimes.com/svc/collections/v1/publish/https://www.nytimes.com/section/world/rss.xml"),
                    new LinkItem("The Guardian world news", "https://www.theguardian.com/world/rss"),
                    new LinkItem("Reuters RSS Feed", "http://feeds.reuters.com/Reuters/worldNews"),
                    new LinkItem("Independent UK world news", "http://www.independent.co.uk/news/world/rss")));
        }
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavController navigationController = Navigation.findNavController(this, R.id.nav_host_fragment);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        NavigationUI.setupActionBarWithNavController(this, navigationController, drawer);
        NavigationUI.setupWithNavController(navigationView, navigationController);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController);
    }

    public void saveListInPreferences() {
        List<LinkItem> linkItems = urls;
        String json = new Gson().toJson(linkItems);
        SharedPreferences prefs = getSharedPreferences(getString(R.string.add_entry), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getString(R.string.add_entry), json);
        editor.apply();
    }

    public boolean getListFromPreferences() {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.add_entry), Context.MODE_PRIVATE);
        if (prefs.contains(getString(R.string.add_entry))) {
            String prefsString = prefs.getString(getString(R.string.add_entry), "");

            List<LinkItem> linkItemList =
                    new Gson().fromJson(prefsString, new TypeToken<List<LinkItem>>() {
                    }.getType());
            urls = (ArrayList<LinkItem>) linkItemList;
            return true;
        }
        return false;
    }

    @Override
    public void updateTitle(String title) {
        toolbar.setTitle(title);
    }

}



