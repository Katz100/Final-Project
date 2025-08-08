package com.example.final_project.SignUp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.final_project.database.entities.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignUpViewModel extends AndroidViewModel {

    private final MutableLiveData<User> _user = new MutableLiveData<User>(null);
    public final LiveData<User> user = _user;

    private final SignUpRepository signUpRepository;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    public SignUpViewModel(@NonNull Application application) {
        super(application);
        signUpRepository = SignUpRepository.getRepository(application);
    }

    public void insertUser(User user) {
        signUpRepository.insertUser(user);
    }

    public void getUserByUserName(String username) {
        executorService.execute(() -> {
            User user = signUpRepository.getUserByUsername(username);
            _user.postValue(user);
        });
    }
}
