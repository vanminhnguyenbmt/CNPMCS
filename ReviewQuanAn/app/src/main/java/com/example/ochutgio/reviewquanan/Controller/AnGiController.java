package com.example.ochutgio.reviewquanan.Controller;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.ochutgio.reviewquanan.Adapter.AdapterRecyclerAnGi;
import com.example.ochutgio.reviewquanan.Controller.Interface.OdauInterface;
import com.example.ochutgio.reviewquanan.Model.QuanAnModel;
import com.example.ochutgio.reviewquanan.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ochutgio on 4/19/2018.
 */

public class AnGiController {
    Context context;
    QuanAnModel quanAnModel;
    AdapterRecyclerAnGi adapterRecyclerAnGi;
    List<QuanAnModel> quanAnModelList;

    public AnGiController(Context context){
        this.context = context;
        quanAnModel = new QuanAnModel();
    }

    public void getDanhSachQuanAnController(RecyclerView recyclerAnGi){

        quanAnModelList  = new ArrayList<>();
        // set layout cho recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerAnGi.setLayoutManager(layoutManager);
        // tạo adapterRecycleView và set adapter cho recyclerView
        adapterRecyclerAnGi = new AdapterRecyclerAnGi(quanAnModelList, R.layout.custom_layout_recycleview_angi);
        recyclerAnGi.setAdapter(adapterRecyclerAnGi);

        OdauInterface odauInterface = new OdauInterface() {
            @Override
            public void getDanhSachQuanAnModel(QuanAnModel quanAnModel) {
                quanAnModelList.add(quanAnModel);
                adapterRecyclerAnGi.notifyDataSetChanged();
            }
        };
        // gọi hàm getdanhsachquanan của tầng model
        quanAnModel.getDanhSachQuanAn(odauInterface);
    }

}
