package com.example.rssreader.screens;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rssreader.MainActivity;
import com.example.rssreader.R;

import java.util.Objects;

public class RssViewFragment extends Fragment {

    public RssViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        WebView webView = Objects.requireNonNull(getView()).findViewById(R.id.fragmentWebView);
        assert getArguments() != null;
        RssViewFragmentArgs args = RssViewFragmentArgs.fromBundle(getArguments());

        String postContent = args.getRssLink();
        String postTitle = args.getRssTitle();

        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        activity.updateTitle(postTitle);

        if (Patterns.WEB_URL.matcher(postContent.toLowerCase()).matches()) {
            webView.loadUrl(postContent);
        } else {
            webView.loadData(postContent, "text/html; charset=utf-8", "utf-8");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rss_view, container, false);
    }
}
