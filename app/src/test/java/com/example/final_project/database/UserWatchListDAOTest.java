package com.example.final_project.database;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.final_project.database.entities.Movie;
import com.example.final_project.database.entities.MovieDAO;
import com.example.final_project.database.entities.User;
import com.example.final_project.database.entities.UserWatchList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.IOException;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class UserWatchListDAOTest {

    private MovieWatchlistDatabase db;
    private UserWatchListDAO userWatchListDao;
    private MovieDAO movieDAO;
    private UserDAO userDAO;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, MovieWatchlistDatabase.class)
                .allowMainThreadQueries()
                .build();
        userWatchListDao = db.userWatchListDAO();
        movieDAO = db.movieDAO();
        userDAO = db.userDAO();
    }

    @After
    public void tearDown() throws IOException {
        db.close();
    }

    public void testUpdateWatchlistItem() {
        // Insert user and movie first
        User user = new User("updateUser", "password", false);
        int userId = (int) userDAO.insert(user);

        Movie movie = new Movie(2, "titanic", "i dont know");
        int movieId = (int) movieDAO.insert(movie);

        UserWatchList item = new UserWatchList(userId, movieId, false, 0.0);
        userWatchListDao.insert(item);

        UserWatchList existing = userWatchListDao.getAllWatchlistItems(userId).get(0);
        existing.setCompleted(true);
        existing.setRating(4.5);

        userWatchListDao.update(existing);

        LiveData<List<UserCompletedMovies>> updatedList = userWatchListDao.getCompletedMoviesWithRatings(userId);
        assertEquals(1, updatedList.getValue().get(0));
        assertEquals(4.5, updatedList.getValue().get(0).getRating());
    }

    @Test
    public void testDeleteWatchlistItem(){
        User user = new User("testuser", "pass", false);
        int userId = (int) userDAO.insert(user);

        Movie movie = new Movie(1, "back to the future", "Science fiction");
        int movieId = (int) movieDAO.insert(movie);

        UserWatchList item = new UserWatchList(userId, movieId, false, 0);
        userWatchListDao.insert(item);

        List<UserWatchList> itemsBefore = userWatchListDao.getAllWatchlistItems(userId);
        assertEquals(1, itemsBefore.size());

        userWatchListDao.delete(item);

        List<UserWatchList> itemsAfter = userWatchListDao.getAllWatchlistItems(userId);
        assertTrue(itemsAfter.isEmpty());
    }

    @Test
    public void testInsertAndGetAllWatchlistItems() {
        User user = new User("testuser", "password123", false);
        int userId = (int) userDAO.insert(user);

        Movie movie = new Movie(1, "Sci-fi", "1999");
        int movieId = (int) movieDAO.insert(movie);

        UserWatchList watchItem = new UserWatchList(userId, movieId, false, 0.0);
        userWatchListDao.insert(watchItem);

        List<UserWatchList> items = userWatchListDao.getAllWatchlistItems(userId);
        assertEquals(1, items.size());
        assertEquals(movieId, items.get(0).getMovieId());
    }
}