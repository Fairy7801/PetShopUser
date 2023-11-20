package com.example.petshopuser.common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.petshopuser.DangNhapActivity;
import com.example.petshopuser.R;
import com.example.petshopuser.adapter.SliderAdapter;
import com.example.petshopuser.databinding.ActivityBeginBinding;
import com.example.petshopuser.databinding.ActivityIntroBinding;
import com.example.petshopuser.model.ScreenItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {
    private ActivityIntroBinding binding;
    SliderAdapter sliderAdapter;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isIntroActivityOpenedPreviously();

        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("FindFood", "Ứng dụng tìm kiếm vào giao đồ ăn thông minh", R.drawable.a1));
        mList.add(new ScreenItem("Gọi đâu có đó", "Tìm kiếm quán ăn, và đặt đồ ăn toàn quốc với tốc độ bàn thờ", R.drawable.a3));
        mList.add(new ScreenItem("Ăn uống thả ga, không lo về giá", "Giá cả không là vấn đề", R.drawable.a2));

        sliderAdapter = new SliderAdapter(this, mList);
        binding.screenViewpagerIntroActivity.setAdapter(sliderAdapter);

        binding.tabIndicatorIntroActivity.setupWithViewPager(binding.screenViewpagerIntroActivity);

        //Sự kiện click Next
        binding.btnNextIntroActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = binding.screenViewpagerIntroActivity.getCurrentItem();
                if (position < mList.size()) {
                    position++;
                    binding.screenViewpagerIntroActivity.setCurrentItem(position);
                }
                if (position == mList.size() - 1) { // Khi đến màn hình cuối
                    // TODO : Hiện nút bắt đầu, ẩn indicator và button tiếp theo
                    loaddLastScreen();
                }
            }
        });

        //Sự kiện cho dấu chấm trên màn hình
        binding.tabIndicatorIntroActivity.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size() - 1) {
                    loaddLastScreen();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        //Sự kiện khi click start
        binding.btnGetStartedIntroActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lưu lại biến boolean vào bộ nhớ để lần sau khi người dùng mở app
                // Chúng ta sẽ biết họ đã vào introActivity rồi
                // Gọi hàm lưu biến boolean
                savePrefsData();
                // Mở màn hình đăng nhập
                Intent mainActivity = new Intent(getApplicationContext(), DangNhapActivity.class);
                startActivity(mainActivity);
                finish();
            }
        });

        // Sự kiện cho skip
        binding.tvSkipIntroActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.screenViewpagerIntroActivity.setCurrentItem(mList.size());
            }
        });

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(view);
    }

    // Kiểm tra xem đã mở activity này chưa và thựuc hiện chuyển sang màn hình đăng nhập
    private void isIntroActivityOpenedPreviously() {
        if (restorePrefData()) {
            Intent mainActivity = new Intent(getApplicationContext(), DangNhapActivity.class);
            startActivity(mainActivity);
            finish();
        }
    }

    private boolean restorePrefData() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        return pref.getBoolean("isIntroOpnend", false);
    }

    private void savePrefsData() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend", true);
        editor.apply();
    }

    // Ẩn nút next, skip và dấu chấm, hiển thị animation cho nút start
    private void loaddLastScreen() {
        binding.btnNextIntroActivity.setVisibility(View.INVISIBLE);
        binding.btnGetStartedIntroActivity.setVisibility(View.VISIBLE);
        binding.tvSkipIntroActivity.setVisibility(View.INVISIBLE);
        binding.tabIndicatorIntroActivity.setVisibility(View.INVISIBLE);
        binding.btnGetStartedIntroActivity.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation));
    }
}