package com.example.final_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.final_project.databinding.ActivityAdminBinding;

public class AdminActivity extends AppCompatActivity {

    private ActivityAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     binding = ActivityAdminBinding.inflate(getLayoutInflater());
     setContentView(binding.getRoot());

     binding.manageUsersButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Toast.makeText(AdminActivity.this, "Manage Users", Toast.LENGTH_SHORT).show();
         }
     });

     binding.adminThingsButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Toast.makeText(AdminActivity.this, "Admin Things", Toast.LENGTH_SHORT).show();
         }
     });

     binding.manageGenresButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Toast.makeText(AdminActivity.this, "Manage Genres", Toast.LENGTH_SHORT).show();
         }
     });

    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    static Intent adminIntentFactory(Context context) {
        return new Intent(context, AdminActivity.class);
    }

}
