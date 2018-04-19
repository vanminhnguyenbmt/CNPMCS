package com.example.ochutgio.reviewquanan.View;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.example.ochutgio.reviewquanan.Adapter.AdapterViewPagerTrangchu;
import com.example.ochutgio.reviewquanan.R;

/**
 * Created by ochutgio on 4/3/2018.
 */

public class TrangChuActivity extends AppCompatActivity {

    ViewPager viewPagerTrangChu;
    private DrawerLayout mDrawerLayout;
    ImageView imgLogo;
    RadioGroup rbg_odau_angi;

    RadioButton rb_odau;
    RadioButton rb_angi;
    RadioButton rb_tabmoinhat;
    RadioButton rb_tabdanhmuc;
    RadioButton rb_tabkhuvuc;

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_trangchu);

        mDrawerLayout =(DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        imgLogo = (ImageView)findViewById(R.id.imgLogo);
        viewPagerTrangChu = (ViewPager) findViewById(R.id.viewpager_trangchu);
        rbg_odau_angi = (RadioGroup) findViewById(R.id.rbg_odau_angi);

        rb_angi = (RadioButton) findViewById(R.id.rb_angi);
        rb_odau = (RadioButton) findViewById(R.id.rb_odau);
        rb_tabmoinhat = (RadioButton) findViewById(R.id.rb_tabmoinhat);
        rb_tabdanhmuc = (RadioButton) findViewById(R.id.rb_tabdanhmuc);
        rb_tabkhuvuc = (RadioButton) findViewById(R.id.rb_tabkhuvuc);

        AdapterViewPagerTrangchu adapterViewPagerTrangchu = new AdapterViewPagerTrangchu(getSupportFragmentManager());
        viewPagerTrangChu.setAdapter(adapterViewPagerTrangchu);
        viewPagerTrangChu.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        rb_angi.setChecked(true);
                        break;
                    case 1:
                        rb_odau.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        rbg_odau_angi.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_angi:
                        viewPagerTrangChu.setCurrentItem(0);
                        break;
                    case R.id.rb_odau:
                        viewPagerTrangChu.setCurrentItem(1);
                        break;
                }
            }
        });

        rb_tabmoinhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View popupView = getLayoutInflater().inflate(R.layout.popup_moinhat, null);

                RadioGroup rbgroup_tabmoinhat = (RadioGroup) popupView.findViewById(R.id.rbgroup_tab_moinhat);
                rbgroup_tabmoinhat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        switch (i){
                            case R.id.rb_moinhat:
                                RadioButton rb_moinhat = (RadioButton) popupView.findViewById(R.id.rb_moinhat);
                                rb_tabmoinhat.setText(rb_moinhat.getText().toString());
                                break;
                            case R.id.rb_gantoi:
                                RadioButton rb_gantoi = (RadioButton) popupView.findViewById(R.id.rb_gantoi);
                                rb_tabmoinhat.setText(rb_gantoi.getText().toString());
                                break;
                            case R.id.rb_giaohang:
                                RadioButton rb_giaohang = (RadioButton) popupView.findViewById(R.id.rb_giaohang);
                                rb_tabmoinhat.setText(rb_giaohang.getText().toString());
                                break;
                            case R.id.rb_khuyenmai:
                                RadioButton rb_khuyenmai =(RadioButton) popupView.findViewById(R.id.rb_khuyenmai);
                                rb_tabmoinhat.setText(rb_khuyenmai.getText().toString());
                                break;
                        }
                    }
                });

                PopupWindow popupWindow = new PopupWindow(popupView);
                popupWindow.setWidth(getScreenWidth());
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

                popupWindow.setAnimationStyle(R.style.popup_fade_in_out);

                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.argb(100,255,255,255)));

                popupWindow.setFocusable(true);

                popupWindow.setOutsideTouchable(true);

                //popupWindow.update();
                // Show popup window offset 1,1 to tabRadioGroup.
                popupWindow.showAsDropDown(rb_tabmoinhat, 1, 1);

            }
        });


        rb_tabdanhmuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View popupView = getLayoutInflater().inflate(R.layout.popup_danhmuc, null);
                RadioGroup rbgroup_tabdanhmuc = (RadioGroup) popupView.findViewById(R.id.rbgroup_tab_danhmuc);
                rbgroup_tabdanhmuc.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        switch (i){
                            case R.id.rb_hot:
                                RadioButton rb_moinhat = (RadioButton) popupView.findViewById(R.id.rb_hot);
                                rb_tabdanhmuc.setText(rb_moinhat.getText().toString());
                                break;
                            case R.id.rb_dacsan:
                                RadioButton rb_gantoi = (RadioButton) popupView.findViewById(R.id.rb_dacsan);
                                rb_tabdanhmuc.setText(rb_gantoi.getText().toString());
                                break;
                            case R.id.rb_monlau:
                                RadioButton rb_giaohang = (RadioButton) popupView.findViewById(R.id.rb_monlau);
                                rb_tabdanhmuc.setText(rb_giaohang.getText().toString());
                                break;
                            case R.id.rb_monnuong:
                                RadioButton rb_khuyenmai =(RadioButton) popupView.findViewById(R.id.rb_monnuong);
                                rb_tabdanhmuc.setText(rb_khuyenmai.getText().toString());
                                break;
                        }
                    }
                });


                PopupWindow popupWindow = new PopupWindow(popupView);
                popupWindow.setWidth(getScreenWidth());
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.argb(100,255,255,255)));

                popupWindow.setAnimationStyle(R.style.popup_fade_in_out);

                popupWindow.setFocusable(true);

                popupWindow.setOutsideTouchable(true);

                //popupWindow.update();

                // Show popup window offset 1,1 to tabRadioGroup.
                popupWindow.showAsDropDown(rb_tabmoinhat, 1, 1);
            }
        });

        rb_tabkhuvuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View popupView = getLayoutInflater().inflate(R.layout.popup_khuvuc, null);

                RadioGroup rbgroup_tabkhuvuc = (RadioGroup) popupView.findViewById(R.id.rbgroup_tab_khuvuc);
                rbgroup_tabkhuvuc.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        switch (i){
                            case R.id.rb_langdaihoc:
                                RadioButton rb_moinhat = (RadioButton) popupView.findViewById(R.id.rb_langdaihoc);
                                rb_tabkhuvuc.setText(rb_moinhat.getText().toString());
                                break;
                            case R.id.rb_thuduc:
                                RadioButton rb_gantoi = (RadioButton) popupView.findViewById(R.id.rb_thuduc);
                                rb_tabkhuvuc.setText(rb_gantoi.getText().toString());
                                break;

                        }
                    }
                });

                PopupWindow popupWindow = new PopupWindow(popupView);
                popupWindow.setWidth(getScreenWidth());
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.argb(100,255,255,255)));

                popupWindow.setAnimationStyle(R.style.popup_fade_in_out);

                popupWindow.setFocusable(true);

                popupWindow.setOutsideTouchable(true);

                //popupWindow.update();

                // Show popup window offset 1,1 to tabRadioGroup.
                popupWindow.showAsDropDown(rb_tabmoinhat, 1, 1);
            }
        });


        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });



        // drawer listener event
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // set item as selected to persist highlight
                item.setChecked(true);
                // close drawer when item is tapped
                mDrawerLayout.closeDrawers();

                // Add code here to update the UI based on the item selected
                // For example, swap UI fragments here

                return true;
            }
        });

    }
}
