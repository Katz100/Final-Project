package com.example.final_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.databinding.ActivityAdminBinding;

public class AdminActivity extends AppCompatActivity {

    private ActivityAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.manageUsersButton.setOnClickListener(v -> {
            Intent intent = ManageUsersActivity.manageUsersIntentFactory(getApplicationContext());
            startActivity(intent);
        });

        binding.adminThingsButton.setOnClickListener(v -> {
            Intent intent = AdminThingsActivity.adminThingsIntentFactory(getApplicationContext());
            startActivity(intent);
        });

        binding.manageGenresButton.setOnClickListener(v -> {
            Intent intent = ManageGenresActivity.manageGenresIntentFactory(getApplicationContext());
            startActivity(intent);
        });

    }

    static Intent adminIntentFactory(Context context) {
        return new Intent(context, AdminActivity.class);
    }

}
