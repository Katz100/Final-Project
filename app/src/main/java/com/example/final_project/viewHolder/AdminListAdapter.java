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
import java.util.Set;

public class AdminListAdapter extends RecyclerView.Adapter<AdminListAdapter.ViewHolder> {

    private List<User> mData;
    private LayoutInflater mInflater;
    private Set<User> selectedUsers;

    public interface OnCheckedChangeListener {
        void onItemCheckedChanged(User item, boolean isChecked);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.checkedChangeListener = listener;
    }

    private AdminListAdapter.OnCheckedChangeListener checkedChangeListener;

    public AdminListAdapter(Context context, List<User> data, Set<User> selectedUsers) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.selectedUsers = selectedUsers;

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
        holder.checkBox.setChecked(selectedUsers.contains(item));

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setAdmin(isChecked);
            Log.d("WatchListAdapter", "User " + item.getUsername() + " checked: " + isChecked);
            if (checkedChangeListener != null) {
                checkedChangeListener.onItemCheckedChanged(item, isChecked);
            }
        });
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
            Log.d("UserListAdapter", "Clicked item at position " + position);
        }
    }
}