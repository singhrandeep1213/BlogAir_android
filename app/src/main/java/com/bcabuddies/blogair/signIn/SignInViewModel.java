package com.bcabuddies.blogair.signIn;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.ViewModel;

public class SignInViewModel extends ViewModel {

    private Context context;
    private View baseLayout;

    public SignInViewModel(Context context, View baseLayout) {
        this.context = context;
        this.baseLayout = baseLayout;
    }

}
