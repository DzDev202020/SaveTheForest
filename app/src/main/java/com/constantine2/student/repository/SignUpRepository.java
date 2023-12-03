package com.constantine2.student.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.constantine2.student.model.Client;
import com.constantine2.student.retrofit.ClientApi;
import com.constantine2.student.retrofit.RetrofitApi;
import com.constantine2.student.viewModel.SignUpViewModel;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpRepository {


    MutableLiveData<Integer> signUpState;
    ClientApi clientApi;
    Client newClient;

    public SignUpRepository() {
        signUpState = new MutableLiveData<>(SignUpViewModel.IDEAL_SIGN_UP);
        clientApi = RetrofitApi.getClientApi();
    }

    public LiveData<Integer> getSignUpState() {
        return signUpState;
    }

    public void sinUp(String firstName, String lastName, String email, String password, String phoneNumber, Date date) {
        signUpState.setValue(SignUpViewModel.ON_SIGN_UP);

        Client client = new Client(firstName, lastName, email, date, password, phoneNumber);

        clientApi.createClient(client).enqueue(new Callback<Client>() {
            @Override
            public void onResponse(@NotNull Call<Client> call, @NotNull Response<Client> response) {
                if (response.isSuccessful()) {
                    newClient = response.body();
                    signUpState.setValue(SignUpViewModel.DONE_SIGN_UP);
                } else {

                    signUpState.setValue(SignUpViewModel.WRONG_SIGN_UP);
                }
            }

            @Override
            public void onFailure(@NotNull Call<Client> call, @NotNull Throwable t) {

                signUpState.setValue(SignUpViewModel.FAIL_SIGN_UP);
            }
        });
    }
}
