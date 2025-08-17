package com.example.final_project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project.database.UserCompletedMovies;
import com.example.final_project.databinding.ActivityCompletedListBinding;
import com.example.final_project.viewHolder.CompletedListAdapter;
import com.example.final_project.viewHolder.WatchListViewModel;

import java.util.ArrayList;

/**
 * Allows users to view their completed movie list
 * @author Mariah Stinson
 * <br>
 * created: 8/16/2025
 * @since 0.1.0
 */
public class CompletedListActivity extends AppCompatActivity {

    public static final String TAG = "CompletedListActivity";
    public static final String COMPLETED_ACTIVITY_USERNAME_KEY =
            "com.example.final_project.CompletedListActivity.username";

    ActivityCompletedListBinding binding;
    WatchListViewModel viewModel;
    CompletedListAdapter completedListAdapter;

    /**
     * Initializes the activity, sets up the toolbar, and configures the RecyclerView for completed movies.
     * Observes the completed movies from the ViewModel and updates the adapter accordingly.
     *
     * @param savedInstanceState saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompletedListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // toolbar + back button
        setSupportActionBar(binding.mainActivityToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewModel = new ViewModelProvider(this).get(WatchListViewModel.class);

        ArrayList<UserCompletedMovies> initialCompletedList = new ArrayList<>();
        RecyclerView recyclerViewCompleted = findViewById(R.id.completedWatchListRecyclerView);
        recyclerViewCompleted.setLayoutManager(new LinearLayoutManager(this));
        completedListAdapter = new CompletedListAdapter(this, initialCompletedList);
        recyclerViewCompleted.setAdapter(completedListAdapter);

        // get signed-in user
        String username = getIntent().getStringExtra(COMPLETED_ACTIVITY_USERNAME_KEY);
        if (username != null) {
            viewModel.getUserByUsername(username);
        } else {
            Log.e(TAG, "Username extra was null!");
        }

        // observe completed movies
        viewModel.completedMovies.observe(this, movies -> {
            if (movies != null) {
                completedListAdapter.updateUsersCompletedMovies(movies);
            }
        });

        // title
        try {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) actionBar.setTitle("Completed List");
            invalidateOptionsMenu();
        } catch (Exception e) {
            Log.e("ActionBar", "An error occurred", e);
        }
    }

    /**
     * Handles the back navigation when the user presses the back button in the action bar.
     * If the user is not logged in, it returns false.
     * Otherwise, it navigates to MainActivity with the user's username.
     *
     * @return true if navigation was successful, false otherwise
     */
    @Override
    public boolean onSupportNavigateUp() {
        if (viewModel.user.getValue() == null) return false;
        Intent intent = MainActivity.mainIntentFactory(
                this, viewModel.user.getValue().getUsername());
        startActivity(intent);
        finish();
        return true;
    }

    /**
     * Factory method to create an Intent for CompletedListActivity.
     * @param context the context from which the activity is started
     * @param username the username of the user whose completed list is to be displayed
     * @return an Intent to start CompletedListActivity
     */
    public static Intent CompletedListActivityIntentFactory(Context context, String username) {
        Intent intent = new Intent(context, CompletedListActivity.class);
        intent.putExtra(COMPLETED_ACTIVITY_USERNAME_KEY, username);
        return intent;
    }

    /**
     * Displays a confirmation dialog asking the user if they want to log out.
     * If confirmed, navigates back to LoginActivity.
     */
    private void showLogoutDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CompletedListActivity.this);
        alertBuilder.setMessage("Logout?");
        alertBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { logout(); }
        });
        alertBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        alertBuilder.create().show();
    }

    /**
     * Logs out the user and navigates back to LoginActivity.
     */
    private void logout() {
        startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
    }

    /**
     * Inflates the options menu and sets the title of the logout menu item to the username.
     * If the username is null, it defaults to "User".
     *
     * @param menu The options menu in which you place your items.
     * @return true if the menu was created successfully.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.watchlist_menu, menu);
        MenuItem item = menu.findItem(R.id.logoutMenuItem);
        if (item == null) {
            Log.e(TAG, "Menu item R.id.logoutMenuItem not found. Check watchlist_menu.xml id.");
            return true;
        }
        String username = getIntent().getStringExtra(COMPLETED_ACTIVITY_USERNAME_KEY);
        item.setTitle(username != null ? username : "User");
        item.setEnabled(true);
        return true;
    }

    /**
     * Handles selection events from the options menu.
     *
     * @param item item that was selected
     * @return true if the selection was handled here
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) return onSupportNavigateUp();
        if (item.getItemId() == R.id.logoutMenuItem) { // MUST match the XML id
            showLogoutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
