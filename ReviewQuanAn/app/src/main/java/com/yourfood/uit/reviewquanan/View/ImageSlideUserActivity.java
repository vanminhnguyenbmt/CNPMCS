package com.yourfood.uit.reviewquanan.View;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.yourfood.uit.reviewquanan.Adapter.AdapterViewPagerSlideHinh;
import com.yourfood.uit.reviewquanan.Model.BinhLuanModel;
import com.yourfood.uit.reviewquanan.R;
import com.yourfood.uit.reviewquanan.View.Fragment.SlideHinhFrament;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ImageSlideUserActivity extends AppCompatActivity{

    BinhLuanModel binhLuanModel;
    List<Fragment> fragmentList;

    ViewPager viewPagerImageSlide;
    TextView ttxtViTri;
    ProgressDialog progressDialog;

    List<String> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_imageslide);

        String mabinhluan = getIntent().getStringExtra("mabinhluan");
        int vitri = getIntent().getIntExtra("vitri", 0);

        ttxtViTri = (TextView) findViewById(R.id.txtViTri);
        viewPagerImageSlide = (ViewPager) findViewById(R.id.viewpagerSlideHinh);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading...");
        progressDialog.setIndeterminate(true);

        binhLuanModel = new BinhLuanModel();
        list = new ArrayList<>();

        Log.d( "kiemtra", mabinhluan);
        if(mabinhluan != null){
            if(!mabinhluan.equals("")){
                LoadHinh(mabinhluan, vitri);
            }

        }
    }

    private void LoadHinh(final String mabinhluan, final int vitri){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        progressDialog.show();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<String> hinhanhBinhLuan = new ArrayList<>();
                DataSnapshot dataHinhAnhBinhLuan = dataSnapshot.child("hinhanhbinhluans").child(mabinhluan);
                for (DataSnapshot valueHinhAnhBinhLuan : dataHinhAnhBinhLuan.getChildren()) {
                    hinhanhBinhLuan.add(valueHinhAnhBinhLuan.getValue(String.class));
                }

                binhLuanModel.setHinhanhBinhLuan(hinhanhBinhLuan);

                for(String linkhinh : binhLuanModel.getHinhanhBinhLuan()){
                    list.add(linkhinh);
                }

                progressDialog.dismiss();
                ttxtViTri.setText("1 / " + list.size());
                SlideHinh(list, vitri);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void SlideHinh(final List<String> list, int vitri){

        fragmentList = new ArrayList<>();
        for(int i = 0; i < list.size(); i++) {
            SlideHinhFrament slideHinhFrament = new SlideHinhFrament();

            Bundle bundle = new Bundle();

            bundle.putString("string", list.get(i));
            slideHinhFrament.setArguments(bundle);

            fragmentList.add(slideHinhFrament);
        }

        AdapterViewPagerSlideHinh adapterViewPagerSlideHinh = new AdapterViewPagerSlideHinh(getSupportFragmentManager(), fragmentList);
        viewPagerImageSlide.setAdapter(adapterViewPagerSlideHinh);
        viewPagerImageSlide.setCurrentItem(vitri);
        viewPagerImageSlide.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                ttxtViTri.setText( (position + 1) + " / " + list.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        adapterViewPagerSlideHinh.notifyDataSetChanged();

    }
}
