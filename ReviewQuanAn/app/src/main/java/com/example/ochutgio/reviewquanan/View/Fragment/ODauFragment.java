package com.example.ochutgio.reviewquanan.View.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ochutgio.reviewquanan.Controller.OdauController;
import com.example.ochutgio.reviewquanan.Model.QuanAnModel;
import com.example.ochutgio.reviewquanan.R;

import java.util.zip.Inflater;

/**
 * Created by ochutgio on 4/18/2018.
 */

public class ODauFragment extends Fragment {

    OdauController odauController;

    RecyclerView recyclerOdau;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_odau, container,false);
        recyclerOdau = (RecyclerView) view.findViewById(R.id.recyclerOdau);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        odauController = new OdauController(getContext());
        odauController.getDanhSachQuanAnController(recyclerOdau);
    }

}
