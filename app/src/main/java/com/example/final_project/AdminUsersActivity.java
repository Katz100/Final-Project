package com.example.final_project;

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
import com.example.final_project.databinding.ActivityAdminBinding;
import com.example.final_project.databinding.ActivityAdminUsersBinding;
import com.example.final_project.viewHolder.AdminDashboardListViewModel;
import com.example.final_project.viewHolder.AdminListAdapter;
import com.example.final_project.viewHolder.UserListAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Allows Admins to view & manage Admin users
 * @author Justin Martlock
 * <br>
 * created: 8/16/2025
 * @since 0.1.0
 */


public class AdminUsersActivity extends AppCompatActivity {

    private ActivityAdminUsersBinding binding;
    UserListAdapter nonAdminListAdapter;
    private AdminDashboardListViewModel viewModel;
    private final Set<User> selectedUsers = new HashSet<>();
    public static final String ADMIN_ACTIVITY_USERNAME_KEY = "com.example.final_project.AdminUsersActivity.username";

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

        //adds back button to action bar
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onSupportNavigateUp();
            return true;
        } else if (item.getItemId() == R.id.action_logout) {
            showLogoutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLogoutDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AdminUsersActivity.this);
        //final AlertDialog alertDialog = alertBuilder.create();

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

    private void showPromoteDialog() {
        if (selectedUsers.isEmpty()) {
            Toast.makeText(this, "No users selected", Toast.LENGTH_SHORT).show();
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle("Confirm Promotion")
                .setMessage("Are you sure you want to promote this user?") //change to show selected user
                .setPositiveButton("Yes", (dialog, which) -> {
                    for (User user : selectedUsers) {
                        viewModel.promoteUser(user);
                    }
                    Toast.makeText(this, "User has been promoted", Toast.LENGTH_SHORT).show(); //change to show selected user
                    selectedUsers.clear();
                })
                .setNegativeButton("Cancel", null).show();
    }

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

    private void logout() {
        startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
    }

    // opens LoginActivity when back button is pressed
    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    public static Intent adminUsersIntentFactory(Context context, String username) {
        Intent intent = new Intent(context, AdminUsersActivity.class);
        intent.putExtra(ADMIN_ACTIVITY_USERNAME_KEY, username);
        return intent;
    }

}