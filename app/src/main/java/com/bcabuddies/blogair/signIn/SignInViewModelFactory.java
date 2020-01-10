package com.bcabuddies.blogair.signIn;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SignInViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Context context;
    private View baseView;

    public SignInViewModelFactory(Context context, View baseView) {
        this.context = context;
        this.baseView = baseView;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SignInViewModel(context, baseView);
    }
}
