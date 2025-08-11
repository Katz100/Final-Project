package com.example.final_project.SignIn;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.final_project.database.entities.IUserRepository;
import com.example.final_project.database.entities.User;

public class TestSignInViewModel extends ViewModel {
    private final IUserRepository userRepository;
    private final MutableLiveData<User> _user = new MutableLiveData<>();
    public final LiveData<User> user = _user;

    public TestSignInViewModel(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void getUserByUserNameSync(String username) {
        User user = userRepository.getUserByUsername(username);
        _user.setValue(user);
    }
}

