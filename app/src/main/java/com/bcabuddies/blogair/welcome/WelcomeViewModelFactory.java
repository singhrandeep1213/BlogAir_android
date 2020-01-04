package com.bcabuddies.blogair.welcome;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class WelcomeViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Context context;
    private ConstraintLayout baseLayout;

    public WelcomeViewModelFactory(Context context, ConstraintLayout baseLayout) {
        this.context = context;
        this.baseLayout = baseLayout;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new WelcomeViewModel(context, baseLayout);
    }
}
