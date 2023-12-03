package com.constantine2.student;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.constantine2.student.databinding.ActivitySignUpBinding;
import com.constantine2.student.viewModel.SignUpViewModel;

import java.sql.Date;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG ="TAG" ;
    ActivitySignUpBinding bind;
    SignUpViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivitySignUpBinding.inflate(getLayoutInflater());

        viewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        viewModel.getSignUpState().observe(this, integer -> {
            Log.e("TAG", "observe: " + integer);
            switch (integer) {
                case SignUpViewModel.IDEAL_SIGN_UP:
                    hideProgressBar();
                    break;

                case SignUpViewModel.ON_SIGN_UP:
                    showProgressBar();
                    lock();
                    break;

                case SignUpViewModel.FAIL_SIGN_UP:
                    hideProgressBar();
                    unlock();
                    break;

                case SignUpViewModel.WRONG_SIGN_UP:
                    hideProgressBar();
                    unlock();
                    break;

                case SignUpViewModel.DONE_SIGN_UP:
                    showProgressBar();
                    lock();
                    finish();
                    break;

            }
        });

        bind.createAccountButton.setOnClickListener(v -> signUp());
        bind.birthdayButton.setOnClickListener(v -> pickBirthday());

        setContentView(bind.getRoot());
    }

    private void unlock() {
        bind.firstName.setEnabled(true);
        bind.lastName.setEnabled(true);
        bind.email.setEnabled(true);
        bind.password.setEnabled(true);
        bind.phoneNumber.setEnabled(true);
        bind.birthdayButton.setEnabled(true);
        bind.createAccountButton.setEnabled(true);
    }

    private void lock() {
        bind.firstName.setEnabled(false);
        bind.lastName.setEnabled(false);
        bind.email.setEnabled(false);
        bind.password.setEnabled(false);
        bind.phoneNumber.setEnabled(false);
        bind.birthdayButton.setEnabled(false);
        bind.createAccountButton.setEnabled(false);
    }

    private void hideProgressBar() {
        bind.progressBar.setVisibility(View.INVISIBLE);
    }

    private void showProgressBar() {
        bind.progressBar.setVisibility(View.VISIBLE);
    }

    DatePickerDialog datePickerDialog;

    private void pickBirthday() {
        Log.e(TAG, "pickBirthday: ");
        datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> date = new Date(year, month, dayOfMonth), 2022, 2, 1);
        datePickerDialog.show();
    }


    Date date;

    private void signUp() {
        Log.e(TAG, "signUp: ");
        String firstName = bind.firstName.getEditText().getText().toString();
        String lastName = bind.lastName.getEditText().getText().toString();
        String email = bind.email.getEditText().getText().toString();
        String password = bind.password.getEditText().getText().toString();
        String phoneNumber = bind.phoneNumber.getEditText().getText().toString();
        viewModel.signUp(firstName, lastName, email, password, phoneNumber, date);
    }


    @Override
    public void finish() {
        if (viewModel.getSignUpState().getValue() == SignUpViewModel.DONE_SIGN_UP) {
            Log.e(TAG, "finish: DONE_SIGN_UP" );
            Intent intentOutput = new Intent();
            intentOutput.putExtra("email", bind.email.getEditText().getText().toString());
            intentOutput.putExtra("password", bind.password.getEditText().getText().toString());
            setResult(RESULT_OK, intentOutput);
        } else {
            setResult(RESULT_CANCELED, null);
        }

        super.finish();
    }
}