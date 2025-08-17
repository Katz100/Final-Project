package com.example.final_project;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.final_project.SignUp.SignUpViewModel;
import com.example.final_project.database.entities.User;
import com.example.final_project.databinding.ActivitySignupBinding;

/**
 * Allows new users to Create an Account by entering a unique username
 * matching passwords
 *
 * @author Claudia Fierro
 * created: 8/16/2025
 * @since 0.1.0
 */


/**
 * SignUpActivity handles user registration within the app.
 * It validates input, checks for existing usernames, and inserts new users into the database.
 * Provides navigation to LoginActivity and MainActivity upon successful sign-up or logout.
 */

public class SignUpActivity extends AppCompatActivity {
    public static final String TAG = "SignUpPage";
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private ActivitySignupBinding binding;
    private SignUpViewModel viewModel;

    /**
     * Initializes the activity, sets up UI bindings, observers, and click listeners.
     * Handles user input validation and user creation logic.
     *
     * @param savedInstanceState Bundle containing saved state, if any.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        usernameEditText = binding.newUsernameEditText;
        passwordEditText = binding.newPasswordEditText;
        confirmPasswordEditText = binding.confirmPasswordEditText;
        Button signUpButton = binding.signUpButton;

        //adds back button to action bar
        setSupportActionBar(binding.signUpToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewModel.user.observe(this, user -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (user != null) {
                Log.i(TAG, "User is not null: " + user.getUsername());
                Toast.makeText(this, "This username already exists", Toast.LENGTH_SHORT).show();
            } else if (!username.isEmpty()) {
                Log.i(TAG, "User is null, creating new user");
                User newUser = new User(username, password, false);
                viewModel.insertUser(newUser);

                // clear input fields after successful sign up
                usernameEditText.setText("");
                passwordEditText.setText("");
                confirmPasswordEditText.setText("");

                Toast.makeText(this, "Welcome, " + username + "!", Toast.LENGTH_SHORT).show();
                Intent intent = MainActivity.mainIntentFactory(this, username);
                startActivity(intent);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidInput()){
                    verifyNewUser();
                }
            }
        });

        binding.loginButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = LoginActivity.loginIntentFactory(SignUpActivity.this);
                startActivity(intent);
            }
        });
    }

    /**
     * Validates user input fields for username, password, and password confirmation.
     *
     * @return true if all fields are valid and passwords match; false otherwise.
     */
    private boolean isValidInput(){
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if(username.isEmpty()){
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.isEmpty()){
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(confirmPassword.isEmpty()){
            Toast.makeText(this, "Please confirm password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    /**
     * Triggers a check in the database to verify if the entered username already exists.
     * If not, proceeds to create a new user.
     */
    private void verifyNewUser() {
        String username = usernameEditText.getText().toString();
        viewModel.getUserByUserName(username);
    }

    /**
     * Overrides the default back button behavior in the toolbar.
     * Navigates the user back to the LoginActivity.
     *
     * @return true to indicate the navigation was handled.
     */
    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    /**
     * Factory method to create an Intent for launching SignUpActivity.
     *
     * @param context Context from which the activity is launched.
     * @return Intent configured to start SignUpActivity.
     */
    static Intent signupIntentFactory(Context context) {
        Intent intent = new Intent(context, SignUpActivity.class);
        return intent;
    }

    /**
     * Inflates the logout menu in the toolbar.
     *
     * @param menu The options menu in which items are placed.
     * @return true to display the menu.
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    /**
     * Handles selection of menu items.
     * Specifically responds to logout action by showing a confirmation dialog.
     *
     * @param item The selected menu item.
     * @return true if the item was handled; false otherwise.
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            showLogoutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Displays a confirmation dialog asking the user if they want to log out.
     * If confirmed, navigates back to LoginActivity.
     */
    private void showLogoutDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(SignUpActivity.this);

        alertBuilder.setMessage("Logout?");
        alertBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });
        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertBuilder.create().show();
    }

    /**
     * Logs the user out and navigates to LoginActivity.
     */
    private void logout(){
        startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
    }
}

