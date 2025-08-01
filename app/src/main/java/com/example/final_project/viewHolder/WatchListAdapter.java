package com.example.final_project.viewHolder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project.R;

import java.util.List;
public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.ViewHolder> {
    private List<WatchListItem> mData;
    private LayoutInflater mInflater;

    public WatchListAdapter(Context context, List<WatchListItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.watchlist_recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WatchListItem item = mData.get(position);
        holder.titleTextView.setText(item.getTitle());
        holder.genreTextView.setText(item.getGenre());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public WatchListItem getItem(int id) {
        return mData.get(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleTextView;
        TextView genreTextView;

        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleWatchListRecyclerItemTextView);
            genreTextView = itemView.findViewById(R.id.genreWatchListRecyclerItemTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Handle item click if needed
            int position = getAdapterPosition();
            Log.d("WatchListAdapter", "Clicked item at position " + position);
        }
    }
}

