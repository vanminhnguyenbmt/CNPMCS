package com.example.ochutgio.reviewquanan.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ochutgio.reviewquanan.Model.QuanAnModel;
import com.example.ochutgio.reviewquanan.R;

import java.util.List;

/**
 * Created by ochutgio on 4/19/2018.
 */

public class AdapterRecyclerAnGi extends RecyclerView.Adapter<AdapterRecyclerAnGi.ViewHolder> {

    List<QuanAnModel> quanAnModelList;
    int resource;

    public AdapterRecyclerAnGi( List<QuanAnModel> quanAnModelList, int resource) {
        this.quanAnModelList = quanAnModelList;
        this.resource = resource;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenQuanAn1;
        public ViewHolder(View itemView) {
            super(itemView);
            txtTenQuanAn1 = itemView.findViewById(R.id.txtTenQuanAn1);
        }
    }

    @Override
        public AdapterRecyclerAnGi.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            AdapterRecyclerAnGi.ViewHolder viewHolder= new AdapterRecyclerAnGi.ViewHolder(view);
        return viewHolder;
    }

        @Override
        public void onBindViewHolder(AdapterRecyclerAnGi.ViewHolder holder, int position) {
            QuanAnModel quanAnModel = quanAnModelList.get(position);
            holder.txtTenQuanAn1.setText(quanAnModel.getTenquanan());
        }

        @Override
        public int getItemCount() {
            return quanAnModelList.size();
        }


}
