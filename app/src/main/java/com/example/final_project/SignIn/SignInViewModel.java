package com.example.final_project.SignIn;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.final_project.database.entities.User;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignInViewModel extends AndroidViewModel {

    private final MutableLiveData<User> _user = new MutableLiveData<User>();
    public final LiveData<User> user = _user;
    private final SignInRepository signInRepository;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public SignInViewModel(@NonNull Application application) {
        super(application);
        signInRepository = SignInRepository.getRepository(application);
    }
    public void getUserByUserName(String username) {
        executorService.execute(() -> {
            User checkUser = signInRepository.getUserByUsername(username);
            _user.postValue(checkUser);
        });
    }
}
