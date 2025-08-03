package com.example.final_project;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project.database.MovieWatchlistDatabase;
import com.example.final_project.database.entities.Movie;
import com.example.final_project.database.entities.User;
import com.example.final_project.database.entities.UserWatchList;
import com.example.final_project.databinding.ActivityMainBinding;
import com.example.final_project.viewHolder.CompletedListAdapter;
import com.example.final_project.viewHolder.CompletedWatchListItem;
import com.example.final_project.viewHolder.WatchListAdapter;
import com.example.final_project.viewHolder.WatchListItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String MAIN_ACTIVITY_USERNAME_KEY = "com.example.final_project.MainActivity.username";
    ActivityMainBinding binding;

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
            db.userDAO().insert(new User("testuser1", "password1", false));
            db.userWatchListDAO().insert(new UserWatchList(1, 1, true, 5.0));
        }).start();
    }



    public static Intent mainIntentFactory(Context context, String username) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MAIN_ACTIVITY_USERNAME_KEY, username);
        return intent;
    }

}