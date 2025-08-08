package com.example.final_project.viewHolder;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.final_project.database.UserCompletedMovies;
import com.example.final_project.database.UsersMovies;
import com.example.final_project.database.WatchListRepository;
import com.example.final_project.database.entities.Movie;
import com.example.final_project.database.entities.User;
import com.example.final_project.database.entities.UserWatchList;

import java.util.List;
import java.util.Random;
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

    public final LiveData<List<UserCompletedMovies>> completedMovies =
            Transformations.switchMap(_user, user -> {
                if (user != null) {
                    return watchListRepository.getCompletedMovies(user.getId());
                } else {
                    return new MutableLiveData<>(List.of());
                }
            });
    public final LiveData<List<UsersMovies>> uncompletedMovies =
            // observers new changes to _user property. (W/o -- only observers changes from null _user)
            Transformations.switchMap(_user, user -> {
                if (user != null) {
                    return watchListRepository.getUncompletedWatchlistItems(user.getId());
                } else {
                    return new MutableLiveData<>(List.of());
                }
            });


    public void getUserByUsername(String username) {
        executorService.execute(() -> {
            User user = watchListRepository.getUserByUsername(username);
            _user.postValue(user);
        });
    }

    public void insertMovie(Movie movie) {
        User currentUser = _user.getValue();
        executorService.execute(() -> {
            if (currentUser != null) {
                int movieId = watchListRepository.insertMovie(movie);
                int userId = currentUser.getId();
                watchListRepository.insertToWatchList(new UserWatchList(userId, movieId, false, 0.0));
                Log.e("WatchListViewModel", "Movie successfully added to watchlist!");
            } else {
                Log.e("WatchListViewModel", "User not set. Cannot insert movie.");
            }
        });
    }
    public void setRating(UserWatchList userWatchList) {
        watchListRepository.setRating(userWatchList);
    }
}
