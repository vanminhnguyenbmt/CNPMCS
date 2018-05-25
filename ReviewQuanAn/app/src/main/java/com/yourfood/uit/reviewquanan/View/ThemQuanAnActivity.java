package com.yourfood.uit.reviewquanan.View;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;

import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.yourfood.uit.reviewquanan.Model.ChiNhanhQuanAnModel;
import com.yourfood.uit.reviewquanan.Model.MonAnModel;
import com.yourfood.uit.reviewquanan.Model.QuanAnModel;
import com.yourfood.uit.reviewquanan.Model.ThemThucDonModel;
import com.yourfood.uit.reviewquanan.Model.ThucDonModel;
import com.yourfood.uit.reviewquanan.Model.TienIchModel;
import com.yourfood.uit.reviewquanan.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by ochutgio on 5/8/2018.
 */

public class ThemQuanAnActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    final int REQUEST_IMV1 = 111;
    final int REQUEST_IMVTHUCDON = 333;
    final int PLACE_PICKER_REQUEST = 1;

    boolean flag = false;
    Button btnThemQuanAn;
    Button btnGioMoCua;
    Button btnGioDongCua;
    Spinner spinerKhuVuc;
    ImageView imvTam;

    LinearLayout khungTienIch;
    LinearLayout contaninerToaDo;
    LinearLayout containerThucDon;

    EditText edTenQuanAn;
    EditText edGiaToiThieu;
    EditText edGiaToiDa;
    EditText edTenChiNhanh;

    TextView txtLat;
    TextView txtLong;

    ImageView imvHinhQuanAn1;

    Toolbar toolbar;

    String giomocua = "09:00";
    String giodongcua = "21:00";

    double latitude = 0.0;
    double longitude = 0.0;

    List<String> tienichList;
    List<String> khuvucList;

    List<Uri> hinhmonanList;
    List<String> thucdonList;
    List<ThucDonModel> thucDonModelList;
    List<ThemThucDonModel> themThucDonModelList;

    Uri hinhquanan;
    String khuvuc;
    String maquanan;

    ProgressDialog progress;
    ArrayAdapter<String> adapterKhuVuc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_themquanan);
        progress = new ProgressDialog(this);
        progress.setMessage("Đang xử lý...");

        btnThemQuanAn = (Button) findViewById(R.id.btnThemQuanAn);
        btnGioDongCua = (Button) findViewById(R.id.btnGioDongCua);
        btnGioMoCua = (Button) findViewById(R.id.btnGioMoCua);
        spinerKhuVuc = (Spinner) findViewById(R.id.spinerKhuVuc);

        khungTienIch = (LinearLayout) findViewById(R.id.khungTienIch);
        contaninerToaDo = (LinearLayout) findViewById(R.id.containerToaDo);
        containerThucDon = (LinearLayout) findViewById(R.id.containerThucDon);

        imvHinhQuanAn1 = (ImageView) findViewById(R.id.imvHinhQuanAn1);
        txtLat = (TextView) findViewById(R.id.txtLat);
        txtLong = (TextView) findViewById(R.id.txtLong);

        edTenQuanAn = (EditText) findViewById(R.id.edTenQuanAn);
        edGiaToiThieu = (EditText) findViewById(R.id.edGiaToiThieu);
        //edGiaToiThieu.setFilters(new InputFilter[]{ new InputFilterMinMax("1000", "1000000")});
        edGiaToiDa = (EditText) findViewById(R.id.edGiaToiDa);
        //edGiaToiDa.setFilters(new InputFilter[]{new InputFilterMinMax("1000", "10000000")});
        edTenChiNhanh = (EditText) findViewById(R.id.edTenChiNhanh);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        /// set toolbar
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        tienichList = new ArrayList<>();
        khuvucList = new ArrayList<>();
        hinhmonanList = new ArrayList<>();
        thucdonList = new ArrayList<>();
        thucDonModelList = new ArrayList<>();
        themThucDonModelList = new ArrayList<>();


        adapterKhuVuc = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, khuvucList);
        spinerKhuVuc.setAdapter(adapterKhuVuc);
        adapterKhuVuc.notifyDataSetChanged();

        CloneThucDon();

        btnThemQuanAn.setOnClickListener(this);
        btnGioMoCua.setOnClickListener(this);
        btnGioDongCua.setOnClickListener(this);
        spinerKhuVuc.setOnItemSelectedListener(this);
        imvHinhQuanAn1.setOnClickListener(this);
        contaninerToaDo.setOnClickListener(this);

        ///
        LayDanhSachTienIch();
        LayDanhSachKhuVuc();
    }

    private  void ThemQuanAn() throws IOException {

        String tenquanan = edTenQuanAn.getText().toString();
        final String chinhanh = edTenChiNhanh.getText().toString();
        String giatoithieuinput = edGiaToiThieu.getText().toString();
        String giatoidainput = edGiaToiDa.getText().toString();

        if(tenquanan.trim().length() == 0 | chinhanh.trim().length() == 0 | giatoithieuinput.trim().length() == 0 | giatoidainput.trim().length() == 0){
            Toast.makeText(ThemQuanAnActivity.this, "vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
        }else {
            if((int)latitude == 0 | (int) longitude == 0){
                Toast toast = Toast.makeText(ThemQuanAnActivity.this, "bạn chưa chọn tọa độ bản đồ", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();

            }else {

                long giatoithieu = Long.parseLong(giatoithieuinput);
                long giatoida = Long.parseLong(giatoidainput);

                final DatabaseReference noteRoot = FirebaseDatabase.getInstance().getReference();
                maquanan = noteRoot.child("quanans").push().getKey();

                final QuanAnModel quanAnModel = new QuanAnModel();
                quanAnModel.setLuotthich(0);
                quanAnModel.setTenquanan(tenquanan);
                quanAnModel.setGiatoithieu(giatoithieu);
                quanAnModel.setGiatoida(giatoida);
                quanAnModel.setGiomocua(giomocua);
                quanAnModel.setGiodongcua(giodongcua);
                quanAnModel.setGiaohang(false);
                quanAnModel.setTienich(tienichList);
                quanAnModel.setVideogioithieu("");

                if(maquanan != null){
                    if(hinhquanan != null){
                        if(!hinhquanan.getLastPathSegment().equals("")){

                            progress.show();
                            /// upload hình ảnh quán ăn
                            Bitmap bitmap = null;
                            try {
                                bitmap = resizeFile(hinhquanan);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            if(bitmap != null){
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                byte[] data = stream.toByteArray();
                                final String tenhinh = String.valueOf(Calendar.getInstance().getTimeInMillis()) + ".jpg" ;

                                FirebaseStorage.getInstance().getReference().child("Photo/" + tenhinh).putBytes(data).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if(task.isSuccessful()){
                                            noteRoot.child("hinhanhquanans").child(maquanan).push().setValue(tenhinh);
                                            noteRoot.child("quanans").child(maquanan).setValue(quanAnModel);
                                            noteRoot.child("khuvucs").child(khuvuc).push().setValue(maquanan);

                                            ChiNhanhQuanAnModel chiNhanhQuanAnModel = new ChiNhanhQuanAnModel();
                                            chiNhanhQuanAnModel.setDiachi(chinhanh);
                                            chiNhanhQuanAnModel.setLatitude(latitude);
                                            chiNhanhQuanAnModel.setLongitude(longitude);
                                            noteRoot.child("chinhanhquanans").child(maquanan).push().setValue(chiNhanhQuanAnModel);

                                            progress.dismiss();
                                            Toast.makeText(ThemQuanAnActivity.this, "thêm quán ăn thành công", Toast.LENGTH_SHORT).show();
                                            finish();

                                        }else {
                                            Toast.makeText(ThemQuanAnActivity.this, "Thêm quán ăn thất bại", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }else {
                            progress.dismiss();
                            Toast toast = Toast.makeText(ThemQuanAnActivity.this, "Bạn chưa chọn hình quán ăn", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                        }

                        /// thêm thực đơn và upload hình món ăn
                        if(themThucDonModelList.size() > 0){
                            for(int i = 0; i < themThucDonModelList.size(); i++){
                                if(hinhmonanList.size() > i){

                                    final ThemThucDonModel themThucDonModel = themThucDonModelList.get(i);

                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    Bitmap bitmap = null;
                                    try {
                                        bitmap = resizeFile(hinhmonanList.get(i));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                    byte[] data = stream.toByteArray();
                                    FirebaseStorage.getInstance().getReference().child("Photo/" + themThucDonModel.getMonAnModel().getHinhanh()).putBytes(data).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if(task.isSuccessful()){
                                                noteRoot.child("thucdonquanans").child(maquanan).child(themThucDonModel.getMathucdon()).push().setValue(themThucDonModel.getMonAnModel()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()) {
                                                            flag = true;

                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }else {
                                    break;
                                }
                            }

                            if(flag == true){

                            }
                        }

                    }else {
                        progress.dismiss();
                        Toast toast = Toast.makeText(ThemQuanAnActivity.this, "bạn chưa chọn hình quán ăn", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                    }
                }else {
                    progress.dismiss();
                    Toast toast = Toast.makeText(ThemQuanAnActivity.this, "Thêm quán ăn thất bại", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
            }
        }
    }

    private void CloneThucDon(){

        final View v = LayoutInflater.from(ThemQuanAnActivity.this).inflate(R.layout.layout_clone_thucdon, null);

        final Spinner spinnerThucDon = (Spinner) v.findViewById(R.id.spinerThucDon);
        ImageButton btnThemMonAn = (ImageButton) v.findViewById(R.id.btnThemMonAn);
        final ImageButton btnXoaMonAn = (ImageButton) v.findViewById(R.id.btnXoaMonAn);
        final EditText edTenMonAn = (EditText) v.findViewById(R.id.edTenMonAn);
        final EditText edGiaTien = (EditText) v.findViewById(R.id.edGiaTien);
        ImageView imvChupHinh = (ImageView) v.findViewById(R.id.imvChupHinh);
        imvTam = imvChupHinh;

        ArrayAdapter<String> adapterThucDon = new ArrayAdapter<String>(ThemQuanAnActivity.this, android.R.layout.simple_list_item_1, thucdonList);
        spinnerThucDon.setAdapter(adapterThucDon);
        adapterThucDon.notifyDataSetChanged();

        if(thucDonModelList.size() == 0){
            LayDanhSachThucDon(adapterThucDon);
        }

        imvChupHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent();
                intent1.setType("image/*");
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent1, "Chọn hình"), REQUEST_IMVTHUCDON);
            }
        });

        btnXoaMonAn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemThucDonModel themThucDonModel = (ThemThucDonModel) view.getTag();
                themThucDonModelList.remove(themThucDonModel);
                containerThucDon.removeView(v);
            }
        });

        btnThemMonAn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tenhinh = String.valueOf(Calendar.getInstance().getTimeInMillis()) + ".jpg" ;
                String tenmonan = edTenMonAn.getText().toString();
                String giatien = edGiaTien.getText().toString();
                int position = spinnerThucDon.getSelectedItemPosition();
                String mathucdon = thucDonModelList.get(position).getMathucdon();

                if(tenmonan.trim().length() > 0 & giatien.trim().length() > 0){
                    MonAnModel monAnModel = new MonAnModel();
                    monAnModel.setTenmon(tenmonan);
                    monAnModel.setGiatien(Long.parseLong(giatien));
                    monAnModel.setHinhanh(tenhinh);

                    ThemThucDonModel themThucDonModel = new ThemThucDonModel();
                    themThucDonModel.setMathucdon(mathucdon);
                    themThucDonModel.setMonAnModel(monAnModel);
                    themThucDonModelList.add(themThucDonModel);
                    view.setVisibility(View.GONE);
                    btnXoaMonAn.setVisibility(View.VISIBLE);
                    btnXoaMonAn.setTag(themThucDonModel);

                    CloneThucDon();
                }else {
                    Toast.makeText(ThemQuanAnActivity.this, "vui lòng nhập thông tin món ăn", Toast.LENGTH_SHORT).show();
                }
            }
        });

        containerThucDon.addView(v);
    }

    private void LayDanhSachThucDon(final ArrayAdapter<String> adapter){
        FirebaseDatabase.getInstance().getReference().child("thucdons").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot valueThucDon : dataSnapshot.getChildren()){
                    ThucDonModel thucDonModel = new  ThucDonModel();
                    String tenthucdon = valueThucDon.getValue(String.class);
                    String mathucdon = valueThucDon.getKey();

                    thucdonList.add(tenthucdon);
                    thucDonModel.setMathucdon(mathucdon);
                    thucDonModel.setTenthucdon(tenthucdon);
                    thucDonModelList.add(thucDonModel);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    ///
    private Bitmap resizeBitMap(Bitmap bitmap){
        Bitmap b;
        int max_size = 600;
        int scale = 1;
        if (bitmap.getWidth() > max_size || bitmap.getHeight() > max_size)
        {
            scale = (int) Math.pow(2, (int) Math.ceil(Math.log(max_size / (double) Math.max(bitmap.getWidth(), bitmap.getHeight())) / Math.log(0.5)));
        }

        b = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / scale, bitmap.getHeight() / scale, false);
        return  b;
    }

    /// hàm resize kích thước ảnh
    private Bitmap resizeFile(Uri uri) throws IOException {
        Bitmap b = null;
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();

            int max_size = 600;
            int scale = 1;
            if (bitmap.getWidth() > max_size || bitmap.getHeight() > max_size)
            {
                scale = (int) Math.pow(2, (int) Math.ceil(Math.log(max_size / (double) Math.max(bitmap.getWidth(), bitmap.getHeight())) / Math.log(0.5)));
            }

            b = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / scale, bitmap.getHeight() / scale, false);



        }catch (Exception e){

        }
        return b;
    }

    @Override
    public void onClick(final View view) {

        Calendar calendar = Calendar.getInstance();
        final int gio = calendar.get(Calendar.HOUR_OF_DAY);
        int phut = calendar.get(Calendar.MINUTE);

        switch (view.getId()){
            case R.id.btnThemQuanAn:
                try {
                    ThemQuanAn();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnGioMoCua:
                TimePickerDialog timePickerDialogMoCua = new TimePickerDialog(ThemQuanAnActivity.this, new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        giomocua = i + ":" + i1;
                        ((Button)view).setText(giomocua);
                    }
                }, gio, phut, true);

                timePickerDialogMoCua.show();
                break;
            case  R.id.btnGioDongCua:
                TimePickerDialog timePickerDialogDongCua = new TimePickerDialog(ThemQuanAnActivity.this, new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        giodongcua = i + ":" + i1;
                        ((Button)view).setText(giodongcua);
                    }
                }, gio, phut, true);

                timePickerDialogDongCua.show();
                break;

            case R.id.imvHinhQuanAn1:
                Intent intent1 = new Intent();
                intent1.setType("image/*");
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent1, "Chọn hình"), REQUEST_IMV1);
                break;
            case R.id.containerToaDo:
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_IMVTHUCDON:
                if(data != null){
                    Uri uri = data.getData();
                    try {
                        Bitmap b = resizeFile(uri);
                        imvTam.setImageBitmap(b);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    hinhmonanList.add(uri);
                    Log.d("kiemtra", uri + "");
                }
                break;

            case REQUEST_IMV1:
                if( resultCode == RESULT_OK){
                    if(data != null){
                        Uri uri = data.getData();
                        try {
                            Bitmap b = resizeFile(uri);
                            imvHinhQuanAn1.setImageBitmap(b);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        hinhquanan = uri;
                        Log.d("kiemtra", uri + "");
                    }

                }
                break;
            case PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    if(data != null){
                        Place place = PlacePicker.getPlace(this, data);
                        LatLng location = place.getLatLng();
                        latitude = location.latitude;
                        longitude = location.longitude;
                        txtLat.setText("lat " + latitude);
                        txtLong.setText("Long " + longitude);
                    }
                }
                break;
        }
    }


    private void LayDanhSachTienIch(){
        FirebaseDatabase.getInstance().getReference().child("quanlytienichs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot valueTienIch : dataSnapshot.getChildren()){
                    final String matienich = valueTienIch.getKey();
                    TienIchModel tienIchModel = valueTienIch.getValue(TienIchModel.class);
                    tienIchModel.setMatienich(matienich);

                    CheckBox checkBox = new CheckBox(ThemQuanAnActivity.this);
                    checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    checkBox.setTag(tienIchModel.getMatienich());
                    checkBox.setText(tienIchModel.getTentienich());
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if(b == true){
                                tienichList.add(matienich);
                            }else {
                                tienichList.remove(matienich);
                            }
                        }
                    });
                    khungTienIch.addView(checkBox);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void LayDanhSachKhuVuc(){
        FirebaseDatabase.getInstance().getReference().child("khuvucs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot valueKhuVuc : dataSnapshot.getChildren()){
                    String tenkhuvuc = valueKhuVuc.getKey();
                    khuvucList.add(tenkhuvuc);
                }
                adapterKhuVuc.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.spinerKhuVuc:
                khuvuc = khuvucList.get(i);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /// sự kiện nhấn nút Back
    @Override
    public void onBackPressed() {
        AlertDialog myAlertDialog = thoatAlertDialog();
        myAlertDialog.show();
    }

    /// tạo hộp thoại xác nhận thoát ứng dụng
    private AlertDialog thoatAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Xác Nhận!");
        builder.setMessage("Bạn có muốn thoát màn hình thêm quán ăn ?");
        builder.setCancelable(false);
        builder.setNegativeButton("Có",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        builder.setNeutralButton("Không", null);


        AlertDialog dialog = builder.create();
        return dialog;
    }
}
