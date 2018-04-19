package com.example.ochutgio.reviewquanan.View.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ochutgio.reviewquanan.Controller.AnGiController;
import com.example.ochutgio.reviewquanan.Model.QuanAnModel;
import com.example.ochutgio.reviewquanan.R;

/**
 * Created by ochutgio on 4/18/2018.
 */

public class AnGiFragment extends Fragment {

    AnGiController anGiController;
    RecyclerView recyclerAnGi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_angi, container,false);

        recyclerAnGi = (RecyclerView) view.findViewById(R.id.recyclerAnGi);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        anGiController = new AnGiController(getContext());
        anGiController.getDanhSachQuanAnController(recyclerAnGi);
    }
}
