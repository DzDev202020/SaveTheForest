package com.constantine2.student.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.constantine2.student.databinding.DialogCreateDirectionBinding;

public class CreateFireMarkerSelectDialog extends Dialog {
    DirectionDialogCallback callback;

    DialogCreateDirectionBinding bind;

    public CreateFireMarkerSelectDialog(@NonNull Context context, DirectionDialogCallback callback) {
        super(context);
        this.callback = callback;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (bind == null) {
            bind = DialogCreateDirectionBinding.inflate(getLayoutInflater());

            bind.createDirection.setOnClickListener(v -> {
                dismiss();
                callback.createDirection();
            });

            bind.readPosts.setOnClickListener(v -> {
                dismiss();
                callback.readPosts();
            });
            bind.cancel.setOnClickListener(v -> {
                dismiss();
            });
        }
        setContentView(bind.getRoot());

    }


    public interface DirectionDialogCallback {

        void createDirection();

        void readPosts();

    }
}
