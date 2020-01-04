package com.bcabuddies.blogair.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.model.WelcomeViewPagerModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class WelcomeViewPagerAdapter extends PagerAdapter {

    private List<WelcomeViewPagerModel> pageList;
    private Context context;

    public WelcomeViewPagerAdapter(List<WelcomeViewPagerModel> pageList, Context context) {
        this.pageList = pageList;
        this.context = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert inflater != null;
        @SuppressLint("InflateParams") View screenView = inflater.inflate(R.layout.image_item, null);

        ImageView imageView = screenView.findViewById(R.id.image_item_imageView);

        try {
            Glide.with(context).load(pageList.get(position).getImage()).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        container.addView(screenView);

        return screenView;
    }

    @Override
    public int getCount() {
        return pageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
