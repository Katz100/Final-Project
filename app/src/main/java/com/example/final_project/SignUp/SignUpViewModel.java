package com.example.final_project.SignUp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.final_project.database.entities.User;

public class SignUpViewModel extends AndroidViewModel {

    private final SignUpRepository signUpRepository;
    public SignUpViewModel(@NonNull Application application) {
        super(application);
        signUpRepository = SignUpRepository.getRepository(application);
    }

    public void insertUser(User user) {
        signUpRepository.insertUser(user);
    }

    public LiveData<User> getUserByUserName(String username) {
        return signUpRepository.getUserByUsername(username);
    }
}
