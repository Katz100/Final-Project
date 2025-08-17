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
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.final_project.database.entities.Movie;
import com.example.final_project.databinding.ActivityMainBinding;
import com.example.final_project.viewHolder.WatchListViewModel;

/**
 * This is the main activity of the app, where users can add movies
 * @author Cody Hopkins
 * created: 7/30/2025
 * @since 0.1.0
 */
public class MainActivity extends AppCompatActivity {

    public static final String MAIN_ACTIVITY_USERNAME_KEY = "com.example.final_project.MainActivity.username";
    public static final String TAG = "MovieWatchlistApp";

    private ActivityMainBinding binding;
    WatchListViewModel viewModel;

    private EditText editTitle;
    private EditText editGenre;
    private String movieTitle = "";
    private String movieGenre = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ViewModel + views
        editTitle = findViewById(R.id.movieTitleInputEditText);
        editGenre = findViewById(R.id.movieGenreInputEditText);
        viewModel = new ViewModelProvider(this).get(WatchListViewModel.class);

        // Toolbar
        setSupportActionBar(binding.mainActivityToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // if you want a back button on Main
        }

        // Add movie
        binding.addMovieButton.setOnClickListener(v -> {
            getInformationFromDisplay();
            insertMovieRecord();
            editGenre.setText("");
            editTitle.setText("");
        });

        // Go to Watch List
        binding.goToWatchListButton.setOnClickListener(view -> {
            if (viewModel.user.getValue() == null) return;
            startActivity(WatchListActivity.WatchListActivityIntentFactory(
                    MainActivity.this,
                    viewModel.user.getValue().getUsername()
            ));
        });

        // Go to Completed List
        binding.viewCompletedMoviesButton.setOnClickListener(view -> {
            if (viewModel.user.getValue() == null) return;
            startActivity(CompletedListActivity.CompletedListActivityIntentFactory(
                    MainActivity.this,
                    viewModel.user.getValue().getUsername()
            ));
        });

        // Store signed-in user
        String username = getIntent().getStringExtra(MAIN_ACTIVITY_USERNAME_KEY);
        if (username != null) {
            viewModel.getUserByUsername(username);
        } else {
            Log.e(TAG, "Username extra was null!");
        }

        // Title
        try {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle("MovieWatchlist");
            } else {
                Log.e("ActionBar", "ActionBar is null");
            }
            invalidateOptionsMenu();
        } catch (Exception e) {
            Log.e("ActionBar", "An error occurred", e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.watchlist_menu, menu);

        MenuItem item = menu.findItem(R.id.logoutMenuItem);
        if (item != null) {
            String username = getIntent().getStringExtra(MAIN_ACTIVITY_USERNAME_KEY);
            item.setTitle(username != null ? username : "User");
            item.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.logoutMenuItem);
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

    private void showLogoutDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
        alertBuilder.setMessage("Logout?");
        alertBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { logout(); }
        });
        alertBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        alertBuilder.create().show();
    }

    private void logout() {
        startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
    }

    // If Main is your app's entry screen, you could remove the "up" affordance entirely.
    // Keeping this for now to match your current behavior.
    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    public static Intent mainIntentFactory(Context context, String username) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MAIN_ACTIVITY_USERNAME_KEY, username);
        return intent;
    }

    private void insertMovieRecord() {
        if (movieTitle.isEmpty()) return;
        Movie movie = new Movie(movieTitle, movieGenre);
        viewModel.insertMovie(movie);
    }

    private void getInformationFromDisplay() {
        movieTitle = binding.movieTitleInputEditText.getText().toString();
        movieGenre = binding.movieGenreInputEditText.getText().toString();
        // Optional: trim inputs
        movieTitle = movieTitle.trim();
        movieGenre = movieGenre.trim();
    }
}
