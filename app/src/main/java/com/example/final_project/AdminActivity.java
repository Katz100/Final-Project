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

public class AdminActivity extends AppCompatActivity {

    private ActivityAdminBinding binding;
    UserListAdapter userListAdapter;
    AdminListAdapter adminListAdapter;
    private AdminDashboardListViewModel viewModel;
    public static final String ADMIN_ACTIVITY_USERNAME_KEY = "com.example.final_project.AdminActivity.username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(AdminDashboardListViewModel.class);

        ArrayList<User> userList = new ArrayList<>();

        RecyclerView recyclerViewUsers = findViewById(R.id.userAdminRecyclerView);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        userListAdapter = new UserListAdapter(this, userList);
        recyclerViewUsers.setAdapter(userListAdapter);

        //adds back button to action bar
        setSupportActionBar(binding.adminToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String username = getIntent().getStringExtra(ADMIN_ACTIVITY_USERNAME_KEY);
        if (username != null) {
            viewModel.getAllUsers();
        } else {
            Log.e("MainActivity", "Username extra was null!");
        }

        viewModel.getUsers.observe(this, users -> {
            if (users != null) {
                Log.i("Admin Activity", "Users: " + users.toString());
                userListAdapter.updateUsers(users);
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
