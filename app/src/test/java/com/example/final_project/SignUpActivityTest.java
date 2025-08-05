package com.example.final_project;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.shadows.ShadowToast;
import static org.junit.Assert.*;
import static org.robolectric.Shadows.shadowOf;
import android.content.Intent;
import android.widget.EditText;

@RunWith(RobolectricTestRunner.class)
public class SignUpActivityTest {

    @Test
    public void passwordsDoNotMatch() {
        try (ActivityController<SignUpActivity> controller = Robolectric.buildActivity(SignUpActivity.class)) {
            controller.setup();
            SignUpActivity activity = controller.get();

            EditText usernameField = activity.findViewById(R.id.newUsernameEditText);
            EditText passwordField = activity.findViewById(R.id.newPasswordEditText);
            EditText confirmPasswordField = activity.findViewById(R.id.confirmPasswordEditText);

            usernameField.setText("user123");
            passwordField.setText("password1");
            confirmPasswordField.setText("password2");

            assertNotEquals(passwordField.getText().toString(), confirmPasswordField.getText().toString());

            activity.findViewById(R.id.signUpButton).performClick();

            String expectedToast = "Passwords do not match";
            String actualToast = ShadowToast.getTextOfLatestToast();
            assertEquals(expectedToast, actualToast);

            Intent actualIntent = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
            assertNull(actualIntent); // No intent should be started
        }
    }

    @Test
    public void emptyUsername() {
        try (ActivityController<SignUpActivity> controller = Robolectric.buildActivity(SignUpActivity.class)) {
            controller.setup();
            SignUpActivity activity = controller.get();

            EditText usernameField = activity.findViewById(R.id.newUsernameEditText);
            EditText passwordField = activity.findViewById(R.id.newPasswordEditText);
            EditText confirmPasswordField = activity.findViewById(R.id.confirmPasswordEditText);

            usernameField.setText("");
            passwordField.setText("password1");
            confirmPasswordField.setText("password1");

            assertEquals("", usernameField.getText().toString());

            activity.findViewById(R.id.signUpButton).performClick();

            String expectedToast = "Username cannot be empty";
            String actualToast = ShadowToast.getTextOfLatestToast();
            assertEquals(expectedToast, actualToast);
            assertNotEquals("Welcome, user123!", actualToast);

            Intent actualIntent = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
            assertNull(actualIntent); // No intent should be started
        }
    }
}
