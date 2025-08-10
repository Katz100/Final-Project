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
import com.example.final_project.database.entities.User;

import java.util.List;

public class AdminListAdapter extends RecyclerView.Adapter<AdminListAdapter.ViewHolder> {

    private List<User> mData;
    private LayoutInflater mInflater;

    public interface OnCheckedChangeListener {
        void onItemCheckedChanged(User item, boolean isChecked);
    }

    private AdminListAdapter.OnCheckedChangeListener checkedChangeListener;

    public AdminListAdapter(Context context, List<User> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    public void updateUsers(List<User> newList) {
        this.mData = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdminListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_admin_user_recycler_item, parent, false);
        return new AdminListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdminListAdapter.ViewHolder holder, int position) {
        User item = mData.get(position);
        holder.userTextView.setText(item.getUsername());
        holder.isAdminTextView.setText(item.isAdmin()? "Admin: Yes" : "Admin: No");

        holder.checkBox.setOnCheckedChangeListener(null);
        /* will need to be changed to current selected user
        holder.checkBox.setChecked(item.isAdmin());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setCompleted(isChecked);
            Log.d("WatchListAdapter", "Item " + item.getTitle() + " checked: " + isChecked);
            if (checkedChangeListener != null) {
                checkedChangeListener.onItemCheckedChanged(item, isChecked);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public User getItem(int id) {
        return mData.get(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView userTextView;
        TextView isAdminTextView;

        CheckBox checkBox;
        ViewHolder(View itemView)
        {
            super(itemView);
            userTextView = itemView.findViewById(R.id.usernameUserRecyclerItemTextView);
            isAdminTextView = itemView.findViewById(R.id.isAdminUserRecyclerItemTextView);
            checkBox = itemView.findViewById(R.id.userCheckBox);
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
