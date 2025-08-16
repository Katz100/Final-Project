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
import com.example.final_project.database.UserCompletedMovies;
import java.util.List;

public class CompletedListAdapter extends RecyclerView.Adapter<CompletedListAdapter.ViewHolder>{
    private List<UserCompletedMovies> mData;
    private LayoutInflater mInflater;

    public CompletedListAdapter(Context context, List<UserCompletedMovies> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    public void updateUsersCompletedMovies(List<UserCompletedMovies> newList) {
        this.mData = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CompletedListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.completedlist_recycler_item, parent, false);
        return new CompletedListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CompletedListAdapter.ViewHolder holder, int position) {
        UserCompletedMovies item = mData.get(position);
        holder.titleTextView.setText(item.getTitle());
        holder.genreTextView.setText(item.getGenre());
        holder.ratingTextView.setText(String.valueOf(item.getRating()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public UserCompletedMovies getItem(int id) {
        return mData.get(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleTextView;
        TextView genreTextView;
        TextView ratingTextView;

        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleCompletedListRecyclerItemTextView);
            genreTextView = itemView.findViewById(R.id.genreCompletedListRecyclerItemTextView);
            ratingTextView = itemView.findViewById(R.id.ratingCompletedListRecyclerItemTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Handle item click if needed
            int position = getAdapterPosition();
            Log.d("CompletedWatchListAdapter", "Clicked item at position " + position);
        }
    }
}
