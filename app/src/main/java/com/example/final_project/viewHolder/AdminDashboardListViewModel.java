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

    public LiveData<List<User>> allNonAdmins;
    public LiveData<List<User>> allAdmins;
    UserListRepository userListRepository;
    public AdminDashboardListViewModel(@NonNull Application application) {
        super(application);
        userListRepository = UserListRepository.getRepository(application);
        allAdmins = userListRepository.getAllAdmins();
        allNonAdmins = userListRepository.getAllNonAdmins();
    }

    public final LiveData<List<User>> getNonAdmins =
            Transformations.switchMap(_user, user -> {
                if (user != null) {
                    return userListRepository.getAllNonAdmins();
                } else {
                    return new MutableLiveData<>(List.of());
                }
            });

    public final LiveData<List<User>> getAdmins =
            Transformations.switchMap(_user, user -> {
                if (user != null) {
                    return userListRepository.getAllAdmins();
                } else {
                    return new MutableLiveData<>(List.of());
                }
            });

    public void getAllAdmins() {
        userListRepository.getAllAdmins();
    }

    public void getAllNonAdmins() {
        userListRepository.getAllNonAdmins();
    }
    public void promoteUser(User user) {
        userListRepository.promoteUser(user);
    }

    public void demoteUser(User user) {
        userListRepository.demoteUser(user);
    }

    public void deleteUser(User user) {
        userListRepository.deleteUser(user);
    }

}