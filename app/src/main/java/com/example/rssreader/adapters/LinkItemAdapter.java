package com.example.rssreader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rssreader.R;
import com.example.rssreader.models.LinkItem;

import java.util.ArrayList;

public class LinkItemAdapter extends RecyclerView.Adapter<LinkItemAdapter.ViewHolder> {
    private ArrayList<LinkItem> linkItemArrayList;
    private final LinkItemOnItemClickHandler mLinkItemOnItemClickHandler;

    public LinkItemAdapter(ArrayList<LinkItem> objects, LinkItemOnItemClickHandler linkItemOnItemClickHandler) {
        linkItemArrayList = objects;
        mLinkItemOnItemClickHandler = linkItemOnItemClickHandler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.link_item, parent, false);

        // Inflate the custom layout
        // Return a new holder instance
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LinkItem item = linkItemArrayList.get(position);
        holder.titleView.setText(item.Title);
        holder.linkView.setText(item.Link);
    }

    @Override
    public int getItemCount() {
        return linkItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView titleView;
        TextView linkView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.itemTitle);
            linkView = itemView.findViewById(R.id.itemLink);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            LinkItem item = linkItemArrayList.get(pos);
            mLinkItemOnItemClickHandler.onItemClick(item);
        }
    }

    public interface LinkItemOnItemClickHandler {
        void onItemClick(LinkItem item);
    }
}
