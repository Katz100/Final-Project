package com.example.final_project.database;
import android.content.Context;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.final_project.database.entities.Movie;
import com.example.final_project.database.entities.MovieDAO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class MovieDAOTest {

    private MovieWatchlistDatabase db;
    private MovieDAO movieDAO;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, MovieWatchlistDatabase.class)
                .allowMainThreadQueries()
                .build();
        movieDAO = db.movieDAO();
    }

    @After
    public void tearDown() throws IOException {
        db.close();
    }

    @Test
    public void testInsertMovie() {
        Movie movie = new Movie(1, "Spongebob", "Comedy");

        movieDAO.insert(movie);

        Movie movieExpected = movieDAO.getMovieById(1);
        assertEquals(movieExpected, movie);
    }

    @Test
    public void testUpdateMovie() {
        Movie movie = new Movie(999, "Superman", "action");
        movieDAO.insert(movie);
        Movie expectedMovie = new Movie(999, "The Fantastic Four", "action");

        movieDAO.updateMovie(expectedMovie);
        Movie result = movieDAO.getMovieById(999);

        assertEquals(expectedMovie, result);
    }

    @Test
    public void testDeleteMovie() {
        Movie movie = new Movie(1000, "The Room", "Comedy");
        movieDAO.insert(movie);

        movieDAO.delete(movie);
        Movie result = movieDAO.getMovieById(1000);

        assertNull(result);
    }
}
