package com.bcabuddies.blogair.signUp;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.ViewModel;

public class SignUpViewModel extends ViewModel {

    private Context context;
    private View baseLayout;

    public SignUpViewModel(Context context, View baseLayout) {
        this.context = context;
        this.baseLayout = baseLayout;
    }
}
