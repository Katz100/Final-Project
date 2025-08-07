package com.example.final_project.SignIn;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.final_project.SignUp.SignUpRepository;
import com.example.final_project.database.MovieWatchlistDatabase;
import com.example.final_project.database.UserDAO;
import com.example.final_project.database.UserWatchListDAO;
import com.example.final_project.database.entities.MovieDAO;
import com.example.final_project.database.entities.User;

import java.util.concurrent.ExecutorService;

public class SignInRepository {
    private MovieDAO movieDAO;
    private UserWatchListDAO userWatchListDAO;
    private UserDAO userDAO;
    ExecutorService executor = MovieWatchlistDatabase.getDatabaseWriteExecutor();

    private static SignInRepository repository;

    private SignInRepository(Application application) {
        MovieWatchlistDatabase db = MovieWatchlistDatabase.getDatabase(application);
        this.movieDAO = db.movieDAO();
        this.userDAO = db.userDAO();
        this.userWatchListDAO = db.userWatchListDAO();
    }

    public static synchronized SignInRepository getRepository(Application application) {
        if (repository == null) {
            repository = new SignInRepository(application);
        }
        return repository;
    }

    public LiveData<User> getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }
}
