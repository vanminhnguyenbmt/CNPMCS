package com.yourfood.uit.reviewquanan.View;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yourfood.uit.reviewquanan.Model.MonAnModel;
import com.yourfood.uit.reviewquanan.Model.ThemThucDonModel;
import com.yourfood.uit.reviewquanan.Model.ThucDonModel;
import com.yourfood.uit.reviewquanan.R;
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

public class ThemThucDonActivity extends AppCompatActivity {

    final int REQUEST_IMVTHUCDON = 333;
    boolean flag = false;
    Toolbar toolbar;
    ImageView imvTam;
    LinearLayout containerThucDon;
    TextView txtTenQuanAn;
    TextView txtDiaChi;
    TextView txtXong;


    List<Uri> hinhmonanList;
    List<String> thucdonList;
    List<ThucDonModel> thucDonModelList;
    List<ThemThucDonModel> themThucDonModelList;
    ProgressDialog progress;

    String maquanan;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_themthucdon);

        maquanan = getIntent().getStringExtra("maquanan");
        String tenquan = getIntent().getStringExtra("tenquan");
        String diachi = getIntent().getStringExtra("diachi");
        progress = new ProgressDialog(this);
        progress.setMessage("Đang xử lý...");


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtTenQuanAn = (TextView) findViewById(R.id.txtTenQuanAn);
        txtDiaChi = (TextView) findViewById(R.id.txtDiaChi);
        txtXong = (TextView) findViewById(R.id.txtXong);

        containerThucDon = (LinearLayout) findViewById(R.id.containerThucDon);

        txtTenQuanAn.setText(tenquan);
        txtDiaChi.setText(diachi);

        hinhmonanList = new ArrayList<>();
        thucdonList = new ArrayList<>();
        thucDonModelList = new ArrayList<>();
        themThucDonModelList = new ArrayList<>();

        CloneThucDon();

        txtXong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemThucDon();
            }
        });

        toolbar.setTitle("");
        setSupportActionBar(toolbar);


    }

    /// clone thực đơn.
    private void ThemThucDon(){

        final DatabaseReference noteRoot = FirebaseDatabase.getInstance().getReference();
        /// thêm và upload thực đơn quán ăn
        progress.show();
        if(themThucDonModelList.size() > 0){
            //final boolean[] flag = {false};
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
                                            progress.dismiss();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }else {
                    Toast toast = Toast.makeText(ThemThucDonActivity.this, "Thêm món ăn thất bại\nKhông có hình món ăn", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    break;
                }
            }

            if(flag == true){
                Toast toast = Toast.makeText(ThemThucDonActivity.this, "Thêm món ăn thành công", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                finish();
            }

        }else {
            progress.dismiss();
            Toast toast = Toast.makeText(ThemThucDonActivity.this, "bạn chưa lưu món ăn", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
        ///
    }



    private void CloneThucDon(){

        final View v = LayoutInflater.from(ThemThucDonActivity.this).inflate(R.layout.layout_clone_thucdon, null);

        final Spinner spinnerThucDon = (Spinner) v.findViewById(R.id.spinerThucDon);
        ImageButton btnThemMonAn = (ImageButton) v.findViewById(R.id.btnThemMonAn);
        final ImageButton btnXoaMonAn = (ImageButton) v.findViewById(R.id.btnXoaMonAn);
        final EditText edTenMonAn = (EditText) v.findViewById(R.id.edTenMonAn);
        final EditText edGiaTien = (EditText) v.findViewById(R.id.edGiaTien);
        ImageView imvChupHinh = (ImageView) v.findViewById(R.id.imvChupHinh);
        imvTam = imvChupHinh;

        ArrayAdapter<String> adapterThucDon = new ArrayAdapter<String>(ThemThucDonActivity.this, android.R.layout.simple_list_item_1, thucdonList);
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
                    Toast.makeText(ThemThucDonActivity.this, "vui lòng nhập thông tin món ăn", Toast.LENGTH_SHORT).show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMVTHUCDON){
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
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        builder.setMessage("Bạn có muốn thoát thêm thực đơn ?");
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
