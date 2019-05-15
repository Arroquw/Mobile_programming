package com.example.myfragmentapp.screens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.navigation.fragment.NavHostFragment;

import com.example.myfragmentapp.R;
import com.example.myfragmentapp.adapters.LinkItemAdapter;
import com.example.myfragmentapp.models.LinkItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private ArrayList<LinkItem> urls = new ArrayList<>(Arrays.asList(
            new LinkItem("Lifehacker RSS Feed", "https://lifehacker.com/rss"),
            new LinkItem("Google News Feed", "https://news.google.com/news/rss"),
            new LinkItem("BBC UK world news", "http://feeds.bbci.co.uk/news/world/rss.xml"),
            new LinkItem("New York times world news", "https://www.nytimes.com/svc/collections/v1/publish/https://www.nytimes.com/section/world/rss.xml"),
            new LinkItem("The Guardian world news", "https://www.theguardian.com/world/rss"),
            new LinkItem("Reuters RSS Feed", "http://feeds.reuters.com/Reuters/worldNews"),
            new LinkItem("Independent UK world news", "http://www.independent.co.uk/news/world/rss")));

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ListView listView = view.findViewById(R.id.postListView);
        //ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.link_item, urls);
        LinkItemAdapter itemAdapter = new LinkItemAdapter(getActivity(), R.layout.link_item, urls);
        listView.setAdapter(itemAdapter);
        listView.setOnItemClickListener(onItemClickListener);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button button1 = Objects.requireNonNull(getActivity()).findViewById(R.id.button2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Custom link");
                alert.setMessage("Put your custom RSS link here!");
                final EditText input = new EditText(getActivity());
                alert.setView(input);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String message = input.getText().toString();
                        if (Patterns.WEB_URL.matcher(message.toLowerCase()).matches() && message.toLowerCase().contains("rss")) {
                            HomeFragmentDirections.ActionHomeFragmentToRssFragment action =
                                    HomeFragmentDirections.actionHomeFragmentToRssFragment(message);
                            NavHostFragment.findNavController(HomeFragment.this).navigate(action);
                        } else {
                            Toast.makeText(getActivity(), "custom RSS link invalid!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                alert.show();
            }
        });
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HomeFragmentDirections.ActionHomeFragmentToRssFragment action =
                    HomeFragmentDirections.actionHomeFragmentToRssFragment(urls.get(position).Link);
            NavHostFragment.findNavController(HomeFragment.this).navigate(action);
        }
    };
}
