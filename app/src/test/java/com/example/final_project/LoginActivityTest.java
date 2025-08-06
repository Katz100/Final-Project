package com.example.final_project;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.robolectric.Shadows.shadowOf;

import android.content.Intent;
import android.util.Log;
import android.widget.EditText;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;

@RunWith(RobolectricTestRunner.class)
public class LoginActivityTest {

    @Test
    public void clickingOnSignUpButton_LaunchesSignUpActivity() {
        try (ActivityController<LoginActivity> controller = Robolectric.buildActivity(LoginActivity.class)) {
            controller.setup();

            LoginActivity activity = controller.get();
            activity.findViewById(R.id.signUpButton).performClick();

            Intent expectedIntent = SignUpActivity.signupIntentFactory(activity);
            Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
            assertEquals(expectedIntent.getComponent(), actual.getComponent());
        }
    }

    // This will need to be changed once we are comparing the values
    // in the text fields with users from the database

    //TODO: Complete a passing test
//    @Test
//    public void signingInWithValidCreds_LaunchesMainActivity() {
//        try (ActivityController<LoginActivity> controller = Robolectric.buildActivity(LoginActivity.class)) {
//            controller.setup();
//            LoginActivity activity = controller.get();
//
//            EditText usernameField = activity.findViewById(R.id.usernameLoginEditText);
//            EditText passwordField = activity.findViewById(R.id.passwordLoginEditText);
//
//            usernameField.setText("username");
//            passwordField.setText("password");
//
//            activity.findViewById(R.id.loginButton).performClick();
//
//            Intent expectedIntent = MainActivity.mainIntentFactory(activity, "");
//            Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
//            assertEquals(expectedIntent.getComponent(), actual.getComponent());
//        }
//    }

    @Test
    public void signingInWithBlankFields_DoesNotLaunchMainActivity() {
        try (ActivityController<LoginActivity> controller = Robolectric.buildActivity(LoginActivity.class)) {
            controller.setup();
            LoginActivity activity = controller.get();

            EditText usernameField = activity.findViewById(R.id.usernameLoginEditText);
            EditText passwordField = activity.findViewById(R.id.passwordLoginEditText);

            usernameField.setText("");
            passwordField.setText("");

            activity.findViewById(R.id.loginButton).performClick();

            Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
            assertNull(actual);
        }
    }

    //TODO: Complete a passing test

//    @Test
//    public void signingInWithValidCreds_LaunchesAdminActivity(){
//
//        try(ActivityController<LoginActivity> controller = Robolectric.buildActivity(LoginActivity.class)){
//            controller.setup();
//             LoginActivity activity = controller.get();
//
//             EditText usernameField = activity.findViewById(R.id.usernameLoginEditText);
//
//             usernameField.setText("admin");
//
//             activity.findViewById(R.id.loginButton).performClick();
//             Intent expectedIntent = AdminActivity.adminIntentFactory(activity, "admin1");
//             Intent actualIntent = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
//             assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
//        }
//    }

}
