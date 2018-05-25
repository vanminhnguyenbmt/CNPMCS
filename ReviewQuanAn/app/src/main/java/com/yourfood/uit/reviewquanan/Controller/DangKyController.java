package com.yourfood.uit.reviewquanan.Controller;

import com.yourfood.uit.reviewquanan.Model.ThanhVienModel;

/**
 * Created by ochutgio on 4/23/2018.
 */

public class DangKyController {
    ThanhVienModel thanhVienModel;

    public DangKyController(){
        thanhVienModel = new ThanhVienModel();
    }

    public void ThemThanhVienController(ThanhVienModel thanhVienModel, String uid){
        thanhVienModel.ThemThanhVienModel(thanhVienModel, uid);
    }

}
