package com.example.final_project.viewHolder;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.final_project.database.WatchListRepository;
import com.example.final_project.database.entities.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WatchListViewModel extends AndroidViewModel {

    private final MutableLiveData<User> _user = new MutableLiveData<>(null);
    public final LiveData<User> user = _user;
    WatchListRepository watchListRepository;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    public WatchListViewModel(@NonNull Application application) {
        super(application);
        watchListRepository = WatchListRepository.getRepository(application);
    }

    public void getUserByUsername(String username) {
        executorService.execute(() -> {
            User user = watchListRepository.getUserByUsername(username);
            _user.postValue(user);
        });
    }
}
