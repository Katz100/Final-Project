package com.example.final_project.SignUp;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.final_project.database.MovieWatchlistDatabase;
import com.example.final_project.database.UserDAO;
import com.example.final_project.database.UserWatchListDAO;
import com.example.final_project.database.entities.MovieDAO;
import com.example.final_project.database.entities.User;

import java.util.concurrent.ExecutorService;

public class SignUpRepository {

    private MovieDAO movieDAO;
    private UserWatchListDAO userWatchListDAO;
    private UserDAO userDAO;
    ExecutorService executor = MovieWatchlistDatabase.getDatabaseWriteExecutor();

    private static SignUpRepository repository;

    private SignUpRepository(Application application) {
        MovieWatchlistDatabase db = MovieWatchlistDatabase.getDatabase(application);
        this.movieDAO = db.movieDAO();
        this.userDAO = db.userDAO();
        this.userWatchListDAO = db.userWatchListDAO();
    }

    public static synchronized SignUpRepository getRepository(Application application) {
        if (repository == null) {
            repository = new SignUpRepository(application);
        }
        return repository;
    }

    public LiveData<User> getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    public void insertUser(User user) {
        executor.execute(() -> {
            userDAO.insert(user);
        });
    }
}
