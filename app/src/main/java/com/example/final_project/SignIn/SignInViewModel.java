package com.example.final_project.SignIn;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.final_project.SignUp.SignUpRepository;
import com.example.final_project.database.entities.User;

public class SignInViewModel extends AndroidViewModel {

    private final SignInRepository signInRepository;
    public SignInViewModel(@NonNull Application application) {
        super(application);
        signInRepository = SignInRepository.getRepository(application);
    }
    public LiveData<User> getUserByUserName(String username) {
        return signInRepository.getUserByUsername(username);
    }
}
