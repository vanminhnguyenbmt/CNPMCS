package com.example.ochutgio.reviewquanan.Model;

import android.util.Log;

import com.example.ochutgio.reviewquanan.Controller.Interface.OdauInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ochutgio on 4/19/2018.
 */

public class QuanAnModel {

    boolean giaohang;
    long luotthich;
    String giomocua, giodongcua, tenquanan, videogioithieu;
    List<String> tienich;

    List<String> hinhanhquanan;

    DatabaseReference noteRoot;

    public QuanAnModel(){
        noteRoot = FirebaseDatabase.getInstance().getReference();
    }

    public List<String> getHinhanhquanan() {
        return hinhanhquanan;
    }

    public void setHinhanhquanan(List<String> hinhanhquanan) {
        this.hinhanhquanan = hinhanhquanan;
    }

    public long getLuotthich() {
        return luotthich;
    }

    public void setLuotthich(long luotthich) {
        this.luotthich = luotthich;
    }

    public boolean isGiaohang() {
        return giaohang;
    }

    public void setGiaohang(boolean giaohang) {
        this.giaohang = giaohang;
    }

    public String getGiomocua() {
        return giomocua;
    }

    public void setGiomocua(String giomocua) {
        this.giomocua = giomocua;
    }

    public String getGiodongcua() {
        return giodongcua;
    }

    public void setGiodongcua(String giodongcua) {
        this.giodongcua = giodongcua;
    }

    public String getTenquanan() {
        return tenquanan;
    }

    public void setTenquanan(String tenquanan) {
        this.tenquanan = tenquanan;
    }

    public String getVideogioithieu() {
        return videogioithieu;
    }

    public void setVideogioithieu(String videogioithieu) {
        this.videogioithieu = videogioithieu;
    }

    public List<String> getTienich() {
        return tienich;
    }

    public void setTienich(List<String> tienich) {
        this.tienich = tienich;
    }

    public void getDanhSachQuanAn(final OdauInterface odauInterface){

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot dataSnapshotQuanAn = dataSnapshot.child("quanans");

                for(DataSnapshot valueQuanAn : dataSnapshotQuanAn.getChildren()){
                    List<String> hinhAnhQuanAnlist = new ArrayList<>();
                    QuanAnModel quanAnModel = valueQuanAn.getValue(QuanAnModel.class);
                    DataSnapshot dataHinhAnhQuanAnList = dataSnapshot.child("hinhanhquanans").child(valueQuanAn.getKey());
                    for(DataSnapshot valueHinhAnh : dataHinhAnhQuanAnList.getChildren()){
                        hinhAnhQuanAnlist.add(valueHinhAnh.getValue(String.class));
                    }
                    quanAnModel.setHinhanhquanan(hinhAnhQuanAnlist);
                    odauInterface.getDanhSachQuanAnModel(quanAnModel);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        noteRoot.addListenerForSingleValueEvent(valueEventListener);
    }
}
