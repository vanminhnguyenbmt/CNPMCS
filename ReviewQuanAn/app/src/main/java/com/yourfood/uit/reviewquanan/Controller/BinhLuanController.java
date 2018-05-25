package com.yourfood.uit.reviewquanan.Controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ProgressBar;

import com.yourfood.uit.reviewquanan.Model.BinhLuanModel;

import java.util.List;

/**
 * Created by ochutgio on 5/7/2018.
 */

public class BinhLuanController {

    BinhLuanModel binhLuanModel;

    public BinhLuanController(){
        binhLuanModel = new BinhLuanModel();
    }

    public  void ThemBinhLuan(BinhLuanModel binhLuanModel, List<String> listHinh, String maquanan, Context context, ProgressBar progressBar){
        binhLuanModel.ThemBinhLuan(binhLuanModel, listHinh, maquanan, context, progressBar);
    }
}
