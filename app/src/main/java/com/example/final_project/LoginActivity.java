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

    /**
     * Logout menu creation
     * @param menu the menu to be created
     * @return true if the menu is created successfully
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    /**
     * Handles the logout action when the menu item is selected
     * @param item the selected menu item
     * @return true if the action is handled, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show();
            finishAffinity(); // or redirect to LoginActivity if needed
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

/**
     * Factory method to create an intent for starting the LoginActivity.
     * @param context the context from which the activity is started
     * @return an Intent to start LoginActivity
     */
    static Intent loginIntentFactory(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    /**
     * Verifies the user credentials entered in the login form.
     * It checks if the username and password fields are not empty,
     * and then queries the ViewModel for the user by username.
     */
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

    /**
     * Verifies if the entered password matches the stored password.
     * @param password the stored password
     * @param enteredPassword the password entered by the user
     * @return true if the passwords match, false otherwise
     */
    private boolean verifyPassword(String password, String enteredPassword){
        return Objects.equals(password, enteredPassword);
    }

    /**
     * Displays a toast message to the user.
     * @param message the message to be displayed
     */
    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Starts the MainActivity with the given username.
     * @param username the username to be passed to the MainActivity
     */
    public void startMainActivity(String username) {
        Intent intent = MainActivity.mainIntentFactory(this, username);
        startActivity(intent);
    }

    /**
     * Starts the AdminActivity with the given username.
     * @param username the username to be passed to the AdminActivity
     */
    public void startAdminActivity(String username){
        startActivity(AdminActivity.adminIntentFactory(getApplicationContext(), username));
    }
}
