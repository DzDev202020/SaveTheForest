package com.constantine2.student;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.constantine2.student.databinding.ActivitySignInBinding;
import com.constantine2.student.model.Client;
import com.constantine2.student.viewModel.SignInViewModel;
import com.mapbox.search.MapboxSearchSdk;

public class SignInActivity extends AppCompatActivity {

    private static final int SIGN_UP_CODE = 1988;
    private static final String TAG = "TAG";
    ActivitySignInBinding bind;
    SignInViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivitySignInBinding.inflate(getLayoutInflater());
        MapboxSearchSdk.initialize(getApplication(), getString(R.string.mapbox_access_token));


        viewModel = new ViewModelProvider(this).get(SignInViewModel.class);

        viewModel.getSignInState().observe(this, integer -> {

            Log.e(TAG, "observe: " + integer);
            switch (integer) {
                case SignInViewModel.IDEAL:
                    hideProgress();
                    break;
                case SignInViewModel.ON_SIGN_IN:
                    lock();
                    showProgress();
                    break;
                case SignInViewModel.FAIL_SIGN_IN:
                    unlock();
                    failSignIn();
                    hideProgress();
                    break;
                case SignInViewModel.WRONG_SIGN_IN:
                    unlock();
                    wrongSignIn();
                    hideProgress();
                    break;
                case SignInViewModel.DONE_SIGN_IN:
                    lock();
                    DoneSignIn();
                    hideProgress();
                    break;
            }
        });

        bind.signInButton.setOnClickListener(v -> signIn());

        bind.createAccountText.setOnClickListener(v -> signUp());
        setContentView(bind.getRoot());
    }


    private void showProgress() {
        bind.facebookSignIn.setVisibility(View.GONE);
        bind.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        bind.progressBar.setVisibility(View.GONE);
        bind.facebookSignIn.setVisibility(View.VISIBLE);
    }

    private void signUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivityForResult(intent, SIGN_UP_CODE);
    }

    private void signIn() {
        Log.e(TAG, "signIn: ");
        String email = bind.email.getEditText().getText().toString();
        Log.e(TAG, "email: " + email);
        String password = bind.password.getEditText().getText().toString();
        Log.e(TAG, "password: " + password);

        int error = 0;
        if (!email.equals("") && email.contains("@")) {

        } else {
            bind.email.getEditText().setText("");
            error++;
        }
        if (!password.equals("")) {

        } else {
            bind.password.getEditText().setText("");
            error++;
        }

        if (error == 0) {
            viewModel.signIn(email, password);
        }


    }


    private void DoneSignIn() {
        Toast.makeText(this, "nice well sign in", Toast.LENGTH_LONG).show();

        Client c = viewModel.getClient();

        Intent i = new Intent(this, DashboardActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("client", c);
        i.putExtras(b);
        startActivity(i);
        finish();
    }

    private void failSignIn() {
        Toast.makeText(this, R.string.error_when_sign_in, Toast.LENGTH_LONG).show();
    }

    private void wrongSignIn() {
        Toast.makeText(this, R.string.wrong_sign_in, Toast.LENGTH_LONG).show();
    }

    private void unlock() {
        bind.email.setEnabled(true);
        bind.password.setEnabled(true);
        bind.signInButton.setEnabled(true);
        bind.createAccountText.setEnabled(true);
    }

    private void lock() {
        bind.email.setEnabled(false);
        bind.password.setEnabled(false);
        bind.signInButton.setEnabled(false);
        bind.createAccountText.setEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult");
        if (requestCode == SIGN_UP_CODE) {
            Log.e(TAG, "SIGN_UP_CODE");
            if (resultCode == Activity.RESULT_OK) {
                Log.e(TAG, "RESULT_OK");
                Bundle b = data.getExtras();

                Log.e(TAG, b.getString("email"));
                Log.e(TAG, b.getString("password"));
                Log.e(TAG, "RESULT_OK");

                bind.email.getEditText().setText(b.getString("email"));
                bind.password.getEditText().setText(b.getString("password"));

            } else {
                Toast.makeText(getApplicationContext(), R.string.sign_up_fialed, Toast.LENGTH_LONG).show();
            }
        }

    }
}