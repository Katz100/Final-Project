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

public class SignUpActivity extends AppCompatActivity {
    public static final String TAG = "SignUpPage";
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private ActivitySignupBinding binding;
    private SignUpViewModel viewModel;

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

    private void verifyNewUser() {
        String username = usernameEditText.getText().toString();
        viewModel.getUserByUserName(username);
    }

    // opens LoginActivity when back button is pressed
    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    static Intent signupIntentFactory(Context context) {
        Intent intent = new Intent(context, SignUpActivity.class);
        return intent;
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
            showLogoutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //After a user confirms they want to logout, it takes them back to the sign in screen
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

    private void logout(){
        startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
    }
}

