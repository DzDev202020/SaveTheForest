package com.constantine2.student;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.constantine2.student.databinding.LayoutRecyclerviewAllDataStateBinding;
import com.constantine2.student.databinding.LayoutRecyclerviewEmptyDataBinding;
import com.constantine2.student.databinding.LayoutRecyclerviewErrorDataBinding;
import com.constantine2.student.databinding.LayoutRecyclerviewLoadingDataBinding;


import org.jetbrains.annotations.NotNull;

import kotlin.jvm.internal.Intrinsics;

public class CustomRecyclerView extends ConstraintLayout {


    private LayoutRecyclerviewAllDataStateBinding binding;
    private LayoutRecyclerviewErrorDataBinding errorBinding;
    private LayoutRecyclerviewEmptyDataBinding emptyBinding;
    private LayoutRecyclerviewLoadingDataBinding loadingBinding;
    @NotNull
    private String errorText;
    @NotNull
    private String emptyText;
    @DrawableRes
    private int errorIcon;
    @DrawableRes
    private int emptyIcon;

    public CustomRecyclerView(@NonNull Context context) {
        super(context);
        initView(context, null);

    }

    public CustomRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, null);
    }


    public CustomRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView(Context context, @Nullable AttributeSet attrs) {
        this.binding = LayoutRecyclerviewAllDataStateBinding.inflate(LayoutInflater.from(context), (ViewGroup) this);
//        this.errorBinding = LayoutRecyclerviewErrorDataBinding.bind(binding.customErrorView);
//        this.emptyBinding = this.binding.customEmptyView;
//        this.loadingBinding = this.binding.customOverlayView;
        this.errorText = "";
        this.emptyText = "";
    }

    @NotNull
    public final RecyclerView getRecyclerView() {

        return this.binding.recyclerView;
    }

//    @NotNull
//    public final String getErrorText() {
//        return this.errorText;
//    }

    private final void setErrorText(@NotNull String value) {
        this.errorText = value;
        this.errorBinding.errorMsgText.setText(errorText);
    }

//    @NotNull
//    public final String getEmptyText() {
//        return this.emptyText;
//    }

    public final void setEmptyText(@NotNull String value) {
        this.emptyText = value;
        TextView textView = this.emptyBinding.emptyMessage;
        textView.setText(value);
    }

//    public final int getErrorIcon() {
//        return this.errorIcon;
//    }
//
//    public final void setErrorIcon(int value) {
//        this.errorIcon = value;
//        this.errorBinding.errorImage.setImageResource(value);
//    }
//
//    public final int getEmptyIcon() {
//        return this.emptyIcon;
//    }

//    public final void setEmptyIcon(int value) {
//        this.emptyIcon = value;
//        this.emptyBinding.emptyImage.setImageResource(value);
//    }


    boolean loadReady = false;
    boolean emptyReady = false;
    boolean errorReady = false;

    boolean onEmpty = false;
    boolean onError = false;
    boolean onLoad = false;

    public final void showEmptyView() {
        if (onEmpty)
            return;
        onEmpty = true;
        onError = onLoad = false;

        if (!emptyReady) {
            emptyReady = true;
            emptyBinding = LayoutRecyclerviewEmptyDataBinding.bind(binding.emptyData.inflate());
        }

        emptyBinding.getRoot().setVisibility(VISIBLE);
        binding.recyclerView.setVisibility(GONE);

        if (loadReady)
            loadingBinding.getRoot().setVisibility(INVISIBLE);
        if (errorReady)
            errorBinding.getRoot().setVisibility(INVISIBLE);


    }


    public final void showErrorView(@Nullable String msg) {
//    public final void showErrorView(@Nullable String msg, View.OnClickListener clickListener) {

        if (onError)
            return;
        onError = true;
        onEmpty = onLoad = false;

        if (!errorReady) {
            errorReady = true;
            errorBinding = LayoutRecyclerviewErrorDataBinding.bind(binding.errorData.inflate());
        }

        if (msg != null) {
            this.setErrorText(msg);
        }
//        errorBinding.retry.setOnClickListener(clickListener);

        errorBinding.getRoot().setVisibility(VISIBLE);
        binding.recyclerView.setVisibility(GONE);

        if (loadReady)
            loadingBinding.getRoot().setVisibility(INVISIBLE);
        if (emptyReady)
            emptyBinding.getRoot().setVisibility(INVISIBLE);

    }


    public final void showLoadingView() {

        if (onLoad)
            return;
        onLoad = true;
        onEmpty = onError = false;

        if (!loadReady) {
            loadReady = true;
            loadingBinding = LayoutRecyclerviewLoadingDataBinding.bind(binding.loadingData.inflate());
        }

        loadingBinding.getRoot().setVisibility(VISIBLE);
        binding.recyclerView.setVisibility(GONE);

        if (errorReady)
            errorBinding.getRoot().setVisibility(INVISIBLE);
        if (emptyReady)
            emptyBinding.getRoot().setVisibility(INVISIBLE);
    }

    public final void hideAllViews() {

        if (loadReady && onLoad)
            loadingBinding.getRoot().setVisibility(INVISIBLE);
        if (emptyReady && onEmpty)
            emptyBinding.getRoot().setVisibility(INVISIBLE);
        if (errorReady && onError)
            errorBinding.getRoot().setVisibility(INVISIBLE);

        binding.recyclerView.setVisibility(VISIBLE);
        onError = onLoad = onEmpty = false;
    }

    public final void setOnRetryClickListener(@NotNull final OnClickListener callback) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        this.errorBinding.retry.setOnClickListener(callback::onClick);
    }


    public CustomRecyclerView(@NotNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }
}
