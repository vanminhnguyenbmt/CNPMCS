package com.yourfood.uit.reviewquanan.Controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.yourfood.uit.reviewquanan.Adapter.AdapterRecyclerAnGi;
import com.yourfood.uit.reviewquanan.Controller.Interface.OdauInterface;
import com.yourfood.uit.reviewquanan.Model.MonAnModel;
import com.yourfood.uit.reviewquanan.Model.QuanAnModel;
import com.yourfood.uit.reviewquanan.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

    int itemdaco = 3;
    int buocnhay = 3;

    public AnGiController(Context context){
        this.context = context;
        quanAnModel = new QuanAnModel();
    }

    public void getDanhSachQuanAnController(RecyclerView recyclerAnGi, final Location vitrihientai, final SwipeRefreshLayout swipeRefreshLayout){

        quanAnModelList  = new ArrayList<>();

        // set layout cho recyclerView
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
        recyclerAnGi.setLayoutManager(layoutManager);

        // tạo adapterRecycleView và set adapter cho recyclerView
        adapterRecyclerAnGi = new AdapterRecyclerAnGi(quanAnModelList, R.layout.custom_layout_recycleview_angi, context);
        recyclerAnGi.setAdapter(adapterRecyclerAnGi);
        //progressBar.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(true);
        final OdauInterface odauInterface = new OdauInterface() {
            @Override
            public void getDanhSachQuanAnModel(final QuanAnModel quanAnModel) {
                if(quanAnModel.getThucdonquanan().size() > 0) {
                    MonAnModel monAnModel = quanAnModel.getThucdonquanan().get(0).getMonAnModelList().get(0);
                    StorageReference storageHinhAnh = FirebaseStorage.getInstance().getReference().child("Photo").child(monAnModel.getHinhanh());
                    long ONE_MEGABYTE = 1024 * 1024;
                    storageHinhAnh.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmapHinhMonAn = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            quanAnModel.setBitmaphinhmonan(bitmapHinhMonAn);
                            quanAnModelList.add(quanAnModel);
                            adapterRecyclerAnGi.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                            //progressBar.setVisibility(View.GONE);
                        }
                    });

                }

            }

            @Override
            public void timQuanAn(QuanAnModel quanAnModel) {

            }
        };

        ///
        recyclerAnGi.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                itemdaco += buocnhay;
                quanAnModel.getDanhSachQuanAn(odauInterface, vitrihientai, itemdaco, itemdaco - buocnhay);
            }
        });

        // gọi hàm getdanhsachquanan của tầng model
        quanAnModel.getDanhSachQuanAn(odauInterface, vitrihientai, itemdaco, 0);
    }

}
