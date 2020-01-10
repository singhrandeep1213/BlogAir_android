package com.bcabuddies.blogair.signUp;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SignUpViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Context context;
    private View baseLayout;

    public SignUpViewModelFactory(Context context, View baseLayout) {
        this.context = context;
        this.baseLayout = baseLayout;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SignUpViewModel(context, baseLayout);
    }
}
