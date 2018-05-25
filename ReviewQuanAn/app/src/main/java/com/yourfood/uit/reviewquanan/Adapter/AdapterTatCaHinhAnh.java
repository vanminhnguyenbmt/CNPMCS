package com.yourfood.uit.reviewquanan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yourfood.uit.reviewquanan.R;
import com.yourfood.uit.reviewquanan.View.ImageSlideActivity;

import java.util.List;

/**
 * Created by ochutgio on 5/16/2018.
 */

public class AdapterTatCaHinhAnh extends RecyclerView.Adapter<AdapterTatCaHinhAnh.ViewHolder> {

    Context context;
    int layout;
    List<Bitmap> listHinh;
    String maquanan;

    public AdapterTatCaHinhAnh(Context context, int layout, List<Bitmap> listHinh, String maquanan){
        this.context = context;
        this.layout = layout;
        this.listHinh = listHinh;
        this.maquanan = maquanan;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        AdapterTatCaHinhAnh.ViewHolder viewHolder = new AdapterTatCaHinhAnh.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Bitmap b = listHinh.get(position);
        holder.imvHinhAnh.setImageBitmap(b);
        holder.imvHinhAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iImageSlide = new Intent(context, ImageSlideActivity.class);
                iImageSlide.putExtra("maquanan", maquanan);
                iImageSlide.putExtra("vitri", position);
                context.startActivity(iImageSlide);
            }
        });

    }


    @Override
    public int getItemCount() {
        return listHinh.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imvHinhAnh;

        public ViewHolder(View itemView) {
            super(itemView);

            imvHinhAnh = (ImageView) itemView.findViewById(R.id.imvHinhAnh);
        }
    }
}
