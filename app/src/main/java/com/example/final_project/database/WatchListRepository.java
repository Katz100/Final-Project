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

    public void insertMovie(Movie movie) {
        Future<?> future = executor.submit(() -> movieDAO.insert(movie));
        executor.submit(() -> {
            try {
                future.get();
                userWatchListDAO.insert(new UserWatchList(1, 14, true, 0.75));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public User getUserByUsername(String username) {
        return userDAO.getUserByUserName(username);
    }
}
