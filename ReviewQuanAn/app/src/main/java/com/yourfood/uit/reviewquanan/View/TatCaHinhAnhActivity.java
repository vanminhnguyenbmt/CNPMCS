package com.yourfood.uit.reviewquanan.View;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yourfood.uit.reviewquanan.Adapter.AdapterTatCaBinhLuanQuanAn;
import com.yourfood.uit.reviewquanan.Adapter.AdapterTatCaHinhAnh;
import com.yourfood.uit.reviewquanan.Adapter.AdapterViewPagerSlideHinh;
import com.yourfood.uit.reviewquanan.Model.BinhLuanModel;
import com.yourfood.uit.reviewquanan.Model.QuanAnModel;
import com.yourfood.uit.reviewquanan.Model.ThanhVienModel;
import com.yourfood.uit.reviewquanan.R;
import com.yourfood.uit.reviewquanan.View.Fragment.SlideHinhFrament;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ochutgio on 5/16/2018.
 */

public class TatCaHinhAnhActivity extends AppCompatActivity {
    QuanAnModel quanAnModel;
    String maquanan;
    String tenquanan;

    RecyclerView recyclerHinhAnh;
    TextView txtTenQuanAn;
    Toolbar toolbar;
    List<Bitmap>  bitmapList;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tatcahinhanh);

        quanAnModel = new QuanAnModel();
        maquanan = getIntent().getStringExtra("maquanan");
        tenquanan = getIntent().getStringExtra("tenquanan");

        recyclerHinhAnh = (RecyclerView) findViewById(R.id.recyclerHinhAnh);
        txtTenQuanAn = (TextView) findViewById(R.id.txtTenQuanAn);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        progressDialog = new ProgressDialog(this);
        bitmapList = new ArrayList<>();

        txtTenQuanAn.setText(tenquanan);

        /// set toolbar
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progressDialog.setMessage("Đang tải hình");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        LoadHinhAnh(maquanan);

    }

    private void LoadHinhAnh(final String maquanan){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        progressDialog.show();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot dataBinhLuanQuanAnList = dataSnapshot.child("binhluans").child(maquanan);
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
                ///
                for(final BinhLuanModel binhLuanModel : quanAnModel.getBinhluanquanan()){
                    for(String linkhinh : binhLuanModel.getHinhanhBinhLuan()){
                        StorageReference storageHinhBinhLuan = FirebaseStorage.getInstance().getReference().child("Photo").child(linkhinh);
                        long ONE_MEGABYTE = 1024*1024;
                        storageHinhBinhLuan.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                bitmapList.add(bitmap);
                                progressDialog.dismiss();
                                AdapterTatCaHinhAnh adapter = new AdapterTatCaHinhAnh(TatCaHinhAnhActivity.this, R.layout.custom_layout_tatcahinhanh, bitmapList, maquanan);
                                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(TatCaHinhAnhActivity.this, 3);
                                recyclerHinhAnh.setLayoutManager(layoutManager);
                                recyclerHinhAnh.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
