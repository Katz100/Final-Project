package com.example.final_project;
import static org.junit.Assert.*;
import static org.robolectric.Shadows.shadowOf;

import android.content.Intent;
import android.widget.EditText;

import com.example.final_project.database.entities.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    @Test
    public void clickingOnAddMovieButton_ClearsFields() {
        try (ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class)) {
            controller.setup();

            MainActivity activity = controller.get();
            EditText titleField = activity.findViewById(R.id.movieTitleInputEditText);
            EditText genreField = activity.findViewById(R.id.movieGenreInputEditText);

            titleField.setText("Batman");
            genreField.setText("Comedy");
            activity.findViewById(R.id.addMovieButton).performClick();

            assertEquals("", titleField.getText().toString());
            assertEquals("", genreField.getText().toString());
        }
    }

    @Test
    public void clickingOnWatchList_ButtonGoesToWatchListActivity() {
        try (ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class)) {
            controller.setup();

            MainActivity activity = controller.get();

            activity.viewModel.setUser(new User( "test", "test", false));
            activity.findViewById(R.id.goToWatchListButton).performClick();

            Intent expectedIntent = WatchListActivity.WatchListActivityIntentFactory(activity, "");
            Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
            assertEquals(expectedIntent.getComponent(), actual.getComponent());
        }
    }

    @Test
    public void clickingOnCompletedListButton_GoesToCompletedListActivity() {
        try (ActivityController<MainActivity> controller =
                     Robolectric.buildActivity(MainActivity.class)) {
            controller.setup();
            MainActivity activity = controller.get();

            // Pretend a user is signed in (so the click handler will run)
            activity.viewModel.setUser(new User("tester", "pw", false));

            // Click the button
            activity.findViewById(R.id.viewCompletedMoviesButton).performClick();

            // Verify the launched activity matches the intent factory
            Intent expected = CompletedListActivity.CompletedListActivityIntentFactory(activity, "tester");
            Intent actual = shadowOf(RuntimeEnvironment.getApplication()).getNextStartedActivity();

            assertNotNull("No intent was started", actual);
            assertEquals(expected.getComponent(), actual.getComponent());
            // Optional: verify the username extra made it through
            assertEquals(
                    "tester",
                    actual.getStringExtra(CompletedListActivity.COMPLETED_ACTIVITY_USERNAME_KEY)
            );
        }
    }

    @Test
    public void clickingOnCompletedListButton_NoUser_DoesNotStartActivity() {
        try (ActivityController<MainActivity> controller =
                     Robolectric.buildActivity(MainActivity.class)) {
            controller.setup();
            MainActivity activity = controller.get();

            // No user set → click should early return
            activity.findViewById(R.id.viewCompletedMoviesButton).performClick();

            // Should be no started intents
            assertNull(shadowOf(RuntimeEnvironment.getApplication()).getNextStartedActivity());
        }
    }
}