package com.example.ochutgio.reviewquanan.View;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.ochutgio.reviewquanan.Adapter.AdapterViewPagerSlideHinh;
import com.example.ochutgio.reviewquanan.Model.BinhLuanModel;
import com.example.ochutgio.reviewquanan.Model.QuanAnModel;
import com.example.ochutgio.reviewquanan.Model.ThanhVienModel;
import com.example.ochutgio.reviewquanan.R;
import com.example.ochutgio.reviewquanan.View.Fragment.SlideHinhFrament;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ImageSlideActivity extends AppCompatActivity{

    QuanAnModel quanAnModel;

    List<Fragment> fragmentList;
    ViewPager viewPagerImageSlide;
    ProgressDialog progressDialog;
    TextView ttxtViTri;

    List<String> list;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_imageslide);

        String maquanan = getIntent().getStringExtra("maquanan");
        int vitri = getIntent().getIntExtra("vitri", 0);

        ttxtViTri = (TextView) findViewById(R.id.txtViTri);
        viewPagerImageSlide = (ViewPager) findViewById(R.id.viewpagerSlideHinh);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading...");
        progressDialog.setIndeterminate(true);

        quanAnModel = new QuanAnModel();
        list = new ArrayList<>();

        if(maquanan != null){
            if(!maquanan.equals("")){
                LoadHinh(maquanan, vitri);
            }

        }
    }

    private void LoadHinh(final String maqa, final int vitri){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        progressDialog.show();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot dataBinhLuanQuanAnList = dataSnapshot.child("binhluans").child(maqa);
                List<BinhLuanModel> binhLuanModelList = new ArrayList<>();
                for (DataSnapshot valueBinhLuan : dataBinhLuanQuanAnList.getChildren()) {
                    BinhLuanModel binhLuanModel = valueBinhLuan.getValue(BinhLuanModel.class);
                    ThanhVienModel thanhVienModel = dataSnapshot.child("thanhviens").child(binhLuanModel.getMauser()).getValue(ThanhVienModel.class);
                    binhLuanModel.setThanhVienModel(thanhVienModel);

                    List<String> hinhanhBinhLuan = new ArrayList<>();
                    DataSnapshot dataHinhAnhBinhLuan = dataSnapshot.child("hinhanhbinhluans").child(valueBinhLuan.getKey());
                    for (DataSnapshot valueHinhAnhBinhLuan : dataHinhAnhBinhLuan.getChildren()) {
                        hinhanhBinhLuan.add(valueHinhAnhBinhLuan.getValue(String.class));
                    }
                    binhLuanModel.setHinhanhBinhLuan(hinhanhBinhLuan);
                    binhLuanModelList.add(binhLuanModel);
                }

                quanAnModel.setBinhluanquanan(binhLuanModelList);

                ////
                for(final BinhLuanModel binhLuanModel : quanAnModel.getBinhluanquanan()){
                    for(String linkhinh : binhLuanModel.getHinhanhBinhLuan()){
                        list.add(linkhinh);
                    }
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
