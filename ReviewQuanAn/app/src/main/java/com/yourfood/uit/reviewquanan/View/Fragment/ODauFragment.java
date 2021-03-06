package com.yourfood.uit.reviewquanan.View.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.yourfood.uit.reviewquanan.Controller.OdauController;
import com.yourfood.uit.reviewquanan.Model.QuanAnModel;
import com.yourfood.uit.reviewquanan.R;

import java.util.zip.Inflater;

/**
 * Created by ochutgio on 4/18/2018.
 */

public class ODauFragment extends Fragment{
    SwipeRefreshLayout swipeRefreshLayout;
    OdauController odauController;
    RecyclerView recyclerOdau;
   //ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    Location vitrihientai;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_odau, container,false);
        recyclerOdau = (RecyclerView) view.findViewById(R.id.recyclerOdau);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_orange_dark,
                R.color.colorPrimaryDark);
        //progressBar = (ProgressBar) view.findViewById(R.id.progressBarODau);
        //progressBar.getIndeterminateDrawable()
                //.setColorFilter(getContext().getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        sharedPreferences = getContext().getSharedPreferences("toado", Context.MODE_PRIVATE);
        vitrihientai = new Location("");
        vitrihientai.setLatitude(Double.parseDouble(sharedPreferences.getString("latitude","0")));
        vitrihientai.setLongitude(Double.parseDouble(sharedPreferences.getString("longitude","0")));


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                odauController = new OdauController(getContext());
                odauController.getDanhSachQuanAnController(recyclerOdau, vitrihientai, swipeRefreshLayout);
            }
        });


        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                swipeRefreshLayout.setRefreshing(true);
                // Fetching data from server
                odauController = new OdauController(getContext());
                odauController.getDanhSachQuanAnController( recyclerOdau, vitrihientai, swipeRefreshLayout);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

}
