package com.bcabuddies.blogair.welcome;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.adapter.WelcomeViewPagerAdapter;
import com.bcabuddies.blogair.databinding.ActivityWelcomeBinding;
import com.bcabuddies.blogair.model.WelcomeViewPagerModel;

import java.util.ArrayList;
import java.util.List;

public class Welcome extends AppCompatActivity {

    private WelcomeViewModel viewModel;
    private ActivityWelcomeBinding binding;
    private List<WelcomeViewPagerModel> viewPagerImageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        viewModel = ViewModelProviders.of(this, new WelcomeViewModelFactory(this, binding.getRoot()))
                .get(WelcomeViewModel.class);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        viewPagerImageList = new ArrayList<>();
        addImageData();

        WelcomeViewPagerAdapter welcomeViewPagerAdapter = new WelcomeViewPagerAdapter(viewPagerImageList, this);
        binding.welcomeViewPager.setAdapter(welcomeViewPagerAdapter);
        binding.welcomeTabLayout.setupWithViewPager(binding.welcomeViewPager);
    }

    private void addImageData() {
        //right now it is static, change it to dynamic
        viewPagerImageList.add(new WelcomeViewPagerModel(R.drawable.slider_image_1));
        viewPagerImageList.add(new WelcomeViewPagerModel(R.drawable.slider_image_2));
        viewPagerImageList.add(new WelcomeViewPagerModel(R.drawable.slider_image_3));
    }
}
