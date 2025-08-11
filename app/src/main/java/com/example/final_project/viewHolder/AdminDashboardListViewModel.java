package com.example.final_project.viewHolder;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.final_project.database.UserListRepository;
import com.example.final_project.database.entities.User;

import java.util.List;

public class AdminDashboardListViewModel extends AndroidViewModel {
    private final MutableLiveData<User> _user = new MutableLiveData<>(null);
    public final LiveData<User> user = _user;
    UserListRepository userListRepository;
    public AdminDashboardListViewModel(@NonNull Application application) {
        super(application);
        userListRepository = UserListRepository.getRepository(application);
    }

    public final LiveData<List<User>> getUsers =
            Transformations.switchMap(_user, user -> {
                if (user != null) {
                    return userListRepository.getAllUsers();
                } else {
                    return new MutableLiveData<>(List.of());
                }
            });

    public void getAllUsers() {
        userListRepository.getAllUsers();
    }
}
