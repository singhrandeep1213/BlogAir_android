package com.bcabuddies.blogair.welcome;

import android.os.Bundle;

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

        viewModel = ViewModelProviders.of(this, new WelcomeViewModelFactory(this, binding.welcomeBaseLayout))
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
        viewPagerImageList.add(new WelcomeViewPagerModel(R.drawable.ku26itnont711));
        viewPagerImageList.add(new WelcomeViewPagerModel(R.drawable.leaves_plant_dark_129098_800x1420));
        viewPagerImageList.add(new WelcomeViewPagerModel(R.drawable.lpyx));
    }
}
