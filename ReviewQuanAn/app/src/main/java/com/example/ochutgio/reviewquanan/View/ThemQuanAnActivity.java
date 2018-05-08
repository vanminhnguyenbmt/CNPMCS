package com.example.ochutgio.reviewquanan.View;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.ochutgio.reviewquanan.Model.ThucDonModel;
import com.example.ochutgio.reviewquanan.Model.TienIchModel;
import com.example.ochutgio.reviewquanan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ochutgio on 5/8/2018.
 */

public class ThemQuanAnActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Button btnGioMoCua;
    Button btnGioDongCua;
    Spinner spinerKhuVuc;
    Spinner spinnerThucDon;
    LinearLayout khungTienIch;
    LinearLayout khungChiNhanh;
    LinearLayout containerChiNhanh;

    String giomocua;
    String giodongcua;

    List<String> tienichList;
    List<String> khuvucList;
    List<String> thucdonList;
    List<String> chinhanhList;
    List<ThucDonModel> thucDonModelList;

    ArrayAdapter<String> adapterKhuVuc;
    ArrayAdapter<String> adapterThucDon;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_themquanan);

        btnGioDongCua = (Button) findViewById(R.id.btnGioDongCua);
        btnGioMoCua = (Button) findViewById(R.id.btnGioMoCua);
        spinerKhuVuc = (Spinner) findViewById(R.id.spinerKhuVuc);
        spinnerThucDon = (Spinner) findViewById(R.id.spinerThucDon);
        khungTienIch = (LinearLayout) findViewById(R.id.khungTienIch);
        khungChiNhanh = (LinearLayout) findViewById(R.id.khungChiNhanh);
        containerChiNhanh = (LinearLayout) findViewById(R.id.containerChiNhanh);

        thucDonModelList = new ArrayList<>();
        tienichList = new ArrayList<>();
        khuvucList = new ArrayList<>();
        thucdonList = new ArrayList<>();
        chinhanhList = new ArrayList<>();

        adapterKhuVuc = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, khuvucList);
        spinerKhuVuc.setAdapter(adapterKhuVuc);
        adapterKhuVuc.notifyDataSetChanged();

        adapterThucDon = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, thucdonList);
        spinnerThucDon.setAdapter(adapterThucDon);
        adapterThucDon.notifyDataSetChanged();

        LayDanhSachTienIch();
        LayDanhSachKhuVuc();
        LayDanhSachThucDon();

        CloneChiNhanh();

        btnGioMoCua.setOnClickListener(this);
        btnGioDongCua.setOnClickListener(this);

        spinerKhuVuc.setOnItemSelectedListener(this);
        spinnerThucDon.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(final View view) {

        Calendar calendar = Calendar.getInstance();
        final int gio = calendar.get(Calendar.HOUR_OF_DAY);
        int phut = calendar.get(Calendar.MINUTE);

        switch (view.getId()){
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

        }
    }

    private void CloneChiNhanh(){
        View v = LayoutInflater.from(ThemQuanAnActivity.this).inflate(R.layout.layout_clone_chinhanh, null);
        ImageButton imageButton = (ImageButton) v.findViewById(R.id.btnThemChiNhanh);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edTenChiNhanh = view.findViewById(R.id.edTenChiNhanh);
                if(edTenChiNhanh.getText() != null){
                    chinhanhList.add(edTenChiNhanh.getText().toString());
                    CloneChiNhanh();
                }
            }
        });
        containerChiNhanh.addView(v);
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
                            Log.d("check", tienichList.size()+"");
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

    private void LayDanhSachThucDon(){
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
                adapterThucDon.notifyDataSetChanged();
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

                break;
            case R.id.spinerThucDon:
                Log.d("kiemtra", thucdonList.get(i) + " - " + thucDonModelList.get(i).getMathucdon());
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
