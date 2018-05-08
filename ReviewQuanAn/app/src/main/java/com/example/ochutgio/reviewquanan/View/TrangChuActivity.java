package com.example.ochutgio.reviewquanan.View;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
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
import android.widget.TextView;
import android.widget.Toast;


import com.example.ochutgio.reviewquanan.Adapter.AdapterViewPagerTrangchu;
import com.example.ochutgio.reviewquanan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.InputStream;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ochutgio on 4/3/2018.
 */

public class TrangChuActivity extends AppCompatActivity {

    ViewPager viewPagerTrangChu;
    DrawerLayout mDrawerLayout;
    ImageView imgLogo;
    ImageView imgSearch;
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
        imgSearch = (ImageView) findViewById(R.id.imgSearch);
        viewPagerTrangChu = (ViewPager) findViewById(R.id.viewpager_trangchu);
        rbg_odau_angi = (RadioGroup) findViewById(R.id.rbg_odau_angi);

        rb_angi = (RadioButton) findViewById(R.id.rb_angi);
        rb_odau = (RadioButton) findViewById(R.id.rb_odau);
        rb_tabmoinhat = (RadioButton) findViewById(R.id.rb_tabmoinhat);
        rb_tabdanhmuc = (RadioButton) findViewById(R.id.rb_tabdanhmuc);
        rb_tabkhuvuc = (RadioButton) findViewById(R.id.rb_tabkhuvuc);

        /// set adapter cho viewpager
        AdapterViewPagerTrangchu adapterViewPagerTrangchu = new AdapterViewPagerTrangchu(getSupportFragmentManager());
        viewPagerTrangChu.setAdapter(adapterViewPagerTrangchu);

        /// hàm gọi menu khi click logo
        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // lắng nghe các sự kiện của menu
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                TextView txtTenUser = (TextView) findViewById(R.id.txtTenUser);
                ImageView imvProfile = (ImageView) findViewById(R.id.profile_image);
                txtTenUser.setText(user.getDisplayName());
                new DownLoadImageTask(imvProfile).execute(user.getPhotoUrl().toString());
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        /// sự kiện click vào item menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                if(item.getItemId() == R.id.nav_item5 ){
                    FirebaseAuth.getInstance().signOut();
                    Intent iDangNhap = new Intent(TrangChuActivity.this, DangNhapActivity.class);
                    startActivity(iDangNhap);
                    finish();
                }

                mDrawerLayout.closeDrawers();

                return true;
            }
        });


        /// gọi popup khi click vào tab mới nhất
        rb_tabmoinhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View popupView = getLayoutInflater().inflate(R.layout.popup_moinhat, null);

                RadioGroup rbgroup_tabmoinhat = (RadioGroup) popupView.findViewById(R.id.rbgroup_tab_moinhat);
                /// set text cho rbg_tabmoinhat theo text trong radiobutton được chọn
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
                /// khởi tạo popup
                PopupWindow popupWindow = new PopupWindow(popupView);
                popupWindow.setWidth(getScreenWidth());
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setAnimationStyle(R.style.popup_fade_in_out);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.argb(100,255,255,255)));
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                //popupWindow.update();
                /// Show popup window offset 1,1 to tabRadioGroup.
                popupWindow.showAsDropDown(rb_tabmoinhat, 0, 0);

            }
        });

        /// gọi popup khi click vào tab danh mục
        rb_tabdanhmuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View popupView = getLayoutInflater().inflate(R.layout.popup_danhmuc, null);
                RadioGroup rbgroup_tabdanhmuc = (RadioGroup) popupView.findViewById(R.id.rbgroup_tab_danhmuc);
                /// set text cho rbg_tabdanhmuc theo text trong radiobutton được chọn
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
                /// khởi tạo popup
                PopupWindow popupWindow = new PopupWindow(popupView);
                popupWindow.setWidth(getScreenWidth());
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.argb(100,255,255,255)));
                popupWindow.setAnimationStyle(R.style.popup_fade_in_out);
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                //popupWindow.update();
                /// Show popup window offset 1,1 to tabRadioGroup.
                popupWindow.showAsDropDown(rb_tabmoinhat, 0, 0);
            }
        });

        /// gọi popup khi click vào tab khu vực
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
                /// khởi tạo popup
                PopupWindow popupWindow = new PopupWindow(popupView);
                popupWindow.setWidth(getScreenWidth());
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.argb(100,255,255,255)));
                popupWindow.setAnimationStyle(R.style.popup_fade_in_out);
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                //popupWindow.update();
                /// Show popup window offset 1,1 to tabRadioGroup.
                popupWindow.showAsDropDown(rb_tabmoinhat, 0, 0);
            }
        });

        /// set fragment vào viewPager
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

        /// xử lý gọi fragment nào khi check radiogroup_odau_angi
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

    }


    /// hàm load imageview with url
    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
            Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }
}
