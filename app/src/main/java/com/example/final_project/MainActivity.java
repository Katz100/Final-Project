package com.example.final_project;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public static final String MAIN_ACTIVITY_USERNAME_KEY = "com.example.final_project.MainActivity.username";
    ActivityMainBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String username = getIntent().getStringExtra(MAIN_ACTIVITY_USERNAME_KEY);
        if (username != null && !username.isEmpty()) {
            //binding.welcomeTextView.setText("Welcome, " + username + "!");

            if (username.equalsIgnoreCase("admin")) {
                Intent intent = AdminActivity.adminIntentFactory(getApplicationContext());
                startActivity(intent);
            }
        }
    }

    public static Intent mainIntentFactory(Context context, String username) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MAIN_ACTIVITY_USERNAME_KEY, username);
        return intent;
    }

}