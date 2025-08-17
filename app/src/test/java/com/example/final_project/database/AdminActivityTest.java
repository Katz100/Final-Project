package com.example.final_project.database;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.robolectric.Shadows.shadowOf;

import android.content.Intent;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;

import com.example.final_project.AdminActivity;
import com.example.final_project.AdminsActivity;
import com.example.final_project.LoginActivity;
import com.example.final_project.R;
import com.example.final_project.SignUpActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;

@RunWith(RobolectricTestRunner.class)
public class AdminActivityTest {
    @Test
    public void testPromote() {
        // Launch activity, click demote button, verify user is promoted
    }

    @Test
    public void testDemoteButtonUpdatesUser() {
        // Launch activity, click demote button, verify user is promoted
    }

    @Test
    public void testAdminActivityLaunchingAdminsActivity(){
        try (ActivityController<AdminActivity> controller = Robolectric.buildActivity(AdminActivity.class)) {
            controller.setup();
            AdminActivity activity = controller.get();

            Button adminsButton = activity.findViewById(R.id.adminsActivityButton);

            adminsButton.performClick();
            shadowOf(Looper.getMainLooper()).runToEndOfTasks();

            Intent expectedIntent = AdminsActivity.adminsActivityIntentFactory(activity, "admin1");
            Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
            assertEquals(expectedIntent.getComponent(), actual.getComponent());
        }

    }

}
