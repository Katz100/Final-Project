package com.example.final_project.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.final_project.database.entities.Movie;
import com.example.final_project.database.entities.MovieDAO;
import com.example.final_project.database.entities.User;
import com.example.final_project.database.entities.UserWatchList;

import java.util.List;
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

    public LiveData<List<UsersMovies>> getUncompletedWatchlistItems(int userId) {
        return userWatchListDAO.getUncompletedWatchlistItems(userId);
    }

    public int insertMovie(Movie movie) {
        return (int) movieDAO.insert(movie);
    }

    public void insertToWatchList(UserWatchList userWatchList) {
        userWatchListDAO.insert(userWatchList);
    }

    public long insertUser(User user){
        return userDAO.insert(user);
    }

    public User getUserByUsername(String username) {
        return userDAO.getUserByUserName(username);
    }
}
