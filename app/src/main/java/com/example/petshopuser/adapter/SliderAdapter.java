package com.example.petshopuser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.petshopuser.R;
import com.example.petshopuser.databinding.SlidesLayoutBinding;
import com.example.petshopuser.model.ScreenItem;

import java.util.List;

public class SliderAdapter extends PagerAdapter {
    private Context mContext;
    private List<ScreenItem> mListScreen;
    private LayoutInflater inflater;

    // Thêm biến binding để sử dụng View Binding
    private SlidesLayoutBinding binding;

    public SliderAdapter(Context mContext, List<ScreenItem> mListScreen) {
        this.mContext = mContext;
        this.mListScreen = mListScreen;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // Sử dụng View Binding để ánh xạ các thành phần giao diện người dùng
        inflater = LayoutInflater.from(mContext);
        binding = SlidesLayoutBinding.inflate(inflater, container, false);
        View layoutScreen = binding.getRoot();

        // Thiết lập dữ liệu từ ScreenItem
        binding.introTitleSlideLayout.setText(mListScreen.get(position).getTitle());
        binding.introDescriptionSlideLayout.setText(mListScreen.get(position).getDescription());
        binding.introImgSlideLayout.setImageResource(mListScreen.get(position).getScreenImg());

        container.addView(layoutScreen);

        return layoutScreen;
    }

    @Override
    public int getCount() {
        return mListScreen.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}