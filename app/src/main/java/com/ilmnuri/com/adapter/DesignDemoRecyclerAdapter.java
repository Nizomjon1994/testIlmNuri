package com.ilmnuri.com.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ilmnuri.com.R;
import com.ilmnuri.com.model.AlbumModel;

import java.util.ArrayList;


public class DesignDemoRecyclerAdapter extends RecyclerView.Adapter<DesignDemoRecyclerAdapter.ViewHolder> {

    private ArrayList<AlbumModel> mItems;

    public interface OnItemClickListener {
        void onItemClick(AlbumModel item);
    }

    private OnItemClickListener listener;

    public DesignDemoRecyclerAdapter(ArrayList<AlbumModel> items, OnItemClickListener listener) {
        this.mItems = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.bind(mItems.get(i), listener);
        final AlbumModel item = mItems.get(i);
        viewHolder.mTextView.setText(item.getAlbum().replace("_", " "));
        viewHolder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTextView;

        ViewHolder(View v) {
            super(v);
            mTextView = (TextView)v.findViewById(R.id.list_item);
        }
        public void bind(final AlbumModel item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

}
