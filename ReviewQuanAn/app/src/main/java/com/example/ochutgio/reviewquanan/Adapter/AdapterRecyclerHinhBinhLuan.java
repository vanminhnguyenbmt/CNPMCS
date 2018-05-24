package com.example.ochutgio.reviewquanan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ochutgio.reviewquanan.R;
import com.example.ochutgio.reviewquanan.View.ImageSlideActivity;
import com.example.ochutgio.reviewquanan.View.ImageSlideUserActivity;

import java.util.List;

/**
 * Created by ochutgio on 5/5/2018.
 */

public class AdapterRecyclerHinhBinhLuan extends RecyclerView.Adapter<AdapterRecyclerHinhBinhLuan.ViewHolder> {

    Context context;
    int layout;
    List<Bitmap> listHinh;
    String mabinhluan;

    public AdapterRecyclerHinhBinhLuan(Context context, int layout, List<Bitmap> listHinh, String mabinhluan){
        this.context = context;
        this.layout = layout;
        this.listHinh = listHinh;
        this.mabinhluan = mabinhluan;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imvHinhBinhLuan;
        TextView txtSoHinhBinhLuan;
        FrameLayout KhungSoHinhConLai;

        public ViewHolder(View itemView) {
            super(itemView);

            imvHinhBinhLuan = (ImageView) itemView.findViewById(R.id.imvHinhBinhLuan);
            txtSoHinhBinhLuan = (TextView) itemView.findViewById(R.id.txtSoHinhBinhLuan);
            KhungSoHinhConLai = (FrameLayout) itemView.findViewById(R.id.KhungSoHinhBinhLuan);
        }
    }

    @Override
    public AdapterRecyclerHinhBinhLuan.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterRecyclerHinhBinhLuan.ViewHolder holder, final int position) {
        Bitmap b = listHinh.get(position);
        holder.imvHinhBinhLuan.setImageBitmap(b);
        if(position == 3){
            int sohinhconlai = listHinh.size() - 4;
            if(sohinhconlai > 0){
                holder.KhungSoHinhConLai.setVisibility(View.VISIBLE);
                holder.txtSoHinhBinhLuan.setText("+" + sohinhconlai);
            }
        }
        holder.imvHinhBinhLuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iImageSlide = new Intent(context, ImageSlideUserActivity.class);
                iImageSlide.putExtra("mabinhluan", mabinhluan);
                iImageSlide.putExtra("vitri", position);
                context.startActivity(iImageSlide);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listHinh.size() > 4){
            return 4;
        }
        else {
            return listHinh.size();
        }

    }
}
