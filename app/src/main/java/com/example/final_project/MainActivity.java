package com.example.final_project;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String MAIN_ACTIVITY_USERNAME_KEY = "com.example.final_project.MainActivity.username";
    ActivityMainBinding binding;

    private WatchListRepository repository;
    WatchListAdapter watchListAdapter;
    CompletedListAdapter completedListAdapter;
    public static final String TAG = "MovieWatchlistApp";

    String movieTitle = "";
    String movieGenre = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
        if (username != null && username.toLowerCase().contains("admin")) {
            Intent intent = AdminActivity.adminIntentFactory(getApplicationContext());
            startActivity(intent);
        }
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
        String username = getIntent().getStringExtra(MAIN_ACTIVITY_USERNAME_KEY);
        repository.insertMovie(movie, username);
    }

    private void getInformationFromDisplay() {
        movieTitle = binding.movieTitleInputEditText.getText().toString();
        movieGenre = binding.movieGenreInputEditText.getText().toString();
    }

}