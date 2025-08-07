package com.example.final_project;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.database.MovieWatchlistDatabase;
import com.example.final_project.database.WatchListRepository;
import com.example.final_project.database.entities.User;
import com.example.final_project.databinding.ActivityLoginBinding;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.loginToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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
                if (intent != null) {
                    startActivity(intent);
                } else {
                    Log.e("LoginActivity", "Intent is null");
                }
            }
        });
    }

    //LOGOUT MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    // Handle logout action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show();
            finishAffinity(); // or redirect to LoginActivity if needed
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    static Intent loginIntentFactory(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    private void verifyUser() {
        String username = binding.usernameLoginEditText.getText().toString();
        String password = binding.passwordLoginEditText.getText().toString();

        if (username.isEmpty()) {
            toastMaker("Username should not be blank.");
            Log.e("LoginActivity", "Toast is null");
            return;
        }

        WatchListRepository repository = WatchListRepository.getRepository((Application) getApplicationContext());
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            User userFromDB = repository.getUserByUsername(username);

            // Checks if username exists in DB
        if (userFromDB == null) {
            runOnUiThread(() -> {
                Toast.makeText(LoginActivity.this, "This username does not exist", Toast.LENGTH_SHORT).show();
            });
        } else {
            //Verifies password entered matches the stored password for the user
            if (verifyPassword(userFromDB.getPassword(), password)) {
                if (userFromDB.isAdmin()) {
                    Intent intent = AdminActivity.adminIntentFactory(getApplicationContext(), username);
                    startActivity(intent);
                } else {
                    Intent intent = MainActivity.mainIntentFactory(LoginActivity.this, username);
                    startActivity(intent);
                }
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(LoginActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                });
            }

        }
    });
    }

    private boolean verifyPassword(String password, String enteredPassword){
        return Objects.equals(password, enteredPassword);
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
