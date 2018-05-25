package com.yourfood.uit.reviewquanan.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.maps.model.Marker;
import com.yourfood.uit.reviewquanan.Adapter.AdapterBinhLuanQuanAn;
import com.yourfood.uit.reviewquanan.Controller.ThucDonController;
import com.yourfood.uit.reviewquanan.Model.BinhLuanModel;
import com.yourfood.uit.reviewquanan.Model.ChiNhanhQuanAnModel;
import com.yourfood.uit.reviewquanan.Model.QuanAnModel;
import com.yourfood.uit.reviewquanan.Model.ThanhVienModel;
import com.yourfood.uit.reviewquanan.Model.TienIchModel;
import com.yourfood.uit.reviewquanan.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ochutgio on 5/3/2018.
 */

public class ChiTietQuanAnActivity extends AppCompatActivity implements OnMapReadyCallback{

    TextView txtTenQuanAnChiTiet;
    TextView txtTenQuanAn;
    TextView txtTongSoHinhAnhQuanAn;
    TextView txtTongSoBinhLuanQuanAn;
    TextView txtThoiGianHoatDong;
    TextView txtTrangThaiHoatDong;
    TextView txtDiemQuanAnChiTiet;
    TextView txtDiaChi;
    TextView txtKhoangCach;
    TextView txtLoaiQuanAn;
    TextView txtQuangGia;

    LinearLayout KhungTienIch;
    LinearLayout btnBinhLuan;
    LinearLayout containerHinhAnh;
    LinearLayout containerBinhLuan;
    LinearLayout khungThucDon;
    VideoView videoTrailer;
    ImageView imvHinhAnhQuanAn;
    ImageView imvPlayVideo;
    ExpandableListView expanded_thucdon;
    RecyclerView recyclerBinhLuanQuanAn;
    RecyclerView recyclerThucDon;
    Toolbar toolbar;

    NestedScrollView nestedScrollChiTietQuanAn;
    AdapterBinhLuanQuanAn adapterBinhLuanQuanAn;

    MapFragment mapQuanAn;
    private GoogleMap googleMap;
    QuanAnModel quanAnModel;
    ChiNhanhQuanAnModel chiNhanhQuanAnModelMin;
    ThucDonController thucDonController;

