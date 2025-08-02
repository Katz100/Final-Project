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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project.database.MovieWatchlistDatabase;
import com.example.final_project.database.entities.Movie;
import com.example.final_project.database.entities.User;
import com.example.final_project.databinding.ActivityMainBinding;
import com.example.final_project.viewHolder.CompletedListAdapter;
import com.example.final_project.viewHolder.CompletedWatchListItem;
import com.example.final_project.viewHolder.WatchListAdapter;
import com.example.final_project.viewHolder.WatchListItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String MAIN_ACTIVITY_USERNAME_KEY = "com.example.final_project.MainActivity.username";
    ActivityMainBinding binding;
    private User user;
    WatchListAdapter watchListAdapter;
    CompletedListAdapter completedListAdapter;
    public static final String TAG = "MovieWatchlistApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        String username = getIntent().getStringExtra(MAIN_ACTIVITY_USERNAME_KEY);
        if (username != null && !username.isEmpty()) {
            //binding.welcomeTextView.setText("Welcome, " + username + "!");

            if (username.equalsIgnoreCase("admin")) {
                Intent intent = AdminActivity.adminIntentFactory(getApplicationContext());
                startActivity(intent);
            }
        }

        //Temporary code to add a movie to the watchlist database
        new Thread(() -> {
            MovieWatchlistDatabase db = MovieWatchlistDatabase.getDatabase(this);
            db.movieDAO().insert(new Movie("Blade Runner", "Sci-Fi"));
        }).start();

        //Changes the title in the Menu Bar to MovieWatchlist
        invalidateOptionsMenu();
        ActionBar actionBar = super.getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("MovieWatchlist");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.watchlist_menu,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.logoutMenuItem);
        item.setVisible(true);
        //TODO: Set the title to a user's username
        item.setTitle(":)");
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                showLogoutDialog();
                return false;
            }
        });
        return true;
    }

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
        //TODO: finish logout method
        startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
    }

    public static Intent mainIntentFactory(Context context, String username) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MAIN_ACTIVITY_USERNAME_KEY, username);
        return intent;
    }

}