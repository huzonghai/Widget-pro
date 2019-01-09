package com.ck.newssdk.ui.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ck.newssdk.R;
import com.ck.newssdk.ui.CkFragment;
import com.ck.newssdk.utils.indicator.MagicIndicator;
import com.ck.newssdk.utils.indicator.ViewPagerHelper;
import com.ck.newssdk.utils.indicator.buildins.UIUtil;
import com.ck.newssdk.utils.indicator.buildins.commonnavigator.CommonNavigator;
import com.ck.newssdk.utils.indicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.ck.newssdk.utils.indicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.ck.newssdk.utils.indicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.ck.newssdk.utils.indicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity {

    protected List<String> topics = new ArrayList<>();
    protected List<CkFragment> fragments = new ArrayList<>();

    private ViewPager mViewPager;
    private CkPageAdapter mAdapter;
    private MagicIndicator mIndicator;
    private CommonNavigator mCommonNavigator;
    protected ProgressBar loading;
    protected ImageView noNet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.bg));
        }
        setContentView(R.layout.activity_ck);
        mIndicator = findViewById(R.id.magic_Indicator);
        mViewPager = findViewById(R.id.view_pager);
        loading = findViewById(R.id.loading);
        noNet = findViewById(R.id.no_net_iv);

        mIndicator.setBackgroundColor(Color.parseColor("#FF9400"));

        mCommonNavigator = new CommonNavigator(this);
        mCommonNavigator.setSkimOver(true);
        mCommonNavigator.setRightPadding(UIUtil.getScreenWidth(this) / 2);
        mCommonNavigator.setLeftPadding(UIUtil.getScreenWidth(this) / 2);
        mAdapter = new CkPageAdapter(getSupportFragmentManager());
    }

    protected void initNavigator() {
        mAdapter.setFragments(fragments);
        mViewPager.setAdapter(mAdapter);
        mCommonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return topics.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setText(topics.get(index));
                clipPagerTitleView.setTextSize(28);
                clipPagerTitleView.setTextColor(Color.parseColor("#99ffffff"));
                clipPagerTitleView.setClipColor(Color.WHITE);
                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        mIndicator.setNavigator(mCommonNavigator);
        ViewPagerHelper.bind(mIndicator, mViewPager);
        loading.setVisibility(View.GONE);
    }
}
