package com.constantine2.student.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.constantine2.student.repository.SignUpRepository;

import java.sql.Date;

public class SignUpViewModel extends ViewModel {

    public static final int IDEAL_SIGN_UP = 0;
    public static final int ON_SIGN_UP = 1;
    public static final int FAIL_SIGN_UP = 2;
    public static final int WRONG_SIGN_UP = 3;
    public static final int DONE_SIGN_UP = 4;


    SignUpRepository repository;

    public SignUpViewModel() {
        repository = new SignUpRepository();
    }

    public LiveData<Integer> getSignUpState() {
        return repository.getSignUpState();

    }

    public void signUp(String firstName, String lastName, String email, String password, String phoneNumber, Date date) {
        repository.sinUp(firstName, lastName, email, password, phoneNumber, date);
    }
}
