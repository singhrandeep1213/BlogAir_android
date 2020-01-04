package com.bcabuddies.blogair.welcome;

import android.content.Context;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModel;

public class WelcomeViewModel extends ViewModel {

    private Context context;
    private ConstraintLayout baseLayout;

    WelcomeViewModel(Context context, ConstraintLayout baseLayout) {
        this.context = context;
        this.baseLayout = baseLayout;
    }

    public void onSignIn() {

    }

    public void onSignUp() {

    }

}
