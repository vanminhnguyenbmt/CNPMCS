package com.example.ochutgio.reviewquanan.Model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.List;

/**
 * Created by ochutgio on 4/24/2018.
 */

public class BinhLuanModel implements Parcelable {
    String noidung;
    String tieude;
    String mauser;
    long chamdiem, luotthich;

    List<String> hinhanhBinhLuan;

    ThanhVienModel thanhVienModel;

    public BinhLuanModel(){

    }

    protected BinhLuanModel(Parcel in) {
        noidung = in.readString();
        tieude = in.readString();
        mauser = in.readString();
        chamdiem = in.readLong();
        luotthich = in.readLong();
        hinhanhBinhLuan = in.createStringArrayList();
        thanhVienModel = in.readParcelable(ThanhVienModel.class.getClassLoader());
    }

    public static final Creator<BinhLuanModel> CREATOR = new Creator<BinhLuanModel>() {
        @Override
        public BinhLuanModel createFromParcel(Parcel in) {
            return new BinhLuanModel(in);
        }

        @Override
        public BinhLuanModel[] newArray(int size) {
            return new BinhLuanModel[size];
        }
    };

    public List<String> getHinhanhBinhLuan() {
        return hinhanhBinhLuan;
    }

    public void setHinhanhBinhLuan(List<String> hinhanhBinhLuan) {
        this.hinhanhBinhLuan = hinhanhBinhLuan;
    }

    public String getMauser() {
        return mauser;
    }

    public void setMauser(String mauser) {
        this.mauser = mauser;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public String getTieude() {
        return tieude;
    }

    public void setTieude(String tieude) {
        this.tieude = tieude;
    }

    public long getChamdiem() {
        return chamdiem;
    }

    public void setChamdiem(long chamdiem) {
        this.chamdiem = chamdiem;
    }

    public long getLuotthich() {
        return luotthich;
    }

    public void setLuotthich(long luotthich) {
        this.luotthich = luotthich;
    }

    public ThanhVienModel getThanhVienModel() {
        return thanhVienModel;
    }

    public void setThanhVienModel(ThanhVienModel thanhVienModel) {
        this.thanhVienModel = thanhVienModel;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(noidung);
        parcel.writeString(tieude);
        parcel.writeString(mauser);
        parcel.writeLong(chamdiem);
        parcel.writeLong(luotthich);
        parcel.writeStringList(hinhanhBinhLuan);
        parcel.writeParcelable(thanhVienModel, i);
    }

    public void ThemBinhLuan(BinhLuanModel binhLuanModel, final List<String> listHinh, String maquanan){

        DatabaseReference noteBinhLuan = FirebaseDatabase.getInstance().getReference().child("binhluans");
        String mabinhluan = noteBinhLuan.child(maquanan).push().getKey();
        noteBinhLuan.child(maquanan).child(mabinhluan).setValue(binhLuanModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){

                    if(listHinh.size() > 0){
                        for(String valueHinh : listHinh){
                            Uri uri = Uri.fromFile(new File(valueHinh));
                            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Photo/" + uri.getLastPathSegment());
                            storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                }
                            });
                            ///
                        }
                    }
                    ///
                }
            }
        });

        ///
        if(listHinh.size() > 0){
            for(String valueHinh : listHinh){
                Uri uri = Uri.fromFile(new File(valueHinh));
                FirebaseDatabase.getInstance().getReference().child("hinhanhbinhluans").child(mabinhluan).push().setValue(uri.getLastPathSegment());
            }

        }
    }
}
