package com.example.rssreader.screens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rssreader.MainActivity;
import com.example.rssreader.R;
import com.example.rssreader.adapters.LinkItemAdapter;
import com.example.rssreader.models.LinkItem;

import java.util.Objects;
import static com.example.rssreader.MainActivity.urls;

public class HomeFragment extends Fragment implements LinkItemAdapter.LinkItemOnItemClickHandler {
    private LinkItemAdapter itemAdapter;
    private RecyclerView recyclerView;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((MainActivity) Objects.requireNonNull(getActivity())).saveListInPreferences();
        recyclerView = getActivity().findViewById(R.id.postListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        itemAdapter = new LinkItemAdapter(urls, this);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setHasFixedSize(true);

        itemAdapter.notifyDataSetChanged();

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
            itemAdapter = new LinkItemAdapter(urls, this);
            recyclerView.setAdapter(itemAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

    }

    @Override
    public void onItemClick(LinkItem item) {
        HomeFragmentDirections.ActionHomeFragmentToRssFragment action =
                HomeFragmentDirections.actionHomeFragmentToRssFragment(item.Link, item.Title);
        NavHostFragment.findNavController(HomeFragment.this).navigate(action);
    }
}
