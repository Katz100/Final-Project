package com.example.final_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.databinding.ActivityAdminBinding;
import com.example.final_project.databinding.ActivityManageGenresBinding;

public class ManageGenresActivity extends AppCompatActivity {

    private ActivityManageGenresBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageGenresBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up the UI components and listeners for managing users
        // RecyclerView, buttons, etc. can be initialized here
    }

    static Intent manageGenresIntentFactory(Context context) {
        return new Intent(context, ManageGenresActivity.class);
    }

}
