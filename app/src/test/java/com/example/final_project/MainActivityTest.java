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

}