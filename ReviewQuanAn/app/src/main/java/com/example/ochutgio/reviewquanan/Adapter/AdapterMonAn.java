package com.example.ochutgio.reviewquanan.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ochutgio.reviewquanan.Model.MonAnModel;
import com.example.ochutgio.reviewquanan.R;

import java.util.List;

/**
 * Created by ochutgio on 5/8/2018.
 */

public class AdapterMonAn extends RecyclerView.Adapter<AdapterMonAn.ViewHolder> {

    Context context;
    List<MonAnModel> monAnModelList;

    public AdapterMonAn(Context context, List<MonAnModel> monAnModelList){
        this.context = context;
        this.monAnModelList = monAnModelList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenMonAn;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTenMonAn = (TextView) itemView.findViewById(R.id.txtTenMonAn);

        }
    }

    @Override
    public AdapterMonAn.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout_monan, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterMonAn.ViewHolder holder, int position) {
        MonAnModel monAnModel = monAnModelList.get(position);
        holder.txtTenMonAn.setText(monAnModel.getTenmon());

    }

    @Override
    public int getItemCount() {
        return monAnModelList.size();
    }


}
