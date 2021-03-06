package com.yourfood.uit.reviewquanan.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yourfood.uit.reviewquanan.Model.BinhLuanModel;
import com.yourfood.uit.reviewquanan.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ochutgio on 5/4/2018.
 */

public class AdapterBinhLuanQuanAn extends RecyclerView.Adapter<AdapterBinhLuanQuanAn.ViewHolder> {

    Context context;
    List<BinhLuanModel> binhLuanModelList;
    int resource;

    public AdapterBinhLuanQuanAn(Context context, int resource, List<BinhLuanModel> binhLuanModelList){
        this.context = context;
        this.resource = resource;
        this.binhLuanModelList = binhLuanModelList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imvAvatar;
        RecyclerView recyclerHinhBinhLuan;
        TextView txtTieuDeBinhLuan;
        TextView txtNoiDungBinhLuan;
        TextView txtChamDiem;
        TextView txtTenUser;

        public ViewHolder(View itemView) {
            super(itemView);

            imvAvatar = (CircleImageView) itemView.findViewById(R.id.imvAvatar);
            recyclerHinhBinhLuan = (RecyclerView) itemView.findViewById(R.id.recyclerHinhBinhLuan);
            txtTieuDeBinhLuan = (TextView) itemView.findViewById(R.id.txtTieuDeBinhLuan);
            txtNoiDungBinhLuan = (TextView) itemView.findViewById(R.id.txtNoiDungBinhLuan);
            txtChamDiem = (TextView) itemView.findViewById(R.id.txtChamDiem);
            txtTenUser = (TextView) itemView.findViewById(R.id.txtTenUser);
        }
    }

    @Override
    public AdapterBinhLuanQuanAn.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterBinhLuanQuanAn.ViewHolder holder, int position) {

        final BinhLuanModel binhLuanModel = binhLuanModelList.get(position);

        if(binhLuanModel.getChamdiem() > 5){
            holder.txtChamDiem.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }else {
            holder.txtChamDiem.setTextColor(Color.RED);
        }
        holder.txtTenUser.setText(binhLuanModel.getThanhVienModel().getHoten());
        holder.txtChamDiem.setText(binhLuanModel.getChamdiem() + " đ");

        holder.txtTieuDeBinhLuan.setText(binhLuanModel.getTieude());
        holder.txtNoiDungBinhLuan.setText(binhLuanModel.getNoidung());

        StorageReference storageAvatar = FirebaseStorage.getInstance().getReference().child("User").child(binhLuanModel.getThanhVienModel().getHinhanh());
        long ONE_MAGEBYTE = 1024 * 1024;
        storageAvatar.getBytes(ONE_MAGEBYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.imvAvatar.setImageBitmap(bitmap);
            }
        });

        final List<Bitmap>  bitmapList = new ArrayList<>();
        for(String linkhinh : binhLuanModel.getHinhanhBinhLuan()){
            StorageReference storageHinhBinhLuan = FirebaseStorage.getInstance().getReference().child("Photo").child(linkhinh);
            storageHinhBinhLuan.getBytes(ONE_MAGEBYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    bitmapList.add(bitmap);

                    if(bitmapList.size() == binhLuanModel.getHinhanhBinhLuan().size()){
                        AdapterRecyclerHinhBinhLuan adapterRecyclerHinhBinhLuan = new AdapterRecyclerHinhBinhLuan(context, R.layout.custom_layout_hinhbinhluan, bitmapList, binhLuanModel.getMabinhluan());
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
                        holder.recyclerHinhBinhLuan.setLayoutManager(layoutManager);
                        holder.recyclerHinhBinhLuan.setAdapter(adapterRecyclerHinhBinhLuan);
                        adapterRecyclerHinhBinhLuan.notifyDataSetChanged();
                    }
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        if(binhLuanModelList.size() > 5){
            return 5;
        }else {
            return binhLuanModelList.size();
        }

    }


}
