package com.example.final_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = AdminActivity.adminIntentFactory(getApplicationContext());
        startActivity(intent);

    }

    static Intent mainIntentFactory(Context context) {
        return new Intent(context, MainActivity.class);
    }
}