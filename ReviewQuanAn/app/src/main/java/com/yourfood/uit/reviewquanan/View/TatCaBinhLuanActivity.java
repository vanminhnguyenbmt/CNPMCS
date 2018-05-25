package com.yourfood.uit.reviewquanan.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yourfood.uit.reviewquanan.Adapter.AdapterTatCaBinhLuanQuanAn;
import com.yourfood.uit.reviewquanan.Model.BinhLuanModel;
import com.yourfood.uit.reviewquanan.Model.QuanAnModel;
import com.yourfood.uit.reviewquanan.Model.ThanhVienModel;
import com.yourfood.uit.reviewquanan.R;
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
    LinearLayout btnBinhLuan;
    SwipeRefreshLayout swipeRefreshLayout;
    String maquanan;
    String tenquanan;
    String diachi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tatcabinhluan);

        //quanAnModel = getIntent().getParcelableExtra("binhluanquanan");
        quanAnModel = new QuanAnModel();
        maquanan = getIntent().getStringExtra("maquanan");
        tenquanan = getIntent().getStringExtra("tenquanan");
        diachi = getIntent().getStringExtra("diachi");
        txtTenQuanAn = (TextView) findViewById(R.id.txtTenQuanAn);
        btnBinhLuan = (LinearLayout) findViewById(R.id.btnBinhLuan);
        recyclerBinhLuanQuanAn = (RecyclerView) findViewById(R.id.recyclerBinhLuan) ;
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_orange_dark,
                R.color.colorPrimaryDark);

        /// set toolbar
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtTenQuanAn.setText(tenquanan);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                // Fetching data from server
                LoadBinhLuan(maquanan);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                LoadBinhLuan(maquanan);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        /// sự kiện click button bình luận
        btnBinhLuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iBinhLuan = new Intent(TatCaBinhLuanActivity.this, BinhLuanActivity.class);
                iBinhLuan.putExtra("maquanan", maquanan);
                iBinhLuan.putExtra("tenquan", tenquanan);
                iBinhLuan.putExtra("diachi", diachi);
                startActivity(iBinhLuan);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
       return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void LoadBinhLuan(final String maquanan) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

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
