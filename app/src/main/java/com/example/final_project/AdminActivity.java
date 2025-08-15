package com.example.final_project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.example.final_project.viewHolder.AdminDashboardListViewModel;
import com.example.final_project.viewHolder.AdminListAdapter;
import com.example.final_project.viewHolder.UserListAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AdminActivity extends AppCompatActivity {

    private ActivityAdminBinding binding;
    UserListAdapter nonAdminListAdapter;
    AdminListAdapter adminListAdapter;
    private AdminDashboardListViewModel viewModel;
    private final Set<User> selectedUsers = new HashSet<>();

    public static final String ADMIN_ACTIVITY_USERNAME_KEY = "com.example.final_project.AdminActivity.username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(AdminDashboardListViewModel.class);

        ArrayList<User> nonAdminList = new ArrayList<>();
        ArrayList<User> adminList = new ArrayList<>();

        RecyclerView recyclerViewNonAdmins = findViewById(R.id.userAdminRecyclerView);
        recyclerViewNonAdmins.setLayoutManager(new LinearLayoutManager(this));
        nonAdminListAdapter = new UserListAdapter(this, nonAdminList);
        recyclerViewNonAdmins.setAdapter(nonAdminListAdapter);

        RecyclerView recyclerViewAdmins = findViewById(R.id.adminUserAdminRecyclerView);
        recyclerViewAdmins.setLayoutManager(new LinearLayoutManager(this));
        adminListAdapter = new AdminListAdapter(this, adminList, selectedUsers);
        recyclerViewAdmins.setAdapter(adminListAdapter);

        nonAdminListAdapter.setOnCheckedChangeListener((user, isChecked) -> {
            if (isChecked) {
                selectedUsers.add(user);
            } else {
                selectedUsers.remove(user);
            }
        });

        adminListAdapter.setOnCheckedChangeListener((user, isChecked) -> {
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
        Button demoteButton = findViewById(R.id.demoteAdminButton);
        Button deleteButtonAdmin = findViewById((R.id.deleteButtonAdmin));

        promoteButton.setOnClickListener(v -> showPromoteDialog());
        deleteUserButton.setOnClickListener(v -> showDeleteUserDialog());

        demoteButton.setOnClickListener(v -> showDemoteDialog());
        deleteButtonAdmin.setOnClickListener(v -> showDeleteAdminDialog());
      
        String username = getIntent().getStringExtra(ADMIN_ACTIVITY_USERNAME_KEY);
        if (username != null) {
            viewModel.getAllAdmins();
            viewModel.getAllNonAdmins();
        } else {
            Log.e("MainActivity", "Username extra was null!");
        }

        viewModel.allAdmins.observe(this, users -> {
            if (users != null) {
                Log.i("Admin Activity", "Admins: " + users.toString());
                adminListAdapter.updateUsers(users);
            }
        });

        viewModel.allNonAdmins.observe(this, users -> {
            if (users != null) {
                Log.i("Admin Activity", "Non-Admins: " + users.toString());
                nonAdminListAdapter.updateUsers(users);
            }
        });

    }

    //Shows a menu with the inputted user's name up top.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);

        // Get the logout menu item
        MenuItem item = menu.findItem(R.id.action_logout);

        // Check if the menu item exists
        if (item != null) {
            // Get the username from the intent
            String username = getIntent().getStringExtra(ADMIN_ACTIVITY_USERNAME_KEY);

            // Check if the EditText exists
            if (username != null) {
                //Set the menu item title to username
                item.setTitle(username);

            } else {
                //Set a default title
                item.setTitle("Admin");
            }
            //Make the menu item visible
            item.setVisible(true);
        }
        return true;
    }

    //When a user clicks on their username, it prompts them to logout
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_logout);
        item.setVisible(true);

        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                showLogoutDialog();
                return false;
            }
        });
        return true;
    }

    //Afer a user confirms they want to logout, it takes them back to the sign in screen
    private void showLogoutDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AdminActivity.this);
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

    private void showDemoteDialog() {
        if (selectedUsers.isEmpty()) {
            Toast.makeText(this, "No users selected", Toast.LENGTH_SHORT).show();
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle("Confirm Demotion")
                .setMessage("Are you sure you want to demote this user?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    for (User user : selectedUsers) {
                        viewModel.demoteUser(user);
                    }
                    Toast.makeText(this, "This user has been demoted", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, "This user has been deleted", Toast.LENGTH_SHORT).show(); //change to show selected user
                })
                .setNegativeButton("Cancel", null).show();

    }

    private void showDeleteAdminDialog() {
        if (selectedUsers.isEmpty()) {
            Toast.makeText(this, "No users selected", Toast.LENGTH_SHORT).show();
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this user?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    for (User user : selectedUsers) {
                        viewModel.deleteUser(user);
                    }
                    Toast.makeText(this, "This user has been deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null).show();
    }

    private void logout(){
        startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
    }

    // opens LoginActivity when back button is pressed
    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    static Intent adminIntentFactory(Context context, String username) {
        Intent intent = new Intent(context, AdminActivity.class);
        intent.putExtra(ADMIN_ACTIVITY_USERNAME_KEY, username);
        return intent;
    }


}