    static long ONE_MEGABYTE = 1024 * 1024;
    double diemquanan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_chitietquanan);

        quanAnModel = getIntent().getParcelableExtra("quanan");
        thucDonController = new ThucDonController();

        txtTenQuanAnChiTiet = (TextView) findViewById(R.id.txtTenQuanAnChiTiet);
        txtTenQuanAn = (TextView) findViewById(R.id.txtTenQuanAn);
        txtTongSoHinhAnhQuanAn = (TextView) findViewById(R.id.txtTongSoHinhAnhQuanAn);
        txtTongSoBinhLuanQuanAn = (TextView) findViewById(R.id.txtTongSoBinhLuanQuanAn);
        txtThoiGianHoatDong = (TextView) findViewById(R.id.txtThoiGianHoatDong);
        txtTrangThaiHoatDong = (TextView) findViewById(R.id.txtTrangThaiHoatDong);
        txtDiemQuanAnChiTiet = (TextView) findViewById(R.id.txtDiemQuanAnChiTiet);
        txtDiaChi = (TextView) findViewById(R.id.txtDiaChi);
        txtKhoangCach = (TextView) findViewById(R.id.txtKhoangCach);
        txtQuangGia = (TextView) findViewById(R.id.txtQuangGia);

        nestedScrollChiTietQuanAn = (NestedScrollView) findViewById(R.id.nestScrollViewChiTietQuanAn);
        imvHinhAnhQuanAn = (ImageView) findViewById(R.id.imvHinhAnhQuanAn);
        imvPlayVideo = (ImageView) findViewById(R.id.imvPlayVideo);
        recyclerBinhLuanQuanAn = (RecyclerView) findViewById(R.id.recyclerBinhLuan) ;
        recyclerThucDon = (RecyclerView) findViewById(R.id.recyclerThucDon);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnBinhLuan = (LinearLayout) findViewById(R.id.btnBinhLuan);
        KhungTienIch = (LinearLayout) findViewById(R.id.KhungTienIch);
        khungThucDon = (LinearLayout) findViewById(R.id.khungThucDon);
        containerBinhLuan = (LinearLayout) findViewById(R.id.containerBinhLuan);
        containerHinhAnh = (LinearLayout) findViewById(R.id.containerHinhAnh);
        videoTrailer = (VideoView) findViewById(R.id.videoTrailer);

        mapQuanAn = (MapFragment) getFragmentManager().findFragmentById(R.id.mapQuanAn);
        mapQuanAn.getMapAsync(this);

        /// set toolbar
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /// tìm chi nhánh quán ăn gần nhất
        if(quanAnModel.getChinhanhquanan().size() > 0) {
            chiNhanhQuanAnModelMin = quanAnModel.getChinhanhquanan().get(0);
            for (ChiNhanhQuanAnModel chiNhanhQuanAnModel : quanAnModel.getChinhanhquanan()) {
                if (chiNhanhQuanAnModelMin.getKhoangcach() > chiNhanhQuanAnModel.getKhoangcach()) {
                    chiNhanhQuanAnModelMin = chiNhanhQuanAnModel;
                }
            }
        }

        HienThiChiTietQuanAn();

        ///
        khungThucDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iBinhLuan = new Intent(ChiTietQuanAnActivity.this, ThemThucDonActivity.class);
                iBinhLuan.putExtra("maquanan", quanAnModel.getMaquanan());
                iBinhLuan.putExtra("tenquan", quanAnModel.getTenquanan());
                iBinhLuan.putExtra("diachi", chiNhanhQuanAnModelMin.getDiachi());
                startActivity(iBinhLuan);
            }
        });

        /// sự kiện click button bình luận
        btnBinhLuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iBinhLuan = new Intent(ChiTietQuanAnActivity.this, BinhLuanActivity.class);
                iBinhLuan.putExtra("maquanan", quanAnModel.getMaquanan());
                iBinhLuan.putExtra("tenquan", quanAnModel.getTenquanan());
                iBinhLuan.putExtra("diachi", chiNhanhQuanAnModelMin.getDiachi());
                startActivity(iBinhLuan);
            }
        });

        containerBinhLuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quanAnModel.getBinhluanquanan().size() > 0){
                    Intent iTatCaBinhLuan = new Intent(ChiTietQuanAnActivity.this, TatCaBinhLuanActivity.class);
                    iTatCaBinhLuan.putExtra("maquanan", quanAnModel.getMaquanan());
                    iTatCaBinhLuan.putExtra("tenquanan", quanAnModel.getTenquanan());
                    iTatCaBinhLuan.putExtra("diachi", chiNhanhQuanAnModelMin.getDiachi());
                    startActivity(iTatCaBinhLuan);
                }

            }
        });

        containerHinhAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(txtTongSoHinhAnhQuanAn.getText().toString()) > 0){
                    Intent iTatCaHinhAnh = new Intent(ChiTietQuanAnActivity.this, TatCaHinhAnhActivity.class);
                    iTatCaHinhAnh.putExtra("maquanan", quanAnModel.getMaquanan());
                    iTatCaHinhAnh.putExtra("tenquanan", quanAnModel.getTenquanan());
                    startActivity(iTatCaHinhAnh);
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void HienThiChiTietQuanAn(){
        /// xử lý thời gian đóng mở cửa
        Calendar calendar =  Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String giohientai = dateFormat.format(calendar.getTime());
        String giommocua = quanAnModel.getGiomocua();
        String giodongcua = quanAnModel.getGiodongcua();
        try{
            Date dateHienTai = dateFormat.parse(giohientai);
            Date dateMoCua = dateFormat.parse(giommocua);
            Date dateDongCua = dateFormat.parse(giodongcua);

            if (dateHienTai.after(dateMoCua) && dateHienTai.before(dateDongCua)){
                txtTrangThaiHoatDong.setText("Đang mở cửa");
            }else {
                txtTrangThaiHoatDong.setText("Đã đóng cửa");
            }
        }catch (ParseException e){
            e.printStackTrace();
        }

        /// lấy các thông tin cơ bản của quán ăn
        nestedScrollChiTietQuanAn.smoothScrollTo(0,0);
        txtTenQuanAn.setText(quanAnModel.getTenquanan());
        txtTenQuanAnChiTiet.setText(quanAnModel.getTenquanan());

        txtThoiGianHoatDong.setText(quanAnModel.getGiomocua() + " - " + quanAnModel.getGiodongcua());
        if( quanAnModel.getGiatoida() != 0 && quanAnModel.getGiatoithieu() != 0){
            txtQuangGia.setText(quanAnModel.getGiatoithieu() + "VNĐ - " + quanAnModel.getGiatoida() + "VNĐ");
        }


        /// lấy video của quán ăn
        if(quanAnModel.getVideogioithieu() != null){
            if(!quanAnModel.getVideogioithieu().equals("")){
                videoTrailer.setVisibility(View.VISIBLE);
                imvPlayVideo.setVisibility(View.VISIBLE);
                imvHinhAnhQuanAn.setVisibility(View.GONE);

                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Video").child(quanAnModel.getVideogioithieu());
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        videoTrailer.setVideoURI(uri);
                        videoTrailer.seekTo(1000);
                    }
                });
                imvPlayVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imvPlayVideo.setVisibility(View.GONE);
                        videoTrailer.start();
                        MediaController mediaController = new MediaController(ChiTietQuanAnActivity.this);
                        videoTrailer.setMediaController(mediaController);
                    }
                });
            }else {
                imvHinhAnhQuanAn.setVisibility(View.VISIBLE);
                videoTrailer.setVisibility(View.GONE);
                imvPlayVideo.setVisibility(View.GONE);
            }

        }else {
            imvHinhAnhQuanAn.setVisibility(View.VISIBLE);
            videoTrailer.setVisibility(View.GONE);
            imvPlayVideo.setVisibility(View.GONE);
        }


        /// Hiển thị chi nhánh quán ăn gần nhất
        txtDiaChi.setText(chiNhanhQuanAnModelMin.getDiachi());
        txtKhoangCach.setText(String.format("%.1f", chiNhanhQuanAnModelMin.getKhoangcach()) + " km");

        if(quanAnModel.getHinhanhquanan().size() > 0){
            /// lấy hình ảnh quán ăn từ firebase
            StorageReference storageHinhQuanAn = FirebaseStorage.getInstance().getReference().child("Photo").child(quanAnModel.getHinhanhquanan().get(0));
            storageHinhQuanAn.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imvHinhAnhQuanAn.setImageBitmap(bitmap);
                }
            });
        }

        DownLoadHinhTienIch();

    }

    private void LoadBinhLuan(final String maquanan) {
        final QuanAnModel quanAnModeln = new QuanAnModel();
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

                quanAnModeln.setBinhluanquanan(binhLuanModelList);
                if(quanAnModeln.getBinhluanquanan() != null){
                    txtTongSoBinhLuanQuanAn.setText(quanAnModeln.getBinhluanquanan().size() + "");

                    /// tính điểm trung bình quán ăn
                    int tonghinhanh = 0;
                    double tongdiem = 0;
                    for(BinhLuanModel binhLuanModel: quanAnModeln.getBinhluanquanan()){
                        tonghinhanh = tonghinhanh + binhLuanModel.getHinhanhBinhLuan().size();
                        tongdiem = tongdiem + binhLuanModel.getChamdiem();
                    }
                    if(tongdiem > 0){
                        double diemtb = tongdiem / quanAnModeln.getBinhluanquanan().size();
                        if(diemtb > 5){
                            txtDiemQuanAnChiTiet.setTextColor(Color.WHITE);
                        }else {
                            txtDiemQuanAnChiTiet.setTextColor(Color.RED);
                        }
                        txtDiemQuanAnChiTiet.setText(String.format("%.1f", diemtb));
                    }
                    txtTongSoHinhAnhQuanAn.setText(tonghinhanh + "");


                    /// lấy bình luận quán ăn
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ChiTietQuanAnActivity.this);
                    recyclerBinhLuanQuanAn.setLayoutManager(layoutManager);
                    adapterBinhLuanQuanAn = new AdapterBinhLuanQuanAn(ChiTietQuanAnActivity.this, R.layout.custom_layout_binhluanquanan, quanAnModeln.getBinhluanquanan());
                    recyclerBinhLuanQuanAn.setAdapter(adapterBinhLuanQuanAn);
                    adapterBinhLuanQuanAn.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /// lấy thực đơn quán ăn
        thucDonController.getThucDonQuanAn(this, quanAnModel.getMaquanan(), recyclerThucDon);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LoadBinhLuan(quanAnModel.getMaquanan());
    }

    private  void DownLoadHinhTienIch(){

        if(quanAnModel.getTienich() != null){

            for(String matienich : quanAnModel.getTienich()){

                DatabaseReference noteTienIch = FirebaseDatabase.getInstance().getReference().child("quanlytienichs").child(matienich);
                noteTienIch.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        TienIchModel tienIchModel = dataSnapshot.getValue(TienIchModel.class);
                        StorageReference storageHinhTienIch = FirebaseStorage.getInstance().getReference().child("ExtendService").child(tienIchModel.getHinhtienich());
                        storageHinhTienIch.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                ImageView imvHinhTienIch = new ImageView(ChiTietQuanAnActivity.this);
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(50, 50);
                                layoutParams.setMargins(5, 5, 5,5);
                                layoutParams.weight = 1;
                                imvHinhTienIch.setLayoutParams(layoutParams);
                                imvHinhTienIch.setImageBitmap(bitmap);
                                KhungTienIch.addView(imvHinhTienIch);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                ///
            }
            ///
        }

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;

        LatLng quanAn = new LatLng(chiNhanhQuanAnModelMin.getLatitude(), chiNhanhQuanAnModelMin.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(quanAn).title(quanAnModel.getTenquanan())).showInfoWindow();
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(quanAn, 15));

    }
}
