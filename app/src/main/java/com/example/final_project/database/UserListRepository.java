package com.example.final_project.database;
import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.final_project.database.entities.User;
import java.util.List;

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

    public LiveData<List<User>> getAllNonAdmins() {
        return userDAO.getAllNonAdmins();
    }

    public LiveData<List<User>> getAllAdmins() {
        return userDAO.getAllAdmins();
    }

    public LiveData<List<User>> getAllUsers() {
        return userDAO.getAllUsers();
    }
    public void promoteUser(User user) {
        MovieWatchlistDatabase.databaseWriteExecutor.execute(() -> {
            userDAO.promoteUser(user.getUsername());
        });
    }

    public void demoteUser(User user) {
        MovieWatchlistDatabase.databaseWriteExecutor.execute(() -> {
            userDAO.demoteUser(user.getUsername());
        });
    }

    public void deleteUser(User user) {
        MovieWatchlistDatabase.databaseWriteExecutor.execute(() -> {
            userDAO.delete(user);
        });
    }

    public LiveData<Integer> getNonAdminCount() {
        return userDAO.countNonAdmins();
    }
    public LiveData<Integer> getAdminCount() {
        return userDAO.countAdmins();
    }
}