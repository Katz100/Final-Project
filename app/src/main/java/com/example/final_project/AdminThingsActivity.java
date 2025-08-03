package com.example.final_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.databinding.ActivityAdminThingsBinding;
import com.example.final_project.databinding.ActivityManageUsersBinding;

public class AdminThingsActivity extends AppCompatActivity {

    private ActivityAdminThingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminThingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //adds back button to action bar
        setSupportActionBar(binding.adminThingsToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        // Set up the UI components and listeners for managing users
        // RecyclerView, buttons, etc. can be initialized here
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
        finish();
        return true;
    }
    static Intent adminThingsIntentFactory(Context context) {
        return new Intent(context, AdminThingsActivity.class);
    }

}
