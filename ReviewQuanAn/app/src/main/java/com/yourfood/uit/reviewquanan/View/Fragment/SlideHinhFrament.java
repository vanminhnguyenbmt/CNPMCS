package com.yourfood.uit.reviewquanan.View.Fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.support.v4.app.Fragment;

import com.yourfood.uit.reviewquanan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SlideHinhFrament extends Fragment {

    ImageView imvFullHinh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_fragment_slidehinh, container,false);
        imvFullHinh = (ImageView)view.findViewById(R.id.imvFullHinh);

        Bundle bundle = getArguments();
        String hinh = bundle.getString("string");
        StorageReference storageHinhBinhLuan = FirebaseStorage.getInstance().getReference().child("Photo").child(hinh);
        long ONE_MEGABYTE = 1024*1024;
        storageHinhBinhLuan.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imvFullHinh.setImageBitmap(bitmap);
            }
        });

        return view;
    }
}
