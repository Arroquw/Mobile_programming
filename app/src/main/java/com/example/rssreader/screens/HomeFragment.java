package com.example.rssreader.screens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.rssreader.MainActivity;
import com.example.rssreader.R;
import com.example.rssreader.adapters.LinkItemAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Objects;
import static com.example.rssreader.MainActivity.urls;

public class HomeFragment extends Fragment {
    private LinkItemAdapter itemAdapter;
    private ListView listView;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ((MainActivity) Objects.requireNonNull(getActivity())).saveListInPreferences();
        listView = view.findViewById(R.id.postListView);
        itemAdapter = new LinkItemAdapter(getActivity(), R.layout.link_item, urls);
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
                                    HomeFragmentDirections.actionHomeFragmentToRssFragment(message, "Custom RSS Link");
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

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity) Objects.requireNonNull(getActivity())).saveListInPreferences();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) Objects.requireNonNull(getActivity())).getListFromPreferences();

        if (itemAdapter != null) itemAdapter.notifyDataSetChanged();
        else {
            itemAdapter = new LinkItemAdapter(getActivity(), R.layout.link_item, urls);
            listView.setAdapter(itemAdapter);
            listView.setOnItemClickListener(onItemClickListener);
        }

    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HomeFragmentDirections.ActionHomeFragmentToRssFragment action =
                    HomeFragmentDirections.actionHomeFragmentToRssFragment(urls.get(position).Link, urls.get(position).Title);
            NavHostFragment.findNavController(HomeFragment.this).navigate(action);
        }
    };
}
