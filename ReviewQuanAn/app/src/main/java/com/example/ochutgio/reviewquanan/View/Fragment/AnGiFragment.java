package com.example.ochutgio.reviewquanan.View.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.ochutgio.reviewquanan.Controller.AnGiController;

import com.example.ochutgio.reviewquanan.R;

/**
 * Created by ochutgio on 4/18/2018.
 */

public class AnGiFragment extends Fragment {

    AnGiController anGiController;
    RecyclerView recyclerAnGi;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_angi, container,false);
        recyclerAnGi = (RecyclerView) view.findViewById(R.id.recyclerAnGi);
        sharedPreferences = getContext().getSharedPreferences("toado", Context.MODE_PRIVATE);
        Location vitrihientai = new Location("");
        vitrihientai.setLatitude(Double.parseDouble(sharedPreferences.getString("latitude","0")));
        vitrihientai.setLongitude(Double.parseDouble(sharedPreferences.getString("longitude","0")));

        // gọi hàm getdanhsachquanan của tầng controller
        anGiController = new AnGiController(getContext());
        anGiController.getDanhSachQuanAnController(recyclerAnGi, vitrihientai);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
