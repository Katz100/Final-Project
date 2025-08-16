package com.example.final_project;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.final_project.SignIn.SignInViewModel;
import com.example.final_project.databinding.ActivityLoginBinding;
import java.util.Objects;

/**
 * This is a simple login activity that allows users to log in with their username and password.
 * @author Mariah Stinson
 * <br>
 * created: 7/30/2025
 * @since 0.1.0
 */

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivityTAG";
    private ActivityLoginBinding binding;
    private SignInViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.loginToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        viewModel = new ViewModelProvider(this).get(SignInViewModel.class);

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

        viewModel.user.observe(this, user -> {
            if (user == null) {
                toastMaker("This username does not exist");
                Log.i(TAG, "User is null");
            } else {
                Log.i(TAG, user.getUsername() + " is a user");
                String enteredPassword = binding.passwordLoginEditText.getText().toString();
                if (verifyPassword(user.getPassword(), enteredPassword)) {
                    if (user.isAdmin()) {
                        startAdminActivity(user.getUsername());
                    } else {
                        startMainActivity(user.getUsername());
                    }
                } else {
                    toastMaker("Password is incorrect");
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

        if (username.isEmpty() || password.isEmpty()) {
            toastMaker("Username or password should not be blank.");
            Log.i(TAG, "Username or password is blank");
            return;
        }

        viewModel.getUserByUserName(username);
    }

    private boolean verifyPassword(String password, String enteredPassword){
        return Objects.equals(password, enteredPassword);
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void startMainActivity(String username) {
        Intent intent = MainActivity.mainIntentFactory(this, username);
        startActivity(intent);
    }

    public void startAdminActivity(String username){
        startActivity(AdminActivity.adminIntentFactory(getApplicationContext(), username));
    }
}
