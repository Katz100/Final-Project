package com.example.final_project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project.database.UsersMovies;
import com.example.final_project.database.entities.UserWatchList;
import com.example.final_project.databinding.ActivityWatchListBinding;
import com.example.final_project.viewHolder.WatchListAdapter;
import com.example.final_project.viewHolder.WatchListViewModel;

import java.util.ArrayList;

/**
 * Allows users to view their watch list
 * @author Cody Hopkins
 * <br>
 * created: 8/16/2025
 * @since 0.1.0
 */
public class WatchListActivity extends AppCompatActivity {

    public static String TAG = "WatchListActivity";
    public static final String WATCHLIST_ACTIVITY_USERNAME_KEY = "com.example.final_project.WatchListActivity.username";
    ActivityWatchListBinding binding;

    WatchListViewModel viewModel;
    WatchListAdapter watchListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWatchListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //adds back button to action bar
        setSupportActionBar(binding.mainActivityToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewModel = new ViewModelProvider(this).get(WatchListViewModel.class);

        ArrayList<UsersMovies> initialWatchList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.watchListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        watchListAdapter = new WatchListAdapter(this, initialWatchList);
        recyclerView.setAdapter(watchListAdapter);

        // Set a click listener for the checkboxes inside the recyclerview items
        watchListAdapter.setOnCheckedChangeListener((movie, position, isChecked) -> {
            if (isChecked) {
                showInputDialog(movie, position);
            }
        });

        // Store signed-in user
        String username = getIntent().getStringExtra(WATCHLIST_ACTIVITY_USERNAME_KEY);
        if (username != null) {
            viewModel.getUserByUsername(username);
        } else {
            Log.e(TAG, "Username extra was null!");
        }

        // Observe changes to the UserWatchList table and update the recyclerview
        viewModel.uncompletedMovies.observe(this, movies -> {
            if (movies != null) {
                Log.i(TAG, "Movies: " + movies.toString());
                watchListAdapter.updateUsersMovies(movies);
            }
        });

        try {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle("Watch List");
            } else {
                Log.e("ActionBar", "ActionBar is null");
            }
            invalidateOptionsMenu();
        } catch (Exception e) {
            Log.e("ActionBar", "An error occurred", e);
        }
    }

    // opens MainActivity when back button is pressed
    @Override
    public boolean onSupportNavigateUp() {
        if (viewModel.user.getValue() == null) return false;
        Intent intent = MainActivity.mainIntentFactory(this, viewModel.user.getValue().getUsername());
        startActivity(intent);
        finish();
        return true;
    }

    public static Intent WatchListActivityIntentFactory(Context context, String username) {
        Intent intent = new Intent(context, WatchListActivity.class);
        intent.putExtra(WATCHLIST_ACTIVITY_USERNAME_KEY, username);
        return intent;
    }

    /**
     * Shows a dialog to allow the user to input a rating for a movie
     * @param movie Movie to be rated
     * @param position The movie's position in the recyclerview
     */
    private void showInputDialog(UsersMovies movie, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(WatchListActivity.this);
        builder.setTitle("Add rating for " + movie.getTitle());

        // Create input field
        final EditText input = new EditText(WatchListActivity.this);
        input.setHint("Enter your rating");
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String userInput = input.getText().toString().trim();
            Log.d(TAG, "User entered: " + userInput + " for movie: " + movie.getTitle());
            if (!userInput.isEmpty()) {
                try {
                    double rating = Double.parseDouble(userInput);
                    if (rating < 0.0 || rating > 5.0) {
                        input.setError("Rating must be between 0.0 and 5.0");
                        Toast.makeText(WatchListActivity.this, "Rating must be between 0.0 and 5.0",
                                Toast.LENGTH_SHORT).show();
                        watchListAdapter.setItemChecked(position, false);
                        return;
                    }
                    Log.d(TAG, "User entered rating: " + rating + " for movie: " + movie.getTitle());
                    UserWatchList userWatchList = new UserWatchList(
                            movie.getUserId(),
                            movie.getMovieId(),
                            true,
                            rating
                    );
                    movie.setCompleted(false);
                    viewModel.setRating(userWatchList);
                    dialog.dismiss();

                } catch (NumberFormatException e) {
                    Log.e(TAG, "Invalid rating format", e);
                    watchListAdapter.setItemChecked(position, false);
                }
            } else {
                Log.w(TAG, "No rating entered");
            }
        });

        builder.show();
    }

    /**
     * Displays a confirmation dialog asking the user if they want to log out.
     * If confirmed, navigates back to LoginActivity.
     */
    private void showLogoutDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(WatchListActivity.this);
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
        String username = getIntent().getStringExtra(WATCHLIST_ACTIVITY_USERNAME_KEY);
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