package com.ck.newssdk.ui.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.ck.newssdk.ui.CkFragment;

import java.util.List;

public class CkPageAdapter extends FragmentStatePagerAdapter {
    private List<CkFragment> fragments;

    CkPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setFragments(List<CkFragment> fragments) {
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments != null ? fragments.size() : 0;
    }

    //防止重新销毁视图
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //如果注释这行，那么不管怎么切换，page都不会被销毁
//        super.destroyItem(container, position, object);
    }
}
