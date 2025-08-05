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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.example.final_project.database.WatchListRepository;
import com.example.final_project.database.entities.Movie;
import com.example.final_project.databinding.ActivityMainBinding;
import com.example.final_project.viewHolder.CompletedListAdapter;
import com.example.final_project.viewHolder.CompletedWatchListItem;
import com.example.final_project.viewHolder.WatchListAdapter;
import com.example.final_project.viewHolder.WatchListItem;
import com.example.final_project.viewHolder.WatchListViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String MAIN_ACTIVITY_USERNAME_KEY = "com.example.final_project.MainActivity.username";
    ActivityMainBinding binding;

    private WatchListRepository repository;
    WatchListAdapter watchListAdapter;
    CompletedListAdapter completedListAdapter;
    public static final String TAG = "MovieWatchlistApp";

    private WatchListViewModel viewModel;

    String movieTitle = "";
    String movieGenre = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(WatchListViewModel.class);

        //adds back button to action bar
        setSupportActionBar(binding.mainActivityToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        // TODO: get movies from db
        // dummy values for now
        CompletedWatchListItem item2 = new CompletedWatchListItem("title", "genre", "5/5");
        ArrayList<CompletedWatchListItem> completedWatchListItems = new ArrayList<>();
        completedWatchListItems.add(item2);
        completedWatchListItems.add(item2);
        completedWatchListItems.add(item2);
        completedWatchListItems.add(item2);
        completedWatchListItems.add(item2);
        completedWatchListItems.add(item2);
        completedWatchListItems.add(item2);

        RecyclerView recyclerView2 = findViewById(R.id.completedWatchListRecyclerView);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        completedListAdapter = new CompletedListAdapter(this, completedWatchListItems);
        recyclerView2.setAdapter(completedListAdapter);


        WatchListItem item = new WatchListItem("title", "genre");
        ArrayList<WatchListItem> watchListItems = new ArrayList<>();
        watchListItems.add(item);
        watchListItems.add(item);
        watchListItems.add(item);
        watchListItems.add(item);
        watchListItems.add(item);
        watchListItems.add(item);

        RecyclerView recyclerView = findViewById(R.id.watchListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        watchListAdapter = new WatchListAdapter(this, watchListItems);
        recyclerView.setAdapter(watchListAdapter);

        repository = WatchListRepository.getRepository(getApplication());

        binding.addMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInformationFromDisplay();
                insertMovieRecord();
            }
        });

        String username = getIntent().getStringExtra(MAIN_ACTIVITY_USERNAME_KEY);
        if (username != null) {
            viewModel.getUserByUsername(username);
        } else {
            Log.e("MainActivity", "Username extra was null!");
        }
        if (username != null && username.toLowerCase().contains("admin")) {
            Intent intent = AdminActivity.adminIntentFactory(getApplicationContext(), username);
            startActivity(intent);
        }

        //Changes the title in the Menu Bar to MovieWatchlist
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

    //Shows a menu with the inputted user's name up top.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.watchlist_menu, menu);

        // Get the logout menu item
        MenuItem item = menu.findItem(R.id.logoutMenuItem);

        // Check if the menu item exists
        if (item != null) {
            // Get the username from the intent
            String username = getIntent().getStringExtra(MAIN_ACTIVITY_USERNAME_KEY);

            // Check if the EditText exists
            if (username != null) {
                //Set the menu item title to username
                item.setTitle(username);

            } else {
                //Set a default title
                item.setTitle("User");
            }
            //Make the menu item visible
            item.setVisible(true);
        }
        return true;
    }

    //When a user clicks on their username, it prompts them to logout
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

    //Afer a user confirms they want to logout, it takes them back to the sign in screen
    private void showLogoutDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog alertDialog = alertBuilder.create();

        alertDialog.setMessage("Logout?");
        alertBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();

            }
        });
        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
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

    public static Intent mainIntentFactory(Context context, String username) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MAIN_ACTIVITY_USERNAME_KEY, username);
        return intent;
    }

    private void insertMovieRecord(){
        if(movieTitle.isEmpty()){
            return;
        }
        Movie movie = new Movie(movieTitle, movieGenre);

        viewModel.insertMovie(movie);
    }

    private void getInformationFromDisplay() {
        movieTitle = binding.movieTitleInputEditText.getText().toString();
        movieGenre = binding.movieGenreInputEditText.getText().toString();
    }

}