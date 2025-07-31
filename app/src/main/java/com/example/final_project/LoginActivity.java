package com.example.final_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.databinding.ActivityLoginBinding;
import com.google.firebase.firestore.auth.User;

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

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyUser();
            }
        });
    }

    static Intent mainIntentFactory(Context context, String username) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.MAIN_ACTIVITY_USERNAME_KEY, username);
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

        Intent intent = mainIntentFactory(this, username);
        startActivity(intent);
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
