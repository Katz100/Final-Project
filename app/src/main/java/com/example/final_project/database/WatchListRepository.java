package com.example.final_project.database;

import android.app.Application;

import com.example.final_project.database.entities.MovieDAO;
import com.example.final_project.database.entities.User;

public class WatchListRepository {
    private MovieDAO movieDAO;
    private UserWatchListDAO userWatchListDAO;
    private UserDAO userDAO;

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

    public User getUserByUsername(String username) {
        return userDAO.getUserByUserName(username);
    }
}
