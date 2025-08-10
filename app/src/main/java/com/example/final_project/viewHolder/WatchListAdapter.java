package com.example.final_project.viewHolder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project.R;
import com.example.final_project.database.UsersMovies;

import java.util.List;
public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.ViewHolder> {
    private List<UsersMovies> mData;
    private LayoutInflater mInflater;

    public interface OnCheckedChangeListener {
        void onItemCheckedChanged(UsersMovies item, boolean isChecked);
    }

    private OnCheckedChangeListener checkedChangeListener;

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.checkedChangeListener = listener;
    }

    public WatchListAdapter(Context context, List<UsersMovies> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    public void updateUsersMovies(List<UsersMovies> newList) {
        this.mData = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.watchlist_recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UsersMovies item = mData.get(position);
        holder.titleTextView.setText(item.getTitle());
        holder.genreTextView.setText(item.getGenre());

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(item.isCompleted());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setCompleted(isChecked);
            Log.d("WatchListAdapter", "Item " + item.getTitle() + " checked: " + isChecked);
            if (checkedChangeListener != null) {
                checkedChangeListener.onItemCheckedChanged(item, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public UsersMovies getItem(int id) {
        return mData.get(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleTextView;
        TextView genreTextView;

        CheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleWatchListRecyclerItemTextView);
            genreTextView = itemView.findViewById(R.id.genreWatchListRecyclerItemTextView);
            checkBox = itemView.findViewById(R.id.watchListCheckBox);
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

