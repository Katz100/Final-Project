package com.example.final_project;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.robolectric.Shadows.shadowOf;

import android.content.Intent;
import android.os.Looper;
import android.widget.EditText;

import com.example.final_project.SignIn.TestSignInViewModel;
import com.example.final_project.database.FakeRepository;
import com.example.final_project.database.entities.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;

@RunWith(RobolectricTestRunner.class)
public class LoginActivityTest {

    @Test
    public void getUserByUserName_returnsExpectedUser() {
        TestSignInViewModel viewModel = new TestSignInViewModel(new FakeRepository());

        viewModel.getUserByUserNameSync("testuser1");

        User result = viewModel.user.getValue();
        assertNotNull(result);
        assertEquals("testuser1", result.getUsername());
    }
    @Test
    public void signingInWithWrongPassword_DoesNotLaunchMainActivity() {
        try (ActivityController<LoginActivity> controller = Robolectric.buildActivity(LoginActivity.class)) {
            controller.setup();
            LoginActivity activity = controller.get();

            EditText usernameField = activity.findViewById(R.id.usernameLoginEditText);
            EditText passwordField = activity.findViewById(R.id.passwordLoginEditText);

            usernameField.setText("testuser1");
            passwordField.setText("wrongpass");

            activity.findViewById(R.id.loginButton).performClick();
            shadowOf(Looper.getMainLooper()).runToEndOfTasks();

            Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
            assertNull(actual);
        }
    }

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

    @Test
    public void signingInWithUnknownUser_DoesNotLaunchMainActivity() {
        try (ActivityController<LoginActivity> controller = Robolectric.buildActivity(LoginActivity.class)) {
            controller.setup();
            LoginActivity activity = controller.get();

            EditText usernameField = activity.findViewById(R.id.usernameLoginEditText);
            EditText passwordField = activity.findViewById(R.id.passwordLoginEditText);

            usernameField.setText("nonuser");
            passwordField.setText("nonpass");

            activity.findViewById(R.id.loginButton).performClick();
            shadowOf(Looper.getMainLooper()).runToEndOfTasks();

            Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
            assertNull(actual);
        }
    }

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



    // This will need to be changed once we are comparing the values
    // in the text fields with users from the database

//    TODO: Complete a passing test
//    @Test
//    public void signingInWithValidCreds_LaunchesMainActivity() {
//        try (ActivityController<LoginActivity> controller = Robolectric.buildActivity(LoginActivity.class)) {
//            controller.setup();
//            LoginActivity activity = controller.get();
//
//            EditText usernameField = activity.findViewById(R.id.usernameLoginEditText);
//            EditText passwordField = activity.findViewById(R.id.passwordLoginEditText);
//
//            usernameField.setText("testuser1");
//            passwordField.setText("testuser1");
//
//            activity.findViewById(R.id.loginButton).performClick();
//
//            shadowOf(Looper.getMainLooper()).runToEndOfTasks();
//
//
//            Intent expectedIntent = MainActivity.mainIntentFactory(activity, "testuser1");
//            Intent actual = shadowOf(activity).getNextStartedActivity();
//
//            assertNotNull(actual);
//            assertEquals(expectedIntent.getComponent(), actual.getComponent());
//        }
//    }

    //TODO: Complete a passing test

//    @Test
//    public void signingInWithValidCreds_LaunchesAdminActivity(){
//
//        try (ActivityController<LoginActivity> controller = Robolectric.buildActivity(LoginActivity.class)) {
//            controller.setup();
//            LoginActivity activity = controller.get();
//
//            EditText usernameField = activity.findViewById(R.id.usernameLoginEditText);
//            EditText passwordField = activity.findViewById(R.id.passwordLoginEditText);
//
//            usernameField.setText("admin1");
//            passwordField.setText("admin1");
//
//            activity.findViewById(R.id.loginButton).performClick();
//
//           // shadowOf(Looper.getMainLooper()).runToEndOfTasks();
//            RuntimeEnvironment.getMasterScheduler().advanceToLastPostedRunnable();
//
//
//            Intent expectedIntent = AdminActivity.adminIntentFactory(activity, "admin1");
//            Intent actual = shadowOf(activity).getNextStartedActivity();
//
//            assertEquals(expectedIntent.getComponent(), actual.getComponent());
//        }
//    }

}
