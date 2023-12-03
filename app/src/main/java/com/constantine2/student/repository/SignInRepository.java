package com.constantine2.student.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.constantine2.student.model.Client;
import com.constantine2.student.retrofit.ClientApi;
import com.constantine2.student.retrofit.RetrofitApi;
import com.constantine2.student.viewModel.SignInViewModel;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInRepository {

    MutableLiveData<Integer> signInState;
    ClientApi clientApi;
    Client client;

    public SignInRepository() {
        signInState = new MutableLiveData<>(SignInViewModel.IDEAL);
        clientApi = RetrofitApi.getClientApi();
    }


    public LiveData<Integer> getSignInState() {
        return signInState;
    }

    public void signIn(String email, String password) {
        signInState.setValue(SignInViewModel.ON_SIGN_IN);
        clientApi.getClient(email, password).enqueue(new Callback<Client>() {
            @Override
            public void onResponse(@NotNull Call<Client> call, @NotNull Response<Client> response) {

                if (response.isSuccessful()) {
                    client = response.body();
                    signInState.setValue(SignInViewModel.DONE_SIGN_IN);
                } else {
                    signInState.setValue(SignInViewModel.WRONG_SIGN_IN);
                    signInState.setValue(SignInViewModel.IDEAL);
                }
            }

            @Override
            public void onFailure(@NotNull Call<Client> call, @NotNull Throwable t) {
                signInState.setValue(SignInViewModel.FAIL_SIGN_IN);
                signInState.setValue(SignInViewModel.IDEAL);
            }
        });
    }

    public Client getClient() {
        return client;
    }
}
