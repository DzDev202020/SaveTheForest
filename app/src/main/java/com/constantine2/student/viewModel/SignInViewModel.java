package com.constantine2.student.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.constantine2.student.model.Client;
import com.constantine2.student.repository.SignInRepository;

public class SignInViewModel extends ViewModel {


    public static final int IDEAL = 0;
    public static final int ON_SIGN_IN = 1;
    public static final int FAIL_SIGN_IN = 2;
    public static final int WRONG_SIGN_IN = 3;
    public static final int DONE_SIGN_IN = 4;
    private static final String TAG ="TAG" ;

    SignInRepository repository;

    public SignInViewModel() {
        repository = new SignInRepository();
    }

    public LiveData<Integer> getSignInState() {
        return repository.getSignInState();
    }

    public void signIn(String email, String password) {
        Log.e(TAG, "View model signIn");

        repository.signIn(email, password);
    }

    public Client getClient() {
        return repository.getClient();
    }
}
