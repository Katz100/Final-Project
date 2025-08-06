package com.example.final_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.database.WatchListRepository;
import com.example.final_project.database.entities.User;

import com.example.final_project.database.entities.User;
import com.example.final_project.databinding.ActivitySignupBinding;

import com.example.final_project.database.MovieWatchlistDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SignUpActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private ActivitySignupBinding binding;
    private WatchListRepository watchListRepository;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        watchListRepository = WatchListRepository.getRepository(getApplication());

        usernameEditText = binding.newUsernameEditText;
        passwordEditText = binding.newPasswordEditText;
        confirmPasswordEditText = binding.confirmPasswordEditText;
        Button signUpButton = binding.signUpButton;

        //adds back button to action bar
        setSupportActionBar(binding.signUpToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidInput()){
                    verifyNewUser();
                }
            }
        });

        binding.loginButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isValidInput(){
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if(username.isEmpty()){
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.isEmpty()){
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(confirmPassword.isEmpty()){
            Toast.makeText(this, "Please confirm password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void verifyNewUser(){
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        MovieWatchlistDatabase db = MovieWatchlistDatabase.getDatabase(getApplicationContext());
        db.getDatabaseWriteExecutor().execute(() -> {
            User existingUser = db.userDAO().getUserByUserName(username);
            if(existingUser != null) {
                runOnUiThread(() -> {
                    Toast.makeText(SignUpActivity.this, "This username already exists", Toast.LENGTH_SHORT).show();
                });
            }else{
                //Inserts new user
                User user = new User(username, password, false);
                db.userDAO().insert(user);
                runOnUiThread(()->{
                    Toast.makeText(SignUpActivity.this, "Welcome, " + username + "!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    intent.putExtra(MainActivity.MAIN_ACTIVITY_USERNAME_KEY, username);
                    startActivity(intent);
                });
            }
        });
    }

    // opens LoginActivity when back button is pressed
    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    static Intent signupIntentFactory(Context context) {
        Intent intent = new Intent(context, SignUpActivity.class);
        return intent;
    }
}

