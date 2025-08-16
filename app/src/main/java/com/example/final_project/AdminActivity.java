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
    private AdminDashboardListViewModel viewModel;
    public static final String ADMIN_ACTIVITY_USERNAME_KEY = "com.example.final_project.AdminActivity.username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(AdminDashboardListViewModel.class);

        //adds back button to action bar
        setSupportActionBar(binding.adminToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        String username = getIntent().getStringExtra(ADMIN_ACTIVITY_USERNAME_KEY);
        if (username != null) {
            viewModel.getAllAdmins();
            viewModel.getAllNonAdmins();
        } else {
            Log.e("MainActivity", "Username extra was null!");
        }

        // Set up button listeners
        Button adminsActivityButton = findViewById(R.id.adminsActivityButton);
        Button usersActivityButton = findViewById(R.id.usersActivityButton);

        usersActivityButton.setOnClickListener(v -> adminUsersActivity(username));
        adminsActivityButton.setOnClickListener(v -> adminsActivity(username));

    }

    private void adminsActivity(String username) {
        startActivity(AdminsActivity.adminsActivityIntentFactory(getApplicationContext(), username));
    }

    private void adminUsersActivity(String username) {
        startActivity(AdminUsersActivity.adminUsersIntentFactory(getApplicationContext(), username));
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

    //Afer a user confirms they want to logout, it takes them back to the sign in screen
    private void showLogoutDialog() {
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

    private void logout() {
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
