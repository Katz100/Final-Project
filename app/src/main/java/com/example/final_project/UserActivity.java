package com.example.final_project;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project.database.entities.User;
import com.example.final_project.databinding.ActivityAdminUsersBinding;
import com.example.final_project.viewHolder.AdminDashboardListViewModel;
import com.example.final_project.viewHolder.UserListAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Allows Admins to promote users to Admin or delete them from the database
 * @author Claudia Fierro
 * created: 8/16/2025
 * @since 0.1.0
 */

public class UserActivity extends AppCompatActivity {

    private ActivityAdminUsersBinding binding;
    UserListAdapter nonAdminListAdapter;
    private AdminDashboardListViewModel viewModel;
    private final Set<User> selectedUsers = new HashSet<>();
    public static final String ADMIN_ACTIVITY_USERNAME_KEY = "com.example.final_project.AdminUsersActivity.username";

    /**
     * Initializes the Admin Users activity, setting up the UI components, ViewModel, and user list.
     * <p>
     * This method inflates the layout using view binding, RecyclerView displays
     * non-admin users, and sets up listeners for user selection and promote/delete buttons.
     * Initializes the toolbar with back navigation and observes the ViewModel for updates
     * to the non-admin user list.
     * </p>
     *
     * <param name="savedInstanceState">If the activity is being re-initialized after being shut down,
     * this Bundle contains the data it most recently supplied.</param>
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(AdminDashboardListViewModel.class);

        ArrayList<User> nonAdminList = new ArrayList<>();

        RecyclerView recyclerViewNonAdmins = findViewById(R.id.userAdminRecyclerView);
        recyclerViewNonAdmins.setLayoutManager(new LinearLayoutManager(this));
        nonAdminListAdapter = new UserListAdapter(this, nonAdminList);
        recyclerViewNonAdmins.setAdapter(nonAdminListAdapter);

        nonAdminListAdapter.setOnCheckedChangeListener((user, isChecked) -> {
            if (isChecked) {
                selectedUsers.add(user);
            } else {
                selectedUsers.remove(user);
            }
        });

        setSupportActionBar(binding.adminToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Set up button listeners
        Button promoteButton = findViewById(R.id.promoteUserButton);
        Button deleteUserButton = findViewById(R.id.deleteUserButton);

        promoteButton.setOnClickListener(v -> showPromoteDialog());
        deleteUserButton.setOnClickListener(v -> showDeleteUserDialog());


        String username = getIntent().getStringExtra(ADMIN_ACTIVITY_USERNAME_KEY);
        if (username != null) {
            viewModel.getAllNonAdmins();
        } else {
            Log.e("MainActivity", "Username extra was null!");
        }

        viewModel.allNonAdmins.observe(this, users -> {
            if (users != null) {
                Log.i("Admin Activity", "Non-Admins: " + users.toString());
                nonAdminListAdapter.updateUsers(users);
            }
        });
    }

    /**
     * Create the logout menu
     * @param menu The options menu in which you place your items.
     *
     * @return true upon successful creation
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.watchlist_menu, menu);
        MenuItem item = menu.findItem(R.id.logoutMenuItem);
        if (item == null) {
            Log.e(TAG, "Menu item R.id.logoutMenuItem not found. Check watchlist_menu.xml id.");
            return true;
        }
        String username = getIntent().getStringExtra(ADMIN_ACTIVITY_USERNAME_KEY);
        item.setTitle(username != null ? username : "User");
        item.setEnabled(true);
        return true;
    }

    /**
     * Handles the logout actions
     * @param item The menu item that was selected.
     *
     * @return the choice selected
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) return onSupportNavigateUp();
        if (item.getItemId() == R.id.logoutMenuItem) {
            showLogoutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Prompts logout dialog
     */
    private void showLogoutDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(UserActivity.this);

        alertBuilder.setMessage("Logout?");
        alertBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });
        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertBuilder.create().show();
    }

    /**
     * Prompts promotion dialog
     */
    private void showPromoteDialog() {
        if (selectedUsers.isEmpty()) {
            Toast.makeText(this, "No users selected", Toast.LENGTH_SHORT).show();
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle("Confirm Promotion")
                .setMessage("Are you sure you want to promote this user?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    for (User user : selectedUsers) {
                        viewModel.promoteUser(user);
                    }
                    Toast.makeText(this, "User has been promoted", Toast.LENGTH_SHORT).show();
                    selectedUsers.clear();
                })
                .setNegativeButton("Cancel", null).show();
    }

    /**
     * Prompts delete user dialog
     */
    private void showDeleteUserDialog() {
        if (selectedUsers.isEmpty()) {
            Toast.makeText(this, "No users selected", Toast.LENGTH_SHORT).show();
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this user?") //change to show selected user
                .setPositiveButton("Yes", (dialog, which) -> {
                    for (User user : selectedUsers) {
                        viewModel.deleteUser(user);
                    }
                    Toast.makeText(this, "This user has been deleted",
                            Toast.LENGTH_SHORT).show(); //change to show selected user
                })
                .setNegativeButton("Cancel", null).show();

    }

    /**
     * logs users out, launches LoginActivity
     */
    private void logout() {
        startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
    }

    /**
     * Opens LoginActivity upon pressing back button
     * @return true upon success
     */
    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    /**
     * Factory for creation of Intent
     * @param context context of the activity being launched
     * @param username user credentials
     * @return the Intent
     */
    static Intent adminUsersIntentFactory(Context context, String username) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(ADMIN_ACTIVITY_USERNAME_KEY, username);
        return intent;
    }

}
