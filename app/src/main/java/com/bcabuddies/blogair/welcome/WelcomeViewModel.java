package com.bcabuddies.blogair.welcome;

import android.content.Context;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModel;

import com.bcabuddies.blogair.signIn.SignIn;
import com.bcabuddies.blogair.signUp.SignUp;
import com.rahulgaur.utils.Utils;

public class WelcomeViewModel extends ViewModel {

    private Context context;
    private View baseLayout;

    WelcomeViewModel(Context context, View baseLayout) {
        this.context = context;
        this.baseLayout = baseLayout;
    }

    public void onSignIn() {
        Utils.setIntent(context, SignIn.class);
    }

    public void onSignUp() {
        Utils.setIntent(context, SignUp.class);
    }

}
