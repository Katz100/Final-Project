package com.example.final_project.database;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.final_project.AdminActivity;
import com.example.final_project.AdminsActivity;
import com.example.final_project.LoginActivity;
import com.example.final_project.R;
import com.example.final_project.SignUpActivity;
import com.example.final_project.UserActivity;
import com.example.final_project.database.entities.MovieDAO;
import com.example.final_project.database.entities.User;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;

import java.io.IOException;

@RunWith(RobolectricTestRunner.class)
public class AdminActivityTest {

    private MovieWatchlistDatabase db;
    private UserDAO userDAO;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, MovieWatchlistDatabase.class)
                .allowMainThreadQueries()
                .build();
        userDAO = db.userDAO();
    }

    @After
    public void tearDown() throws IOException {
        db.close();
    }

    @Test
    public void testPromote() {
        String username = "testuser";
        User originalUser = new User(username, "password123", false);
        userDAO.insert(originalUser);

        User userBeforePromotion = userDAO.getUserByUserName(username);
        assertNotNull( userBeforePromotion);
        assertEquals( false, userBeforePromotion.isAdmin());
        userDAO.promoteUser(username);

        User promotedUser = userDAO.getUserByUserName(username);
        assertNotNull( promotedUser);

        assertTrue( promotedUser.isAdmin());

        assertEquals( username, promotedUser.getUsername());
        assertEquals( "password123", promotedUser.getPassword());
    }

    @Test
    public void testDeleteUser(){

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
