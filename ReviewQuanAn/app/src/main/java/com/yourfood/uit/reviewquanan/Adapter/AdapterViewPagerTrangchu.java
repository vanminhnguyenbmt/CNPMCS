package com.yourfood.uit.reviewquanan.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yourfood.uit.reviewquanan.View.Fragment.AnGiFragment;
import com.yourfood.uit.reviewquanan.View.Fragment.ODauFragment;

/**
 * Created by ochutgio on 4/18/2018.
 */

public class AdapterViewPagerTrangchu extends FragmentStatePagerAdapter {

    ODauFragment oDauFragment;
    AnGiFragment anGiFragment;

    public AdapterViewPagerTrangchu(FragmentManager fm) {
        super(fm);
        oDauFragment = new ODauFragment();
        anGiFragment = new AnGiFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return oDauFragment;
            case 1:
                return anGiFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
