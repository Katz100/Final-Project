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
                verifyNewUser();
            }
        });
    }
    private void verifyNewUser(){
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if(username.isEmpty()){
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.isEmpty()){
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(confirmPassword.isEmpty()){
            Toast.makeText(this, "Please confirm password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            Toast.makeText(this, "Sign Up Successful!", Toast.LENGTH_SHORT).show();
        }

        executorService.execute(() -> {
            User currentUser = watchListRepository.getUserByUsername(username);
            if(currentUser != null){
                Toast.makeText(this, "Username already taken.", Toast.LENGTH_SHORT).show();
            } else {
                User newUser = new User(username, password, false);
                watchListRepository.insertUser(newUser);
                Toast.makeText(this, "Successful Sign Up!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
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

