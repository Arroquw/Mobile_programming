package com.example.myfragmentapp.screens;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import com.example.myfragmentapp.R;

import java.util.Objects;

public class RssViewFragment extends Fragment {
    private WebView webView;
    public RssViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        webView = Objects.requireNonNull(getView()).findViewById(R.id.fragmentWebView);
        assert getArguments() != null;
        String postContent = RssViewFragmentArgs.fromBundle(getArguments()).getRssLink();
        if (Patterns.WEB_URL.matcher(postContent.toLowerCase()).matches()) {
            webView.loadUrl(postContent);
        } else {
            webView.loadData(postContent, "text/html; charset=utf-8", "utf-8");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rss_view, container, false);
    }
}
