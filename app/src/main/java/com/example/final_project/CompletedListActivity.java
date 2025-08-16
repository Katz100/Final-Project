package com.example.final_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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

    // back to MainActivity
    @Override
    public boolean onSupportNavigateUp() {
        if (viewModel.user.getValue() == null) return false;
        Intent intent = MainActivity.mainIntentFactory(
                this, viewModel.user.getValue().getUsername());
        startActivity(intent);
        finish();
        return true;
    }

    public static Intent CompletedListActivityIntentFactory(Context context, String username) {
        Intent intent = new Intent(context, CompletedListActivity.class);
        intent.putExtra(COMPLETED_ACTIVITY_USERNAME_KEY, username);
        return intent;
    }
}
