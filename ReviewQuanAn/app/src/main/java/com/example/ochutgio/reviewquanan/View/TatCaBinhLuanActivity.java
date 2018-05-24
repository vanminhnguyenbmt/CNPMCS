package com.example.ochutgio.reviewquanan.View;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.ochutgio.reviewquanan.Adapter.AdapterTatCaBinhLuanQuanAn;
import com.example.ochutgio.reviewquanan.Model.BinhLuanModel;
import com.example.ochutgio.reviewquanan.Model.QuanAnModel;
import com.example.ochutgio.reviewquanan.Model.ThanhVienModel;
import com.example.ochutgio.reviewquanan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ochutgio on 5/16/2018.
 */

public class TatCaBinhLuanActivity extends AppCompatActivity {

    RecyclerView recyclerBinhLuanQuanAn;
    Toolbar toolbar;
    TextView txtTenQuanAn;
    QuanAnModel quanAnModel;
    String maquanan;
    String tenquanan;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tatcabinhluan);

        //quanAnModel = getIntent().getParcelableExtra("binhluanquanan");
        quanAnModel = new QuanAnModel();
        maquanan = getIntent().getStringExtra("maquanan");
        tenquanan = getIntent().getStringExtra("tenquanan");
        txtTenQuanAn = (TextView) findViewById(R.id.txtTenQuanAn);
        recyclerBinhLuanQuanAn = (RecyclerView) findViewById(R.id.recyclerBinhLuan) ;
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Đang tải bình luận");
        progressDialog.setIndeterminate(true);

        /// set toolbar
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtTenQuanAn.setText(tenquanan);

        LoadBinhLuan(maquanan);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
       return true;
    }

    private void LoadBinhLuan(final String maquanan) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        progressDialog.show();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot dataBinhLuanQuanAnList = dataSnapshot.child("binhluans").child(maquanan);
                List<BinhLuanModel> binhLuanModelList = new ArrayList<>();
                for (DataSnapshot valueBinhLuan : dataBinhLuanQuanAnList.getChildren()) {
                    BinhLuanModel binhLuanModel = valueBinhLuan.getValue(BinhLuanModel.class);
                    binhLuanModel.setMabinhluan(valueBinhLuan.getKey());
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
                progressDialog.dismiss();
                quanAnModel.setBinhluanquanan(binhLuanModelList);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(TatCaBinhLuanActivity.this);
                recyclerBinhLuanQuanAn.setLayoutManager(layoutManager);
                AdapterTatCaBinhLuanQuanAn adapterBinhLuanQuanAn = new AdapterTatCaBinhLuanQuanAn(TatCaBinhLuanActivity.this, R.layout.custom_layout_binhluanquanan, quanAnModel.getBinhluanquanan());
                recyclerBinhLuanQuanAn.setAdapter(adapterBinhLuanQuanAn);
                adapterBinhLuanQuanAn.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
