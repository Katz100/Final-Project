package com.example.final_project;

import static org.robolectric.Shadows.shadowOf;

import android.app.Activity;
import android.content.Intent;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;

@RunWith(RobolectricTestRunner.class)
public class AdminActivityTest extends TestCase {

    @Test
    public void testClickingManageUsers() {
        try(ActivityController<AdminActivity> controller = Robolectric.buildActivity(AdminActivity.class)){
            controller.setup();

            AdminActivity activity = controller.get();
            activity.findViewById(R.id.manageUsersButton).performClick();
            Intent expectedIntent = ManageUsersActivity.manageUsersIntentFactory(activity);
            Intent actualIntent = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
            assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
        }
    }


}