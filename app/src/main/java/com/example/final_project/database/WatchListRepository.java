package com.example.final_project.database;

import android.app.Application;

import com.example.final_project.database.entities.Movie;
import com.example.final_project.database.entities.MovieDAO;
import com.example.final_project.database.entities.User;
import com.example.final_project.database.entities.UserWatchList;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class WatchListRepository {
    private MovieDAO movieDAO;
    private UserWatchListDAO userWatchListDAO;
    private UserDAO userDAO;

    ExecutorService executor = MovieWatchlistDatabase.databaseWriteExecutor;


    private static WatchListRepository repository;

    private WatchListRepository(Application application) {
        MovieWatchlistDatabase db = MovieWatchlistDatabase.getDatabase(application);
        this.movieDAO = db.movieDAO();
        this.userDAO = db.userDAO();
        this.userWatchListDAO = db.userWatchListDAO();
    }

    public static synchronized WatchListRepository getRepository(Application application) {
        if (repository == null) {
            repository = new WatchListRepository(application);
        }
        return repository;
    }


    public int insertMovie(Movie movie) {
        try {
            Future<Long> future = executor.submit(() -> movieDAO.insert(movie));
            long id = future.get(); // blocks, so consider error handling
            movie.setId((int) id);
            return (int) id;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void insertToWatchList(UserWatchList userWatchList) {
        executor.execute(() -> userWatchListDAO.insert(userWatchList));
    }

    public User getUserByUsername(String username) {
        return userDAO.getUserByUserName(username);
    }
}
