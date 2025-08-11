package com.example.final_project.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.final_project.database.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class UserListRepository {
    private UserDAO userDAO;

    private static UserListRepository repository;

    private UserListRepository(Application application) {
        MovieWatchlistDatabase db = MovieWatchlistDatabase.getDatabase(application);
        this.userDAO = db.userDAO();
    }

    public static synchronized UserListRepository getRepository(Application application) {
        if (repository == null) {
            repository = new UserListRepository(application);
        }
        return repository;
    }

    public LiveData<List<User>> getAllUsers() {
        return userDAO.getAllUsers();
    }

}