package com.example.final_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.final_project.database.MovieWatchlistDatabase;
import com.example.final_project.database.WatchListRepository;
import com.example.final_project.database.entities.Movie;
import com.example.final_project.database.entities.User;
import com.example.final_project.databinding.ActivityLoginBinding;
import com.example.final_project.viewHolder.WatchListViewModel;

/**
 * This is a simple login activity that allows users to log in with their username and password.
 * @author Mariah Stinson
 * <br>
 * created: 7/30/2025
 * @since 0.1.0
 */

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    //private User user = null;

    WatchListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        viewModel = new ViewModelProvider(this).get(WatchListViewModel.class);

        viewModel.user.observe(this, user -> {
            if (user != null) {
                Toast.makeText(getApplication(), user.getUsername(), Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getUserByUsername("admin1");

        new Thread(() -> {
            MovieWatchlistDatabase db = MovieWatchlistDatabase.getDatabase(this);
            db.movieDAO().insert(new Movie("Blade Runner", "Sci-Fi"));
            User user = WatchListRepository.getRepository(getApplication()).getUserByUsername("admin1");
            if (user != null) {
                Log.i(MainActivity.TAG, user.getUsername());
            }
        }).start();

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyUser();
            }

        });

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = SignUpActivity.signupIntentFactory(LoginActivity.this);
                startActivity(intent);
            }
        });
    }

    static Intent loginIntentFactory(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    private void verifyUser() {
        String username = binding.usernameLoginEditText.getText().toString();
        if (username.isEmpty()) {
            toastMaker("Username should not be blank.");
            return;
        }

        // Here you would typically check the username and password against a database
        // For now, we will just simulate a successful login

        Intent intent = MainActivity.mainIntentFactory(this, username);
        startActivity(intent);
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
